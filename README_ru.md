GiftAPI
=======

[English](README.md)

Плагин для Майнкрафт сервера с ядром Bukkit 1.6.4.
Это API + кастомный инвентарь.

Использование
-------------
1. Скачайте релиз или соберите плагин сами.
2. Переместите в папку `КОРНЕВАЯ-ПАПКА-СЕРВЕРА/plugins`.
3. Скопируйте в рут папку вашего плагина.
4. В вашей IDE импортируете `giftapi.jar`.
5. *Используйте пример использования и документация для использования API* 
6. Соберите ваш плагин в JAR.
7. Скопируйте получившися JAR файл в `КОРНЕВАЯ-ПАПКА-СЕРВЕРА/plugins`.
3. Запустите сервер или введите команду `/reload`.

Документация
------------
### `add(player, item)`
- **Описание**: добавляет предмет в кастомные инвентарь игрока.
- **Параметры**:
 - `player` (`Player`): игрок которому нужно добавить предмет.
 - `item` (`ItemStack`): предмет который должен быть сохранен в инвентарь.

### `remove(player, item)`
- **Описание**: удаляет предмет из кастомного инвентаря игрока.
- **Параметры**:
 - `player` (`Player`): игрок которому нужно удалить предмет.
 - `item` (`ItemStack`): предмет который должен быть удалён из инвентаря.

### `openGUI()`
 - **Описание**: открывает кастомный инвентарь игрока.
 - **Параметры**:
 - `player` (`Player`): игрок которому нужно открыть кастомный инвентарь.

Пример Использования
--------------------
```java
import tokarotik.giftapi.GiftAPI;

// Get plugin instance
Plugin plugin = Bukkit.getPluginManager().getPlugin("GiftAPI");

if (plugin != null && plugin.isEnabled() && plugin instanceof GiftAPI) {
    // Set up API
    GiftAPI giftAPI = (GiftAPI) plugin;

    // Get the first online player
    Player player = Bukkit.getOnlinePlayers()[0];

    // Create an item (stick)
    ItemStack item = new ItemStack(Material.STICK);

    // Add item to player's custom inventory
    giftAPI.add(player, item);

    // Open the custom inventory GUI
    giftAPI.openGUI(player);
}
```

Требования
----------
- Bukkit 1.6.4

Разработчик
-----------
- Разработчик:
	- [GitHub](https://github.com/tokarotik)
	- [Gitea](https://git.artberry.xyz/ToKarotik)
	- Discord: tokarotik

- Заказчик:
	- [Gitea](https://git.artberry.xyz/Miriko)
	- Discord: jetstreambase

Лицензия
--------
Этот проект использует [MIT](https://opensource.org/licenses/MIT) лицензию.