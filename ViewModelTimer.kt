package com.example.mococoroutineexample

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class ViewModelTimer : ViewModel() {
    // LiveData für die verbleibende Zeit in Sekunden
    private val _timeInSeconds = MutableLiveData(25 * 60)
    val timeInSeconds: LiveData<Int> get() = _timeInSeconds

    // LiveData, das angibt, ob der Timer läuft
    private val _isRunning = MutableLiveData(false)
    val isRunning: LiveData<Boolean> get() = _isRunning

    // LiveData für die Farben des Timers
    private val _timerColor = MutableLiveData(listOf(Color(0xFF302D31), Color(0xFF7B7B7C)))
    val timerColor: LiveData<List<androidx.compose.ui.graphics.Color>> get() = _timerColor

    // Job für die Coroutine, die den Timer aktualisiert
    private var job: Job? = null

    // Funktion zum Starten des Timers
    fun onStart() {
        if (_isRunning.value == true) return  // Timer läuft bereits
        _isRunning.value = true
        job = CoroutineScope(Dispatchers.Main).launch {
            // Coroutine, die den Timer jede Sekunde aktualisiert
            while (_timeInSeconds.value ?: 0 > 0 && _isRunning.value == true) {
                delay(1000L)  // Eine Sekunde warten
                _timeInSeconds.value = (_timeInSeconds.value ?: 0) - 1  // Zeit um eine Sekunde verringern
            }
            _isRunning.value = false  // Timer anhalten, wenn die Zeit abgelaufen ist
        }
    }

    // Funktion zum Anhalten des Timers
    fun onPause() {
        _isRunning.value = false  // Timer-Status auf "nicht laufend" setzen
        job?.cancel()  // Laufende Coroutine abbrechen
    }

    // Methode, die aufgerufen wird, wenn das ViewModel zerstört wird
    override fun onCleared() {
        super.onCleared()
        job?.cancel()  // Laufende Coroutine abbrechen, um Speicherlecks zu verhindern
    }
}