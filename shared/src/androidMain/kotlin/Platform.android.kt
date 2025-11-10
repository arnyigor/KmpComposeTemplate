import android.content.Context
import java.util.UUID
import android.os.Build
import android.widget.Toast

// Глобальная переменная для хранения контекста
private var appContext: Context? = null

// Функция для установки контекста
fun initializeAndroidContext(context: Context) {
    appContext = context.applicationContext
}

actual fun showNotification(message: String) {
    appContext?.let { context ->
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

actual fun getPlatformName(): String = "Android ${Build.VERSION.RELEASE}"

actual fun generateId(): String = UUID.randomUUID().toString()