# BBS UI Plugin

![Build](https://github.com/seclerp/bbs-plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/27894.svg)](https://plugins.jetbrains.com/plugin/27894)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/27894.svg)](https://plugins.jetbrains.com/plugin/27894)

<!-- Plugin description -->

A plugin for IntelliJ IDEA and Rider to provide a UI for running internal .NET monorepo build infrastructure (BBS) entrypoints

## Features
- Quickly launch known entry points on selected profile 
- Create BBS run configuration from a template with an arbitrary profile, entry points and CLI arguments autocompletion
- Get notified when BBS execution completes via the system notification
- Synchronize enabled entry points and selected profile with the TUI preferences 

<!-- Plugin description end -->

## Installation

- Using the IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "BBS UI"</kbd> >
  <kbd>Install</kbd>
  
- Using JetBrains Marketplace:

  Go to [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID) and install it by clicking the <kbd>Install to ...</kbd> button in case your IDE is running.

  You can also download the [latest release](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID/versions) from JetBrains Marketplace and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

- Manually:

  Download the [latest release](https://github.com/seclerp/bbs-plugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>
