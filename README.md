# GeoTask
Günümüzde birçok kargo, yemek, market alışverişi ürünlerinin taşınması için ya da belediyeler tarafından arıza durumları için görev atamaları için saha iş gücü uygulamalarına ihtiyaç duyulmaktadır. GeoTask uygulaması da bu ihtiyaç doğrultusunda oluşmuştur. Kullanıcılar haritada istenen yere marker ekler ve ardından görev oluştururlar. İstedikleri zaman mevcut konumdan başlayarak görevlerin konumlarından geçen rota çizilir. Uygulama geliştirilirken Huawei Map kit ve Location kit kullanılmıştır.

Uygulama ilk açıldığında konum izini kontrolü, gps açık mı kontrolü ve rota çizerken internet kontrolü yapmaktadır.

Mapte bir yere tıklanıldığında ya da marker ekleme butonuna tıklanıldığında (ekranın altında sağdaki) haritaya marker eklenir. Markera tıklanırsa sürüklenebilir hale geçer. Marker eklendikten sonra marker ekleme butonuna tekrar basılarak yeni bir görev ekleme ekranına geçilir.

Bir görev eklendiği zaman sol üstteki recycler view'da görüntülenir. Görevin üzerine tıklanıldığı zaman detaylarına yada konumuna erişilebilir. Görevin detayına tıklanırsa, görev bilgileri güncellenebilir. Konumuna tıklanırsa harita o konuma kayar ve marker eklenir.

Tüm görevleri görüntüleme butonu (ekranın altında solda) ile tüm görevlerin detayları incelenebilir ve görev silinebilir.

Rota çizme butonuna tıklanınca (sol üste köşedeki), şu anki konumdan başlayarak tüm görev konumlarından geçen ve güncel konuma geri dönen en optimize edilmiş rotayı haritaya çizer.

Android 10 ve 11'de değişen konum izinleri yüzünden bazı cihazlarda izin problemi çıkabilmektedir.
Eğer konum göstermez, konuma gidemez ya da rota çizemezse uygulamanın izinleri kontrol edilmeli. Konum izini için "her zaman izin ver" seçili olmalıdır. Ardından uygulama kapatılıp açılınca tüm fonksiyonların çalışması gerekir.
(P40 ve Samsung A70 ile test edilebilir)
Uygulama içi ekran görüntüleri, kaynak kod klasörünün en dış dizininde screenshots klasöründe bulunmaktadır.

Proje zip dosyasından çıkarılarılıp, Android Studio'da açılarak, çalıştırılabilir. Uygulama geliştirilirken MVVM mimari tasarımı izlenmiştir.

Kütüphaneler:

Not: İstenen konumlardan geçen rotanın alınması için OpenRouteService API kullanılmıştır.

HMS Map Kit (5.3.0.300): Harita çizilmesi ve harita işlevleri için kullanılmaktadır.
HMS Location Kit (5.1.0.303): Konum bilgisinin alınması için kullanılmaktadır.
Timber 4.7.1: Loglama işlemleri için kullanılmaktadır.
Room 2.3.0: Cihaz veritabanı işlemleri için kullanılmaktadır.
Retrofit 2.9.0: OpenRouteService API'ndan rotanın alınması için kullanılmaktadır.
Gson 2.8.6: JSON converter olarak kullanılmaktadır.
Coroutine 1.4.1: Asenkron işlemler için kullanılmaktadır.
ViewModel 2.2.0: MVVM patternin bir parçası olarak ve state durumlarını korumak için kullanılmaktadır.
Navigation 2.3.5: Uygulama single Activity multiple Fragment mantığı üzerine dayalı olduğundan dolayı Fragment geçişleri için kullanılmaktadır.
LiveData 2.2.0: Asenkron işlemlerin sonucunu dinlemek için kullanılmaktadır.
