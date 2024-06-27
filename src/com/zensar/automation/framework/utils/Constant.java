package com.zensar.automation.framework.utils;

import java.io.File;
/*
 *Class Name : Constatnt.java
 *Description: This final class contains the all constant variables required y other classes. 
 * */
public final class Constant {


	public static final String CONFIG="Config";
	public static final String TESTDATA="TestData";
	public static final String PRESETS="Presets";
	public static final String PATH=System.getProperty("user.dir");
	public static final String TESTDATA_EXCEL_PATH=File.separator+CONFIG+File.separator+"TestData.xls";
	public static final String SUIT_EXCEL_PATH=File.separator+CONFIG+File.separator+"SuitFile.xls";
	public static final String REGRESSION_EXCEL_PATH=File.separator+CONFIG+File.separator+"Regression.xls";
	public static final String PASSWORD="test";
	public static final String INCOMPLETEGAMENAME="thunderstruck";
	public static final String  URL="https://mobilewebserver9-zensarqa2.installprogram.eu/Lobby/en/IslandParadise/games/5reelslots";
	public static final String LOCALHUBURL="http://localhost:4444/wd/hub";
	public static final String OUTPUTIMAGEFOLDER=System.getProperty("user.dir")+"\\ImageScreenshot\\Mobile\\";
	public static final String WINDOWS="Windows"; 
	public static final String FORCE_ULTIMATE_CONSOLE="ForceUltimateConsole";
	public static final String STANDARD_CONSOLE="StandardConsole";
	public static final String STORMCRAFT_CONSOLE="StormcraftConsole";
	public static final String TRUE="true";
	public static final String FALSE="false";
	public static final String ONEDESIGN_NEWFEATURE_CLICKTOCONTINUE="OneDesign_NewFeature_ClickToContinue";
	public static final String CLOCK="clock";
	public static final String CONFIGFILE="Config File";
	public static final int  REFRESH_RETRY_COUNT=3;
	public static final String CICD_DEFAULTPROP_FILE_PATH="."+File.separator+"AutomationBinary"+File.separator+"Default.properties";
	public static final String DEFAULTPROP_FILE_PATH="."+File.separator+"Default.properties";
	public static final String GAMETESTPROP_FILE_PATH=File.separator+CONFIG+File.separator+"TestEnv.properties";
	public static final String CURRENCY_TESTDATA_FILE_PATH=File.separator+TESTDATA+File.separator+"Desktop_Regression_CurrencySymbol.testdata";
	public static final String	FREEGAMES_TESTDATA_FILE_PATH=File.separator+TESTDATA+File.separator+"Desktop_Regression_Language_Verification_FreeGames.testdata";
	public static final	String	FREESPINS_TESTDATA_FILE_PATH=File.separator+TESTDATA+File.separator+"Desktop_Regression_Language_Verification_FreeSpin.testdata";
	public static final	String	BIGWIN_TESTDATA_FILE_PATH=File.separator+TESTDATA+File.separator+"Desktop_Regression_Language_Verification_BigWin.testdata";
	public static final	String	MOBILE_CURRENCY_TESTDATA_FILE_PATH=File.separator+TESTDATA+File.separator+"Mobile_Regression_CurrencySymbol.testdata";
	public static final	String	MOBILE_FREEGAMES_TESTDATA_FILE_PATH=File.separator+TESTDATA+File.separator+"Mobile_Regression_Language_Verification_FreeGames.testdata";
	public static final	String	MOBILE_FREESPINS_TESTDATA_FILE_PATH=File.separator+TESTDATA+File.separator+"Mobile_Regression_Language_Verification_FreeSpin.testdata";
	public static final	String	MOBILE_BIGWIN_TESTDATA_FILE_PATH=File.separator+TESTDATA+File.separator+"Mobile_Regression_Language_Verification_BigWin.testdata";
	public static final String 	PRESETSZIPPATH=File.separator+PRESETS+File.separator;
	public static final String FORCE="force";
	public static final String LANG_XL_SHEET_NAME="LanguageCodes";
	public static final String LANGUAGE="Language";
	public static final String LANG_CODE="Language Code";
	public static final String HELPFILE_SHEET_NAME="HelpFile"; 
	public static final String YES="yes";
	public static final String CURRENCY_XL_SHEET_NAME="SupportedCurrenciesList";
	public static final String ID="ID";
	public static final String ISOCODE="ISOCode";
	public static final String SANITY_TESTDATA_FILE_PATH=File.separator+TESTDATA+File.separator+"Desktop_Sanity_BaseScene.testdata";
	public static final	String MOBILE_SANITY_TESTDATA_FILE_PATH=File.separator+TESTDATA+File.separator+"Mobile_Sanity_BaseScene.testdata";
	public static final String ISONAME = "ISOName";
	public static final String LANGUAGECURRENCY = "LanguageCurrency";
	public static final String DESKTOP = "Desktop";
	public static final String MOBILE = "Mobile";
	public static final String OPENCVPATH="C:"+File.separator+"OPCV"+File.separator+"opencv"+File.separator+"build"+File.separator+"java"+File.separator+"x64"+File.separator;
	public static final String NATIVE_APP="NATIVE_APP";
	public static final String CHROMIUM="CHROMIUM";
	public static final String CHROME="chrome";
	public static final String FIREFOX="firefox";
	public static final String INTERNETEXPLORER="internet explorer";
	public static final String EDGEDRIVER="edge";
	public static final String OPERA="opera";
	
