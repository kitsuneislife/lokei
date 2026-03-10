# Lokei

Projeto mínimo Kotlin/Android com:

* um esquema de banco de dados híbrido (Exposed/H2 no módulo JVM),
* um módulo Android usando Jetpack Compose e Material3 com tema OLED/laranja,
* fluxos de autenticação (registro/login) simples em memória,
* testes unitários para banco de dados e lógica de autenticação.

## Compilação e execução

Para criar o wrapper manualmente (requer Gradle instalado):

```sh
cd /home/lukas/Documentos/Faculdade/Lokei
gradle wrapper --gradle-version 9.3.1 --distribution-type=bin
```

Se não tiver Gradle no sistema, copie os seguintes arquivos de outro projeto com
wrapper já gerado:

* `gradlew` e `gradlew.bat`
* `gradle/wrapper/gradle-wrapper.properties`
* `gradle/wrapper/gradle-wrapper.jar`

Depois que o wrapper estiver presente, você poderá executar

```sh
./gradlew build           # compila todos os módulos
./gradlew :app:installDebug  # instala APK em dispositivo/emulador (Android)
./gradlew test            # executa testes JVM (módulo principal e app)
``` 

Caso contrário utilize `gradle <tarefa>` se o Gradle estiver instalado globalmente.

## Testes

Os testes estão nas pastas `src/test` e `app/src/test`. Executam a lógica de
inserção/consulta do banco e a autenticação de usuários. Rodam no JVM sem
dependências Android.