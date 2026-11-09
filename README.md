# PracticalTest01 

## Partea 1: Workflow-ul sigur pentru Git și Proiect Nou

1. **Creează repository GOL pe GitHub** (FĂRĂ bife la README, .gitignore sau licență).
2. **Clonează repo-ul pe PC:** Deschide un terminal (pe Desktop/Documents) și rulează `git clone <LINK_REPO>`. Această comandă va crea automat un folder cu numele repository-ului.
3. **Creează proiectul:** În Android Studio -> *New Project* -> La secțiunea *Save location*, selectează exact folderul proaspăt creat de `clone`.
4. **Trimite codul:** Deschide tab-ul Terminal din Android Studio și rulează:
   ```bash
   git add .
   git commit -m "Initial commit"
   git push # (sau git push -u origin main prima dată)
   ```

---

## Partea 2: Scheletul pentru Servicii (Started Service) și Broadcasts

### Pasul 1: Înregistrarea Serviciului (Obligatoriu)
În fișierul `AndroidManifest.xml`, chiar deasupra de închiderea tag-ului `</application>`:
```xml
<service android:name=".MyService" />
```

### Pasul 2: Lansarea / Oprirea Serviciului (În `MainActivity.kt`)

**A. Pentru a porni serviciul (ex: la apăsarea unui buton):**
```kotlin
val intent = Intent(this@MainActivity, MyService::class.java)
intent.putExtra("cheie_date", valoare)
startService(intent) // Apelează automat onStartCommand din Serviciu
```

**B. Pentru a opri serviciul (când se închide aplicația):**
```kotlin
override fun onDestroy() {
    val intent = Intent(this, MyService::class.java)
    stopService(intent)
    super.onDestroy()
}
```

### Pasul 3: Crearea Serviciului (În `MyService.kt`)
Creezi o clasă nouă `MyService` care moștenește `Service`. Ea conține logica și firul de execuție.

```kotlin
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder

class MyService : Service() {
    private var processingThread: ProcessingThread? = null

    // 1. Thread-ul care face treaba în fundal
    private inner class ProcessingThread(private val context: Context, private val data: Int) : Thread() {
        private var isRunning = true

        override fun run() {
            while (isRunning) {
                // Aici faci calculele tale matematice/logice
                val rezultat = data * 2 

                // Arunci Broadcast-ul (Megafonul)
                val broadcastIntent = Intent()
                broadcastIntent.action = "ro.pub.cs.systems.eim.MY_ACTION"
                broadcastIntent.putExtra("mesaj", "Rezultat: $rezultat")
                context.sendBroadcast(broadcastIntent)

                // Pui pauză (ex: 10 secunde)
                try {
                    sleep(10000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        fun stopThread() { isRunning = false }
    }

    // 2. Intrarea în serviciu (vine de la startService din MainActivity)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val extrageDate = intent?.getIntExtra("cheie_date", 0) ?: 0
        
        if (processingThread == null) {
            processingThread = ProcessingThread(this, extrageDate)
            processingThread?.start()
        }
        return START_REDELIVER_INTENT
    }

    // 3. Curățenia la final (vine de la stopService)
    override fun onDestroy() {
        processingThread?.stopThread()
        super.onDestroy()
    }

    // 4. Obligatoriu de suprascris, dar returnează null
    override fun onBind(intent: Intent?): IBinder? = null
}
```

### Pasul 4: Recepționarea Mesajelor (Tot în `MainActivity.kt`)
Pentru a asculta megafonul serviciului, declari receptorul, filtrul și le asociezi de ciclul de viață al activității.

**A. Declararea receptorului și a filtrului (ca variabile globale în clasă):**
```kotlin
private val messageBroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val mesajPrimit = intent?.getStringExtra("mesaj") ?: "Empty"
        Log.d("MyBroadcastLogs", mesajPrimit)
    }
}

private val intentFilter = IntentFilter().apply {
    addAction("ro.pub.cs.systems.eim.MY_ACTION") // Aceeași acțiune din Thread
}
```

**B. Activarea / Dezactivarea ascultătorului (metode suprascrise în clasă):**
```kotlin
override fun onResume() {
    super.onResume()
    // Android 13+ cere RECEIVER_EXPORTED
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        registerReceiver(messageBroadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED)
    } else {
        registerReceiver(messageBroadcastReceiver, intentFilter)
    }
}

override fun onPause() {
    unregisterReceiver(messageBroadcastReceiver)
    super.onPause()
}
```
