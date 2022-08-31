# FFmpeg
FFmpeg, video, ses ve diğer multimedya dosyalarını ve akışlarını işlemek için bir dizi kütüphane ve programdan oluşan ücretsiz ve açık kaynaklı bir yazılım projesidir.
# FFmpeg'in Linux ortamına kurulumu
FFmpeg'in en son ve en kararlı sürümünü kurmanın en kolay ve en uygun yolu, onu APT paket yöneticisinden kurmaktır.
FFmpeg'in en son kararlı sürümü 5.1.1'dir ve Ubuntu APT paket deposunda mevcuttur.
Yüklemek için önce sistemin APT önbellek deposunu güncelleyin:
```
sudo apt update
```
![Adsız](https://user-images.githubusercontent.com/109519208/187784361-c335dc09-8572-491b-860b-32c0a70da9ba.png)


Sistemin APT önbellek deposunu güncelledikten sonra, aşağıda verilen basit komutu kullanarak FFmpeg'i kurun:
```
sudo apt install ffmpeg
```
![word-image-43](https://user-images.githubusercontent.com/109519208/187784807-394c7206-a168-4b3a-93f9-f5db37f1438b.png)

Sistem kurulum için ek disk alanı talep edebilir, “Y” yazıp “Enter” tuşuna basın.
![word-image-44](https://user-images.githubusercontent.com/109519208/187785325-5a5f714b-bcec-43d9-b243-3b7613ecd6da.png)

İşlem tamamlandıktan sonra FFmpeg işletim sisteminize yüklenmiş olacaktır.
![word-image-45](https://user-images.githubusercontent.com/109519208/187785571-a87e12aa-1da1-41f7-a9f2-f703d1478b9f.png)

Kurulumunu onayladıktan sonra yapmanız gereken bir sonraki şey, önce aşağıda verilen komutu kullanarak FFmpeg sürümünü kontrol etmektir:
```
ffmpeg -version or ffmpeg --version
```
![Ads1ız](https://user-images.githubusercontent.com/109519208/187786048-30fefada-cb21-4759-8595-f64293cd1ed0.png)

# FFmpeg Kullanımı
Bir video/ses kaynağını başka bir formata dönüştürmenin mantığı şudur:
```
ffmpeg [global_options] {[input_file_options] -i input_url} ... {[output_file_options] output_url} ...
```

# Advanced Linux Sound Architecture (ALSA)
Gelişmiş Linux Ses Mimarisi, bir yazılım çerçevesi ve ses kartı aygıt sürücüleri için bir uygulama programlama arabirimi sağlayan Linux çekirdeğinin bir parçasıdır. 
# ALSA Linux ortamına kurulumu
```
sudo apt update && sudo apt -y install alsa-utils veya sudo apt-get install alsa-utils
```
# FFmpeg - ALSA kullanımı
Ffmpeg ve ALSA ile ses yakalamak oldukça basittir:
```
ffmpeg -f alsa <input_options> -i <input_device> ... output.wav
```
Sistemdeki mevcut ses cihazlarını listelemek için:
```
aplay -l
```
![Ad11sız](https://user-images.githubusercontent.com/109519208/187791009-b18378ed-f1c2-4a62-b5a2-a39571fb8b74.png)

Görüldüğü üzere şu an sadece bir adet ses kartı vardır. Varsayılan ses kayıt cihazından bir çıktı üretmek istersek:
```
ffmpeg -f alsa -i hw:0 -t 30 out.wav
```
Özellikle başka bir kayıt cihazından çıktı üretmek istersek:
```
ffmpeg -f alsa -i hw:0,2 -t 30 out.wav
```
En iyi yol, kartınızı ve varsayılan kayıt cihazınızı alsamixer aracıyla seçmektir, çünkü bazı ses kartlarında ffmpeg komut satırı aracılığıyla varsayılan girişi seçmenin karmaşık bir yolu vardır.
# Alsamixer
Alsamixer Gelişmiş Linux Ses Mimarisi için bir ses mikser (karıştırıcı) programıdır. Ses ayarlarını yapılandırmak ve ses şiddetini değiştirmek için kullanılır. Çoklu aygıtlı ses kartlarını destekler. Alsamixer'i ekrana getirmek için uçbirime şunu yazın:
```
alsamixer
```
![11](https://user-images.githubusercontent.com/109519208/187793185-84c63924-9cdc-4c90-b6d7-e4a751ff287a.png)
# Karıştırıcı Görünümleri
Alsamixer'in sol üst köşesi, bazı temel bilgileri gösteren alandır: ses kart adı, karıştırıcı çip adı, geçerli görümün kipi ve seçili karıştırıcı ögesi.
Birim çubukları, temel bilgilendirme alanının altındadır. Tek bir ekrana sığmayan denetimler için sola sağa kaydırabilirsin. Her denetimin adı çubukların aşağısında gösterilir. Seçili öge kırmızı renkle veya vurgulanarak gösterilir

Her karıştırıcının birimi kutu ile gösterilir. Birim yüzdelikleri, sol ve sağ kanallar için, birim çubuğunun altında gösterilir. Tekli denetim için, orada sadece tek bir değer vardır.

Karıştırıcının bir denetimi kapatıldığında, birim çubuğunun altında sessiz anlamına gelen MM (mute) gözükür. Açıldığında bu kez yeşil kutunun içinde OO olarak gözükür. Anahtarı m tuşuyla değiştirebilirsin.

Karıştırıcı denetimi kayıt (capture) özelliğine sahipse, birim çubuğunun altında kayıt bayrağı ortaya çıkar. Kayıt kapalıysa, ------- gösterilir. Kayıt anahtarı kapalıysa kırmızı yazıya CAPTURE gözükür. Ayrıca, sol ve sağ kanalların açık olduğunu belirten sol ve sağdaki L ve R harfleri de ortaya çıkar.

Bazı denetimler sayısal listeye sahiptir ve kutuları göstermez. Sadece, geçerli etkin ögeyi belirten metin vardır. Yukarı/aşağı tuşlarıyla ögeyi değiştirebilirsin.

# Görünüm Kipleri
Alsamixer'de üç tane görünüm kipi vardır: playback (çalma), capture (kayıt) ve all (tüm). Playback görünümü, sadece çalma ile ilgili denetimleri gösterir. Benzer şekilde capture görünümü de sadece kayıt denetimlerini gösterir. Tüm görünüm kipi, tüm denetimleri gösterir. Geçerli görünüm kipi, sol üst köşede görülür.

Öntanımlı görünüm kipi playback'dir. Bunu -tab ile değiştirebilirsiniz.

# Giriş Seçenekleri
ALSA girişi için tek kullanışlı ses girişi seçenekleri -sample_rate (ses örnekleme hızı) ve -channels (ses kanalları).

Ses örnekleme hızının/frekansının belirtilmesi, ses kartını sesi belirtilen hızda kaydetmeye zorlayacaktır. Genellikle varsayılan değer "48000"dir (Hz).

Ses kanallarının belirtilmesi, ses kartını sesi mono, stereo veya hatta 2.1/5.1 (ses kartınız tarafından destekleniyorsa) olarak kaydetmeye zorlayacaktır. Genellikle varsayılan değer Mic girişi için "1" (mono) ve Line-In girişi için "2" (stereo) şeklindedir.

### Mikrofondan ses kaydetme
```
ffmpeg -f alsa -channels 1 -sample_rate 44100 -i hw:0 -t 30 out.wav
```
### Bir uygulamadan ses kaydetme
```
modprobe snd-aloop pcm_substreams=1
```

# FFmpeg ve Alsa kullanarak canlı ses iletimi









