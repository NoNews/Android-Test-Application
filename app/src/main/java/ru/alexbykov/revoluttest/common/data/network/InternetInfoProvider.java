package ru.alexbykov.revoluttest.common.data.network;

import androidx.annotation.WorkerThread;

public interface InternetInfoProvider {

    @WorkerThread
    boolean isInternetAvailable();
}
