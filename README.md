# KMP Compose Template

Современный шаблон для разработки кроссплатформенных приложений на **Kotlin Multiplatform (KMP)** с использованием **Jetpack Compose Multiplatform**.

## 📋 Содержание

- [Обзор](#обзор)
- [Технологический стек](#технологический-стек)
- [Архитектура проекта](#архитектура-проекта)
- [Структура проекта](#структура-проекта)
- [Требования](#требования)
- [Установка и запуск](#установка-и-запуск)
- [Основные компоненты](#основные-компоненты)
- [Зависимости](#зависимости)
- [Конфигурация](#конфигурация)

---

## 🎯 Обзор

**KMP Compose Template** — это готовый к использованию шаблон для создания кроссплатформенных приложений, поддерживающих **Android** и **Desktop (JVM)**. Проект демонстрирует современные подходы к разработке с использованием:

- **Clean Architecture** с разделением на слои (Domain, Data, Presentation)
- **MVI/MVVM** паттерн для управления состоянием
- **Decompose** для навигации и управления жизненным циклом компонентов
- **Room Database** для локального хранения данных
- **Koin** для dependency injection
- **Jetpack Compose Multiplatform** для декларативного UI

Проект включает пример работы с пользователями (CRUD операции) и демонстрирует лучшие практики разработки на KMP.

---

## 🛠 Технологический стек

### Основные технологии

| Технология | Версия | Назначение |
|-----------|--------|------------|
| **Kotlin** | 2.2.0 | Основной язык программирования |
| **Compose Multiplatform** | 1.8.2 | Декларативный UI фреймворк |
| **Decompose** | 3.4.0 | Навигация и управление компонентами |
| **Room** | 2.8.3 | Локальная база данных |
| **Koin** | 4.1.1 | Dependency Injection |
| **Ktor** | 3.1.3 | HTTP клиент для сетевых запросов |
| **Kotlinx Coroutines** | 1.10.2 | Асинхронное программирование |
| **Kotlinx Serialization** | 1.9.0 | Сериализация данных |

### Платформы

- **Android**: minSdk 24, targetSdk 36, compileSdk 36
- **Desktop (JVM)**: JVM 17

---

## 🏗 Архитектура проекта

Проект следует принципам **Clean Architecture** с четким разделением ответственности:

```
┌─────────────────────────────────────────┐
│           Presentation Layer            │
│  (UI Components, Compose Screens)       │
│  - UserListContent                      │
│  - UserDetailContent                    │
│  - RootContent                          │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│         Component Layer                 │
│  (Decompose Components, State)          │
│  - RootComponent                        │
│  - UserListComponent                    │
│  - UserDetailComponent                  │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│           Domain Layer                  │
│  (Business Logic, Use Cases)            │
│  - User (model)                         │
│  - UserRepository (interface)           │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│            Data Layer                   │
│  (Repository Implementation, DAO)       │
│  - UserRepositoryImpl                   │
│  - UserDao                              │
│  - AppDatabase                          │
└─────────────────────────────────────────┘
```

### Ключевые принципы

1. **Separation of Concerns**: Каждый слой имеет свою ответственность
2. **Dependency Inversion**: Зависимости направлены от внешних слоев к внутренним
3. **Single Source of Truth**: Room Database как единственный источник данных
4. **Reactive Programming**: Flow для реактивного обновления UI
5. **Unidirectional Data Flow**: Состояние изменяется только через actions

---

## 📁 Структура проекта

```
KMPComposeTemplate/
├── androidApp/                    # Android приложение
│   ├── src/androidMain/
│   │   ├── kotlin/
│   │   │   ├── KmpComposeApp.kt  # Application класс
│   │   │   └── MainActivity.kt    # Главная Activity
│   │   └── res/                   # Android ресурсы
│   └── build.gradle.kts
│
├── desktopApp/                    # Desktop приложение
│   ├── src/desktopMain/
│   │   └── kotlin/
│   │       └── Main.kt            # Точка входа Desktop
│   └── build.gradle.kts
│
├── shared/                        # Общий код для всех платформ
│   ├── src/
│   │   ├── commonMain/kotlin/     # Общий код
│   │   │   ├── components/        # Decompose компоненты
│   │   │   │   ├── root/          # Корневой компонент навигации
│   │   │   │   ├── list/          # Компонент списка пользователей
│   │   │   │   └── detail/        # Компонент деталей пользователя
│   │   │   ├── data/              # Data слой
│   │   │   │   └── repository/    # Реализации репозиториев
│   │   │   ├── database/          # Room Database
│   │   │   │   ├── dao/           # Data Access Objects
│   │   │   │   └── entity/        # Database entities
│   │   │   ├── di/                # Dependency Injection
│   │   │   ├── domain/            # Domain слой
│   │   │   │   ├── model/         # Domain модели
│   │   │   │   └── repository/    # Интерфейсы репозиториев
│   │   │   └── ui/                # UI компоненты Compose
│   │   │       ├── list/          # UI списка пользователей
│   │   │       └── detail/        # UI деталей пользователя
│   │   ├── androidMain/kotlin/    # Android-специфичный код
│   │   │   ├── database/          # Android DatabaseBuilder
│   │   │   └── di/                # Android Koin модули
│   │   └── desktopMain/kotlin/    # Desktop-специфичный код
│   │       ├── database/          # Desktop DatabaseBuilder
│   │       └── di/                # Desktop Koin модули
│   ├── schemas/                   # Room Database схемы
│   └── build.gradle.kts
│
├── gradle/
│   ├── libs.versions.toml         # Централизованное управление версиями
│   └── wrapper/
├── build.gradle.kts               # Корневой build файл
├── settings.gradle.kts            # Настройки проекта
└── README.md                      # Этот файл
```

---

## 💻 Требования

### Для разработки

- **JDK**: 17 или выше
- **Android Studio**: Ladybug (2024.2.1) или новее
- **Gradle**: 8.10.1 (включен в wrapper)
- **Kotlin**: 2.2.0

### Для Android

- **minSdk**: 24 (Android 7.0)
- **targetSdk**: 36 (Android 14)
- **compileSdk**: 36

### Для Desktop

- **JVM**: 17
- **Поддерживаемые ОС**: Windows, macOS, Linux

---

## 🚀 Установка и запуск

### 1. Клонирование репозитория

```bash
git clone <repository-url>
cd KMPComposeTemplate
```

### 2. Запуск Android приложения

#### Через Android Studio:
1. Откройте проект в Android Studio
2. Выберите конфигурацию `androidApp`
3. Нажмите Run (▶️)

#### Через командную строку:
```bash
./gradlew :androidApp:installDebug
```

### 3. Запуск Desktop приложения

#### Через Android Studio:
1. Выберите конфигурацию `desktopApp`
2. Нажмите Run (▶️)

#### Через командную строку:
```bash
./gradlew :desktopApp:run
```

### 4. Сборка дистрибутивов

#### Android APK:
```bash
./gradlew :androidApp:assembleDebug
# APK будет в: androidApp/build/outputs/apk/debug/
```

#### Desktop дистрибутивы:
```bash
./gradlew :desktopApp:packageDistributionForCurrentOS
# Дистрибутивы будут в: desktopApp/build/compose/binaries/main/
```

Поддерживаемые форматы:
- **Windows**: MSI, EXE
- **macOS**: DMG
- **Linux**: DEB

---

## 🧩 Основные компоненты

### 1. RootComponent

Корневой компонент навигации, управляющий стеком экранов.

**Файл**: [`shared/src/commonMain/kotlin/components/root/RootComponent.kt`](shared/src/commonMain/kotlin/components/root/RootComponent.kt)

```kotlin
interface RootComponent {
    val stack: Value<ChildStack<*, Child>>
    fun onBackClicked()
    
    sealed class Child {
        data class UserList(val component: UserListComponent) : Child()
        data class UserDetail(val component: UserDetailComponent) : Child()
    }
}
```

**Особенности**:
- Использует Decompose для управления навигацией
- Поддерживает анимации переходов между экранами
- Сохраняет состояние при изменении конфигурации

### 2. UserListComponent

Компонент для отображения списка пользователей.

**Файл**: [`shared/src/commonMain/kotlin/components/list/UserListComponent.kt`](shared/src/commonMain/kotlin/components/list/UserListComponent.kt)

```kotlin
interface UserListComponent {
    val state: StateFlow<UserListState>
    fun onUserClick(userId: String)
    fun onRefresh()
    fun onAddUserClick()
}

data class UserListState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
```

**Функциональность**:
- Загрузка списка пользователей из БД
- Реактивное обновление через Flow
- Обработка состояний загрузки и ошибок
- Pull-to-refresh функциональность

### 3. UserDetailComponent

Компонент для отображения деталей пользователя.

**Файл**: [`shared/src/commonMain/kotlin/components/detail/UserDetailComponent.kt`](shared/src/commonMain/kotlin/components/detail/UserDetailComponent.kt)

```kotlin
interface UserDetailComponent {
    val state: StateFlow<UserDetailState>
    fun onBackClick()
    fun onDeleteClick()
}

data class UserDetailState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
```

**Функциональность**:
- Загрузка данных конкретного пользователя
- Удаление пользователя
- Навигация назад

### 4. UserRepository

Репозиторий для работы с пользователями.

**Интерфейс**: [`shared/src/commonMain/kotlin/domain/repository/UserRepository.kt`](shared/src/commonMain/kotlin/domain/repository/UserRepository.kt)

**Реализация**: [`shared/src/commonMain/kotlin/data/repository/UserRepositoryImpl.kt`](shared/src/commonMain/kotlin/data/repository/UserRepositoryImpl.kt)

```kotlin
interface UserRepository {
    suspend fun getUsers(forceRefresh: Boolean = false): Result<List<User>>
    suspend fun getUserById(id: String): Result<User?>
    fun observeUsers(): Flow<List<User>>
    fun observeUserById(id: String): Flow<User?>
    suspend fun saveUser(user: User): Result<Unit>
    suspend fun deleteUser(userId: String): Result<Unit>
}
```

**Особенности**:
- Использует Room для локального хранения
- Поддерживает реактивные запросы через Flow
- Генерирует mock-данные при первом запуске
- Обработка ошибок через Result

### 5. AppDatabase

Room Database для локального хранения данных.

**Файл**: [`shared/src/commonMain/kotlin/database/AppDatabase.kt`](shared/src/commonMain/kotlin/database/AppDatabase.kt)

```kotlin
@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
```

**Особенности**:
- Singleton паттерн для единственного экземпляра
- Экспорт схемы для миграций
- Платформо-специфичные билдеры (Android/Desktop)

---

## 📦 Зависимости

### Compose Multiplatform

```toml
compose-runtime = "1.8.2"
compose-foundation = "1.8.2"
compose-material3 = "1.8.2"
compose-ui = "1.8.2"
compose-material-icons-extended = "1.7.3"
```

**Использование**: Декларативный UI для всех платформ

### Decompose

```toml
decompose = "3.4.0"
lifecycle-coroutines = "2.5.0"
```

**Использование**: 
- Навигация между экранами
- Управление жизненным циклом компонентов
- Сохранение состояния

### Room Database

```toml
room = "2.8.3"
sqlite-bundled = "2.6.1"
ksp = "2.2.0-2.0.2"
```

**Использование**:
- Локальное хранилище данных
- Type-safe SQL запросы
- Реактивные запросы через Flow

### Koin

```toml
koin = "4.1.1"
```

**Использование**:
- Dependency Injection
- Управление зависимостями
- Платформо-специфичные модули

### Ktor

```toml
ktor = "3.1.3"
```

**Использование**:
- HTTP клиент для сетевых запросов
- Content negotiation
- Логирование запросов

### Kotlinx Libraries

```toml
kotlinx-coroutines = "1.10.2"
kotlinx-serialization = "1.9.0"
kotlinx-datetime = "0.7.1"
```

**Использование**:
- Асинхронное программирование
- Сериализация JSON
- Работа с датой и временем

### Android Specific

```toml
androidx-core = "1.17.0"
androidx-activity = "1.11.0"
androidx-lifecycle = "2.9.6"
```

---

## ⚙️ Конфигурация

### Gradle Configuration

Проект использует **Version Catalog** для централизованного управления версиями зависимостей.

**Файл**: [`gradle/libs.versions.toml`](gradle/libs.versions.toml)

### Koin Modules

#### Common Module
**Файл**: [`shared/src/commonMain/kotlin/di/SharedModule.kt`](shared/src/commonMain/kotlin/di/SharedModule.kt)

```kotlin
val dataModule = module {
    single { AppDatabase.getInstance() }
    single { get<AppDatabase>().userDao() }
    singleOf(::UserRepositoryImpl) bind UserRepository::class
}
```

#### Android Module
**Файл**: [`shared/src/androidMain/kotlin/di/AndroidKoinModules.kt`](shared/src/androidMain/kotlin/di/AndroidKoinModules.kt)

#### Desktop Module
**Файл**: [`shared/src/desktopMain/kotlin/di/DesktopKoinModules.kt`](shared/src/desktopMain/kotlin/di/DesktopKoinModules.kt)

### Database Configuration

#### Android
**Файл**: [`shared/src/androidMain/kotlin/database/DatabaseBuilder.android.kt`](shared/src/androidMain/kotlin/database/DatabaseBuilder.android.kt)

```kotlin
fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val context = AndroidPlatform.applicationContext
    val dbFile = context.getDatabasePath(AppDatabase.DBNAME)
    return Room.databaseBuilder<AppDatabase>(
        context = context,
        name = dbFile.absolutePath
    )
}
```

#### Desktop
**Файл**: [`shared/src/desktopMain/kotlin/database/DatabaseBuilder.desktop.kt`](shared/src/desktopMain/kotlin/database/DatabaseBuilder.desktop.kt)

```kotlin
fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), AppDatabase.DBNAME)
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath,
    )
}
```

---

## 🎨 UI/UX Features

### Material Design 3

Проект использует **Material Design 3** (Material You) для современного и адаптивного UI:

- **Dynamic Color**: Поддержка динамических цветовых схем
- **Typography**: Современная типографика
- **Components**: Card, TopAppBar, IconButton, и др.
- **Animations**: Плавные переходы между экранами

### Responsive Design

UI адаптируется под разные размеры экранов:
- **Android**: Телефоны и планшеты
- **Desktop**: Окна различных размеров (минимум 1200x800)

### State Management

Все компоненты используют **StateFlow** для управления состоянием:
- Реактивное обновление UI
- Автоматическая подписка/отписка
- Type-safe состояния

---

## 🔧 Расширение проекта

### Добавление нового экрана

1. Создайте интерфейс компонента в `components/`
2. Реализуйте компонент с состоянием
3. Создайте UI в `ui/`
4. Добавьте навигацию в `RootComponent`

### Добавление новой сущности

1. Создайте Entity в `database/entity/`
2. Создайте DAO в `database/dao/`
3. Обновите `AppDatabase`
4. Создайте Domain модель в `domain/model/`
5. Создайте Repository в `domain/repository/` и `data/repository/`
6. Зарегистрируйте в Koin модуле

### Добавление сетевых запросов

1. Создайте API интерфейс с Ktor
2. Добавьте в Repository
3. Реализуйте кэширование через Room
4. Обработайте ошибки

---

## 📝 Лицензия

Этот проект является шаблоном и может быть свободно использован для создания собственных приложений.

---

## 🤝 Вклад в проект

Приветствуются любые улучшения и предложения! Создавайте Issues и Pull Requests.

---

## 📞 Контакты

Для вопросов и предложений создавайте Issues в репозитории проекта.

---

**Создано с ❤️ используя Kotlin Multiplatform и Jetpack Compose**