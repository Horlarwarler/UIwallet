package com.crezent.finalyearproject.transaction.presentation.transaction_details

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.crezent.finalyearproject.core.domain.model.Transaction
import com.crezent.finalyearproject.ui.theme.black
import finalyearproject.composeapp.generated.resources.Res
import com.crezent.finalyearproject.R
import com.crezent.finalyearproject.core.data.util.CurrentActivityHolder

actual fun generateReceipt(
    transaction: Transaction
): ByteArray {
    // val koinContext =cont
    val pdfDocument = android.graphics.pdf.PdfDocument()
    val pdfDocumentBuilder = android.graphics.pdf.PdfDocument.PageInfo.Builder(
        595, 842, 1
    ).create() // A4 Paper
    val page = pdfDocument.startPage(pdfDocumentBuilder)

    val context = CurrentActivityHolder.get()!!

    val canvas = page.canvas
    val descriptionPaint = android.graphics.Paint()
    val valuePaint = android.graphics.Paint()
    val valuePoint = Offset(0f, 0f)
    val descriptionPoint = Offset(0f, 0f)

    descriptionPaint.color = android.graphics.Color.GRAY
    valuePaint.color = android.graphics.Color.BLACK
    descriptionPaint.textSize = 20f
    valuePaint.textSize = 16f

    val bitmap = BitmapFactory.decodeResource(
        context.resources, R.drawable.ui_logo
    )
    canvas.drawBitmap(bitmap, 20f, 20f, null)

    canvas.drawText("Transaction Details", 20f, 20f, descriptionPaint)

   // canvas.drawText("Transaction Amount",)

    TODO("Not yet implemented")
}

actual fun saveInvoiceToFile(data: ByteArray, fileName: String) {
    TODO("Not yet implemented")
}

