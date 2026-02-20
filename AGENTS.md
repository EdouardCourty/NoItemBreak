# AGENTS.md — NoItemBreak

## Présentation

**NoItemBreak** est un mod Minecraft Java Edition côté **CLIENT uniquement**, développé avec le
framework [Fabric](https://fabricmc.net/) (API Fabric + Loom).

Son objectif : empêcher l'utilisation d'un item (outil, arme…) lorsqu'il lui reste **1 point de
durabilité**, afin d'éviter de le casser par accident.

---

## Stack technique

| Composant       | Version                 |
|-----------------|-------------------------|
| Minecraft       | 1.21.11                 |
| Fabric Loader   | 0.18.4                  |
| Fabric API      | 0.141.3+1.21.11         |
| Java            | 21                      |
| ModMenu         | optionnel (compile-only) |

---

## Structure du projet

```
src/
├── main/                          # Code commun (chargé sur client ET serveur)
│   ├── java/com/ecourty/noitembreak/
│   │   ├── NoItemBreak.java        # ModInitializer principal (logger, MOD_ID)
│   │   └── mixin/
│   │       └── ExampleMixin.java   # Mixin inutilisé (template)
│   └── resources/
│       ├── fabric.mod.json         # Manifeste du mod
│       ├── noitembreak.mixins.json # Déclaration des mixins main
│       └── assets/noitembreak/
│           ├── icon.png
│           └── lang/
│               └── en_us.json      # Traductions
│
└── client/                        # Code CLIENT uniquement
    ├── java/com/ecourty/noitembreak/
    │   ├── NoItemBreakClient.java          # ClientModInitializer (chargement config)
    │   ├── ModMenuIntegration.java         # Intégration optionnelle ModMenu
    │   ├── config/
    │   │   ├── NoItemBreakConfig.java      # POJO de configuration (enabled + liste items)
    │   │   └── ConfigManager.java          # Lecture/écriture de config/noitembreak.json
    │   ├── screen/
    │   │   └── NoItemBreakConfigScreen.java # Écran de config vanilla (pour ModMenu)
    │   └── mixin/client/
    │       └── ItemUsageMixin.java         # Cœur du mod : bloque l'usage si durabilité = 1
    └── resources/
        └── noitembreak.client.mixins.json  # Déclaration des mixins client
```

---

## Fonctionnement

### Détection de la durabilité critique

Le mixin `ItemUsageMixin` s'injecte dans trois méthodes de la classe `Minecraft` :

- `startAttack()` — clic gauche (attaque entité, début de minage)
- `continueAttack(boolean)` — maintien du clic gauche (continuation du minage)
- `startUseItem()` — clic droit (utilisation d'item)

À chaque appel, le mod vérifie :
1. Que la protection est activée (`config.enabled`)
2. Que l'item en main principale est endommageable
3. Que sa durabilité actuelle est `>= maxDurabilité - 1` (c.-à-d. qu'il reste 1 point)
4. Que l'ID de l'item figure dans la liste `config.protectedItems`

Si toutes ces conditions sont vraies, l'action est **annulée** (via `CallbackInfo.cancel()`) et un
message d'avertissement s'affiche sur la barre d'action du joueur.

### Configuration

La configuration est persistée dans `.minecraft/config/noitembreak.json` :

```json
{
  "enabled": true,
  "protectedItems": [
    "minecraft:diamond_sword",
    "minecraft:diamond_pickaxe",
    "..."
  ]
}
```

Par défaut, tous les outils et armes en **diamant** et **netherite** sont protégés.

### Intégration ModMenu

Si [ModMenu](https://modrinth.com/mod/modmenu) est installé, un écran de configuration in-game
est accessible depuis le menu des mods. Cet écran permet de :
- Activer/désactiver la protection globalement
- Ajouter ou retirer des items de la liste protégée
- Réinitialiser aux valeurs par défaut

ModMenu est une dépendance **optionnelle** — le mod fonctionne parfaitement sans lui.

---

## Ajouter un item à la liste de protection

Deux méthodes :
1. **In-game** : Menu Mods → NoItemBreak → Configure (nécessite ModMenu)
2. **Fichier** : éditer `.minecraft/config/noitembreak.json` et ajouter l'ID de l'item
   (format : `namespace:item_id`, ex. `minecraft:iron_sword`)

---

## Notes pour les agents IA

- **Ne pas modifier le code dans `src/main/`** sauf `NoItemBreak.java` pour des raisons de logging.
  Toute la logique fonctionnelle est dans `src/client/`.
- **Mixin target** : la classe cible des mixins principaux est `net.minecraft.client.Minecraft`.
  Les noms de méthodes utilisent les mappings Mojang officiels.
- **ModMenu** est référencé en `modCompileOnly` dans `build.gradle` : il fournit l'interface à la
  compilation mais n'est pas requis à l'exécution. Ne pas en faire une dépendance obligatoire.
- **Gson** est disponible via les dépendances transitives de Minecraft, pas besoin de l'ajouter.
- Pour tester le mod, lancer `./gradlew runClient` depuis la racine du projet.
