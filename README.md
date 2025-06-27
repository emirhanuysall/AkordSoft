# AkordSoft - Müzik Mağazası Yönetim Sistemi

## 📖 Proje Hakkında

AkordSoft, müzik mağazaları için geliştirilmiş kapsamlı bir yönetim sistemidir. Bu uygulama ile müzik enstrümanları ve aksesuarlarının satışını, müşteri yönetimini, sipariş takibini ve personel işlemlerini kolayca yönetebilirsiniz.

## 🎯 Ana Özellikler

- **Müşteri Yönetimi**: Müşteri bilgilerini kaydetme, güncelleme ve takip etme
- **Ürün Yönetimi**: Müzik enstrümanları ve aksesuarların stok takibi
- **Sipariş Sistemi**: Sipariş oluşturma, güncelleme ve takip
- **Personel Yönetimi**: Çalışan bilgileri ve yetki kontrolü
- **Güvenli Giriş**: Kullanıcı doğrulama sistemi
- **Kullanıcı Dostu Arayüz**: Java Swing ile geliştirilmiş modern arayüz

## 🛠️ Kullanılan Teknolojiler

- **Programlama Dili**: Java
- **Arayüz**: Java Swing
- **Veritabanı**: Microsoft SQL Server
- **Veritabanı Bağlantısı**: JDBC
- **Mimari**: MVC (Model-View-Controller) Deseni
- **IDE**: IntelliJ IDEA

## 📁 Proje Yapısı

```
AkordSoft/
├── src/
│   ├── App.java                 # Ana uygulama dosyası
│   ├── model/                   # Veri modelleri
│   │   ├── Product.java         # Ürün modeli
│   │   ├── Instrument.java      # Enstrüman modeli
│   │   ├── Accessory.java       # Aksesuar modeli
│   │   ├── Customer.java        # Müşteri modeli
│   │   ├── Employee.java        # Çalışan modeli
│   │   ├── Order.java           # Sipariş modeli
│   │   └── Person.java          # Temel kişi modeli
│   ├── service/                 # İş mantığı katmanı
│   │   ├── ProductService.java  # Ürün servisleri
│   │   ├── CustomerService.java # Müşteri servisleri
│   │   ├── OrderService.java    # Sipariş servisleri
│   │   ├── EmployeeService.java # Çalışan servisleri
│   │   └── LoginService.java    # Giriş servisleri
│   ├── ui/                      # Kullanıcı arayüzü
│   │   ├── LoginUI.java         # Giriş ekranı
│   │   ├── ProductUI.java       # Ürün yönetim ekranı
│   │   ├── OrderUI.java         # Sipariş ekranı
│   │   ├── EmployeeUI.java      # Çalışan ekranı
│   │   └── RedirectUI.java      # Yönlendirme ekranı
│   └── db/                      # Veritabanı katmanı
│       ├── DatabaseManager.java # Veritabanı bağlantı yöneticisi
│       ├── ProductDAO.java      # Ürün veri erişim nesnesi
│       ├── CustomerDAO.java     # Müşteri veri erişim nesnesi
│       ├── OrderDAO.java        # Sipariş veri erişim nesnesi
│       └── EmployeeDAO.java     # Çalışan veri erişim nesnesi
```

## 🚀 Kurulum ve Çalıştırma

### Ön Gereksinimler

- Java 8 veya üzeri
- Microsoft SQL Server
- IntelliJ IDEA (önerilen)

### Veritabanı Kurulumu

1. SQL Server'da `AkordSoftDB` adında bir veritabanı oluşturun
2. `DatabaseManager.java` dosyasındaki SQL komutlarını çalıştırarak tabloları oluşturun
3. Veritabanı bağlantı ayarlarını kontrol edin (varsayılan: localhost:1433)

### Uygulamayı Çalıştırma

1. Projeyi IntelliJ IDEA'da açın
2. `App.java` dosyasını çalıştırın
3. Giriş ekranından sisteme giriş yapın

## 💡 Kullanım

1. **Giriş**: Çalışan bilgilerinizle sisteme giriş yapın
2. **Ana Menü**: Yetkinize göre işlem seçeneklerini görüntüleyin
3. **Ürün Yönetimi**: Yeni ürünler ekleyin, mevcut ürünleri güncelleyin
4. **Müşteri İşlemleri**: Müşteri kayıtlarını yönetin
5. **Sipariş Takibi**: Siparişleri oluşturun ve takip edin

## 🏗️ Mimari Deseni

Proje, **MVC (Model-View-Controller)** deseni kullanılarak geliştirilmiştir:

- **Model**: Veri yapıları ve iş kuralları
- **View**: Kullanıcı arayüzü (Swing bileşenleri)
- **Controller**: İş mantığı ve veri akışı (Service katmanı)

## 🔒 Güvenlik

- Kullanıcı doğrulama sistemi
- Veritabanı bağlantısı güvenliği
- Rol tabanlı erişim kontrolü

---

*Nesne Tabanlı Programlama dersi için geliştirilmiştir.* 
