# EscapeHonbab
Git 저장소 사용법:


(처음 소스코드를 받아올 때, 이미 받아져있고, 다른 사람이 작업한 최신 상태의 소스코드를 받으려면 7번으로) 
1. git clone https://github.com/Singlerr/EscapeHonbab.git 으로 소스코드를 받아옵니다.
2. 소스코드에서 작업을 합니다.
3. git add *으로 모든 파일을 선택합니다.
4. git commit -m "<이름> : <어떤 내용을 수정했는지>" 을 입력합니다. git commit은 자신이 작업한 결과물을 저장소에 업로드하기 위한 박스 포장 단계와 같습니다. 쌍따옴표 안에는 이 결과물을 저장하면서 어떤 메시지를 남길 것인지 적는 것입니다. 아무 형식도 없으면 혼란스러울 수 있으니 <이름> : <어떤 내용을 수정했는지>와 같은 형식을 지킵시다.
5. git remote add origin https://github.com/Singlerr/EscapeHonbab 을 입력하여 파일을 업로드할 저장소를 선택합니다.
6. git push origin master로 소스코드를 업로드합니다.
7. 끝

7. git pull(이미 2~5번을 수행한 적이 있을 경우에만 해당됩니다.)을 입력합니다.
8. 2번부터 차례대로 수행하세요.