	public static final String HTMLEXTENSION=".html";
	public static final String JPEGEXTENSION=".jpeg";
	public static final String INTERRUPTED="Interrupted";
	public static final String ANDROID="Android";
	public static final String IOS="iOS";
	public static final String SAFARI="safari";
	
	public static final String DISPALYFORMAT = "DisplayFormat";
	public static final String REGEXPRESSION = "RegExpression"; //
	public static final String REGEXPRESSION_NOSYMBOL = "RegExpressionNoSymbol";
	
	
	public static final String Home = "Home";
	public static final String Banking = "Banking";
	public static final String Settings = "Settings";
	public static final String Sounds = "Sounds";
	public static final String Double = "Double";
	public static final String QuickDeal = "QuickDeal";
	public static final String PoweredByMicrogaming = "PoweredByMicrogaming";
	public static final String CoinsInPaytable = "CoinsInPaytable";	
	public static final String Credits = "Credits";
	public static final String BetPlus1 = "BetPlus1";
	public static final String BetMax = "BetMax";
	public static final String Deal = "Deal";
	public static final String Held = "Held";
	public static final String Draw = "Draw";
	public static final String Collect = "Collect";
	public static final String Bet = "Bet";
	public static final String TotalBet = "TotalBet";
	public static final String CoinsInBet = "CoinsInBet";
	public static final String CoinSize = "CoinSize";
	public static final String DoubleTo = "DoubleTo";
	public static final String PickCard = "PickCard";
	public static final String Congratulations = "Congratulations";
	public static final String YouWin = "YouWin";
	public static final String DoubleLimitReached = "DoubleLimitReached";
	
	public static final String FEATUREROP_FILE_PATH="."+File.separator+"Feature.properties";
	
	//Markets - Priyanka Bethi
	public static final String 	MARKET_TESTDATA_FILE_PATH=File.separator+TESTDATA+File.separator+"Desktop_Regression_Market.testdata";
	public static final	String	MOBILE_MARKET_TESTDATA_FILE_PATH=File.separator+TESTDATA+File.separator+"Mobile_Regression_Market.testdata";
	public static final String 	MARKET_FRESSPIN_TESTDATA_FILE_PATH=File.separator+TESTDATA+File.separator+"Desktop_Regression_Market_FreeSpin.testdata";
	public static final	String	MOBILE_MARKET_FREESPIN_TESTDATA_FILE_PATH=File.separator+TESTDATA+File.separator+"Mobile_Regression_Market_FreeSpin.testdata";
	public static final String 	MARKET_BONUS_TESTDATA_FILE_PATH=File.separator+TESTDATA+File.separator+"Desktop_Regression_Market_Bonus.testdata";
	public static final	String	MOBILE_MARKET_BONUS_TESTDATA_FILE_PATH=File.separator+TESTDATA+File.separator+"Mobile_Regression_Market_Bonus.testdata";
	public static final String 	MARKET_FREEGAMES_TESTDATA_FILE_PATH=File.separator+TESTDATA+File.separator+"Desktop_Regression_Market_FreeGames.testdata";
	public static final	String	MOBILE_MARKET_FREEGAMES_TESTDATA_FILE_PATH=File.separator+TESTDATA+File.separator+"Mobile_Regression_Market_FreeGames.testdata";
	
		
	public static final String MARKET_XL_SHEET_NAME="RegulatedMarkets";
	public static final String COUNTRY_NAME = "Country";
	public static final String PRODUCT_ID ="ProductId";
	public static final String MARKET_TYPEID = "MarketTypeId";
	public static final String BALANCE = "Balance";
	public static final String RUN_FLAG = "runFlag";	
	public static final String BRAND = "Brand";
	public static final String MARKET = "Market";
	public static final String RETURN_URL_PARAMETER= "returnUrlParam";
	public static final String MARKET_URL = "MarketUrl";
	public static final String runFlag = "runFlag";
	public static final String Lang1 = "Lang1";
	public static final String Lang2= "Lang2";
	public static final String LangCode1 = "LangCode1";
	public static final String LangCode2 = "LangCode2";
	public static final String LanguageCount = "LanguageCount";
	
	//Scenarios - Priyanka
	public static final String BaseGameFeatures="BaseGameFeatures";	
	public static final String InfoBarFeatures="InfoBarFeatures";	
	
	// Top bar
	public static final String TopbarFeatures="TopbarFeatures";
	public static final String gameClock="gameClock";
	public static final String GameName= "GameName";
	
