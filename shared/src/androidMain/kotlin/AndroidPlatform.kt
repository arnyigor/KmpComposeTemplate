import android.content.Context

object AndroidPlatform {
    lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context.applicationContext
    }
}