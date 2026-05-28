<div align="center">

# beating-yesterday

**어제의 나와 경쟁하며 자기 성장을 동기부여하는 Android 앱**
**Android app that gamifies daily self-improvement against yesterday's record**

![Platform](https://img.shields.io/badge/platform-Android-3DDC84?logo=android&logoColor=white)
![Language](https://img.shields.io/badge/language-Java-007396?logo=java&logoColor=white)
![Min SDK](https://img.shields.io/badge/minSdk-21-blue)
![License](https://img.shields.io/badge/license-MIT-green)
![Year](https://img.shields.io/badge/year-2021-blue)

**한국어** · [English](#english) · [中文](./README.zh-CN.md)

</div>

---

## 개요

> 어제의 나와 매일 경쟁하며 자기 성장을 동기부여하는 Android 앱

칼로리 섭취량과 학습 시간을 매일 기록하고, 어제의 기록과 비교해 승부를 판정합니다.
사용자의 BMI를 기준으로 칼로리 점수를 산정하기 때문에 단순한 다이어트 앱이 아닌
개인 맞춤형 자기관리 도구입니다.

## 주요 기능

- **사용자 프로필** — 이름, 성별, 나이, 키, 몸무게 입력 후 BMI 자동 계산
- **식단 관리** — 아침/점심/저녁 음식을 등록하면 식품안전나라 OpenAPI를 통해
  칼로리를 자동으로 조회하고, BMI 구간(저체중/정상/과체중)에 따른 칼로리 점수를 산정
- **공부 타이머** — 시/분/초를 드래그로 설정하는 원형 타이머와 스톱워치 기능
- **할 일 관리** — SQLite 기반 TODO 리스트 (Swipe-to-refresh 지원)
- **승부 판정** — 어제의 칼로리 점수 + 학습 시간 vs 오늘의 기록을 비교해
  "어제의 나를 이겼는지" 결과를 알려줌

## 화면 구성

세 개의 탭으로 구성된 Bottom Navigation:

| 탭 | 설명 |
| --- | --- |
| Diet | 식단 입력 · 체중 관리 |
| Home | 사용자 정보 · 어제와의 승부 판정 |
| Productivity | TODO 리스트 · 공부 타이머 |

## 기술 스택

- **언어**: Java
- **플랫폼**: Android (minSdk 21 / targetSdk 32)
- **아키텍처**: Fragment + ViewModel
- **저장소**: SharedPreferences (사용자 데이터, 일일 기록) + SQLite (TODO)
- **네트워크**: HttpURLConnection + 식품안전나라 OpenAPI
- **UI**: Material Components, ConstraintLayout, RecyclerView, Navigation Component

## 프로젝트 구조

```
app/src/main/
├── java/com/jumincho/beatingyesterday/
│   ├── MainActivity.java
│   ├── data/                          # 데이터 계층
│   │   ├── FoodCalorieApi.java        # 식품안전나라 API 클라이언트
│   │   ├── Note.java                  # TODO 모델
│   │   ├── NoteAdapter.java
│   │   └── NoteDatabase.java          # SQLite 헬퍼
│   └── ui/
│       ├── ProfileSetupActivity.java  # 최초 실행 시 프로필 입력
│       ├── home/                      # 홈 (승부 판정)
│       ├── diet/                      # 식단/체중 관리
│       └── productivity/              # TODO + 타이머
│           ├── CircularTimerView.java # 커스텀 원형 타이머 View
│           └── TimerMode.java
└── res/
    ├── layout/        # 화면 레이아웃
    ├── navigation/    # 하단 탭 네비게이션 그래프
    └── ...
```

## 시크릿 처리

빌드 시스템은 `local.properties` 에서 식품안전나라 OpenAPI 키를 읽어 Gradle
`buildConfigField` 를 통해 `BuildConfig.FOOD_API_KEY` 로 주입합니다. 키 문자열은
소스에 포함되지 않으며, `local.properties` 는 `.gitignore` 에 포함되어 있습니다.

| `local.properties` 키 | `BuildConfig` 필드 | 사용처 |
| --- | --- | --- |
| `FOOD_API_KEY` | `BuildConfig.FOOD_API_KEY` | 식품안전나라 칼로리 조회 |

## 빌드 방법

1. [식품안전나라 OpenAPI](https://various.foodsafetykorea.go.kr/nutrient/) 에서 API 키 발급
2. 프로젝트 루트의 `local.properties` 에 다음 줄 추가

   ```properties
   FOOD_API_KEY=발급받은_키
   ```

3. Android Studio 에서 프로젝트 열기 → `Run`

```bash
./gradlew assembleDebug
```

요구사항:
- Android Studio Bumblebee 이상
- JDK 8 이상
- Android SDK 32

## 발표 자료

- 발표 영상: <https://youtu.be/vW4CvgCHdco>
- 발표 슬라이드: [`docs/presentation.pptx`](docs/presentation.pptx)

## 스크린샷

<img width="20%" src="https://user-images.githubusercontent.com/77545063/200374902-2da72615-5cf8-4d20-b950-f00962a1c795.png" alt="앱 스크린샷"/>

## 라이선스

[MIT License](./LICENSE)

---

<a name="english"></a>

## English

> Android app that gamifies daily self-improvement against yesterday's record.

Logs daily calorie intake and study time, then judges today's record against yesterday's.
Calorie scoring is BMI-aware, so this is less a diet tracker than a personalized
self-management tool that frames each day as a contest with yesterday's you.

### Features

- **User profile** — name, sex, age, height, weight → auto-computed BMI.
- **Diet** — log breakfast / lunch / dinner; the Korean Food Safety OpenAPI returns calories, and a BMI-bracket-aware (under / normal / over) calorie score is computed.
- **Study timer** — circular timer with drag-to-set hours / minutes / seconds, plus a stopwatch.
- **TODO** — SQLite-backed list with swipe-to-refresh.
- **Daily contest** — yesterday's calorie score + study time vs today's, reported as "did you beat yesterday?"

### Screens

Bottom navigation with three tabs:

| Tab | Description |
| --- | --- |
| Diet | Food entry · weight management |
| Home | Profile · daily yesterday-vs-today contest |
| Productivity | TODO · study timer |

### Tech stack

- **Language**: Java
- **Platform**: Android (minSdk 21 / targetSdk 32)
- **Architecture**: Fragment + ViewModel
- **Storage**: SharedPreferences (profile, daily records) + SQLite (TODO)
- **Network**: HttpURLConnection + Korean Food Safety OpenAPI
- **UI**: Material Components, ConstraintLayout, RecyclerView, Navigation Component

### Layout

```
app/src/main/
├── java/com/jumincho/beatingyesterday/
│   ├── MainActivity.java
│   ├── data/                          # data layer
│   │   ├── FoodCalorieApi.java        # Food Safety API client
│   │   ├── Note.java                  # TODO model
│   │   ├── NoteAdapter.java
│   │   └── NoteDatabase.java          # SQLite helper
│   └── ui/
│       ├── ProfileSetupActivity.java  # first-launch profile entry
│       ├── home/                      # home (contest result)
│       ├── diet/                      # diet / weight management
│       └── productivity/              # TODO + timer
│           ├── CircularTimerView.java # custom circular timer view
│           └── TimerMode.java
└── res/
    ├── layout/        # screen layouts
    ├── navigation/    # bottom-tab navigation graph
    └── ...
```

### Secrets handling

The build system reads the Food Safety OpenAPI key from `local.properties` and
injects it via Gradle `buildConfigField` as `BuildConfig.FOOD_API_KEY`. No
secrets are present in source. `local.properties` is gitignored.

| `local.properties` key | `BuildConfig` field | Use site |
| --- | --- | --- |
| `FOOD_API_KEY` | `BuildConfig.FOOD_API_KEY` | Food Safety calorie lookup |

### Build

1. Get an API key from the [Korean Food Safety OpenAPI](https://various.foodsafetykorea.go.kr/nutrient/).
2. Add a `local.properties` entry at the repo root:

   ```properties
   FOOD_API_KEY=your_key_here
   ```

3. Open the project in Android Studio → `Run`.

```bash
./gradlew assembleDebug
```

Requirements:
- Android Studio Bumblebee+
- JDK 8+
- Android SDK 32

### Materials

- Demo video: <https://youtu.be/vW4CvgCHdco>
- Slides: [`docs/presentation.pptx`](docs/presentation.pptx)

### License

[MIT License](./LICENSE)
