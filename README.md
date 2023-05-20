**Xây dựng chương trình thực hiện giao thức CoAP**

Nhóm 7

Trường Đại học Công nghệ Đại học Quốc gia Hà Nội

Môn: Lập trình mạng(2223II_INT3304_20)

Thầy:Nguyễn Ngọc Tân

# I.  **Tổng quan về giao thức CoAP**

## 1.  **Giới thiệu về CoAP**

 CoAP, viết tắt của "Constrained Application Protocol" (Giao thức Ứng
 dụng Hạn chế), là một giao thức truyền thông nhẹ nhàng và hiệu quả,
 được đặc biệt thiết kế để hoạt động trong môi trường Internet of
 Things (IoT) - một môi trường đa dạng với sự xuất hiện ngày càng nhiều
 của các thiết bị thông minh, từ cảm biến đến các thiết bị di động và
 hệ thống nhúng. Trong môi trường này, các thiết bị thường có tài
 nguyên hạn chế về băng thông, năng lượng và khả năng tính toán, điều
 này đặt ra một thách thức lớn cho việc truyền thông và giao tiếp. CoAP
 đã được phát triển để đáp ứng các yêu cầu của IoT và tối ưu hóa sự
 truyền thông giữa các thiết bị. Giao thức này sử dụng mô hình
 Client-Server, trong đó các thiết bị được chia thành hai vai trò:
 Client (khách hàng) và Server (máy chủ).

## 2.  **Sự phát triển của CoAP**

 CoAP được phát triển bởi IETF (Internet Engineering Task Force) nhằm
 thay thế cho giao thức truyền thông HTTP (Hypertext Transfer Protocol)
 trên môi trường Internet of Things (IoT). Trong quá trình phát triển,
 CoAP đã trải qua một loạt các phiên bản và cải tiến để đáp ứng được
 yêu cầu của các ứng dụng IoT ngày càng phức tạp.

 CoAP là một giao thức dựa trên UDP (User Datagram Protocol), một giao
 thức giao tiếp không đáng tin cậy, tuy nhiên, điều này cho phép CoAP
 hoạt động hiệu quả trong môi trường IoT với tài nguyên hạn chế. UDP
 không yêu cầu việc thiết lập và duy trì kết nối như TCP, điều này giúp
 giảm thiểu độ trễ và tải lưu lượng mạng, đồng thời tiết kiệm năng
 lượng của các thiết bị IoT.

 CoAP hỗ trợ các yêu cầu phổ biến như GET, POST, PUT và DELETE, tương
 tự như trong giao thức HTTP. Nhờ các yêu cầu này, các thiết bị IoT có
 thể truy cập và thao tác với các tài nguyên thông qua giao thức CoAP.
 Điều này cho phép việc truyền thông và điều khiển linh hoạt hơn trong
 môi trường IoT, nơi mà sự tương tác và quản lý tài nguyên là rất quan
 trọng

## 3.  **Các đặc tính của CoAP**

 Các đặc tính của CoAP bao gồm: Thông lượng và độ trễ thấp, độ tin cậy
 thấp, bảo mật, khả năng mở rộng và tiết kiệm năng lượng. CoAP có thể
 hoạt động với thông lượng và độ trễ thấp, giúp cải thiện hiệu suất
 truyền tải dữ liệu trong môi trường IoT. CoAP hỗ trợ độ tin cậy thấp
 thông qua việc sử dụng bộ đếm thời gian và thực hiện truyền lại các
 gói tin bị mất. CoAP hỗ trợ các tính năng bảo mật như xác thực, mã hóa
 và định tuyến an toàn. CoAP có thể mở rộng để hỗ trợ các ứng dụng và
 thiết bị mới một cách dễ dàng. Cuối cùng, CoAP được thiết kế để tiết
 kiệm năng lượng, giúp tăng thời lượng hoạt động của các thiết bị IoT.

## 4.  **Sử dụng của CoAP**

 CoAP đã được áp dụng và sử dụng rộng rãi trong nhiều lĩnh vực và ứng
 dụng trong môi trường Internet of Things (IoT). Dưới đây là một số ví
 dụ về cách CoAP được áp dụng:

