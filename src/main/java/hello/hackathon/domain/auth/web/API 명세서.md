### **인증 API 명세**

API 요청 시 헤더에 `Content-Type: application/json`을 포함해주세요.

#### 1. 카카오 로그인

카카오 SDK를 통해 얻은 Access Token으로 서비스 로그인을 요청합니다. 성공 시 서비스의 Access Token, Refresh Token 및 사용자 정보를 반환합니다.

* **URL**: `POST /auth/kakao/login`
* **Request Body**:
  
  ```json
  {
    "kakaoAccessToken": "카카오에서 발급받은 액세스 토큰"
  }
  ```
* **Success Response (200 OK)**:
  
  ```json
  {
    "accessToken": "서비스에서 사용할 액세스 토큰",
    "refreshToken": "토큰 재발급에 사용할 리프레시 토큰",
    "user": {
      "name": "사용자 이름",
      "profileImageUrl": "프로필 이미지 URL"
    }
  }
  ```

#### 2. 토큰 재발급 (Access + Refresh)

만료된 Access Token을 갱신할 때 사용합니다. 기존 Refresh Token을 보내면 새로운 Access Token과 새로운 Refresh Token을 발급(회전)합니다.

* **URL**: `POST /auth/token/refresh`
* **Request Body**:
  
  ```json
  {
    "refreshToken": "로그인 시 받았던 리프레시 토큰"
  }
  ```
* **Success Response (200 OK)**:
  
  ```json
  {
    "accessToken": "새로운 액세스 토큰",
    "refreshToken": "새로운 리프레시 토큰",
    "accessTtlSec": 3600, // 액세스 토큰 만료 시간(초)
    "refreshTtlSec": 1209600 // 리프레시 토큰 만료 시간(초)
  }
  ```

#### 3. Access Token만 재발급

Refresh Token의 만료 기간이 충분히 남았을 때, Access Token만 재발급받고 싶을 경우 사용합니다.

* **URL**: `POST /auth/token/access`
* **Request Body**:
  
  ```json
  {
    "refreshToken": "로그인 시 받았던 리프레시 토큰"
  }
  ```
* **Success Response (200 OK)**:
  
  ```json
  {
    "accessToken": "새로운 액세스 토큰",
    "accessTtlSec": 3600 // 액세스 토큰 만료 시간(초)
  }
  ```

#### 4. 로그아웃

서비스에서 로그아웃합니다. 서버에 저장된 Refresh Token을 폐기 처리합니다.

* **URL**: `POST /auth/logout`
* **Request Header**:
  
  ```
  Authorization: Bearer <서비스의 액세스 토큰>
  ```
* **Request Body (Optional)**:
  * 가장 최근에 발급된 Refresh Token을 명시적으로 폐기하고 싶을 때 사용합니다. 필수는 아닙니다.
    
    ```json
    {
    "refreshToken": "폐기할 리프레시 토큰"
    }
    ```
* **Success Response (204 No Content)**:
  * 성공 시 Body 없이 204 상태 코드를 반환합니다.
