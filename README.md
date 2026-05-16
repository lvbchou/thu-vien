Xây dựng một wep-app quản lý thư viện mượn trả sách.

Làm đơn giản
- Có đăng nhập
- Dashboard gôm tổng quan, sách, khách hàng

- Page sách 
 + có table theo thứ tự gồm id sách, tên sách, tên tác giả, nhà xuất bảng , thể loại, giá nhập, chỗ để - kệ số bao nhiêu, ngày nhập, tình trạng bề ngoài (nứt, rách chỗ nào) và tình trạng (còn, đã mượn, đang bảo hành, mất), tổng số lần được mượn, ngày trả khách cuối cùng mượn
(sort số lần mượn, filter thể loại)
 + Khi bấm vào chi tiết (tên sách), ngoài các thông tin trên còn có: hình ảnh sách, 1 bảng lịch sử mượn gồm id khách đã và hiện mượn, họ tên khách đã và hiện mượn, ngày mượn và hạn mượn, ngày trả và quá hạn bao nhiêu ngày.
(có filter khoảng thời gian mượn - ngày bắt đầu mượn và hạn mượn và thời gian trả)
 + có nút thêm sách
 + Trước mỗi hàng sách có checkbox để xoá nhiều sách cùng 1 lần, sau mỗi hàng sách có nút edit và delete

- Page khách hàng:
 + nút thêm khách hàng, cần làm phiếu chi: loại thẻ thành viên(tháng: 100.000, năm: 600.000)
 + có table gồm các thông tin theo thứ tự: id khách, họ tên ( sẽ có một nút loại thẻ), Sđt, cccd, ngày sinh, ngày tham gia, , ngày làm thẻ, hạn thẻ, số sách đã mượn, số sách đã trả, sách vượt quá hạn ngày trả, số sách đang mượn và số sách quá hạn, nút gia hạn thẻ.
(cần có sort ngày sinh, ngày tham gia, số sách đã mượn, đã trả, đang mượn, quá hạn của khách hàng, filter hạn thẻ)
 + Khi bấm vào chi tiết (tên khách hàng), ngoài các thông tin còn có bảng thông tin chi tiết lịch sử mượn từng sách (gồm id sách, tên sách, tên tác giả, ngày mượn, hạn mượn, ngày trả, quá hạn bao nhiêu ngày, tình trạng trả), có nút thêm mượn (khi nhập 1 sách xong sẽ có nút thêm sách mượn ở dưới và hoàn thành) và nút chỉnh sửa mỗi mục (nút gia hạn), nút trả sách (hiện ra popup gồm list danh sách đang mượn và check box; Với các cuốn sách hết hạn: trễ 1 ngày 10% giá sách, trễ 2 ngày 20% giá sách, trễ 3 ngày 50% giá sách, sau 3 ngày thì bị banned và trả full số tiền, có nút gia hạn cho người liên lạc. (với các sách dưới 50.000 VND thì mặc định trễ 1 ngày: 8k, trễ 2 ngày: 15k, trễ 3 ngày: 30k -> tổng tiền hoàn). hệ thống phải tự động cấm khách hàng sau 3 ngày. khi gia hạn thẻ phải cho chọn thẻ tháng hay năm và hiện thời gian hết hạn của thẻ tiếp theo (ngày bắt đầu là sau ngày kết thúc của thẻ hiện tại)
 + lúc thêm phiếu mượn của khách hàng cần nhập được tên sách hoặc mã id sách rồi ra các gợi ý chứ dropbox vậy sẽ làm mất thời gian tìm sách, số ngày mượn thì phải gợi ý là từ tới ngày mấy sẽ trả sách. hi mượn sách cũng phải tính giá cọc: 50% sách với sách mới mua trong vòng 2 năm và 30% với sách mua sau 2 năm. 

 + Khi trả phải có nút chọn sách cần trả (có thể 1 hoặc nhiều cuốn) -> bảng tình trạng trả cho mỗi cuốn, tổng tiền phạt -> nút đã hoàn lại cho khách kèm hình ảnh chứng minh.


mỗi thao cần có popup thành công hay thất bại (ví dụ: cập nhật sách thành công)
Làm bằng reactjs và java spring boot
nếu tổng số sách mượn cho tới hôm nay = 5 thì không được mượn nữa.

- thanh menu bar bên trái có thể ẩn được để cho diện tích bảng và bảng đẹp hơn

- mượn tối đa 5 sách là tổng sách đang mượn trên hệ thống và sách đang làm phiếu mượn.
- trạng thái thẻ khách hàng ngoài tháng và năm còn có cấm và hết hạn

với lúc trả sách thì nếu họ trả full thì họ phải trả thêm -> tính họ phải trả lại bao nhiêu hoặc phải hoàn họ bao nhiêu, tuỳ vào trường hợp mà nút button là hoàn hay nhận

9/3: gặp thầy
15/3: sketch problem statement
17/3: sửa lỗi logic ps

25/3: figma book
3/4: figma customer
10/4: figma login + dashboard
12/4: hoàn thành FE

13 -> 20/4: nghiên cứu + Build
(Mục tiêu CRUD)
14 -> 15/4: Tạo project Spring Boot trên start.spring.io, cấu hình pom.xml, kết nối PostgreSQL trong application.properties, tạo db (thuviendb), tạo entity (Book.java, Customer.java, BorrowRecord.java)
16/4: Tạo BookRepository, CustomerRepository BorrowRecordRepository, test kết nối Db
18 -> 23/4: Viết DTO file
25 -> 26/4: Viết service file
29 -> 2/5: logic
5 -> 9/5: controller
11 -> 14/5: fix bug, tối ưu sort, filter


cd backend
lsof -ti:8080 | xargs kill -9 2>/dev/null
mvn clean spring-boot:run

cd frontend
npm install
npm start

Check db nằm đâu: psql postgres -c "SHOW data_directory;"
-> sẽ hiện ra đường dẫn kiểu: /usr/local/var/postgresql@16


Backup 1 file để dùng lại: 
pg_dump -U postgres thuviendb > ~/Downloads/thu-vien/backend/thuviendb.sql

Hợp db
psql -U postgres -d thuviendb -f ~/Downloads/seed_3weeks.sql