	// Net position
	public static final String NetPositionBaseScene = "NetPositionBaseScene";
	public static final String NetPositionLaunch= "NetPositionLaunch";
	public static final String NetPositionRefresh= "NetPositionRefresh";
	public static final String NetPositionWinLoss= "NetPositionWinLoss";
	public static final String NetPositionBigWin= "NetPositionBigWin";
	public static final String NetPositionFreeSpin= "NetPositionFreeSpin";
	public static final String NetPositionBonusFeature= "NetPositionBonusFeature";
	public static final String NetPositionRefreshWin = "NetPositionRefreshWin";
	public static final String NetPositionRefreshLoss= "NetPositionRefreshLoss";		
	
	// Quick spin
	public static final String QuickSpinFeatures = "QuickSpinFeatures";
	
	// Bet	
	public static final String BetPanel = "BetPanel";
	
	// Menu
	public static final String MenuPanel = "MenuPanel";
	
	// Paytable	
	public static final String PaytableFeatures = "PaytableFeatures";
	
	// Settings	
	public static final String SettingsPanel = "SettingsPanel";
	
	// Autoplay
	public static final String AutoplayPanel= "AutoplayPanel";
	
	// Spin 
	public static final String SpinFeatures= "SpinFeatures";
	public static final String SpinReelLanding = "SpinReelLanding";
	public static final String checkSpinStop ="checkSpinStop";
	public static final String checkSpinStopUsingCoordinates= "checkSpinStopUsingCoordinates";
	
	// Session reminder
	public static final String SessionReminder = "SessionReminder";
	public static final String SessionReminderUserInteraction= "SessionReminderUserInteraction";
	public static final String SessionReminderContinue= "SessionReminderContinue";
	public static final String SessionReminderExitGame= "SessionReminderExitGame";
	
	// CMA
	public static final String BounsFundsNotification= "BounsFundsNotification";
	public static final String closeBtnEnabledCMA= "closeBtnEnabledCMA";
	
	// Value Adds
	public static final String ValueAddsNavigation= "ValueAddsNavigation";
	public static final String PlayerProtectionNavigation = "PlayerProtectionNavigation";
	public static final String CashCheckNavigation = "CashCheckNavigation";	
	public static final String HelpNavigation = "HelpNavigation";
	public static final String PlayCheckNavigation = "PlayCheckNavigation";
	
	// Banking
	public static final String BankingNavigation= "BankingNavigation";
	public static final String BalanceUpdateUsingBanking= "BalanceUpdateUsingBanking";
	
	// Pratice Play
	public static final String PracticePlay="PracticePlay";
	
	// Wager RTS
	public static final String WagerRTS="WagerRTS";
	public static final String SpinScenarios="SpinScenarios";
	public static final String LoadGameWithCredits="LoadGameWithCredits";
	public static final String LoadGameWithoutCredits="LoadGameWithoutCredits";
	
	//Free Games
	public static final String FreeGameFeatures="FreeGameFeatures";
	public static final String FGOfferAccept="FGOfferAccept";
	public static final String FGOfferResume="FGOfferResume";
	public static final String FGOfferDelete="FGOfferDelete";
	public static final String FGPlayLater="FGPlayLater";
	public static final String FGSummaryScreen="FGSummaryScreen";
	public static final String FreeSpinsInFG="FreeSpinsInFG";
	
	// Free Spins
	public static final String FreeSpinFeatures="FreeSpinFeatures";
	public static final String FSRefresh="FSRefresh";
	public static final String FSSessionReminder="FSSessionReminder";
	public static final String FreeSpinReelLanding="FreeSpinReelLanding";
	public static final String FreeSpinTransitionBaseGameToFeature="FreeSpinTransitionBaseGameToFeature";
	public static final String FreeSpinTransitionFeatureToBaseGame="FreeSpinTransitionFeatureToBaseGame";
	public static final String FreeSpinTransitionFeatureToBaseGameRefresh="FreeSpinTransitionFeatureToBaseGameRefresh";
	public static final String NetPositionInFS="NetPositionInFS";

	//Session Duration
	public static final String SessionDurationReset="SessionDurationReset";
	
	//Bonus
	public static final String BonusFeatures="BonusFeatures";
	public static final String BonusTransitionBaseGameToFeature="BonusTransitionBaseGameToFeature";
	public static final String BonusTransitionFeatureToBaseGame="BonusTransitionFeatureToBaseGame";
	public static final String BonusTransitionBaseGameToFeatureRefresh="BonusTransitionBaseGameToFeatureRefresh";
	public static final String BonusTransitionFeatureToBaseGameRefresh="BonusTransitionFeatureToBaseGameRefresh";
	public static final String BonusSessionReminder="BonusSessionReminder";
	public static final String BonusNetPosition="BonusNetPosition";
	public static final String BonusSelection="BonusSelection";
	public static final String BonusReelLanding="BonusReelLanding";
	public static final String BonusRefresh="BonusRefresh";
	public static final String BonusInFreeGames="BonusInFreeGames";
	
	//Link and win
	public static final String LinkAndWin="LinkAndWin";
	
	/*
	 * Defining private constructor as make the class singleton
	 * */
	private Constant() {
	}




}