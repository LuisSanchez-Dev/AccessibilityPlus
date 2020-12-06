<p align="center">
  <h1 align="center">Accessibility Plus </h1>
  <p align="center">
    A Minecraft Fabric mod to enhance your gameplay experience with better accessibility features.
    <br />
    <a href="https://www.curseforge.com/minecraft/mc-mods/accessibility-plus">Download</a>
    ·
    <a href="https://github.com/LuisSanchez-Dev/AccessibilityPlus/issues">Report Bug</a>
    ·
    <a href="https://github.com/LuisSanchez-Dev/AccessibilityPlus/issues">Request Feature</a>
  </p>
</p>

See mod video:

[![Click to see mod video](https://img.youtube.com/vi/jgLycoBoNf0/0.jpg)](https://www.youtube.com/watch?v=jgLycoBoNf0)

## Table of Contents

* [About the Project](#about-the-project)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
  * [To enable NVDA narration on Windows](#to-enable-nvda-narration-on-windows)
* [Usage](#usage)
  * [Inventory keyboard navigation](#inventory-keyboard-navigation)
* [Contact](#contact)
* [License](#license)

## About The Project

This project was started because I couldn't identify most blocks, I lost the ores when mining, I couldn't read the signs' text, I couldn't identify the items on my inventory because I play with a lot of zoom and couldn't read any enchantments because of the zoom.

This mod enhances the narrator functionality to easily identify the world around you and any inventory!

Features:
  * Read the block you are looking at.
  * Read the action bar text. Example: You can only sleep at night or during thunderstorms.
  * In any inventory when hovering over an item:
    * Read the amount of that item
    * Read the entire tooltip (item name, enchantments, custom text like book authors and server items).
  * Use the keyboard to navigate and use the inventories
  * Use NVDA instead of the default narrator on Windows

I'm planning to add more sound cues and narratable text in the future to better identify the Minecraft world.

This mod helped me a lot when playing on servers because they use a lot of signs and custom text on their items.

## Getting Started

This mod is for Fabric, so you must install it before using this mod, it also requires a library called Fabric API, most mods use it.

### Prerequisites

Install Fabric for Minecraft 1.16.3: https://fabricmc.net/use/

Make sure to run Minecraft at least once for the mods folder to be generated.

### Installation

1. Download the latest version of the Accessibility Plus mod from https://www.curseforge.com/minecraft/mc-mods/accessibility-plus .
  * Remember to delete the old version if you have the mod already installed.
2. Download Fabric API for 1.16 from https://www.curseforge.com/minecraft/mc-mods/fabric-api/files .
3. Open the Downloads folder and copy both files.
4. Press Win + R to open the Run box and type `%appdata%\.minecraft\mods` and press Enter.
5. Paste the files in this folder.
6. Open Minecraft and enable the Narrator with Ctrl + B

### To enable NVDA narration on Windows

1. Make sure you have NVDA installed and enabled, you can download it from https://www.nvaccess.org/download/ .
2. Download the DLL corresponding to your operating system's bit version:
  * For Windows 32 bits download [nvdaControllerClient32.dll](https://github.com/LuisSanchez-Dev/AccessibilityPlus/raw/master/NVDA%20DLLs/nvdaControllerClient32.dll)
  * For Windows 64 bits download [nvdaControllerClient64.dll](https://github.com/LuisSanchez-Dev/AccessibilityPlus/raw/master/NVDA%20DLLs/nvdaControllerClient64.dll)
3. Copy the file nvdaControllerClient##.dll from the Downloads folder.
4. Go to the Minecraft folder pressing Win + R to open the Run box and typing `%appdata%\.minecraft`
5. Paste the DLL in this folder.
6. Run the game as usual.

## Usage

Simply look at a block to narrate it, hover over items in your inventory to narrate them and whenever a message is shown on your action bar it will be read automatically.

### Inventory keyboard navigation

Inventories are all the blocks and your own inventory where you store or process items.
Pressing the letter E opens your own inventory, right clicking a crafting table in the world opens the crafting menu, right clicking a furnace in the world opens the furnace menu.

Most inventories have multiple grids, for example, the crafting table has a 3x3 grid for the crafting input, a 1x1 grid for the crafting output, a 9x3 grid for your inventory items and a 9x1 grid for your hotbar, each one of these grids is called a group, sometimes they have a name and sometimes they don't.
Your player inventory has a 1x1 crafting output, a 2x2 crafting input, a 1x4 armor group, a 1x1 off-hand group, a 9x3 inventory and a 9x1 hotbar groups.

Each group is made of slots and you navigate them with the arrow keys. To navigate between groups use the Tab key to go forward and Shift + Tab keys to go backwards.

To grab a stack of items in a slot press Z, press Z again to leave the stack on the slot. If you leave the stack on a slot where there was an item already you will swap the stack you are holding with the stack in the slot.

To grab half of the stack in the current slot press X, to leave one single item from the stack you are currently holding on to the slot press X.

Here are all the keybindings for inventory keyboard navigation:

* Up arrow: Focus the slot above.
* Down arrow: Focus the slot below.
* Left arrow: Focus the slot to the left.
* Right arrow: Focus the slot to the right.
* Home: Focus the first slot in the group.
* End: Focus the last slot in the group.
* Tab: Go to next group.
* Shift + Tab: Go to previous group.
* Shift + Home: Go to the first group.
* Shift + End: Go to the last group.
* Z: Make a mouse click.
* X: Make a mouse right click.

## Contact

* Discord - luissanchezdev#6247
* Discord server - https://discord.gg/3SzpZz7
* luis.sanchez.dev@hotmail.com

## License
Licensed under MIT
Copyright (C) 2020 Luis Sanchez