# 1.8.1
* Fix for sorting bug when building the EvolutionChain
* Misc bug fixes

# 1.8.0
* [#2](https://github.com/r0adkll/DeckBox/issues/2) - Fixed display issue with BREAK (and MEGA) evolutions 
* Fixed bug where @Parcelize wasn't honoring the @Transient flags causing Parcel overflow Binder crashes
* Fixed issue with Shortcuts disappearing when re-launching the app

# 1.7.9
* Hotfix for Java 1.8 bug on Android 6.x and lower devices

# 1.7.8
* [#56](https://github.com/r0adkll/DeckBox/issues/56) - Fixed Glide issue causing recycled bitmap bug
* [#55](https://github.com/r0adkll/DeckBox/issues/55) - Fixed issue where searching was forced into disk even with connectivity
* Fixed issue with validation icon flashing
* Fixed UI bug in expansion browser header


# 1.7.7
* Hotfix for issue where decks were missing cards due to find(ids) paging limit

# 1.7.6
* [#40](https://github.com/r0adkll/DeckBox/issues/40) - Added 'Evolves to' related cards row in card detail view
* [#32](https://github.com/r0adkll/DeckBox/issues/32) - Improved the visualization of deck formats. Added reprint validation.
* [#7](https://github.com/r0adkll/DeckBox/issues/7) - Improved the import decklist screen with more helpful information and automatic clipboard detection
* [#6](https://github.com/r0adkll/DeckBox/issues/6) - Added offline caching support and UI
* Fixed issue where secret rares would not appear in the expansion browser
* Improved offline and card caching support
* Added notch support
* Improved deck > card relation format for better accuracy and a more compact format
* Migrated all database to Room
* Added support for local offline, i.e. Decks are stored in the database
* Improved the expansion preview card format/schema
* Added some support for Lost Thunder

# 1.7.5
* Update to include privacy policy from within the app

# 1.7.4
* Added a custom RxErrorHandler using `RxJavaPlugins.setErrorHandler` method to help track/eliminate these errors that are dying outside the scope of my code
* Fixed second Shortcuts bug that I missed causing a lot of crashes for 7.0+ devices
* Attempt to fix the GridLayoutManager span size lookup bug

# 1.7.3
* Hotfix for Shortcuts crashing issue
* Fixed deck testing overall graph from rendering the mulligan bar offscreen

# 1.7.2

* [#35](https://github.com/r0adkll/DeckBox/issues/35)  - Expansion preview is now based on RemoteConfig
* [#33](https://github.com/r0adkll/DeckBox/issues/33) - App Shortcuts (7.1+) 
* [#31](https://github.com/r0adkll/DeckBox/issues/31) - Improved expansion invalidation based on RemoteConfig 
* Added Adaptive App Icon
* Fixed preview layout on phones
* Adjusted the header coloring and pattern on some expansion set browsers
* Bug fixes

# 1.7.1
* [#25](https://github.com/r0adkll/DeckBox/issues/25) - Improved the layout of the tournament PDF exporter to allow for more cards without cutting out the page

# 1.7.0
* [#8](https://github.com/r0adkll/DeckBox/issues/8) - Added 'Overview' feature to tablet deck builder interface that allows you to view all the cards in your deck.
* [#21](https://github.com/r0adkll/DeckBox/issues/21) - Added ability to search cards from the browse tab
* Added ability to filter expansions by 'GX' and 'Prism Star'
* Optimized expansion browser for tablets
* Optimized landscape phone deck builder interface
* Bug Fixes

# 1.6.0
* [#12](https://github.com/r0adkll/DeckBox/issues/12) - New deck testing feature where users can test out single hand deals or an overall multi-hand percentage view.
* [#20](https://github.com/r0adkll/DeckBox/issues/20) - Added convienence filters for mass selecting standard or expanded expansions when searching
* Misc bug fixes

# 1.5.4
* Hotfix for bug with leaving the testing menu option out of the tablet menu config
* Minor tweaks to testing interface

# 1.5.3
* Support for Celestial Storm
* Fixed a crash in the expansion memory management
* [#10](https://github.com/r0adkll/DeckBox/issues/10) - Improved tournament exporter to condense the supporter and energy card rows in the table (Github#10)
* [#3](https://github.com/r0adkll/DeckBox/issues/issues/3) - Improved the robustness of the deck list importer to accept any list that follows the card format: {COUNT} {CARD-NAME} {PTCGO-CODE} {CARD-NUMBER}

# 1.5.2
* Hotfix for expansion parsing bug

# 1.5.1
* Hotfix for Firestore threading issue
* Added 'new' feature indicator for deck images
* Added 'Browser' for browsing the expansion sets and it's cards

# 1.5.0
* [#11](https://gitlab.com/r0adkll/pokemon-tcg-deckbuilder/issues/11) - Add ability to choose the image for your decks
* Fixed issue where you could no longer edit decks after rotating screen
* Fixed issue with importing decks from the home screen
* Added confirmation dialog to deleting decks
* Misc bug fixes around editing decks

# 1.4.2
* Many bugfixes from the previous release and large change around editing mechanism

# 1.4.1
* Hotfix for previous release

# 1.4.0
* Added Prism Star deck validation
* Large improvements to how the app handles editing decks
* Ability to pick up editing sessions if app dies
* Many bug fixes

# 1.3.3
* Official support for Ultra Prism
* Improved search functionality
* Fixed some bugs with export feature
* Misc. Fixes and Improvements

# 1.3.2
* Hotfix for lower density displays
* Misc. Bug fixes

# 1.3.1
* Added Info card on home screen to explain status of UltraPrism
* Cleaned up missing card report UI and added message/advisement
* Misc. Crashlytics Fixes

# 1.3.0
* Tournament Exporter - Export PDF decklists to submit to tournaments

# 1.2.0
* Missing card feedback form
* Disabled analytics during robo-tests
* Added missing card prompt when search returns no results

# 1.1.1
* Fixed bug with quickstart menu rendering incorrectly
* Added improved search functionality to allow the user to search other fields of the card (i.e. text, ability name, attack text, etc)
* Minor UI improvements

# 1.1.0
* Improved the builder interface to show the non-evolution line Pokémons in a grid pattern similar to trainer and energy cards
* Improved UI in the card detail screen to allow you to add more copies of that card, or of variants and evolved forms
* Removed hard limits on deck rules in favor of soft warnings.
* Added basic Standard/Expanded format indicators in deck builder interface
* Minor bug fixes

# 1.0.0
* Fixed Quickstart view on tablets
* Fixed search when just typing 'n' or 'N' to wrap in quotes
* Fixed issue where '-' and '+' buttons weren't showing in search results
* Misc. Bugs and copy changes

# 0.2.3
* Fixed decklist importer to find missing basic energy
* Fixed keyboard behavior on the deck builder screen
* Improved UX for information panel in deck builder screen
* Added ability to click title in deck builder to begin editing deck name
* Bug Fixes
* Misc. Improvements

# 0.2.2
* Added improved UI for adding/removing cards from your deck/builder interface
* Improved the pokemon evolution chain view to clean up bugs
* Bugfixes and Improvements

# 0.2.1
* Added Analytics
* Misc. Bug Fixes
* Phase I Complete

# 0.2.0
* Tablet optimizations
* Misc BugFixes & Improvements