-   Quản lý mạng cảm biến: Mạng cảm biến không dây trong các hệ thống
     IoT thường bao gồm nhiều thiết bị cảm biến phân tán. CoAP cung cấp
     giao thức truyền thông nhẹ nhàng và hiệu quả để quản lý và giao
     tiếp với các thiết bị cảm biến này. Việc sử dụng CoAP trong quản
     lý mạng cảm biến giúp giảm thiểu tải lưu lượng mạng, tiết kiệm
     năng lượng và đảm bảo truyền thông dữ liệu đáng tin cậy.

-   Quản lý thiết bị IoT: CoAP cung cấp khả năng điều khiển và quản lý
     các thiết bị IoT từ xa. Với việc hỗ trợ các yêu cầu GET, POST, PUT
     và DELETE, CoAP cho phép gửi các yêu cầu điều khiển và nhận phản
     hồi từ các thiết bị IoT. Điều này cho phép các hệ thống quản lý và
     giám sát từ xa để kiểm soát và theo dõi trạng thái của các thiết
     bị IoT một cách linh hoạt và tiện lợi.

-   Truyền tải dữ liệu: CoAP được sử dụng để truyền tải dữ liệu giữa các
     thiết bị trong môi trường IoT. Với việc tối ưu hóa cho các mạng có
     băng thông thấp và độ trễ cao, CoAP giúp cải thiện hiệu suất
     truyền tải dữ liệu. Các ứng dụng như giám sát môi trường, hệ thống
     an ninh, và hệ thống đo lường và điều khiển đều có thể sử dụng
     CoAP để truyền tải dữ liệu một cách hiệu quả và đáng tin cậy.

-   Môi trường công nghiệp thông minh: Trong môi trường công nghiệp
     thông minh, CoAP được sử dụng để kết nối và truyền thông giữa các
     thiết bị công nghiệp như cảm biến, máy móc và hệ thống điều khiển.
     Giao thức này giúp đơn giản hóa việc giao tiếp và tương tác giữa
     các thành phần trong hệ thống công nghiệp thông minh, từ đó cải
     thiện quản lý và kiểm soát quá trình sản xuất và tối ưu hóa hiệu
     suất làm việc.

## 5, **Ưu điểm, nhược điểm của CoAP**

### a.  Ưu điểm

Thông lượng và độ trễ thấp: CoAP được thiết kế để hoạt động trên môi
 trường IoT, với khả năng truyền tải dữ liệu trong thời gian ngắn và
 với băng thông thấp. Giao thức này sử dụng kết nối UDP thay vì TCP,
 cho phép giảm độ trễ và tối ưu hóa thông lượng.

 Độ tin cậy thấp: CoAP không yêu cầu mức độ đảm bảo tin cậy như giao
 thức HTTP, cho phép các thiết bị IoT sử dụng các nguồn tài nguyên hạn
 chế như pin, bộ nhớ và băng thông một cách hiệu quả. Để đảm bảo độ tin
 cậy cho gói tin, CoAP sử dụng các thông tin đánh giá như độ trễ, xác
 nhận, tối ưu gói tin và giảm thiểu số lượng gói tin gửi đi.

 Tiết kiệm năng lượng: CoAP được thiết kế để tiết kiệm năng lượng, cho
 phép các thiết bị IoT hoạt động lâu hơn. Giao thức này cho phép các
 thiết bị IoT tối ưu hóa năng lượng bằng cách sử dụng các cơ chế như
 phương thức truyền dữ liệu, phương thức chuyển tiếp và chọn địa chỉ.
 Các cơ chế này giúp giảm thiểu số lượng gói tin truyền tải và tối ưu
 hóa độ trễ và độ tin cậy.

 Hỗ trợ bảo mật: CoAP hỗ trợ các tính năng bảo mật như xác thực, mã hóa
 và định tuyến an toàn. Giao thức này sử dụng các tiêu chuẩn bảo mật
 như DTLS (Datagram Transport Layer Security) và OSCORE (Object
 Security for Constrained RESTful Environments) để đảm bảo an toàn cho
 dữ liệu truyền tải trên mạng IoT.

 Dễ dàng mở rộng: CoAP cho phép các nhà phát triển dễ dàng mở rộng giao
 thức để hỗ trợ các ứng dụng mới hoặc các thiết bị mới. Giao thức này
 được thiết kế để hỗ trợ việc đăng ký và phát hiện tài nguyên một cách
 linh hoạt, cho phép các thiết bị IoT dễ dàng tương tác với nhau.

