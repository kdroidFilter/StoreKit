package io.github.kdroidfilter.storekit.apkpure.scraper.services

import kotlin.test.Test
import kotlin.test.assertEquals

class ApkPureSignatureAndVersionCodeTest {

    @Test
    fun `extractSignature from More App Info section`() {
        val html = SAMPLE_SIGNATURE_HTML
        val sig = extractSignature(html)
        assertEquals("35b438fe1bc69d975dc8702dc16ab69ebf65f26f", sig)
    }

    @Test
    fun `extractVersionCode fallback from variant block`() {
        val html = SAMPLE_VARIANT_HTML
        val code = extractVersionCodeFallback(html)
        assertEquals("1030640", code)
    }
}

private const val SAMPLE_SIGNATURE_HTML = """
<div class="more-information-container">
  <ul>
    <li class="sign">
      <svg class></svg>
      <div class="info">
        <div class="label one-line">Signature</div>
        <div class="value double-lines">35b438fe1bc69d975dc8702dc16ab69ebf65f26f</div>
      </div>
    </li>
  </ul>
</div>
"""

private const val SAMPLE_VARIANT_HTML = """
<div class="apk" data-dt-app="com.waze">
  <div class="info-top">
    <span class="name one-line">5.11.0.0</span>
    <span class="code one-line">(1030640)</span>
    <span class="tag one-line" data-tag="XAPK">XAPK</span>
  </div>
  <a class="download-btn" href="https://d.apkpure.com/b/XAPK/com.waze?versionCode=1030640" title="">
    <span>Download</span>
  </a>
</div>
"""
