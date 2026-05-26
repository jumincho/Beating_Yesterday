# Beating Yesterday (어제의 나 이기기)

> 어제의 나와 매일 경쟁하며 자기 성장을 동기부여하는 Android 앱

칼로리 섭취량과 학습 시간을 매일 기록하고, 어제의 기록과 비교해 승부를 판정합니다.
사용자의 BMI를 기준으로 칼로리 점수를 산정하기 때문에 단순한 다이어트 앱이 아닌
개인 맞춤형 자기관리 도구입니다.

## 주요 기능

- **사용자 프로필** — 이름, 성별, 나이, 키, 몸무게 입력 후 BMI 자동 계산
- **식단 관리** — 아침/점심/저녁 음식을 등록하면 식품안전나라 OpenAPI를 통해
  칼로리를 자동 조회하고, BMI 구간(저체중/정상/과체중)에 따른 칼로리 점수를 산정
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

## 빌드 방법

Android Studio에서 프로젝트를 열고 `Run`을 실행하면 됩니다.

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