### b.  Hạn chế

 Cơ chế định tuyến hạn chế: CoAP sử dụng cơ chế định tuyến bằng IP
 multicast, tuy nhiên IP multicast không được hỗ trợ trên một số mạng
 IoT như mạng LoRaWAN. Điều này có thể gây ra khó khăn trong việc
 truyền tải dữ liệu trong mạng IoT.

 Không thể sử dụng trên mạng Internet: CoAP được thiết kế để hoạt động
 trên các mạng IoT cục bộ và không thể sử dụng trên mạng Internet, điều
 này giới hạn tính ứng dụng của giao thức này.

 Thời gian hoàn tất yêu cầu chậm: CoAP sử dụng các cơ chế giảm thiểu số
 lượng gói tin để tối ưu hóa độ trễ, điều này có thể dẫn đến việc thời
 gian hoàn tất yêu cầu chậm hơn so với giao thức HTTP truyền thống.

## 7. **Cấu trúc CoAP**

### a.  Định dạng gói tin

<p align="center">
<img src="https://raw.githubusercontent.com/U3Vzc3kgQW1vZ3Vz/imagedump/main/coapImg/img3.png"/>
</p>

- Việc truyền các gói tin với CoAP tương tự như với HTTP. Tuy nhiên, ở
CoAP, giao thức tầng ứng dụng được sử dụng là UDP, máy chủ xử lý không
đồng bộ, có thể hỗ trợ tùy chọn truyền tin tin cậy dựa trên cơ chế
truyền lại các gói tin lỗi hoặc mất mát (xác định qua time out).</br>
- Định dạng thông điệp CoAP:

<p align="center">
<img src="https://raw.githubusercontent.com/U3Vzc3kgQW1vZ3Vz/imagedump/main/coapImg/img2.png"/>
</p>
- Một thông điệp CoAP bao gồm 4 bytes header mặc định. Tùy vào loại
 thông điệp mà còn có thể có thêm các bytes cho Option (tùy chọn),
 payload và Token. CoAP hỗ trợ 4 loại thông điệp: CON (confirmable --
 có thể xác nhận), NON (non-confirmable -- không thể xác nhận), ACK
 (acknowledgment -- xác nhận) và RST (reset -- đặt lại).

-   Version (Ver): phiên bản của giao thức CoAP, phiên bản duy nhất hiện
     nay là 1 (theo CoAP rfc 7252).

-   Kiểu thông điệp (T): CoAP hỗ trợ 4 loại thông điệp: CON (confirmable
     -- có thể xác nhận), NON (non-confirmable -- không thể xác nhận),
     ACK (acknowledgment -- xác nhận) và RST (reset -- đặt lại).

-   Token length (TKL): số bytes có trong phần token.

