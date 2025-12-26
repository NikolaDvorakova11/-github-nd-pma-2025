package com.example.vanocniapp.utils

import android.graphics.Bitmap

/**
 * Jednoduchý objekt pro rozřezání obrázku na mřížku menších dílků.
 */
object PuzzleCutter {

    /**
     * Rozdělí zdrojový obrázek (Bitmap) na zadaný počet řádků a sloupců.
     * @param source Zdrojový obrázek.
     * @param rows Počet řádků, na které se má obrázek rozdělit.
     * @param cols Počet sloupců, na které se má obrázek rozdělit.
     * @return Seznam (List) jednotlivých dílků jako Bitmap.
     */
    fun split(source: Bitmap, rows: Int, cols: Int): List<Bitmap> {
        val pieces = mutableListOf<Bitmap>()
        val pieceWidth = source.width / cols
        val pieceHeight = source.height / rows

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val x = col * pieceWidth
                val y = row * pieceHeight
                val piece = Bitmap.createBitmap(source, x, y, pieceWidth, pieceHeight)
                pieces.add(piece)
            }
        }
        return pieces
    }
}
