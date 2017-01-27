# monopoly-game
Имена: Калоян Кръстев	
Дата: 2017-01-28 				Предмет: Програмиране с Java, част 1	
имейл: kaloyanmk@gmail.com		GitHub: https://github.com/koksyncho/monopoly-game

Монополи

1. Условие
-избира се броят на играчите (максимум 4)
-въвеждат се имена за играчите
-играта подканя играч да хвърля двата зара (пример: Лили ти си на ход! Натисни „enter“, за да хвърлиш заровете )
-след хвърлянето играта ти казва колко си хвърлил и къде се намираш след преместването
-пита те искаш ли да купиш/наемеш/продадеш имот или не
-ако полето е специално може да се изтегли карта или попадаш в затвор. Ако си в поле с чужд имот, дължиш пари на собственика или фалираш и губиш играта.

2. Въведение
Приложението е реализирано на платформата Java.

3. Теория
Алгоритъмът, работещ под Java е реализиран с помощта на софтуера Eclipse – многоезична среда за разработване на софтуер, която включва интегрирана среда за разработка (IDE) и плъгин система.

4. Използвани технологии
В текущата програма алгоритъмът най-напред проверява дали въведените стойности са реални, тоест дали са по-големи или по-малки от зададените ограничения, ако е така след въвеждане на съответните данни започва играта, като всеки ход изисква въведено потвърждение от потребителя, за да продължи с играта.

5. Инсталация и настройки
Трябва да имате инсталирана Java на компютъра си, която може да изтеглите от тук: https://java.com/en/download/ , след което просто стартирайте Main.java файла, който се намира в главната директория.

6. Кратко ръководство на потребителя
Стартирайте програмата, вижте помощния текст, който ще се появи на екрана.
От него може да изберете какво да въведете, за да осъществите някои от описаните в точка Условие, операции.

7. Примерни данни

Въвеждате броя на играчите, броя на имената им, след което започва играта. В зависимост от полето, на което попаднете имате различни опции, които се активират чрез въвеждането на съответните букви показани в конзолата.

8. Описание на програмния код
Някои от по-важните променливи в програмата са:

formattedBoard[][] - двумерния масив съхранява нашето поле, като редовете представляват съответните полета, а колоните – техните свойства. Със същата функция са и масивите formattedCommunityChestCards[][] и formattedLuckyDrawCards[][], които съхраняват информация за свойствата на двата вида карти в играта.

BOARD_FIELDS_COUNT – заедно с още няколко статични променливи се използва за задаване на стойностите на променливи, чиито начални числени стойности или дължини  са константни.

playerNames[] – и още няколко промени (playerMoney[], firstDiceRows[]...) съхраняват информация поотделно за всеки от играчите, която се извежда в конзолата и записва във formattedBoard[][]  

9. Приноси на курсиста, ограничения и възможности за бъдещо разширение
	
Програмата е неоптимизирана и има случаи, в които резултатът не е очаквания. Ако тези проблеми се отстранят и се добавят още функционалности, както и графичен интерфейс, играта вероятно би била забавна за дуелиране с друг играч като двамата играете от един компютър, което също би било по-забавно, ако играчите са свързани чрез интернет.
Програмата е проста и лесна за употреба, би била полезна на всеки пътуващ човек, за да пресметне колко би струвало неговото пътуване с кола. 

10. Използвани източници

- http://stackoverflow.com/
-цялата документация на Тито