-   Mã thông điệp (Code): Mỗi code gồm 8 bits, chia ra thành 3 bit class
     và 5 bit detail. Dải giá trị của mã thông điệp theo RFC 7252 được
     xác định như sau:

    -   0.00: Empty message

    -   0.01-0.31 Request. Chi tiết các giá trị tham khảo tại [[RFC
         7252](https://www.rfc-editor.org/rfc/rfc7252#section-12.1.1)]

    -   2.00-5.31 Phản hồi. Chi tiết các giá trị tham khảo tại [[RFC
         7252](https://www.rfc-editor.org/rfc/rfc7252#section-12.1.2)]

-   ID thông điệp (Message ID): số nguyên dương 16 bit. Sử dụng để phát
     hiện thông điệp trùng lặp và match các message với ACK/RST của nó.

-   Token value (TKL bytes): sử dụng để match 1 request với 1 response

-   Options: (Bổ xung tính năng)

    -   Option Delta: Số nhị phân không dấu 4 bit, được sử dụng để tính
         Option Number (có giá trị bằng Option number trước đó +
         delta). Ý nghĩa của các Option Number được định nghĩa tại
         [[RFC
         7252](https://www.rfc-editor.org/rfc/rfc7252#section-12.2)]

    -   Option Length: số nhị phân không dấu 4 bit, biểu diễn số byte có
         trong phần option payload.

    -   Định dạng:

 <p align="center">
<img src="https://raw.githubusercontent.com/U3Vzc3kgQW1vZ3Vz/imagedump/main/coapImg/options.jpg"/>
</p>

### b.  Phương thức CoAP

 Tương tự HTTP, CoAP cung cấp 4 phương thức cơ bản theo cấu trúc REST:
 GET, POST, PUT và DELETE. Mã của 4 phương thức được định nghĩa trong
 phần code của header như sau:

+------+------------+---------------+</br>
|Code| &ensp;Name&emsp;| Reference&emsp;|</br>
+------+------------+---------------+</br>
| 0.01 |&ensp;&ensp; GET&ensp;&ensp;|
[[RFC7252](https://www.rfc-editor.org/rfc/rfc7252)] |</br>
| 0.02 |&ensp;&ensp;POST&ensp;|
[[RFC7252](https://www.rfc-editor.org/rfc/rfc7252)] |</br>
| 0.03 |&ensp;&ensp; PUT&ensp;&ensp;|
[[RFC7252](https://www.rfc-editor.org/rfc/rfc7252)] |</br>
| 0.04 |DELETE&ensp;|
[[RFC7252](https://www.rfc-editor.org/rfc/rfc7252)] |


### c.  Mô hình yêu cầu/phản hồi

CoAP truyền tin theo mô hình yêu cầu/ phản hồi tương tự với HTTP. Tuy
nhiên thay vì truyền tin qua một kết nối được thiết lập từ trước, CoAP
truyền tin bất đồng bộ bằng việc gửi thông điệp CoAP. Client sẽ gửi 1
hoặc nhiều yêu cầu CoAP và Server sẽ phục vụ client bằng việc gửi lại
phản hồi CoAP.

Có 3 kiểu truyền thông điệp: piggy-backed, phản hồi riêng biệt, và yêu
 cầu và phản hồi không xác nhận:</br>
- Piggy-bagged: Client gửi thông điệp CON hoặc NON và nhận phản hồi 
ACK chứa thông điệp có thể xác nhận ngay lập tức

<p align="center">
<img src="https://raw.githubusercontent.com/U3Vzc3kgQW1vZ3Vz/imagedump/main/coapImg/unnamed.png"/>
</p></br>

- Phản hồi riêng biệt: Nếu server nhận thông điệp CON mà chưa thể phản
hồi thì có thể gửi thông điệp ACK trống. Khi sẵn sàng, Server gửi CON mới cho client và client phản hồi với ACK.
<p align="center">
<img src="https://raw.githubusercontent.com/U3Vzc3kgQW1vZ3Vz/imagedump/main/coapImg/delayedresponse.png"/>
</p>

- Yêu cầu phản hồi không xác nhận: Client gửi yêu cầu dưới dạng
     NON, Server phản hồi thông điệp NON hoặc CON.
<p align="center">
<img src="https://raw.githubusercontent.com/U3Vzc3kgQW1vZ3Vz/imagedump/main/coapImg/nontransport.png"/>
</p>

### d.  Cơ chế quan sát (Observation)

 Dựa trên mô hình thiết kế
 [[GOF](https://www.rfc-editor.org/rfc/rfc7641#ref-GOF)],
 theo đó một "người quan sát" sẽ đăng ký một đối tượng chỉ định đặc
 biết. Mỗi khi đối tượng này thay đổi trạng thái, người quan sát sẽ
 nhận được thông báo về đối tượng này.

 Tương tự vậy, CoAP hỗ trợ tính năng observation, cho phép client "quan
 sát" một hoặc nhiều tài nguyên mà server quản lý. Mỗi lần giá trị của
 tài nguyên thay đổi, server sẽ gửi thông báo về sự thay đổi đó cho
 client.

 <p align="center">
<img src="https://raw.githubusercontent.com/U3Vzc3kgQW1vZ3Vz/imagedump/main/coapImg/observer.jpg"/>
</p>

# II. Giới thiệu về chương trình




## a.  Giao thức:

### **Server**

Server xử lý đồng thời đơn luồng bằng 1 Datagram Channel.
```java
public CoapServer() {
try {
port = CoAP.DEFAULT_PORT;

            selector = Selector.open();
            channel = DatagramChannel.open();
            InetSocketAddress address = new InetSocketAddress(port);
            channel.socket().bind(address);
            channel.configureBlocking(false);

            channel.register(selector, SelectionKey.OP_READ);

            buffer = ByteBuffer.allocate(BUFFER_SIZE);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
```

 Bên trong server, các resource tổ chức dưới dạng cây, với gốc là một
 Root Resource. Các resource ngoài lưu trữ thông tin về resource cha
 của nó còn lưu trữ tập các resource con trong một map. Điều này tạo
 thuận tiện trong việc tìm kiếm resource.
```java
    private Resource parent;
    private final HashMap<String, Resource> children = new HashMap<>();
```

 Giao thức thiết kế ở đây cho phép client sử dụng 2 phương thức là GET
 và POST. Các giao thức khác được yêu cầu sẽ bị trả về phản hồi với mã
 METHOD NOT ALLOWED. Khi một request gửi đến, server sẽ xác định
 resource yêu cầu qua uri-path trong option value của request. Sau đó
 sẽ gọi hàm xử lý yêu cầu của resource, đây là 1 hàm mà người dùng có
 thể Override để tùy chỉnh cách xử lý request. Nếu resource yêu cầu
 không tồn tại, server trả phản hồi mã lỗi NOT FOUND.

 Thuật toán tìm kiểm resource tương tự thuật toán tìm kiếm trong cấu
 trúc dữ liệu Trie, nhưng thay vì đầu vào là 1 mảng ký tự thì đầu vào ở
 đây là 1 mảng các resource sắp xếp theo thứ tự có trong uri-path của
 request.
```java
    public CoapResource getResource(String url) {
        try {
            if (url.charAt(0) != '/')
                return null;
            String[] name = url.substring(1).split("/");
            Resource resource = getResource(this.root, name, 0);
            return (CoapResource) resource;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Resource getResource(Resource resource ,String[] name, int index) {
        try {
            if (resource == null)
                return null;

            if (index == name.length)
                return resource;

            return getResource(resource.getChild(name[index]), name, index + 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
```

 Giao thức cũng cung cấp tính năng quan sát (Observation). Khi một
 request gửi đến với mã GET và Option Definition là OBSERVE (theo RFC
 7641), nếu resource là một resource quan sát được, nó sẽ thêm địa chỉ
 socket của client (biểu diễn bằng class Endpoint gồm 2 thành phần định
 danh một socket UDP là cổng và địa chỉ đích), đồng thời phản hồi 1
 response mã 2.05 (CONTENT).

 Mỗi lần resource thay đổi trạng thái (được quyết định bởi người cài
 đặt resource), nó sẽ duyệt qua toàn bộ danh sách các Endpoint đăng ký
 và gửi thông báo.

###  **Client**

 Khởi tạo với đầu vào là URL của resource muốn request.
```java
CoapClient client=new CoapClient("coap://localhost:5683/temp");
```

 Ở đây chúng tôi cài đặt 2 phương thức GET và POST, sử dụng truyền tin
 không tin cậy với request type là NON. 2 phương thức trả về response
 tương ứng với request đã gửi. Nếu không nhận được request trong thời
 gian time out (2 giây), trả về giá trị null.
```java
Response response1= client.get();
Response response2=client.post("hello".getBytes());
```
Tính năng quan sát tài nguyên được thực hiện qua Class ObservationHandler. Class thực hiện thao tác đăng ký bằng truyền tin đảm bảo với UDP và nhận thông báo theo truyền tin không đảm bảo.
-   Thuật toán truyền tin tin cậy khi đăng ký:

    -   ObservationHandler gửi request GET, type CON, Option Definition
         OBSERVE và Option value là URL của resource đến Server.

    -   Socket datagram trong Observation chờ phản hồi, time out 3 giây.

    -   Nếu tài nguyên không quan sát được (response code METHOD NOT
         ALLOW), kết thúc đăng ký, đặt cờ Observing = 0;

    -   Nếu nhận được phản hồi xác nhận (type ACK, Code CONTENT) có
         token khớp với token request; kết thúc quá trình đăng ký.

    -   Nếu không nhận được xác nhận do time out hoặc phản hồi không
         thỏa mã điều kiện ở bước 4, lặp lại bước 1.

-   

 Khi hủy đăng ký, cờ cancelled sẽ bật, lúc này, với mỗi thông báo từ
 Server gửi đến, ObservationHanlder sẽ gửi 1 request type RST, Option
 Definition là OBSERVE, có URL của resource hủy thông báo ở phần option
 value. Server khi nhận được sẽ xóa Endpoint của client ra khỏi danh
 sách đăng ký theo dõi resource đó.

## b.  **Backend**

###   Mô tả:

 Ứng dụng gồm 1 server và 1 tập các sensor. Sensor sinh dữ liệu nhiệt
 độ gửi đến cho server, server nhận dữ liệu và biểu diễn bằng biểu đồ
 trung bình nhiệt độ do client gửi đến, cùng với số sensor đang kết nối
 đến server. Phía server cho phép gửi dữ liệu điều khiển (bật và tắt
 việc truyền dữ liệu) sensor. Client và server giao tiếp với nhau bằng
 giao thức đã xây dựng bên trên. Phía server cho phép hiển thị các
 thông số đánh giá hiệu năng giao thức: delay và throughout theo thời
 gian.

###   Mô hình thiết kế: Chương trình gồm các thành phần chính: Server và Sensor simulator
 -   Server: gồm phần xử lý back end, database lưu thông tin về
         sensor gửi đến và phần giao diện:

        -   Phần server back end dựa trên 1 CoapServer với 2 tài nguyên:
             /temperature là nhiệt độ do các sensor gửi đến và /command
             là dữ liệu điều khiển từ phía server.

        -   Database lưu các thông số:

            -   Sensor_id: id của sensor gửi đến

            -   Sensor_data: giá trị dữ liệu sensor gửi đến

            -   Last_time_modified: thời gian cuối sensor gửi dữ liệu
                 đến server. Last_time_modified biểu diễn ở dạng nano
                 giây.

            -   Delay: độ trễ trong lần gửi gần nhất của sensor

            -   Throughput: Thông lượng trong lần truyền gần nhất của
                 sensor.
<p align="center">
<img src="https://raw.githubusercontent.com/U3Vzc3kgQW1vZ3Vz/imagedump/main/coapImg/database.png"/>
</p></br>


   - Tính toán delay: Khi nhận được 1 gói tin từ sensor, server
        lấy thời gian nhận phía server trừ đi thời gian gửi được
             mô tả trong gói gói tin sensor gửi đến (nói ở phía bên
             dưới). Dữ liệu biểu diễn trên biểu đồ là trung bình delay
             của các sensor kết nối đến server.
   - Tính toán throughput: Để tính throughtput ta lấy kích thước
               gói tin gửi đến chia cho delay nhận được gói tin đó. Dữ
               liệu biểu diễn trên biểu đồ là trung bình throughput của
               các sensor kết nối đến server.

   - Xác định số sensor đang kết nối: Server kiểm tra thời gian
               gửi dữ liệu cuối của các sensor theo chu kỳ 6 giây. Các
               sensor không gửi dữ liệu nào trong khoảng thời gian này sẽ
               được xem là đã ngắt kết nối và bị xóa khỏi database.

   -   Gửi dữ liệu điều khiển: Phía server tạo 1 client kết nối đến
            /command, client này sẽ gửi gói tin chữa lệnh điều khiển
            đến cho server, các sensor đang quan sát tài nguyên này sẽ
            nhận được thông báo và thực hiện lệnh điều khiển.
 -   Sensor simulator:

        -   Gồm 2 CoapClient: 1 client kết nối đến tài nguyên
             /temperature để gửi dữ liệu, và 1 client kết nối đến tài
             nguyên /command để nhận dữ liệu điều khiển từ server.
             Client kết nối đến tài nguyên /command sẽ để ở trạng thái
             quan sát tài nguyên này.
     
        -   Các message sử dụng thư viện ngoài faster.xml.jackson để
             chuyển các class massage sang dạng json.

 Định dạng gói tin client nhận được từ /command
```json
{

"Sensor_id":,// id của client sẽ thực hiện lệnh trong command

 "Command": //lệnh điều khiển từ server. Có 2 lệnh là SUSPEND để tạm dừng việc gửi dữ liệu và RESUME để sensor đang tạm dừng gửi dữ liệu trở lại

}
```


 
Định dạng gói tin của client gửi đến /temperature:

```json
 {
  "Sensor_id":,
  //id của sensor gửi đến.

  "Temperature":,
  //giá trị nhiệt độ sensor đo được

  "Last_time_modified": 
  //thời gian gửi bên phái sensor.
}
```
 Để khởi tạo 1 cảm biến nhiệt độ, ta khởi tạo cảm biến với ID cảm biến sensor_id và 1 khoảng thời gian time_interval là khoảng cách giữa các lần sinh dữ liệu. Sau đó ta kết nối với địa chỉ URI của Server
và bắt đầu cảm biến
```java
TemperatureSensor sensor=new TemperatureSensor(10 , 5000);
sensor.connect("coap://localhost:5683");
sensor.start();
```
## c, Frontend
Giao diện gồm danh sách các cảm biến đã kết nối với Server ở bên trái
cửa sổ, ở trên cùng là số sensor đang hoạt động, và ở giữa là các nút để
hiển thị biểu đồ. Khi ta kích chuột vào một cảm biến bất kì thì nó sẽ
hiện ra các thông số của cảm biến được chọn bao gồm nhiệt độ, độ trễ và
thông lượng. Phần danh sách hiển thị tất cả các cảm biến đã và đang kết
nối với server theo thứ tự ID của cảm biến.

<p align="center">
<img src="https://raw.githubusercontent.com/U3Vzc3kgQW1vZ3Vz/imagedump/main/coapImg/guioverview.png"/>
</p>
Chương trình có 4 biểu đồ, gồm số sensor đang kết nối, nhiệt độ trung
bình, độ trễ trung bình và thông lượng trung bình. Các biểu đồ và danh
sách cảm biến sẽ cập nhật dữ liệu từ cơ sở dữ liệu sau 3 giây.

<p align="center">
<img src="https://raw.githubusercontent.com/U3Vzc3kgQW1vZ3Vz/imagedump/main/coapImg/graphs.png"/>
</p>


Nếu ta kích chuột phải vào 1 cảm biến đang
chạy thì ta sẽ có lựa chọn tắt cảm biến ấy đi.
<p align="center">
<img src="https://raw.githubusercontent.com/U3Vzc3kgQW1vZ3Vz/imagedump/main/coapImg/offdialoguebox.png"/>
</p>

Nếu chọn tắt thì có thông báo đã tắt cảm biến, cảm biến chuyển sang màu
đỏ và số cảm biến đang hoạt đồng giảm.
<p align="center">
<img src="https://raw.githubusercontent.com/U3Vzc3kgQW1vZ3Vz/imagedump/main/coapImg/offsuccess.png"/>
</p>

Như hình dưới, cảm biến 1 đã được tắt thành công

<p align="center">
<img src="https://raw.githubusercontent.com/U3Vzc3kgQW1vZ3Vz/imagedump/main/coapImg/disabled.png"/>
</p>

Ngược lại, khi kích chuột phải và chọn bật lại cảm biến đã tắt thì cảm
biến chuyển màu xanh và số cảm biến đang hoạt động tăng.

<p align="center">
<img src="https://raw.githubusercontent.com/U3Vzc3kgQW1vZ3Vz/imagedump/main/coapImg/ondialoguebox.png"/>
</p>

<p align="center">
<img src="https://raw.githubusercontent.com/U3Vzc3kgQW1vZ3Vz/imagedump/main/coapImg/onsuccess.png"/>
</p>

Như hình dưới, cảm biến 1 đã bật lại thành công

<p align="center">
<img src="https://raw.githubusercontent.com/U3Vzc3kgQW1vZ3Vz/imagedump/main/coapImg/enabled.png"/>
</p>



# III.Thành viên
Nguyễn Phúc Sơn(MSV: 21021539): BackEnd

Bùi Trần Hải Nam(MSV: 21020525):Báo cáo

Nguyễn Hồng Lĩnh(MSV: 21020468):FrontEnd

Phạm Trung Kiên(MSV: 21021511):FrontEnd

Nguyễn Mạnh Hoàng(MSV: 21020629):Báo cáo

Quá trình làm việc:

-   Báo cáo:
     [báo cáo đợt 1](https://docs.google.com/document/d/17fh7am8WzpeE_TXwNa5yPZ4_sTS1SWWk2RmbOQMp_Yo/edit?usp=sharing) và
 [báo cáo đợt 2](https://docs.google.com/document/d/1Pm-LVSQXHzqz5kAdLQNi0dLLFiqgq3htARo-Oa3t6iM/edit?usp=sharing)
 với manh hoang nguyen là Nguyễn Mạnh Hoàng

-   Code:
     [link github ](https://github.com/imperator0309/CoAP/commits/main)với imperator0309 và NguyenPhucSon là Nguyễn Phúc Sơn, chanchan011
     là Nguyễn Hồng Lĩnh, KIENAYA là Phạm Trung Kiên và U3Vzc3kgQW1vZ3Vz và Bùi Trần Hải Nam
