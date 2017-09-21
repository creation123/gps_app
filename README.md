#gps_app

version 0.0.0 
Features : User can register his ID onto the server, User can log in / log out, If User is located into the targetted gps location, he can click "attend"-> attend data is added onto the sever database





version 0.0.1 (plan)
modify the datatable -> contain arrive time (datetime) and leave time (datetime) -> 
Show user the following information in the MainActivity

"출근", "퇴근", "logout"

database records the following

ID / User_Id / time1(출근) / time2(퇴근)

edge cases -> allow multiple 출퇴근 in a day

Procedure

출근을 누름
1: 해당유저에게 해당되는 row 모두확인 혹시나 출근만 기록되있고 퇴근이 기록 안되있는 row 있으면 애러처리
2: 해당유저에게 해당되는 row 모두 괜찮을시
-> 새로운 row 생성하기

퇴근을 누름
1: 해당유저에게 해당되는 row 모두확인 혹시나 출근만 기록되있는 row 가 아무것도 없으면 애러처리
2: 해당유저에게 해당되는 row 존재할시
-> 출근만있는 row 수정하기









