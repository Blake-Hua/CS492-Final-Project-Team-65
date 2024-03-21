package edu.oregonstate.cs492.assignment4.data

import com.squareup.moshi.JsonClass

/**
 * {
 *     "message": {
 *         "header": {
 *             "status_code": 200,
 *             "execute_time": 0.012869834899902
 *         },
 *         "body": {
 *             "lyrics": {
 *                 "lyrics_id": 31635577,
 *                 "explicit": 1,
 *                 "lyrics_body": "(Honorable C.N.O.T.E.)\n(Metro)\n\nKeep the bitch jump, uh-uh\nKeep it on jump, uh-uh (jump)\nKeep the bitch ju-u-ump\nI caught it cool, for a ten\nThe bitch get loose, she tryna win\nI beat her by the house, I beat her in\nThere's forty in the couch, I let her spend\n\nWhen the car's lit, better call in\nShe done popped all out, she done called twin\nI done went too spazzed out, I put the raw in\nI done hit the strip club and spent a tall ten\nLil' shawty off the Clicquot\nShe been comin' hot just like a heat stroke (heat stroke)\nI could see you lurkin' through the peephole\nI'm stackin' different money, type of C notes (C notes)\nI'm talkin' C notes, nigga, hit C notes\n\nYou spend what you want and you get what you want\nI guess you got what you wanted\nYou're hittin' the pole and you give it your all\nNow, you keepin' it honest (Yeah)\nIt's too many nights I went nameless\n...\n\n******* This Lyrics is NOT for Commercial use *******\n(1409624389746)",
 *                 "script_tracking_url": "https://tracking.musixmatch.com/t1.0/m_js/e_1/sn_0/l_31635577/su_0/rs_0/tr_3vUCAMEJtN2xc4aV3Su0I_g_kDZNUy6Wm5dxf2wU6roDe_vB1DOhArZ1eLJBZfjhEFUwyf-K9tXrgDQNSwOJIkCoDst1dLW5H797E6H8NYPHoPG5xfyIz8Kzg_T5bwZ0aNiksD2fag-kAflHFyM-BMViPAPOwW0d9qU6DX2qxM7HAJjvr1tSz6c9wScote1kNKzFnvBQxzkhIAxsCRnfFkIj208hKQ7ii8jFOkiSUmJnGQtEvQ1s9gjBW4XlwQZwttoaqzNj1zqVLtRpuk96ztAyqsNDyITL_SK6ndwy5dcFCJW-ggU6H0NX8QXiIxYb6LMmddLFuxRlyTHjDzWDAF-fr-_OVnMeAuPU9qXsVmGZNRh7jjTYvvbyvhXo_I82g2iBpMwVnAZk54EDnzICrXnmx_1Rer0PKUC7ozpkebPIwXszrxOZYcRoFvEGkS3gyeFD-DC1l0xm9lWs4Izu3xhgcLoYxYuNbzTWFOq0Q7xnEQFxZ6581JOKDohtzK55Ds5f/",
 *                 "pixel_tracking_url": "https://tracking.musixmatch.com/t1.0/m_img/e_1/sn_0/l_31635577/su_0/rs_0/tr_3vUCAMsL0MSzV8QgUFLAZ9aH9bldtLjLIlg0iPqFXYLFt6rLRtxRFx72_wZGwUnSQ1pxRVgi8Qvpdvc4nzi77F3oLv2dNhCqlW0nO4_WPJu2qjZtInOZJjyQKlx_5oNS1MAseCwm1_zIYiN1rkj1gqGRpg3eH2YjVHGukdYBuD9G49F52ef7RyKojhniOUd8CSXOWbJPODuLu6fJvk0W01lWJ4BrzR42h-2B-U8HpIUrbFM93-lzX-F36LpNr_W_hux2in6bGZi-rzj2jiHF6AG6tYIR7hyfFPw-sN2NUQSZHUAraQ_JfvhczBRZ7rJxcn-SuBx5lbFJXWSkCVHWCUNLXy_Fd6qh6KOJQgVupQsqcWIgweedqRst2DPVAvWjqcXMZdMLbedN7U2mLJRuZh63Juc9LgYdMqCKTmb0MR_saDk8_9BX2mIcNVZ9E5AVrB5TeQlknuTAjNix5PGwIcnMqKaYr_rWKmfv6K8aK0bIBmSjuANC2fpZSHlUfehydEy7/",
 *                 "lyrics_copyright": "Lyrics powered by www.musixmatch.com. This Lyrics is NOT for Commercial use and only 30% of the lyrics are returned.",
 *                 "updated_time": "2024-01-17T05:12:19Z"
 *             }
 *         }
 *     }
 * }
 */

/*
    * Above is an example API request for a lyrics API
    * Parse the lyrics fields from the JSON
    * Create a lyrics JSON adapter
 */

import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class ApiResponse(
    @Json(name = "message") val message: LyricsMessage
)

@JsonClass(generateAdapter = true)
data class LyricsMessage(
    @Json(name = "header") val header: Header,
    @Json(name = "body") val body: Body
)

@JsonClass(generateAdapter = true)
data class Header(
    @Json(name = "status_code") val statusCode: Int,
    @Json(name = "execute_time") val executeTime: Double
)

@JsonClass(generateAdapter = true)
data class Body(
    @Json(name = "lyrics") val lyrics: Lyrics
)

@JsonClass(generateAdapter = true)
data class Lyrics(
    @Json(name = "lyrics_id") val lyricsId: Int?,
    @Json(name = "explicit") val explicit: Int?,
    @Json(name = "lyrics_body") val lyricsBody: String?,
    @Json(name = "script_tracking_url") val scriptTrackingUrl: String?,
    @Json(name = "pixel_tracking_url") val pixelTrackingUrl: String?,
    @Json(name = "lyrics_copyright") val lyricsCopyright: String?,
    @Json(name = "updated_time") val updatedTime: String?
)
