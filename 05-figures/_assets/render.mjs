// render.mjs — render a designed HTML diagram to a cropped, print-DPI PNG using
// the system Chrome via puppeteer-core (no bundled Chromium download).
//
//   node 05-figures/_assets/render.mjs <in.html> <out.png>
//
// Crops to the element id="figure" (the house contract; see .claude/commands/figure.md).
// Chrome path: $CHROME_PATH, else the macOS default. NEVER an image model — this
// renders authored HTML only.
import puppeteer from 'puppeteer-core';
import { pathToFileURL } from 'node:url';
import { existsSync } from 'node:fs';
import { resolve } from 'node:path';

const [, , inHtml, outPng] = process.argv;
if (!inHtml || !outPng) {
  console.error('usage: node render.mjs <in.html> <out.png>');
  process.exit(2);
}

const CHROME_CANDIDATES = [
  process.env.CHROME_PATH,
  '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome',
  '/Applications/Chromium.app/Contents/MacOS/Chromium',
  '/Applications/Microsoft Edge.app/Contents/MacOS/Microsoft Edge',
].filter(Boolean);
const chrome = CHROME_CANDIDATES.find((p) => existsSync(p));
if (!chrome) {
  console.error('render: no Chrome/Chromium found; set CHROME_PATH'); process.exit(3);
}

const browser = await puppeteer.launch({
  executablePath: chrome,
  headless: 'new',
  args: ['--no-sandbox', '--force-color-profile=srgb', '--hide-scrollbars'],
});
try {
  const page = await browser.newPage();
  // deviceScaleFactor 3 → ~print DPI for a screen-sized diagram.
  await page.setViewport({ width: 1400, height: 1000, deviceScaleFactor: 3 });
  await page.goto(pathToFileURL(resolve(inHtml)).href, { waitUntil: 'networkidle0' });
  await page.evaluateHandle('document.fonts.ready');
  const target = (await page.$('#figure')) || (await page.$('body'));
  await target.screenshot({ path: outPng, captureBeyondViewport: true });
  console.log('rendered', outPng);
} finally {
  await browser.close();
}
