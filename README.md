# The Acme Electronic Supermarket

**The Acme Electronic Supermarket** is a secure, Android-based shopping and payment. It consists of three main components:

- **Client App** – Android app built with Kotlin and Jetpack Compose. Used by customers to register, scan products via QR code, and complete purchases.
- **Terminal Checkout App** – Android app built with Kotlin and Jetpack Compose. Used in physical stores by the cashier to process payments and apply vouchers.
- **Backend Server** – A RESTful API built with Spring Boot and Kotlin that handles authentication, transactions, and secure data synchronization.

This is a brief overview of the project. For more details, please check the [report](report.pdf) and the [presentation slides](presentation.pdf).

## Group Members

1. Henrique Gardé (up202108725@up.pt)
2. João Padrão (up202108766@up.pt)
3. José Francisco Veiga (up202108753@up.pt)

## Execution

### Server

To run the backend server, you need **Java** installed on your machine. Then, you can run it directly on IntelliJ IDEA by clicking the green `Run` button.

### Android Apps

You can open the Android apps in Android Studio and run them on an emulator or a physical device with USB debugging and developer mode enabled.

Because the server is running on localhost in a personal computer, you need to set the server URL in the Android apps to point to your local server. You can do this by:

1. Check your local IP address by running `ipconfig` (Windows) or `ifconfig` (Linux/Mac) in the terminal.
2. Open the `NetworkModule.kt` file in both the client and terminal apps.
3. If running the Android emulator, simply set the `isEmulator()` function to return `true`. If running on a physical device, please change the `baseUrl` variable to your local IP address.

## Main Features

- **User Authentication**: Register users on the Client App which stores the information locally in the device and on the server. The login process is done locally, without the server. Clients can also logout, change their password, and delete their account.
- **Product Scanning**: For this feature, the camera is needed in a physical device. You can scan the products QR codes present in the [products](products.pdf) file. It's possible to change quantities and remove products from the cart.
- **Create a Transaction**: After having some products in the cart, the client can create a transaction QR code. This code is read by the Terminal Checkout App (also via camera) and the information is parsed and sent to the server for validation.
- **Consult Transactions and Vouchers**: The client can consult their past transactions and unused vouchers. These are stored in the Server database and fetched on demand when the client wants to see them.
- **Cryptography**: Various algorithms and techniques are used to ensure the security in the communication between the components, such as **RSA encryption**, **ECDSA signatures** and **Base64 encoding**.

## Main Technologies & Libraries

- **Kotlin & Jetpack Compose**: Used for building the Android apps.
- **Kotlin & Spring Boot**: Used for building the backend server.
- **Clean Architecture & MVVM**: Used for structuring the Android apps.
- **Hilt**: Dependency injection library used in the Android apps.
- **Retrofit & OkHttp**: Used for making network requests in the Android apps.
- **Room**: SQLite database used for local storage in the Client App.
- **Jetpack DataStore**: Used for storing user information in the Client App, substituting SharedPreferences.
- **Android Keystore**: Used for storing cryptographic keys securely in the Client App.
- **Zxing**: Library used for QR code scanning in the Android apps.
- **Kotlinx Serialization & Gson**: Used for serializing and deserializing data in the Android apps.
- **KSP**: Kotlin Symbol Processing which substitutes the deprecated kapt.
