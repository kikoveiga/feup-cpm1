import com.feup.jtp.checkout_terminal.data.model.dto.TransactionToServer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class CheckoutController(private val paymentService: PaymentService) {

    @PostMapping("/checkout")
    fun processTransaction(@RequestBody transaction: TransactionToServer): ResponseEntity<TransactionToServer> {
        return try {
            val result = paymentService.processTransaction(transaction.encryptedTransaction)
            ResponseEntity.ok(result)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(PaymentResult("Error processing payment"))
        }
    }
}
