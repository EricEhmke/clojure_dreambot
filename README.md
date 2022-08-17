# Rusty Fisher

A simple fishing bot that will continuously fish and bank. 

**Supports Fish:**
Lobster
Shrimp
Anchovies
Trout
Salmon
Pike
Tuna
Swordfish
Shark
Sardines
Herring
Cod
Bass
Mackerel

**In Areas:**
Catherby
Fishing Guild
Lumbridge Swamp
Barbarian Village
Draynor

## Installation

If the dreambot client dependency is not detected correctly:
Install the dreambot `client.jar` as a dependency with the command:
`mvn deploy:deploy-file -Dfile=client.jar -DartifactId=client -Dversion=1.0.0 -DgroupId=local -Dpackaging=jar -Durl=file:repo`
## Usage

Install into your `DreamBot/Scripts` folder.

### Bugs
- Choosing a fishing area that doesn't have the selected fish can result in the bot walking between spots forever. For example, trying to fish trout in Catherby.
...


