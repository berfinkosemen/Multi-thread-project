# Multi-thread-project

Projenin Amacı:
Projede bir sunucuya gelen isteklerdeki aşırı yoğunluğu, multithread kullanarak alt 
sunucularla birlikte azaltmaktır.

Programlama Dili: 
JAVA(Eclipse/Netbeans) programlama dilinde form uygulaması şeklinde geliştirilmiştir.

Proje bileşenlerinin özellikleri:

1) Ana Sunucu (Main Thread): 10000 istek kapasitesine sahiptir. 500 ms zaman 
aralıklarıyla [1-100] arasında rastgele sayıda istek kabul etmektedir. İstek olduğu 
sürece 200 ms zaman aralıklarıyla [1-50] arasında rastgele sayıda isteğe geri dönüş 
yapmaktadır.

2) Alt Sunucu (Sub Thread): 5000 istek kapasitesine sahiptir. 500 ms zaman 
aralıklarıyla [1-50] arasında rastgele sayıda ana sunucudan istek almaktadır. İstek 
olduğu sürece 300 ms zaman aralıklarıyla [1-50] arasında rastgele sayıda isteğe geri 
dönüş yapmaktadır.

3) Alt sunucu oluşturucu (Sub Thread): Mevcut olan alt sunucuları kontrol eder. Eğer 
herhangi bir alt sunucunun kapasitesi %70 ve üzerinde ise yeni bir alt sunucu 
oluşturur ve kapasitenin yarısını yeni oluşturduğu alt sunucuya gönderir. Eğer 
herhangi bir alt sunucunun kapasitesi %0 a ulaşır ise mevcut olan alt sunucu 
sistemden kaldırılır. En az iki alt sunucu sürekli çalışır durumda kalması 
gerekmektedir.

4) Sunucu takip (Sub Thread): Sistemde mevcut olan tüm sunucuların bilgilerini 
(Sunucu/Thread sayısı, ve kapasiteleri(%)) canlı olarak göstermektedir.
