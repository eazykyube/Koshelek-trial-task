# Koshelek trial task
Trial task for Android developers to Koshelek.ru. It is necessary to perceive the ability to work with WebSocket, REST API. The application should be based on MVVM architecture, and SOLID principles should be followed.

## Task description

- The application should have three screens. Navigation between screens should be managed by BottomNavigationView. 
- First two screens should contain tables with bids and asks accordingly. Each table contains three columns: Amount, Price and Total. Data is kept updated by WebSockets. Price and Amount are got from the API, and the Total is calculated as Price * Amount.
- Third screen should contain the diff-changes(not implemented).
- A separate selection (drop-down list) for the current and quoted order currencies (BTCUSDT, BNBBTC and ETHBTC) should be implemented.
- Tables should display 100 items. Loaders should appear while loading data in tables and when navigating between application screens.
- Updating data in tables must be implemented smoothly, without leaps and table expansions.

### Additionally implemented

- Handling case of network disconnection
- Done good visually

## Screenshots

![](https://drive.google.com/uc?export=view&id=11XDvGyQt8uMHIaoTxeLi9jC-wl_3A0VF)

![](https://drive.google.com/uc?export=view&id=1jjnoXtIg92CmDvs2l3zvPtOm0Z3KIV7A)

![](https://drive.google.com/uc?export=view&id=1UjDA9CXBa5cq9AiGqox9deDmg6fb_0Ud)

