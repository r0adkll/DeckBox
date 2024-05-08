package app.deckbox.tournament.limitless

const val TournamentsHtmlResponse = """
<!DOCTYPE html>


<html lang="en" >

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tournaments – Limitless</title>
    <meta name="description" content="Results and decklists from all major Pokémon TCG tournaments around the world.">

            <meta property="og:title" content="Tournaments – Limitless">
        <meta property="og:description" content="Results and decklists from all major Pokémon TCG tournaments around the world.">
        <meta property="og:image" content="https://limitlesstcg.com/images/preview.png">
        <meta property="og:type" content="website">
        <meta property="og:site_name" content="Limitless">

            <meta name="twitter:card" content="summary">
        <meta name="twitter:site" content="@LimitlessTCG">
        <meta name="twitter:title" content="Tournaments – Limitless">
        <meta name="twitter:description" content="Results and decklists from all major Pokémon TCG tournaments around the world.">
        <meta name="twitter:image" content="https://limitlesstcg.com/images/preview.png">

    <link rel="manifest" href="/app.webmanifest">

            <!-- Global site tag (gtag.js) - Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=UA-102651668-1"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', 'UA-102651668-1', { 'anonymize_ip': true });
</script>        <link rel="preconnect" href="https://securepubads.g.doubleclick.net/" crossorigin>
<link rel="preconnect" href="https://cdn.consentmanager.mgr.consensu.org/" crossorigin>

<script>window.AdSlots = window.AdSlots || {cmd: [], disableScripts: ['gpt']};</script>
<script async src="https://securepubads.g.doubleclick.net/tag/js/gpt.js"></script>
<script async src="https://kumo.network-n.com/dist/app.js" site="limitlesstcg"></script>

<style>
body {
    --cmpBgColor: #f7f7f7;
    --cmpTextColor: #000000;
    --cmpLinkColor: #d40000;
    --cmpPurposesColor: #6a6a6a;
    --cmpBrandColor: #282828;
    --cmpLogo: url('https://limitlesstcg.com/images/limitless80.png');
}

@media (max-width: 767px) {
    .nn-sticky .close-btn {
        width: 38px !important;
        height: 38px !important;
        right: 0 !important;
        top: 0 !important;
        margin-top: -38px !important;
        opacity: 0.9;
    }
}

.top_ad {
    min-width: 320px;
    min-height: 50px;
    position: relative;
}

@media (min-width: 768px) {
    .top_ad {
        min-width: 728px;
        min-height: 90px;
    }
}

.top_ad .ads-notice {
    width: 100%;
    height: 100%;
    background-color: var(--bg-color-two);
    position: absolute;
    top: 0;
    left: 0;
    z-index: -1;
    display: flex;
    justify-content: center;
    align-items: center;
}

@media print {
    .nn-sticky, .nn-player-floating {
        display: none !important;
    }
}

.celtra-view {
    z-index: 10 !important;
}

.nn-sticky,
.nn-sticky > * {
    z-index: 10 !important;
}

#nn_skinr,
#nn_skinl {
    position: fixed !important;
    top: 64px !important;
    left: 50%;
    margin-left: -890px;
    z-index: 100;
}

#nn_skinr {
    margin-left: 590px;
}

body:not(.celtra-mini) .celtra-reveal-header-sticky {
    position: relative !important;
    top: 0px !important;
}

.after-close-header {
    top: 0px !important;
}

.celtra-reveal-header-sticky {
    width: 100% !important;
    left: 0px !important;
    margin-left: 0px !important;
}

.celtra-reveal-header-sticky:not(.no-trans-all) {
    top: 0px;
}

#celtra-reveal-wrapper {
    z-index: 999 !important;
}

.header-neutral {
    top: 0px !important;
}
</style>
    <link rel="preload" as="style" href="https://limitlesstcg.com/build/assets/main-4834344a.css" /><link rel="modulepreload" href="https://limitlesstcg.com/build/assets/main-e1ea6f41.js" /><link rel="modulepreload" href="https://limitlesstcg.com/build/assets/dom-7f7df130.js" /><link rel="modulepreload" href="https://limitlesstcg.com/build/assets/fetch-a833fccf.js" /><link rel="modulepreload" href="https://limitlesstcg.com/build/assets/limitless-f331a6bc.js" /><link rel="modulepreload" href="https://limitlesstcg.com/build/assets/tables-bcf04db3.js" /><link rel="modulepreload" href="https://limitlesstcg.com/build/assets/ws-search-e6ffb889.js" /><link rel="modulepreload" href="https://limitlesstcg.com/build/assets/suggest-4ff36e79.js" /><link rel="stylesheet" href="https://limitlesstcg.com/build/assets/main-4834344a.css" /><script type="module" src="https://limitlesstcg.com/build/assets/main-e1ea6f41.js"></script>
            <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link rel="preload" as="style" href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400..700&family=Open+Sans+Condensed:wght@700&display=swap">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400..700&family=Open+Sans+Condensed:wght@700&display=swap">
        <link rel="stylesheet" href="/inc/fontawesome/css/all.min.css?v=5.11.2">


    <script>var LS_SEARCH_RESULTS = '~ search results', LS_NO_SEARCH_RESULTS = 'no search results found';</script>
</head>

<body>
    <header>
    <div class="header-top" data-mini-bar>
        <div class="container top">
            <div class="flex gap-4 items-center">

                <div class="site-news"> Latest Updates:
                                            <a href="/tournaments/401">Orlando</a>  •                                             <a href="/cards/jp/SV6?translate=en">Mask of Change</a>  •                                             <a href="/tournaments/419">São Paulo</a>                                      </div>
            </div>

            <div class="site-settings">
                <select class="language-switch" data-site-language>
                    <option value="en_US"  selected >English</option>
                    <option value="jp_JP" >日本語</option>
                    <option value="de_DE" >Deutsch</option>
                    <option value="fr_FR" >Français</option>
                    <option value="es_ES" >Español</option>
                    <option value="it_IT" >Italiano</option>
                    <option value="pt_BR" >Português</option>
                </select>
                <button class="darkmode-toggle" aria-label="darkmode toggle" data-site-theme>
                    <i class="fas fa-lg fa-moon"></i>
                </button>
            </div>
        </div>
    </div>

    <div class="container header">
        <a class="site-logo" href="/">
            <img src="/images/limitless.png" alt="Limitless">
        </a>

                <form class="site-search" method="get" action="/" data-lang="en">
            <div class="category-label">
                <span class="selected-category">Tournaments</span>
                <i class="fas fa-sm fa-caret-down"></i>
            </div>

            <select name="type" class="category-select">

                <option value="cards" >Cards</option>
                <option value="tournaments"  selected >Tournaments</option>
                <option value="decks" >Decks</option>
                <option value="players" >Players</option>
            </select>

            <input type="text" name="q" placeholder="Search database" autocomplete="off" autocapitalize="off"
                autocorrect="off" )>

            <button type="submit" aria-label="submit search">
                <i class="fas fa-lg fa-search"></i>
            </button>

            <div class="search-suggestions" data-suggestions>
                <span class="suggestions-message" data-message></span>
                <div class="suggestions-list" data-options>
                </div>
            </div>
        </form>

        <nav class="site-nav resp-nav" data-site-nav>
            <button class="nav-toggle">
                <i data-open class="fas fa-lg fa-bars"></i>
                <i data-close class="fas fa-lg fa-times" style="display: none"></i>
            </button>

            <ul class="links">

                <li><a href="/tournaments">Tournaments</a></li>
                <li><a href="/decks">Decks</a></li>
                <li class="sub-category">
                    <a href="/cards">Cards</a>
                    <ul class="sub-menu">
                        <li><a href="/cards/advanced">Advanced Search</a></li>
                        <li><a href="/cards/syntax">Search Syntax</a></li>
                        <li><a href="/translations">Translations</a></li>
                    </ul>
                </li>
                <li class="sub-category">
                    <a href="/tools">Tools</a>
                    <ul class="sub-menu shift">
                        <li><a href="/tools/proxies">Proxy Printer</a></li>
                        <li><a href="/tools/swisscalc">Swiss Calculator</a></li>
                        <li><a href="/tools/imggen">Image Generator</a></li>
                        <li><a href="/tools/drawcalc">DrawCalc</a></li>
                        <li><a href="/tools/tabletop">Tabletop</a></li>
                        <li><a href="https://my.limitlesstcg.com/builder">Deck Builder (Beta)</a></li>
                    </ul>
                </li>
                <li class="sub-category more">
                    <a class="expand">More</a>
                    <ul class="sub-menu shift">
                        <li><a href="/players">Rankings</a></li>
                        <li><a href="/blog">Blog</a></li>
                        <li><a href="https://play.limitlesstcg.com">Play</a></li>
                    </ul>
                </li>
            </ul>
        </nav>
    </div>
</header>
    <div class="before-content">
<div class="container secondary-menu">
    <ul class="nav">
        <li class="active"><a href="/tournaments">Completed</a></li>
        <li class=""><a href="/tournaments/upcoming">Upcoming</a></li>
        <li><a href="/tournaments/jp">City Leagues</a></li>
        <li><a class="external" href="https://play.limitlesstcg.com/tournaments?game=PTCG">Online</a></li>
    </ul>
</div>

</div>

            <div class="container dont-print top_ad">
            <div class="ads-notice">This website is supported by ads.</div>
            <div id="nn_lb1"></div>
            <div id="nn_lb2"></div>
            <div id="nn_1by1"></div>
            <div id="nn_mobile_lb1"></div>
        </div>

    <main>
        <div class="container content">
<div class="tournament-filter">

    <div class="filter-visible">
                    <ul class="multi-selection active-options">
                                <li>
                    <span class="label">Time:</span> Past 6 months
                </li>
                        </ul>

        <button class="toggle regular small" data-toggle>Edit tournament filter</button>
    </div>

    <div class="filter-selection" data-options>

        <div class="options-group">
            <div class="options-row">
                <label for="time-select">Time</label>
                <select id="time-select" name="time">
                    <option selected disabled>Select Options</option>
                    <option value="modern">Modern era</option>
                    <option value="legacy">Legacy era</option>
                    <option value="1months">Past month</option>
                    <option value="3months">Past 3 months</option>
                    <option value="6months">Past 6 months</option>
                    <option value="12months">Past 12 months</option>
                    <optgroup label="Modern era">
                        <option value="2324">2023–2024</option>
                        <option value="2223">2022–2023</option>
                        <option value="2122">2021–2022</option>
                        <option value="2021">2020–2021</option>
                        <option value="1920">2019–2020</option>
                        <option value="1819">2018–2019</option>
                        <option value="1718">2017–2018</option>
                        <option value="1617">2016–2017</option>
                    </optgroup>
                    <optgroup label="Legacy era">
                        <option value="1516">2015–2016</option>
                        <option value="1415">2014–2015</option>
                        <option value="1314">2013–2014</option>
                        <option value="1213">2012–2013</option>
                        <option value="1112">2011–2012</option>
                    </optgroup>
                </select>
            </div>

            <div class="options-row">
                <label for="type-select">Type</label>
                <select id="type-select" name="type">
                    <option selected disabled>Select Options</option>
                                            <option value="regional">Regional</option>
                                            <option value="international">International</option>
                                            <option value="worlds">Worlds</option>
                                            <option value="special">Special Event</option>
                                            <option value="national">National</option>
                                            <option value="cl">Champions League</option>
                                            <option value="rl">Regional League</option>
                                            <option value="online">Online Event</option>
                                            <option value="players_cup">Players Cup</option>
                                            <option value="invitational">Invitational</option>
                                    </select>
            </div>

            <div class="options-row">
                <label for="format-select">Format</label>
                <select id="format-select" name="format">
                    <option selected disabled>Select Options</option>
                                            <option value="standard">Standard</option>
                                            <option value="expanded">Expanded</option>
                                            <option value="standard-jp">Standard (JP)</option>
                                            <option value="expanded-jp">Expanded (JP)</option>
                                    </select>
            </div>

            <div class="options-row">
                <label for="region-select">Region</label>
                <select id="region-select" name="region">
                    <option selected disabled>Select Options</option>
                                            <option value="eu">Europe</option>
                                            <option value="na">North America</option>
                                            <option value="la">Latin America</option>
                                            <option value="oc">Oceania</option>
                                            <option value="asia">Asia</option>
                                            <option value="other">Other</option>
                                    </select>
            </div>
        </div>

        <ul class="selection multi-selection" data-selection>
                            <li data-key="time" data-value="6months">
                    <i class="fas fa-times"></i><span class="label">Time:</span> Past 6 months
                </li>
                    </ul>

        <button class="regular small" data-apply>Apply Selection</button>

    </div>
</div>

<table class="data-table striped completed-tournaments">
    <tr>
        <th class="sort" data-sort="date">Date</th>
        <th class="sort" data-sort="country">Country</th>
        <th class="sort" data-sort="name">Name</th>
        <th class="sort" data-sort="format"></th>
        <th class="sort landscape-only" data-sort="players" data-type="number">Players</th>
        <th class="sort landscape-only" data-sort="winner">Winner</th>
    </tr>
                    <tr data-date="2024-04-20" data-country="ID"
            data-name="Indonesia Regional League Vol.3" data-format="standard"
            data-players="143" data-winner="ID-Christian ">

            <td>20 Apr 24</td>
            <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/ID.png" alt="ID" data-tooltip="Indonesia"></td>
            <td><a href="/tournaments/436">Indonesia Regional League Vol.3</a></td>
            <td><img class="format" src="/images/icons/formats/standard.png" alt="standard" data-tooltip="Standard"></td>
            <td class="landscape-only">143</td>
            <td class="winner landscape-only">
                <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/ID.png" alt="ID" data-tooltip="Indonesia">
                <a class="inline" href="/players/4609">Christian </a>
            </td>

        </tr>
                    <tr data-date="2024-04-20" data-country="BR"
            data-name="Regional São Paulo" data-format="standard"
            data-players="1142" data-winner="BR-Pedro Pertusi">

            <td>20 Apr 24</td>
            <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/BR.png" alt="BR" data-tooltip="Brazil"></td>
            <td><a href="/tournaments/419">Regional São Paulo</a></td>
            <td><img class="format" src="/images/icons/formats/standard.png" alt="standard" data-tooltip="Standard"></td>
            <td class="landscape-only">1142</td>
            <td class="winner landscape-only">
                <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/BR.png" alt="BR" data-tooltip="Brazil">
                <a class="inline" href="/players/2774">Pedro Pertusi</a>
            </td>

        </tr>
                    <tr data-date="2024-04-14" data-country="JP"
            data-name="Champions League Aichi Expanded" data-format="expanded-jp"
            data-players="705" data-winner="JP-Takashi Yoneda">

            <td>14 Apr 24</td>
            <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/JP.png" alt="JP" data-tooltip="Japan"></td>
            <td><a href="/tournaments/434">Champions League Aichi Expanded</a></td>
            <td><img class="format" src="/images/icons/formats/expanded-jp.png" alt="expanded-jp" data-tooltip="Expanded (JP)"></td>
            <td class="landscape-only">705</td>
            <td class="winner landscape-only">
                <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/JP.png" alt="JP" data-tooltip="Japan">
                <a class="inline" href="/players/6115">Takashi Yoneda</a>
            </td>

        </tr>
                    <tr data-date="2024-04-13" data-country="AU"
            data-name="Regional Perth" data-format="standard"
            data-players="257" data-winner="AU-Kaiwen Cabbabe">

            <td>13 Apr 24</td>
            <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/AU.png" alt="AU" data-tooltip="Australia"></td>
            <td><a href="/tournaments/409">Regional Perth</a></td>
            <td><img class="format" src="/images/icons/formats/standard.png" alt="standard" data-tooltip="Standard"></td>
            <td class="landscape-only">257</td>
            <td class="winner landscape-only">
                <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/AU.png" alt="AU" data-tooltip="Australia">
                <a class="inline" href="/players/202">Kaiwen Cabbabe</a>
            </td>

        </tr>
                    <tr data-date="2024-04-13" data-country="SG"
            data-name="Singapore Regional League Vol.3" data-format="standard"
            data-players="342" data-winner="SG-Bryan Quah">

            <td>13 Apr 24</td>
            <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/SG.png" alt="SG" data-tooltip="Singapore"></td>
            <td><a href="/tournaments/433">Singapore Regional League Vol.3</a></td>
            <td><img class="format" src="/images/icons/formats/standard.png" alt="standard" data-tooltip="Standard"></td>
            <td class="landscape-only">342</td>
            <td class="winner landscape-only">
                <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/SG.png" alt="SG" data-tooltip="Singapore">
                <a class="inline" href="/players/5235">Bryan Quah</a>
            </td>

        </tr>
                    <tr data-date="2024-04-13" data-country="JP"
            data-name="Champions League Aichi" data-format="standard-jp"
            data-players="2931" data-winner="JP-Shinya Kaneko">

            <td>13 Apr 24</td>
            <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/JP.png" alt="JP" data-tooltip="Japan"></td>
            <td><a href="/tournaments/435">Champions League Aichi</a></td>
            <td><img class="format" src="/images/icons/formats/standard-jp.png" alt="standard-jp" data-tooltip="Standard (JP)"></td>
            <td class="landscape-only">2931</td>
            <td class="winner landscape-only">
                <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/JP.png" alt="JP" data-tooltip="Japan">
                <a class="inline" href="/players/6129">Shinya Kaneko</a>
            </td>

        </tr>
                    <tr data-date="2024-04-13" data-country="US"
            data-name="Regional Orlando, FL" data-format="standard"
            data-players="2369" data-winner="US-Liam Halliburton">

            <td>13 Apr 24</td>
            <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/US.png" alt="US" data-tooltip="United States"></td>
            <td><a href="/tournaments/401">Regional Orlando, FL</a></td>
            <td><img class="format" src="/images/icons/formats/standard.png" alt="standard" data-tooltip="Standard"></td>
            <td class="landscape-only">2369</td>
            <td class="winner landscape-only">
                <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/US.png" alt="US" data-tooltip="United States">
                <a class="inline" href="/players/4035">Liam Halliburton</a>
            </td>

        </tr>
                    <tr data-date="2024-04-06" data-country="PH"
            data-name="Philippines Regional League Vol.3" data-format="standard"
            data-players="243" data-winner="PH-Christian Sky Mendoza">

            <td>06 Apr 24</td>
            <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/PH.png" alt="PH" data-tooltip="Philippines"></td>
            <td><a href="/tournaments/432">Philippines Regional League Vol.3</a></td>
            <td><img class="format" src="/images/icons/formats/standard.png" alt="standard" data-tooltip="Standard"></td>
            <td class="landscape-only">243</td>
            <td class="winner landscape-only">
                <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/PH.png" alt="PH" data-tooltip="Philippines">
                <a class="inline" href="/players/3135">Christian Sky Mendoza</a>
            </td>

        </tr>
                    <tr data-date="2024-04-05" data-country="GB"
            data-name="EUIC 2024, London" data-format="standard"
            data-players="2605" data-winner="NO-Tord Reklev">

            <td>05 Apr 24</td>
            <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/GB.png" alt="GB" data-tooltip="Great Britain"></td>
            <td><a href="/tournaments/405">EUIC 2024, London</a></td>
            <td><img class="format" src="/images/icons/formats/standard.png" alt="standard" data-tooltip="Standard"></td>
            <td class="landscape-only">2605</td>
            <td class="winner landscape-only">
                <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/NO.png" alt="NO" data-tooltip="Norway">
                <a class="inline" href="/players/86">Tord Reklev</a>
            </td>

        </tr>
                    <tr data-date="2024-03-23" data-country="TH"
            data-name="Thailand Regional League Vol.3" data-format="standard-jp"
            data-players="380" data-winner="TH-Wasin Taechakijviboon">

            <td>23 Mar 24</td>
            <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/TH.png" alt="TH" data-tooltip="Thailand"></td>
            <td><a href="/tournaments/431">Thailand Regional League Vol.3</a></td>
            <td><img class="format" src="/images/icons/formats/standard-jp.png" alt="standard-jp" data-tooltip="Standard (JP)"></td>
            <td class="landscape-only">380</td>
            <td class="winner landscape-only">
                <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/TH.png" alt="TH" data-tooltip="Thailand">
                <a class="inline" href="/players/5882">Wasin Taechakijviboon</a>
            </td>

        </tr>
                    <tr data-date="2024-03-23" data-country="CA"
            data-name="Regional Vancouver" data-format="standard"
            data-players="985" data-winner="US-Nathian Beck">

            <td>23 Mar 24</td>
            <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/CA.png" alt="CA" data-tooltip="Canada"></td>
            <td><a href="/tournaments/400">Regional Vancouver</a></td>
            <td><img class="format" src="/images/icons/formats/standard.png" alt="standard" data-tooltip="Standard"></td>
            <td class="landscape-only">985</td>
            <td class="winner landscape-only">
                <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/US.png" alt="US" data-tooltip="United States">
                <a class="inline" href="/players/434">Nathian Beck</a>
            </td>

        </tr>
                    <tr data-date="2024-03-09" data-country="BR"
            data-name="Regional Goiânia" data-format="standard"
            data-players="536" data-winner="CL-Marco Cifuentes Meta">

            <td>09 Mar 24</td>
            <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/BR.png" alt="BR" data-tooltip="Brazil"></td>
            <td><a href="/tournaments/418">Regional Goiânia</a></td>
            <td><img class="format" src="/images/icons/formats/standard.png" alt="standard" data-tooltip="Standard"></td>
            <td class="landscape-only">536</td>
            <td class="winner landscape-only">
                <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/CL.png" alt="CL" data-tooltip="Chile">
                <a class="inline" href="/players/1391">Marco Cifuentes Meta</a>
            </td>

        </tr>
                    <tr data-date="2024-03-02" data-country="NL"
            data-name="Special Event Utrecht" data-format="standard"
            data-players="860" data-winner="NL-Owyn Kamerman">

            <td>02 Mar 24</td>
            <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/NL.png" alt="NL" data-tooltip="Netherlands"></td>
            <td><a href="/tournaments/399">Special Event Utrecht</a></td>
            <td><img class="format" src="/images/icons/formats/standard.png" alt="standard" data-tooltip="Standard"></td>
            <td class="landscape-only">860</td>
            <td class="winner landscape-only">
                <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/NL.png" alt="NL" data-tooltip="Netherlands">
                <a class="inline" href="/players/1738">Owyn Kamerman</a>
            </td>

        </tr>
                    <tr data-date="2024-02-24" data-country="PH"
            data-name="Philippines Regional League Vol.2" data-format="standard"
            data-players="244" data-winner="PH-Rhian Famaran">

            <td>24 Feb 24</td>
            <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/PH.png" alt="PH" data-tooltip="Philippines"></td>
            <td><a href="/tournaments/426">Philippines Regional League Vol.2</a></td>
            <td><img class="format" src="/images/icons/formats/standard.png" alt="standard" data-tooltip="Standard"></td>
            <td class="landscape-only">244</td>
            <td class="winner landscape-only">
                <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/PH.png" alt="PH" data-tooltip="Philippines">
                <a class="inline" href="/players/4474">Rhian Famaran</a>
            </td>

        </tr>
                    <tr data-date="2024-02-24" data-country="ID"
            data-name="Indonesia Regional League Vol.2" data-format="standard"
            data-players="371" data-winner="ID-Terry Hilario">

            <td>24 Feb 24</td>
            <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/ID.png" alt="ID" data-tooltip="Indonesia"></td>
            <td><a href="/tournaments/427">Indonesia Regional League Vol.2</a></td>
            <td><img class="format" src="/images/icons/formats/standard.png" alt="standard" data-tooltip="Standard"></td>
            <td class="landscape-only">371</td>
            <td class="winner landscape-only">
                <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/ID.png" alt="ID" data-tooltip="Indonesia">
                <a class="inline" href="/players/4611">Terry Hilario</a>
            </td>

        </tr>
                    <tr data-date="2024-02-17" data-country="JP"
            data-name="Champions League Fukuoka" data-format="standard-jp"
            data-players="2655" data-winner="JP-Daisuke Kubo">

            <td>17 Feb 24</td>
            <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/JP.png" alt="JP" data-tooltip="Japan"></td>
            <td><a href="/tournaments/424">Champions League Fukuoka</a></td>
            <td><img class="format" src="/images/icons/formats/standard-jp.png" alt="standard-jp" data-tooltip="Standard (JP)"></td>
            <td class="landscape-only">2655</td>
            <td class="winner landscape-only">
                <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/JP.png" alt="JP" data-tooltip="Japan">
                <a class="inline" href="/players/5788">Daisuke Kubo</a>
            </td>

        </tr>
                    <tr data-date="2024-02-10" data-country="DE"
            data-name="Regional Dortmund" data-format="standard"
            data-players="1348" data-winner="NL-Owyn Kamerman">

            <td>10 Feb 24</td>
            <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/DE.png" alt="DE" data-tooltip="Germany"></td>
            <td><a href="/tournaments/398">Regional Dortmund</a></td>
            <td><img class="format" src="/images/icons/formats/standard.png" alt="standard" data-tooltip="Standard"></td>
            <td class="landscape-only">1348</td>
            <td class="winner landscape-only">
                <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/NL.png" alt="NL" data-tooltip="Netherlands">
                <a class="inline" href="/players/1738">Owyn Kamerman</a>
            </td>

        </tr>
                    <tr data-date="2024-02-03" data-country="SG"
            data-name="Singapore Regional League Vol.2" data-format="standard"
            data-players="308" data-winner="SG-Aaron Tan">

            <td>03 Feb 24</td>
            <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/SG.png" alt="SG" data-tooltip="Singapore"></td>
            <td><a href="/tournaments/422">Singapore Regional League Vol.2</a></td>
            <td><img class="format" src="/images/icons/formats/standard.png" alt="standard" data-tooltip="Standard"></td>
            <td class="landscape-only">308</td>
            <td class="winner landscape-only">
                <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/SG.png" alt="SG" data-tooltip="Singapore">
                <a class="inline" href="/players/5735">Aaron Tan</a>
            </td>

        </tr>
                    <tr data-date="2024-02-03" data-country="AU"
            data-name="Regional Melbourne" data-format="standard"
            data-players="531" data-winner="AU-Brent Tonisson">

            <td>03 Feb 24</td>
            <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/AU.png" alt="AU" data-tooltip="Australia"></td>
            <td><a href="/tournaments/408">Regional Melbourne</a></td>
            <td><img class="format" src="/images/icons/formats/standard.png" alt="standard" data-tooltip="Standard"></td>
            <td class="landscape-only">531</td>
            <td class="winner landscape-only">
                <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/AU.png" alt="AU" data-tooltip="Australia">
                <a class="inline" href="/players/138">Brent Tonisson</a>
            </td>

        </tr>
                    <tr data-date="2024-02-03" data-country="US"
            data-name="Regional Knoxville, TN" data-format="standard"
            data-players="1367" data-winner="US-Ryan Antonucci">

            <td>03 Feb 24</td>
            <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/US.png" alt="US" data-tooltip="United States"></td>
            <td><a href="/tournaments/397">Regional Knoxville, TN</a></td>
            <td><img class="format" src="/images/icons/formats/standard.png" alt="standard" data-tooltip="Standard"></td>
            <td class="landscape-only">1367</td>
            <td class="winner landscape-only">
                <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/US.png" alt="US" data-tooltip="United States">
                <a class="inline" href="/players/909">Ryan Antonucci</a>
            </td>

        </tr>
                    <tr data-date="2024-01-27" data-country="GB"
            data-name="Regional Liverpool" data-format="standard"
            data-players="1518" data-winner="PE-Fabrizio Inga Silva">

            <td>27 Jan 24</td>
            <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/GB.png" alt="GB" data-tooltip="Great Britain"></td>
            <td><a href="/tournaments/396">Regional Liverpool</a></td>
            <td><img class="format" src="/images/icons/formats/standard.png" alt="standard" data-tooltip="Standard"></td>
            <td class="landscape-only">1518</td>
            <td class="winner landscape-only">
                <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/PE.png" alt="PE" data-tooltip="Peru">
                <a class="inline" href="/players/1148">Fabrizio Inga Silva</a>
            </td>

        </tr>
                    <tr data-date="2024-01-20" data-country="US"
            data-name="Regional Charlotte, NC" data-format="standard"
            data-players="2133" data-winner="BR-Vinícius Fernandez">

            <td>20 Jan 24</td>
            <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/US.png" alt="US" data-tooltip="United States"></td>
            <td><a href="/tournaments/395">Regional Charlotte, NC</a></td>
            <td><img class="format" src="/images/icons/formats/standard.png" alt="standard" data-tooltip="Standard"></td>
            <td class="landscape-only">2133</td>
            <td class="winner landscape-only">
                <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/BR.png" alt="BR" data-tooltip="Brazil">
                <a class="inline" href="/players/5034">Vinícius Fernandez</a>
            </td>

        </tr>
                    <tr data-date="2024-01-06" data-country="US"
            data-name="Regional Portland, OR" data-format="standard"
            data-players="1498" data-winner="US-Jon Eng">

            <td>06 Jan 24</td>
            <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/US.png" alt="US" data-tooltip="United States"></td>
            <td><a href="/tournaments/394">Regional Portland, OR</a></td>
            <td><img class="format" src="/images/icons/formats/standard.png" alt="standard" data-tooltip="Standard"></td>
            <td class="landscape-only">1498</td>
            <td class="winner landscape-only">
                <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/US.png" alt="US" data-tooltip="United States">
                <a class="inline" href="/players/868">Jon Eng</a>
            </td>

        </tr>
                    <tr data-date="2023-12-30" data-country="KR"
            data-name="Korean League Season 1" data-format="standard-jp"
            data-players="350" data-winner="KR-Jae Yong Shin">

            <td>30 Dec 23</td>
            <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/KR.png" alt="KR" data-tooltip="Korea"></td>
            <td><a href="/tournaments/421">Korean League Season 1</a></td>
            <td><img class="format" src="/images/icons/formats/standard-jp.png" alt="standard-jp" data-tooltip="Standard (JP)"></td>
            <td class="landscape-only">350</td>
            <td class="winner landscape-only">
                <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/KR.png" alt="KR" data-tooltip="Korea">
                <a class="inline" href="/players/5602">Jae Yong Shin</a>
            </td>

        </tr>
                    <tr data-date="2023-12-23" data-country="JP"
            data-name="Champions League Kyoto" data-format="standard-jp"
            data-players="2800" data-winner="JP-Haruki Miwa">

            <td>23 Dec 23</td>
            <td><img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/JP.png" alt="JP" data-tooltip="Japan"></td>
            <td><a href="/tournaments/417">Champions League Kyoto</a></td>
            <td><img class="format" src="/images/icons/formats/standard-jp.png" alt="standard-jp" data-tooltip="Standard (JP)"></td>
            <td class="landscape-only">2800</td>
            <td class="winner landscape-only">
                <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/JP.png" alt="JP" data-tooltip="Japan">
                <a class="inline" href="/players/3978">Haruki Miwa</a>
            </td>

        </tr>
    </table>

    <div class="page-options">

    <ul class="pagination" data-current="1" data-max="2">
        <li  class="disabled"
            >&laquo;</li>
                                        <li  class="active"
                >1</li>
                                            <li  data-target="2"
                >2</li>
                            <li  data-target="2"
            >&raquo;</li>
    </ul>

    <div class="labeled-select">
        <label>Entries per page</label>
        <select name="show" data-reload>
                        <option value="25"  selected >25</option>
            <option value="50" >50</option>
            <option value="100" >100</option>
        </select>
    </div>

</div>
</div>
    </main>

            <div class="container dont-print">
            <div id="nn_mobile_lb2_sticky"></div>
            <div id="nn_mobile_mpu1"></div>
        </div>

    <div class="after-content"></div>

    <footer>
    <div class="container footer">
        <div class="menus">
            <div class="column">
                <h2><a href="https://limitlesstcg.com">Limitless TCG</a></h2>
                <ul>
                    <li><a href="/about">About</a></li>
                    <li><a href="/contact">Contact</a></li>
                    <li><a href="/privacy">Privacy Policy</a></li>
                    <li><a href="/legal">Site Notice</a></li>
                </ul>
            </div>
            <div class="column">
                <h2>Our Other Sites</h2>
                <ul>
                    <li><a href="https://my.limitlesstcg.com">Deck Builder</a></li>
                    <li><a href="https://play.limitlesstcg.com">Tournament Platform</a></li>
                    <li><a href="https://onepiece.limitlesstcg.com">One Piece Database</a></li>
                    <li><a href="https://limitlessvgc.com">Limitless VGC</a></li>
                </ul>
            </div>
            <div class="column">
                <h2>Follow</h2>
                <ul>
                    <li><a href="https://twitter.com/limitlesstcg"><i class="fab fa-twitter"></i> Twitter</a></li>
                    <li><a href="https://facebook.com/LimitlessTCG/"><i class="fab fa-facebook"></i> Facebook</a></li>
                    <li><a href="https://twitch.tv/limitless_tcg"><i class="fab fa-twitch"></i> Twitch</a></li>
                    <li><a href="https://www.patreon.com/limitlesstcg"><i class="fab fa-patreon"></i> Patreon</a></li>
                </ul>
            </div>
            <div class="column full">
                                                    <h2>TCGplayer</h2>
                    <a class="promo" href="https://tcgplayer.pxf.io/LIMITLESS?u=https%3A%2F%2Fwww.tcgplayer.com%2Fsearch%2Fpokemon%2Fproduct" target="_blank">
                        <img src="/images/tcgplayer.png" alt="tcgplayer logo" />
                        <p>You can support us by using our Affiliate Link when buying on TCGplayer!</p>
                    </a>
                            </div>

            <a href="#" class="cookie-link nn-cmp-show">Manage Cookies</a>
        </div>

        <div class="disclaimer">The literal and graphical information presented on this website about the Pokémon Trading Card Game, including card images and text, is copyright The Pokémon Company (Pokémon), Nintendo, Game Freak and/or Creatures. This website is not produced by, endorsed by, supported by, or affiliated with Pokémon, Nintendo, Game Freak or Creatures.</div>
    </div>
</footer>

    <div id="jsg">
        <div class="overlay"></div>
    </div>
</body>

</html>

"""

const val ParticipantListHtmlResponse = """
<!DOCTYPE html>


<html lang="en" >

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Indonesia Regional League Vol.3 – Limitless</title>
    <meta name="description" content="20th April 2024 - Indonesia Regional League Vol.3 - results, decklists &amp; statistics">

            <meta property="og:title" content="Indonesia Regional League Vol.3 – Limitless">
        <meta property="og:description" content="20th April 2024 - Indonesia Regional League Vol.3 - results, decklists &amp; statistics">
        <meta property="og:image" content="https://limitlesstcg.com/images/preview.png">
        <meta property="og:type" content="website">
        <meta property="og:site_name" content="Limitless">

            <meta name="twitter:card" content="summary">
        <meta name="twitter:site" content="@LimitlessTCG">
        <meta name="twitter:title" content="Indonesia Regional League Vol.3 – Limitless">
        <meta name="twitter:description" content="20th April 2024 - Indonesia Regional League Vol.3 - results, decklists &amp; statistics">
        <meta name="twitter:image" content="https://limitlesstcg.com/images/preview.png">

    <link rel="manifest" href="/app.webmanifest">

            <!-- Global site tag (gtag.js) - Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=UA-102651668-1"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', 'UA-102651668-1', { 'anonymize_ip': true });
</script>        <link rel="preconnect" href="https://securepubads.g.doubleclick.net/" crossorigin>
<link rel="preconnect" href="https://cdn.consentmanager.mgr.consensu.org/" crossorigin>

<script>window.AdSlots = window.AdSlots || {cmd: [], disableScripts: ['gpt']};</script>
<script async src="https://securepubads.g.doubleclick.net/tag/js/gpt.js"></script>
<script async src="https://kumo.network-n.com/dist/app.js" site="limitlesstcg"></script>

<style>
body {
    --cmpBgColor: #f7f7f7;
    --cmpTextColor: #000000;
    --cmpLinkColor: #d40000;
    --cmpPurposesColor: #6a6a6a;
    --cmpBrandColor: #282828;
    --cmpLogo: url('https://limitlesstcg.com/images/limitless80.png');
}

@media (max-width: 767px) {
    .nn-sticky .close-btn {
        width: 38px !important;
        height: 38px !important;
        right: 0 !important;
        top: 0 !important;
        margin-top: -38px !important;
        opacity: 0.9;
    }
}

.top_ad {
    min-width: 320px;
    min-height: 50px;
    position: relative;
}

@media (min-width: 768px) {
    .top_ad {
        min-width: 728px;
        min-height: 90px;
    }
}

.top_ad .ads-notice {
    width: 100%;
    height: 100%;
    background-color: var(--bg-color-two);
    position: absolute;
    top: 0;
    left: 0;
    z-index: -1;
    display: flex;
    justify-content: center;
    align-items: center;
}

@media print {
    .nn-sticky, .nn-player-floating {
        display: none !important;
    }
}

.celtra-view {
    z-index: 10 !important;
}

.nn-sticky,
.nn-sticky > * {
    z-index: 10 !important;
}

#nn_skinr,
#nn_skinl {
    position: fixed !important;
    top: 64px !important;
    left: 50%;
    margin-left: -890px;
    z-index: 100;
}

#nn_skinr {
    margin-left: 590px;
}

body:not(.celtra-mini) .celtra-reveal-header-sticky {
    position: relative !important;
    top: 0px !important;
}

.after-close-header {
    top: 0px !important;
}

.celtra-reveal-header-sticky {
    width: 100% !important;
    left: 0px !important;
    margin-left: 0px !important;
}

.celtra-reveal-header-sticky:not(.no-trans-all) {
    top: 0px;
}

#celtra-reveal-wrapper {
    z-index: 999 !important;
}

.header-neutral {
    top: 0px !important;
}
</style>
    <link rel="preload" as="style" href="https://limitlesstcg.com/build/assets/main-4834344a.css" /><link rel="modulepreload" href="https://limitlesstcg.com/build/assets/main-e1ea6f41.js" /><link rel="modulepreload" href="https://limitlesstcg.com/build/assets/dom-7f7df130.js" /><link rel="modulepreload" href="https://limitlesstcg.com/build/assets/fetch-a833fccf.js" /><link rel="modulepreload" href="https://limitlesstcg.com/build/assets/limitless-f331a6bc.js" /><link rel="modulepreload" href="https://limitlesstcg.com/build/assets/tables-bcf04db3.js" /><link rel="modulepreload" href="https://limitlesstcg.com/build/assets/ws-search-e6ffb889.js" /><link rel="modulepreload" href="https://limitlesstcg.com/build/assets/suggest-4ff36e79.js" /><link rel="stylesheet" href="https://limitlesstcg.com/build/assets/main-4834344a.css" /><script type="module" src="https://limitlesstcg.com/build/assets/main-e1ea6f41.js"></script>
            <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link rel="preload" as="style" href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400..700&family=Open+Sans+Condensed:wght@700&display=swap">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400..700&family=Open+Sans+Condensed:wght@700&display=swap">
        <link rel="stylesheet" href="/inc/fontawesome/css/all.min.css?v=5.11.2">


    <script>var LS_SEARCH_RESULTS = '~ search results', LS_NO_SEARCH_RESULTS = 'no search results found';</script>
</head>

<body>
    <header>
    <div class="header-top" data-mini-bar>
        <div class="container top">
            <div class="flex gap-4 items-center">

                <div class="site-news"> Latest Updates:
                                            <a href="/tournaments/401">Orlando</a>  •                                             <a href="/cards/jp/SV6?translate=en">Mask of Change</a>  •                                             <a href="/tournaments/419">São Paulo</a>                                      </div>
            </div>

            <div class="site-settings">
                <select class="language-switch" data-site-language>
                    <option value="en_US"  selected >English</option>
                    <option value="jp_JP" >日本語</option>
                    <option value="de_DE" >Deutsch</option>
                    <option value="fr_FR" >Français</option>
                    <option value="es_ES" >Español</option>
                    <option value="it_IT" >Italiano</option>
                    <option value="pt_BR" >Português</option>
                </select>
                <button class="darkmode-toggle" aria-label="darkmode toggle" data-site-theme>
                    <i class="fas fa-lg fa-moon"></i>
                </button>
            </div>
        </div>
    </div>

    <div class="container header">
        <a class="site-logo" href="/">
            <img src="/images/limitless.png" alt="Limitless">
        </a>

                <form class="site-search" method="get" action="/" data-lang="en">
            <div class="category-label">
                <span class="selected-category">Tournaments</span>
                <i class="fas fa-sm fa-caret-down"></i>
            </div>

            <select name="type" class="category-select">

                <option value="cards" >Cards</option>
                <option value="tournaments"  selected >Tournaments</option>
                <option value="decks" >Decks</option>
                <option value="players" >Players</option>
            </select>

            <input type="text" name="q" placeholder="Search database" autocomplete="off" autocapitalize="off"
                autocorrect="off" )>

            <button type="submit" aria-label="submit search">
                <i class="fas fa-lg fa-search"></i>
            </button>

            <div class="search-suggestions" data-suggestions>
                <span class="suggestions-message" data-message></span>
                <div class="suggestions-list" data-options>
                </div>
            </div>
        </form>

        <nav class="site-nav resp-nav" data-site-nav>
            <button class="nav-toggle">
                <i data-open class="fas fa-lg fa-bars"></i>
                <i data-close class="fas fa-lg fa-times" style="display: none"></i>
            </button>

            <ul class="links">

                <li><a href="/tournaments">Tournaments</a></li>
                <li><a href="/decks">Decks</a></li>
                <li class="sub-category">
                    <a href="/cards">Cards</a>
                    <ul class="sub-menu">
                        <li><a href="/cards/advanced">Advanced Search</a></li>
                        <li><a href="/cards/syntax">Search Syntax</a></li>
                        <li><a href="/translations">Translations</a></li>
                    </ul>
                </li>
                <li class="sub-category">
                    <a href="/tools">Tools</a>
                    <ul class="sub-menu shift">
                        <li><a href="/tools/proxies">Proxy Printer</a></li>
                        <li><a href="/tools/swisscalc">Swiss Calculator</a></li>
                        <li><a href="/tools/imggen">Image Generator</a></li>
                        <li><a href="/tools/drawcalc">DrawCalc</a></li>
                        <li><a href="/tools/tabletop">Tabletop</a></li>
                        <li><a href="https://my.limitlesstcg.com/builder">Deck Builder (Beta)</a></li>
                    </ul>
                </li>
                <li class="sub-category more">
                    <a class="expand">More</a>
                    <ul class="sub-menu shift">
                        <li><a href="/players">Rankings</a></li>
                        <li><a href="/blog">Blog</a></li>
                        <li><a href="https://play.limitlesstcg.com">Play</a></li>
                    </ul>
                </li>
            </ul>
        </nav>
    </div>
</header>
    <div class="before-content">
<div class="container info-menu">
    <div class="infobox">
        <div class="infobox-heading">
            Indonesia Regional League Vol.3
             <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/ID.png" alt="ID" data-tooltip="Indonesia">         </div>
        <div class="infobox-line">
            20th April 2024
             • <span title="Masters Division">143 Players</span>
                         • <a href="/decks/?time=all&format=BRS-TEF">Brilliant Stars - Temporal Forces</a>                     </div>
    </div>

    <div class="secondary-menu">
        <ul class="nav">
                        <li class="active"><a href="/tournaments/436">Results</a></li>
            <li class=""><a href="/tournaments/436/decklists">Decklists</a></li>
            <li class=""><a href="/tournaments/436/statistics">Statistics</a></li>
            <li class=""><a href="/tournaments/436/cards">Cards</a></li>
        </ul>
    </div>
</div>

</div>

            <div class="container dont-print top_ad">
            <div class="ads-notice">This website is supported by ads.</div>
            <div id="nn_lb1"></div>
            <div id="nn_lb2"></div>
            <div id="nn_1by1"></div>
            <div id="nn_mobile_lb1"></div>
        </div>

    <main>
        <div class="container content">
    <table class="data-table striped">
        <tr>
            <th>#</th>
            <th>Player</th>
            <th>Country</th>
            <th>Deck</th>
            <th>List</th>
        </tr>
                    <tr>
                <td>1</td>
                <td>
                                            <a href="/players/4609">Christian </a>
                                    </td>
                <td> <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/ID.png" alt="ID" data-tooltip="Indonesia"> </td>
                <td><a href="/decks/273"><span data-tooltip="Ancient Box"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/roaring-moon.png" alt="roaring-moon"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/koraidon.png" alt="koraidon"></span></a></td>
                <td> <a href="/decks/list/11241"><i class="far fa-lg fa-list-alt"></i></a> </td>
            </tr>
                    <tr>
                <td>2</td>
                <td>
                                            <a href="/players/176">Tito Santoso</a>
                                    </td>
                <td> <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/ID.png" alt="ID" data-tooltip="Indonesia"> </td>
                <td><a href="/decks/255"><span data-tooltip="Gardevoir"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/gardevoir.png" alt="gardevoir"></span></a></td>
                <td> <a href="/decks/list/11242"><i class="far fa-lg fa-list-alt"></i></a> </td>
            </tr>
                    <tr>
                <td>3</td>
                <td>
                                            <a href="/players/6143">Kurniawan Suganda</a>
                                    </td>
                <td> <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/ID.png" alt="ID" data-tooltip="Indonesia"> </td>
                <td><a href="/decks/212?variant=33"><span data-tooltip="Arceus Vulpix"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/arceus.png" alt="arceus"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/vulpix-alola.png" alt="vulpix-alola"></span></a></td>
                <td> <a href="/decks/list/11243"><i class="far fa-lg fa-list-alt"></i></a> </td>
            </tr>
                    <tr>
                <td>4</td>
                <td>
                                            <a href="/players/6144">Raymond Siswara Wong</a>
                                    </td>
                <td> <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/ID.png" alt="ID" data-tooltip="Indonesia"> </td>
                <td><a href="/decks/241"><span data-tooltip="Lost Zone Box"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/comfey.png" alt="comfey"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/sableye.png" alt="sableye"></span></a></td>
                <td> <a href="/decks/list/10615"><i class="far fa-lg fa-list-alt"></i></a> </td>
            </tr>
                    <tr>
                <td>5</td>
                <td>
                                            <a href="/players/6145">Lim Tje Fei</a>
                                    </td>
                <td> <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/ID.png" alt="ID" data-tooltip="Indonesia"> </td>
                <td><a href="/decks/264?variant=5"><span data-tooltip="Charizard Pidgeot"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/charizard.png" alt="charizard"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/pidgeot.png" alt="pidgeot"></span></a></td>
                <td> <a href="/decks/list/11244"><i class="far fa-lg fa-list-alt"></i></a> </td>
            </tr>
                    <tr>
                <td>6</td>
                <td>
                                            <a href="/players/6146">Jun Adenta Isnardi Nanda</a>
                                    </td>
                <td> <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/ID.png" alt="ID" data-tooltip="Indonesia"> </td>
                <td><a href="/decks/267"><span data-tooltip="Gholdengo"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/gholdengo.png" alt="gholdengo"></span></a></td>
                <td> <a href="/decks/list/11245"><i class="far fa-lg fa-list-alt"></i></a> </td>
            </tr>
                    <tr>
                <td>7</td>
                <td>
                                            <a href="/players/4503">Marvin Julio</a>
                                    </td>
                <td> <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/ID.png" alt="ID" data-tooltip="Indonesia"> </td>
                <td><a href="/decks/264"><span data-tooltip="Charizard"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/charizard.png" alt="charizard"></span></a></td>
                <td> <a href="/decks/list/11253"><i class="far fa-lg fa-list-alt"></i></a> </td>
            </tr>
                    <tr>
                <td>8</td>
                <td>
                                            <a href="/players/6147">Jordan Marcell</a>
                                    </td>
                <td> <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/ID.png" alt="ID" data-tooltip="Indonesia"> </td>
                <td><a href="/decks/264?variant=5"><span data-tooltip="Charizard Pidgeot"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/charizard.png" alt="charizard"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/pidgeot.png" alt="pidgeot"></span></a></td>
                <td> <a href="/decks/list/11246"><i class="far fa-lg fa-list-alt"></i></a> </td>
            </tr>
                    <tr>
                <td>9</td>
                <td>
                                            <a href="/players/4099">Daniel Daniharja Tan</a>
                                    </td>
                <td> <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/ID.png" alt="ID" data-tooltip="Indonesia"> </td>
                <td><a href="/decks/264"><span data-tooltip="Charizard"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/charizard.png" alt="charizard"></span></a></td>
                <td> <a href="/decks/list/11253"><i class="far fa-lg fa-list-alt"></i></a> </td>
            </tr>
                    <tr>
                <td>10</td>
                <td>
                                            <a href="/players/3254">Audityo Pramudya Mulyo</a>
                                    </td>
                <td> <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/ID.png" alt="ID" data-tooltip="Indonesia"> </td>
                <td><a href="/decks/240?variant=1"><span data-tooltip="Giratina LZ Box"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/giratina-origin.png" alt="giratina-origin"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/comfey.png" alt="comfey"></span></a></td>
                <td> <a href="/decks/list/11247"><i class="far fa-lg fa-list-alt"></i></a> </td>
            </tr>
                    <tr>
                <td>11</td>
                <td>
                                            <a href="/players/6148">William Hartato</a>
                                    </td>
                <td> <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/ID.png" alt="ID" data-tooltip="Indonesia"> </td>
                <td><a href="/decks/240?variant=1"><span data-tooltip="Giratina LZ Box"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/giratina-origin.png" alt="giratina-origin"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/comfey.png" alt="comfey"></span></a></td>
                <td> <a href="/decks/list/11248"><i class="far fa-lg fa-list-alt"></i></a> </td>
            </tr>
                    <tr>
                <td>12</td>
                <td>
                                            <a href="/players/129">Rafli Attar Ricco</a>
                                    </td>
                <td> <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/ID.png" alt="ID" data-tooltip="Indonesia"> </td>
                <td><a href="/decks/264"><span data-tooltip="Charizard"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/charizard.png" alt="charizard"></span></a></td>
                <td> <a href="/decks/list/11253"><i class="far fa-lg fa-list-alt"></i></a> </td>
            </tr>
                    <tr>
                <td>13</td>
                <td>
                                            <a href="/players/6149">Kristian Sumurung</a>
                                    </td>
                <td> <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/ID.png" alt="ID" data-tooltip="Indonesia"> </td>
                <td><a href="/decks/264?variant=5"><span data-tooltip="Charizard Pidgeot"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/charizard.png" alt="charizard"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/pidgeot.png" alt="pidgeot"></span></a></td>
                <td> <a href="/decks/list/11249"><i class="far fa-lg fa-list-alt"></i></a> </td>
            </tr>
                    <tr>
                <td>14</td>
                <td>
                                            <a href="/players/6150">Rudy </a>
                                    </td>
                <td> <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/ID.png" alt="ID" data-tooltip="Indonesia"> </td>
                <td><a href="/decks/264?variant=5"><span data-tooltip="Charizard Pidgeot"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/charizard.png" alt="charizard"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/pidgeot.png" alt="pidgeot"></span></a></td>
                <td> <a href="/decks/list/11250"><i class="far fa-lg fa-list-alt"></i></a> </td>
            </tr>
                    <tr>
                <td>15</td>
                <td>
                                            <a href="/players/6151">Vincentius Salim</a>
                                    </td>
                <td> <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/ID.png" alt="ID" data-tooltip="Indonesia"> </td>
                <td><a href="/decks/261"><span data-tooltip="Chien-Pao Baxcalibur"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/chien-pao.png" alt="chien-pao"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/baxcalibur.png" alt="baxcalibur"></span></a></td>
                <td> <a href="/decks/list/11251"><i class="far fa-lg fa-list-alt"></i></a> </td>
            </tr>
                    <tr>
                <td>16</td>
                <td>
                                            <a href="/players/6152">Andrew Januristy Romero</a>
                                    </td>
                <td> <img class="flag" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/flags/ID.png" alt="ID" data-tooltip="Indonesia"> </td>
                <td><a href="/decks/268?variant=2"><span data-tooltip="Roaring Moon Dudunsparce"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/roaring-moon.png" alt="roaring-moon"><img class="pokemon" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/pokemon/gen9/dudunsparce.png" alt="dudunsparce"></span></a></td>
                <td> <a href="/decks/list/11252"><i class="far fa-lg fa-list-alt"></i></a> </td>
            </tr>
            </table>

</div>
    </main>

            <div class="container dont-print">
            <div id="nn_mobile_lb2_sticky"></div>
            <div id="nn_mobile_mpu1"></div>
        </div>

    <div class="after-content"></div>

    <footer>
    <div class="container footer">
        <div class="menus">
            <div class="column">
                <h2><a href="https://limitlesstcg.com">Limitless TCG</a></h2>
                <ul>
                    <li><a href="/about">About</a></li>
                    <li><a href="/contact">Contact</a></li>
                    <li><a href="/privacy">Privacy Policy</a></li>
                    <li><a href="/legal">Site Notice</a></li>
                </ul>
            </div>
            <div class="column">
                <h2>Our Other Sites</h2>
                <ul>
                    <li><a href="https://my.limitlesstcg.com">Deck Builder</a></li>
                    <li><a href="https://play.limitlesstcg.com">Tournament Platform</a></li>
                    <li><a href="https://onepiece.limitlesstcg.com">One Piece Database</a></li>
                    <li><a href="https://limitlessvgc.com">Limitless VGC</a></li>
                </ul>
            </div>
            <div class="column">
                <h2>Follow</h2>
                <ul>
                    <li><a href="https://twitter.com/limitlesstcg"><i class="fab fa-twitter"></i> Twitter</a></li>
                    <li><a href="https://facebook.com/LimitlessTCG/"><i class="fab fa-facebook"></i> Facebook</a></li>
                    <li><a href="https://twitch.tv/limitless_tcg"><i class="fab fa-twitch"></i> Twitch</a></li>
                    <li><a href="https://www.patreon.com/limitlesstcg"><i class="fab fa-patreon"></i> Patreon</a></li>
                </ul>
            </div>
            <div class="column full">
                                                    <h2>TCGplayer</h2>
                    <a class="promo" href="https://tcgplayer.pxf.io/LIMITLESS?u=https%3A%2F%2Fwww.tcgplayer.com%2Fsearch%2Fpokemon%2Fproduct" target="_blank">
                        <img src="/images/tcgplayer.png" alt="tcgplayer logo" />
                        <p>You can support us by using our Affiliate Link when buying on TCGplayer!</p>
                    </a>
                            </div>

            <a href="#" class="cookie-link nn-cmp-show">Manage Cookies</a>
        </div>

        <div class="disclaimer">The literal and graphical information presented on this website about the Pokémon Trading Card Game, including card images and text, is copyright The Pokémon Company (Pokémon), Nintendo, Game Freak and/or Creatures. This website is not produced by, endorsed by, supported by, or affiliated with Pokémon, Nintendo, Game Freak or Creatures.</div>
    </div>
</footer>

    <div id="jsg">
        <div class="overlay"></div>
    </div>
</body>

</html>

"""

const val DeckListHtmlResponse = """
<!DOCTYPE html>


<html lang="en" >

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ancient Box by Christian – Limitless</title>
    <meta name="description" content="Ancient Box decklist by Christian  - 1st Place Indonesia Regional League Vol.3 - 20th April 2024">

            <meta property="og:title" content="Ancient Box by Christian – Limitless">
        <meta property="og:description" content="Ancient Box decklist by Christian  - 1st Place Indonesia Regional League Vol.3 - 20th April 2024">
        <meta property="og:image" content="https://limitlesstcg.s3.us-east-2.amazonaws.com/decklists/11241.png">
        <meta property="og:type" content="website">
        <meta property="og:site_name" content="Limitless">

            <meta name="twitter:card" content="summary">
        <meta name="twitter:site" content="@LimitlessTCG">
        <meta name="twitter:title" content="Ancient Box by Christian – Limitless">
        <meta name="twitter:description" content="Ancient Box decklist by Christian  - 1st Place Indonesia Regional League Vol.3 - 20th April 2024">
        <meta name="twitter:image" content="https://limitlesstcg.s3.us-east-2.amazonaws.com/decklists/11241.png">

    <link rel="manifest" href="/app.webmanifest">

            <!-- Global site tag (gtag.js) - Google Analytics -->
<script async src="https://www.googletagmanager.com/gtag/js?id=UA-102651668-1"></script>
<script>
  window.dataLayer = window.dataLayer || [];
  function gtag(){dataLayer.push(arguments);}
  gtag('js', new Date());

  gtag('config', 'UA-102651668-1', { 'anonymize_ip': true });
</script>        <link rel="preconnect" href="https://securepubads.g.doubleclick.net/" crossorigin>
<link rel="preconnect" href="https://cdn.consentmanager.mgr.consensu.org/" crossorigin>

<script>window.AdSlots = window.AdSlots || {cmd: [], disableScripts: ['gpt']};</script>
<script async src="https://securepubads.g.doubleclick.net/tag/js/gpt.js"></script>
<script async src="https://kumo.network-n.com/dist/app.js" site="limitlesstcg"></script>

<style>
body {
    --cmpBgColor: #f7f7f7;
    --cmpTextColor: #000000;
    --cmpLinkColor: #d40000;
    --cmpPurposesColor: #6a6a6a;
    --cmpBrandColor: #282828;
    --cmpLogo: url('https://limitlesstcg.com/images/limitless80.png');
}

@media (max-width: 767px) {
    .nn-sticky .close-btn {
        width: 38px !important;
        height: 38px !important;
        right: 0 !important;
        top: 0 !important;
        margin-top: -38px !important;
        opacity: 0.9;
    }
}

.top_ad {
    min-width: 320px;
    min-height: 50px;
    position: relative;
}

@media (min-width: 768px) {
    .top_ad {
        min-width: 728px;
        min-height: 90px;
    }
}

.top_ad .ads-notice {
    width: 100%;
    height: 100%;
    background-color: var(--bg-color-two);
    position: absolute;
    top: 0;
    left: 0;
    z-index: -1;
    display: flex;
    justify-content: center;
    align-items: center;
}

@media print {
    .nn-sticky, .nn-player-floating {
        display: none !important;
    }
}

.celtra-view {
    z-index: 10 !important;
}

.nn-sticky,
.nn-sticky > * {
    z-index: 10 !important;
}

#nn_skinr,
#nn_skinl {
    position: fixed !important;
    top: 64px !important;
    left: 50%;
    margin-left: -890px;
    z-index: 100;
}

#nn_skinr {
    margin-left: 590px;
}

body:not(.celtra-mini) .celtra-reveal-header-sticky {
    position: relative !important;
    top: 0px !important;
}

.after-close-header {
    top: 0px !important;
}

.celtra-reveal-header-sticky {
    width: 100% !important;
    left: 0px !important;
    margin-left: 0px !important;
}

.celtra-reveal-header-sticky:not(.no-trans-all) {
    top: 0px;
}

#celtra-reveal-wrapper {
    z-index: 999 !important;
}

.header-neutral {
    top: 0px !important;
}
</style>
    <link rel="preload" as="style" href="https://limitlesstcg.com/build/assets/main-4834344a.css" /><link rel="modulepreload" href="https://limitlesstcg.com/build/assets/main-e1ea6f41.js" /><link rel="modulepreload" href="https://limitlesstcg.com/build/assets/dom-7f7df130.js" /><link rel="modulepreload" href="https://limitlesstcg.com/build/assets/fetch-a833fccf.js" /><link rel="modulepreload" href="https://limitlesstcg.com/build/assets/limitless-f331a6bc.js" /><link rel="modulepreload" href="https://limitlesstcg.com/build/assets/tables-bcf04db3.js" /><link rel="modulepreload" href="https://limitlesstcg.com/build/assets/ws-search-e6ffb889.js" /><link rel="modulepreload" href="https://limitlesstcg.com/build/assets/suggest-4ff36e79.js" /><link rel="stylesheet" href="https://limitlesstcg.com/build/assets/main-4834344a.css" /><script type="module" src="https://limitlesstcg.com/build/assets/main-e1ea6f41.js"></script>
            <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link rel="preload" as="style" href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400..700&family=Open+Sans+Condensed:wght@700&display=swap">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400..700&family=Open+Sans+Condensed:wght@700&display=swap">
        <link rel="stylesheet" href="/inc/fontawesome/css/all.min.css?v=5.11.2">

                <link rel="modulepreload" href="https://limitlesstcg.com/build/assets/decklist-ae407475.js" /><link rel="modulepreload" href="https://limitlesstcg.com/build/assets/dom-7f7df130.js" /><link rel="modulepreload" href="https://limitlesstcg.com/build/assets/limitless-f331a6bc.js" /><script type="module" src="https://limitlesstcg.com/build/assets/decklist-ae407475.js"></script>
    <script>var LS_SEARCH_RESULTS = '~ search results', LS_NO_SEARCH_RESULTS = 'no search results found';</script>
</head>

<body>
    <header>
    <div class="header-top" data-mini-bar>
        <div class="container top">
            <div class="flex gap-4 items-center">

                <div class="site-news"> Latest Updates:
                                            <a href="/tournaments/401">Orlando</a>  •                                             <a href="/cards/jp/SV6?translate=en">Mask of Change</a>  •                                             <a href="/tournaments/419">São Paulo</a>                                      </div>
            </div>

            <div class="site-settings">
                <select class="language-switch" data-site-language>
                    <option value="en_US"  selected >English</option>
                    <option value="jp_JP" >日本語</option>
                    <option value="de_DE" >Deutsch</option>
                    <option value="fr_FR" >Français</option>
                    <option value="es_ES" >Español</option>
                    <option value="it_IT" >Italiano</option>
                    <option value="pt_BR" >Português</option>
                </select>
                <button class="darkmode-toggle" aria-label="darkmode toggle" data-site-theme>
                    <i class="fas fa-lg fa-moon"></i>
                </button>
            </div>
        </div>
    </div>

    <div class="container header">
        <a class="site-logo" href="/">
            <img src="/images/limitless.png" alt="Limitless">
        </a>

                <form class="site-search" method="get" action="/" data-lang="en">
            <div class="category-label">
                <span class="selected-category">Decks</span>
                <i class="fas fa-sm fa-caret-down"></i>
            </div>

            <select name="type" class="category-select">

                <option value="cards" >Cards</option>
                <option value="tournaments" >Tournaments</option>
                <option value="decks"  selected >Decks</option>
                <option value="players" >Players</option>
            </select>

            <input type="text" name="q" placeholder="Search database" autocomplete="off" autocapitalize="off"
                autocorrect="off" )>

            <button type="submit" aria-label="submit search">
                <i class="fas fa-lg fa-search"></i>
            </button>

            <div class="search-suggestions" data-suggestions>
                <span class="suggestions-message" data-message></span>
                <div class="suggestions-list" data-options>
                </div>
            </div>
        </form>

        <nav class="site-nav resp-nav" data-site-nav>
            <button class="nav-toggle">
                <i data-open class="fas fa-lg fa-bars"></i>
                <i data-close class="fas fa-lg fa-times" style="display: none"></i>
            </button>

            <ul class="links">

                <li><a href="/tournaments">Tournaments</a></li>
                <li><a href="/decks">Decks</a></li>
                <li class="sub-category">
                    <a href="/cards">Cards</a>
                    <ul class="sub-menu">
                        <li><a href="/cards/advanced">Advanced Search</a></li>
                        <li><a href="/cards/syntax">Search Syntax</a></li>
                        <li><a href="/translations">Translations</a></li>
                    </ul>
                </li>
                <li class="sub-category">
                    <a href="/tools">Tools</a>
                    <ul class="sub-menu shift">
                        <li><a href="/tools/proxies">Proxy Printer</a></li>
                        <li><a href="/tools/swisscalc">Swiss Calculator</a></li>
                        <li><a href="/tools/imggen">Image Generator</a></li>
                        <li><a href="/tools/drawcalc">DrawCalc</a></li>
                        <li><a href="/tools/tabletop">Tabletop</a></li>
                        <li><a href="https://my.limitlesstcg.com/builder">Deck Builder (Beta)</a></li>
                    </ul>
                </li>
                <li class="sub-category more">
                    <a class="expand">More</a>
                    <ul class="sub-menu shift">
                        <li><a href="/players">Rankings</a></li>
                        <li><a href="/blog">Blog</a></li>
                        <li><a href="https://play.limitlesstcg.com">Play</a></li>
                    </ul>
                </li>
            </ul>
        </nav>
    </div>
</header>
    <div class="before-content"></div>

            <div class="container dont-print top_ad">
            <div class="ads-notice">This website is supported by ads.</div>
            <div id="nn_lb1"></div>
            <div id="nn_lb2"></div>
            <div id="nn_1by1"></div>
            <div id="nn_mobile_lb1"></div>
        </div>

    <main>
        <div class="container content">
<div class="decklist" data-id="1">
    <div class="decklist-top">
        <div class="decklist-title">
            Ancient Box
                            <a class="decklist-price card-price usd external"
                    href="https://tcgplayer.pxf.io/LIMITLESS?u=https%3A%2F%2Fwww.tcgplayer.com%2Fmassentry%3Fproductline%3DPokemon%26c%3D4-542853%7C%7C4-542863%7C%7C4-542822%7C%7C1-272246%7C%7C4-523851%7C%7C4-542662%7C%7C4-523844%7C%7C4-488092%7C%7C4-534446%7C%7C4-534453%7C%7C3-523841%7C%7C2-497560%7C%7C1-497562%7C%7C1-488088%7C%7C1-542654%7C%7C4-542651%7C%7C3-276996%7C%7C6-475431%7C%7C2-475430" target="_blank" data-tooltip="Buy on TCGplayer">
                    37.03${'$'}
                    <img class="md-only" src="/images/icons/vendors/tcgplayer-v2.png">
                </a>
                <span class="decklist-price card-price eur hidden">33.36€</span>
                    </div>

        <div class="decklist-options">
                            <div class="flex gap-1">
                    <select class="blend" data-query="lang" data-tooltip="Change the language of the cards">
                        <option value="en"  selected >English</option>
                        <option value="jp" >Japanese</option>
                        <option value="de" >German</option>
                        <option value="fr" >French</option>
                        <option value="es" >Spanish</option>
                        <option value="it" >Italian</option>
                        <option value="pt" >Portuguese</option>
                    </select>
                    <select class="blend" data-query="mode" data-tooltip="Change the displayed version of the cards">
                        <option value="regular" data-tooltip="the, at the time of the tournament, most recent regular version of each card"  selected >Regular</option>
                        <option value="bling" data-tooltip="the rarest version of each card" >Max Rarity</option>
                        <option value="cheapest" data-tooltip="the least expensive version of each card" >Cheapest</option>
                        <option value="chaos" data-tooltip="a randomly selected print of each card" >Random</option>
                    </select>
                </div>

            <div class="flex gap-1">
                <div class="decklist-option-toggle" data-currency-select>
                    <div class="decklist-option-toggle-tab usd active">USD</div>
                    <div class="decklist-option-toggle-tab eur">EUR</div>
                </div>

                <div class="decklist-option-toggle" data-view-select>
                    <div class="decklist-option-toggle-tab wide active" data-view-text><i class="fas fa-bars"></i></div>
                    <div class="decklist-option-toggle-tab wide" data-view-image><i class="fas fa-th"></i></div>
                </div>
            </div>
        </div>
    </div>

    <div  data-text-decklist>
        <div class="decklist-main">
            <div class="decklist-cards layout1">
                <div class="decklist-column">
    <div class="decklist-column-heading">Pokémon (13)</div>
            <div class="decklist-card" data-set="TEF" data-number="109" data-lang="en"  >
            <a class="card-link" href="/cards/TEF/109">
                <span class="card-count">4</span>
                <span class="card-name">Roaring Moon</span>
                 <img class="set" alt="TEF" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/sets/en/TEF_SM.png" data-tooltip="Temporal Forces">                             </a>
             <a class="card-price usd" href="https://tcgplayer.pxf.io/LIMITLESS?u=https%3A%2F%2Fwww.tcgplayer.com%2Fproduct%2F542853%2Fpokemon-sv05-temporal-forces-roaring-moon" target="_blank">${'$'}1.00</a>              <a class="card-price eur hidden" href="https://www.cardmarket.com/en/Pokemon/Products/Singles/Temporal-Forces/Roaring-Moon-TEF109?utm_source=limitlesstcg&utm_medium=text&utm_campaign=card_prices" target="_blank">1.44€</a>         </div>
            <div class="decklist-card" data-set="TEF" data-number="119" data-lang="en"  >
            <a class="card-link" href="/cards/TEF/119">
                <span class="card-count">4</span>
                <span class="card-name">Koraidon</span>
                 <img class="set" alt="TEF" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/sets/en/TEF_SM.png" data-tooltip="Temporal Forces">                             </a>
             <a class="card-price usd" href="https://tcgplayer.pxf.io/LIMITLESS?u=https%3A%2F%2Fwww.tcgplayer.com%2Fproduct%2F542863%2Fpokemon-sv05-temporal-forces-koraidon" target="_blank">${'$'}0.48</a>              <a class="card-price eur hidden" href="https://www.cardmarket.com/en/Pokemon/Products/Singles/Temporal-Forces/Koraidon-TEF119?utm_source=limitlesstcg&utm_medium=text&utm_campaign=card_prices" target="_blank">0.56€</a>         </div>
            <div class="decklist-card" data-set="TEF" data-number="78" data-lang="en"  >
            <a class="card-link" href="/cards/TEF/78">
                <span class="card-count">4</span>
                <span class="card-name">Flutter Mane</span>
                 <img class="set" alt="TEF" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/sets/en/TEF_SM.png" data-tooltip="Temporal Forces">                             </a>
             <a class="card-price usd" href="https://tcgplayer.pxf.io/LIMITLESS?u=https%3A%2F%2Fwww.tcgplayer.com%2Fproduct%2F542822%2Fpokemon-sv05-temporal-forces-flutter-mane" target="_blank">${'$'}0.56</a>              <a class="card-price eur hidden" href="https://www.cardmarket.com/en/Pokemon/Products/Singles/Temporal-Forces/Flutter-Mane-TEF078?utm_source=limitlesstcg&utm_medium=text&utm_campaign=card_prices" target="_blank">0.60€</a>         </div>
            <div class="decklist-card" data-set="ASR" data-number="46" data-lang="en"  >
            <a class="card-link" href="/cards/ASR/46">
                <span class="card-count">1</span>
                <span class="card-name">Radiant Greninja</span>
                 <img class="set" alt="ASR" src="https://limitlesstcg.s3.us-east-2.amazonaws.com/sets/en/ASR_SM.png" data-tooltip="Astral Radiance">                             </a>
             <a class="card-price usd" href="https://tcgplayer.pxf.io/LIMITLESS?u=https%3A%2F%2Fwww.tcgplayer.com%2Fproduct%2F272246%2Fswsh10-astral-radiance-radiant-greninja" target="_blank">${'$'}5.26</a>              <a class="card-price eur hidden" href="https://www.cardmarket.com/en/Pokemon/Products/Singles/Astral-Radiance/Radiant-Greninja-ASR046?utm_source=limitlesstcg&utm_medium=text&utm_campaign=card_prices" target="_blank">5.40€</a>         </div>
    </div>
                <div class="decklist-column">
    <div class="decklist-column-heading">Trainer (39)</div>
            <div class="decklist-card" data-set="PAR" data-number="170" data-lang="en"  >
            <a class="card-link" href="/cards/PAR/170">
                <span class="card-count">4</span>
                <span class="card-name">Professor Sada's Vitality</span>
                                            </a>
             <a class="card-price usd" href="https://tcgplayer.pxf.io/LIMITLESS?u=https%3A%2F%2Fwww.tcgplayer.com%2Fproduct%2F523851%2Fpokemon-sv04-paradox-rift-professor-sadas-vitality" target="_blank">${'$'}2.08</a>              <a class="card-price eur hidden" href="https://www.cardmarket.com/en/Pokemon/Products/Singles/Paradox-Rift/Professor-Sadas-Vitality-V1-PAR170?utm_source=limitlesstcg&utm_medium=text&utm_campaign=card_prices" target="_blank">1.60€</a>         </div>
            <div class="decklist-card" data-set="TEF" data-number="147" data-lang="en"  >
            <a class="card-link" href="/cards/TEF/147">
                <span class="card-count">4</span>
                <span class="card-name">Explorer's Guidance</span>
                                            </a>
             <a class="card-price usd" href="https://tcgplayer.pxf.io/LIMITLESS?u=https%3A%2F%2Fwww.tcgplayer.com%2Fproduct%2F542662%2Fpokemon-sv05-temporal-forces-explorers-guidance-147-162" target="_blank">${'$'}0.48</a>              <a class="card-price eur hidden" href="https://www.cardmarket.com/en/Pokemon/Products/Singles/Temporal-Forces/Explorers-Guidance-V1-TEF147?utm_source=limitlesstcg&utm_medium=text&utm_campaign=card_prices" target="_blank">0.72€</a>         </div>
            <div class="decklist-card" data-set="PAR" data-number="163" data-lang="en"  >
            <a class="card-link" href="/cards/PAR/163">
                <span class="card-count">4</span>
                <span class="card-name">Earthen Vessel</span>
                                            </a>
             <a class="card-price usd" href="https://tcgplayer.pxf.io/LIMITLESS?u=https%3A%2F%2Fwww.tcgplayer.com%2Fproduct%2F523844%2Fpokemon-sv04-paradox-rift-earthen-vessel" target="_blank">${'$'}9.40</a>              <a class="card-price eur hidden" href="https://www.cardmarket.com/en/Pokemon/Products/Singles/Paradox-Rift/Earthen-Vessel-PAR163?utm_source=limitlesstcg&utm_medium=text&utm_campaign=card_prices" target="_blank">8.08€</a>         </div>
            <div class="decklist-card" data-set="SVI" data-number="186" data-lang="en"  >
            <a class="card-link" href="/cards/SVI/186">
                <span class="card-count">4</span>
                <span class="card-name">Pokégear 3.0</span>
                                            </a>
             <a class="card-price usd" href="https://tcgplayer.pxf.io/LIMITLESS?u=https%3A%2F%2Fwww.tcgplayer.com%2Fproduct%2F488092%2Fsv01-scarlet-and-violet-base-set-pokegear-30-186-198" target="_blank">${'$'}1.04</a>              <a class="card-price eur hidden" href="https://www.cardmarket.com/en/Pokemon/Products/Singles/Scarlet-Violet/Pokegear-30-SV1en186?utm_source=limitlesstcg&utm_medium=text&utm_campaign=card_prices" target="_blank">1.24€</a>         </div>
            <div class="decklist-card" data-set="PAF" data-number="84" data-lang="en"  >
            <a class="card-link" href="/cards/PAF/84">
                <span class="card-count">4</span>
                <span class="card-name">Nest Ball</span>
                                            </a>
             <a class="card-price usd" href="https://tcgplayer.pxf.io/LIMITLESS?u=https%3A%2F%2Fwww.tcgplayer.com%2Fproduct%2F534446%2Fpokemon-sv-paldean-fates-nest-ball" target="_blank">${'$'}0.76</a>              <a class="card-price eur hidden" href="https://www.cardmarket.com/en/Pokemon/Products/Singles/Paldean-Fates/Nest-Ball-PAF084?utm_source=limitlesstcg&utm_medium=text&utm_campaign=card_prices" target="_blank">0.68€</a>         </div>
            <div class="decklist-card" data-set="PAF" data-number="91" data-lang="en"  >
            <a class="card-link" href="/cards/PAF/91">
                <span class="card-count">4</span>
                <span class="card-name">Ultra Ball</span>
                                            </a>
             <a class="card-price usd" href="https://tcgplayer.pxf.io/LIMITLESS?u=https%3A%2F%2Fwww.tcgplayer.com%2Fproduct%2F534453%2Fpokemon-sv-paldean-fates-ultra-ball" target="_blank">${'$'}0.48</a>              <a class="card-price eur hidden" href="https://www.cardmarket.com/en/Pokemon/Products/Singles/Paldean-Fates/Ultra-Ball-PAF091?utm_source=limitlesstcg&utm_medium=text&utm_campaign=card_prices" target="_blank">0.32€</a>         </div>
            <div class="decklist-card" data-set="PAR" data-number="160" data-lang="en"  >
            <a class="card-link" href="/cards/PAR/160">
                <span class="card-count">3</span>
                <span class="card-name">Counter Catcher</span>
                                            </a>
             <a class="card-price usd" href="https://tcgplayer.pxf.io/LIMITLESS?u=https%3A%2F%2Fwww.tcgplayer.com%2Fproduct%2F523841%2Fpokemon-sv04-paradox-rift-counter-catcher" target="_blank">${'$'}5.31</a>              <a class="card-price eur hidden" href="https://www.cardmarket.com/en/Pokemon/Products/Singles/Paradox-Rift/Counter-Catcher-V1-PAR160?utm_source=limitlesstcg&utm_medium=text&utm_campaign=card_prices" target="_blank">4.05€</a>         </div>
            <div class="decklist-card" data-set="PAL" data-number="188" data-lang="en"  >
            <a class="card-link" href="/cards/PAL/188">
                <span class="card-count">2</span>
                <span class="card-name">Super Rod</span>
                                            </a>
             <a class="card-price usd" href="https://tcgplayer.pxf.io/LIMITLESS?u=https%3A%2F%2Fwww.tcgplayer.com%2Fproduct%2F497560%2Fpokemon-sv02-paldea-evolved-super-rod-188-193" target="_blank">${'$'}2.16</a>              <a class="card-price eur hidden" href="https://www.cardmarket.com/en/Pokemon/Products/Singles/Paldea-Evolved/Super-Rod-V1-PAL188?utm_source=limitlesstcg&utm_medium=text&utm_campaign=card_prices" target="_blank">1.08€</a>         </div>
            <div class="decklist-card" data-set="PAL" data-number="189" data-lang="en"  >
            <a class="card-link" href="/cards/PAL/189">
                <span class="card-count">1</span>
                <span class="card-name">Superior Energy Retrieval</span>
                                            </a>
             <a class="card-price usd" href="https://tcgplayer.pxf.io/LIMITLESS?u=https%3A%2F%2Fwww.tcgplayer.com%2Fproduct%2F497562%2Fpokemon-sv02-paldea-evolved-superior-energy-retrieval-189-193" target="_blank">${'$'}0.81</a>              <a class="card-price eur hidden" href="https://www.cardmarket.com/en/Pokemon/Products/Singles/Paldea-Evolved/Superior-Energy-Retrieval-V1-PAL189?utm_source=limitlesstcg&utm_medium=text&utm_campaign=card_prices" target="_blank">1.18€</a>         </div>
            <div class="decklist-card" data-set="SVI" data-number="182" data-lang="en"  >
            <a class="card-link" href="/cards/SVI/182">
                <span class="card-count">1</span>
                <span class="card-name">Pal Pad</span>
                                            </a>
             <a class="card-price usd" href="https://tcgplayer.pxf.io/LIMITLESS?u=https%3A%2F%2Fwww.tcgplayer.com%2Fproduct%2F488088%2Fsv01-scarlet-and-violet-base-set-pal-pad-182-198" target="_blank">${'$'}0.16</a>              <a class="card-price eur hidden" href="https://www.cardmarket.com/en/Pokemon/Products/Singles/Scarlet-Violet/Pal-Pad-SV1en182?utm_source=limitlesstcg&utm_medium=text&utm_campaign=card_prices" target="_blank">0.24€</a>         </div>
            <div class="decklist-card" data-set="TEF" data-number="141" data-lang="en"  >
            <a class="card-link" href="/cards/TEF/141">
                <span class="card-count">1</span>
                <span class="card-name">Awakening Drum</span>
                                            </a>
             <a class="card-price usd" href="https://tcgplayer.pxf.io/LIMITLESS?u=https%3A%2F%2Fwww.tcgplayer.com%2Fproduct%2F542654%2Fpokemon-sv05-temporal-forces-awakening-drum" target="_blank">${'$'}1.93</a>              <a class="card-price eur hidden" href="https://www.cardmarket.com/en/Pokemon/Products/Singles/Temporal-Forces/Awakening-Drum-TEF141?utm_source=limitlesstcg&utm_medium=text&utm_campaign=card_prices" target="_blank">4.32€</a>         </div>
            <div class="decklist-card" data-set="TEF" data-number="140" data-lang="en"  >
            <a class="card-link" href="/cards/TEF/140">
                <span class="card-count">4</span>
                <span class="card-name">Ancient Booster Energy Capsule</span>
                                            </a>
             <a class="card-price usd" href="https://tcgplayer.pxf.io/LIMITLESS?u=https%3A%2F%2Fwww.tcgplayer.com%2Fproduct%2F542651%2Fpokemon-sv05-temporal-forces-ancient-booster-energy-capsule" target="_blank">${'$'}0.32</a>              <a class="card-price eur hidden" href="https://www.cardmarket.com/en/Pokemon/Products/Singles/Temporal-Forces/Ancient-Booster-Energy-Capsule-TEF140?utm_source=limitlesstcg&utm_medium=text&utm_campaign=card_prices" target="_blank">0.28€</a>         </div>
            <div class="decklist-card" data-set="PGO" data-number="68" data-lang="en"  >
            <a class="card-link" href="/cards/PGO/68">
                <span class="card-count">3</span>
                <span class="card-name">PokéStop</span>
                                            </a>
             <a class="card-price usd" href="https://tcgplayer.pxf.io/LIMITLESS?u=https%3A%2F%2Fwww.tcgplayer.com%2Fproduct%2F276996%2Fpokemon-go-pokestop" target="_blank">${'$'}4.62</a>              <a class="card-price eur hidden" href="https://www.cardmarket.com/en/Pokemon/Products/Singles/Pokemon-GO/PokeStop-V1-PGO068?utm_source=limitlesstcg&utm_medium=text&utm_campaign=card_prices" target="_blank">1.41€</a>         </div>
    </div>
                <div class="decklist-column">
    <div class="decklist-column-heading">Energy (8)</div>
            <div class="decklist-card" data-set="SVE" data-number="7" data-lang="en"   data-basic-energy="7" >
            <a class="card-link" href="/cards/SVE/7">
                <span class="card-count">6</span>
                <span class="card-name">Darkness Energy</span>
                                            </a>
             <a class="card-price usd" href="https://tcgplayer.pxf.io/LIMITLESS?u=https%3A%2F%2Fwww.tcgplayer.com%2Fproduct%2F475431%2Fsv01-scarlet-and-violet-base-set-basic-darkness-energy" target="_blank">${'$'}0.12</a>              <a class="card-price eur hidden" href="https://www.cardmarket.com/en/Pokemon/Products/Singles/Scarlet-Violet-Energies/Darkness-Energy-SVEen007?utm_source=limitlesstcg&utm_medium=text&utm_campaign=card_prices" target="_blank">0.12€</a>         </div>
            <div class="decklist-card" data-set="SVE" data-number="6" data-lang="en"   data-basic-energy="6" >
            <a class="card-link" href="/cards/SVE/6">
                <span class="card-count">2</span>
                <span class="card-name">Fighting Energy</span>
                                            </a>
             <a class="card-price usd" href="https://tcgplayer.pxf.io/LIMITLESS?u=https%3A%2F%2Fwww.tcgplayer.com%2Fproduct%2F475430%2Fsv01-scarlet-and-violet-base-set-basic-fighting-energy" target="_blank">${'$'}0.06</a>              <a class="card-price eur hidden" href="https://www.cardmarket.com/en/Pokemon/Products/Singles/Scarlet-Violet-Energies/Fighting-Energy-SVEen006?utm_source=limitlesstcg&utm_medium=text&utm_campaign=card_prices" target="_blank">0.04€</a>         </div>
    </div>
            </div>
            <div class="decklist-extras">
                <script>var LS_COPY = 'Copy to Clipboard', LS_COPY_SUCCESS = 'Copied!';</script>
                <div class="decklist-card-preview"><img class="card resp-w" width=353 height=491 src="https://limitlesstcg.nyc3.digitaloceanspaces.com/tpci/TEF/TEF_109_R_EN_MD.png" loading="lazy"></div>
                <button class="export">Copy to Clipboard</button>
                <button class="tool-link" data-target="/tools/proxies">Print Proxies</button>
                <button class="tool-link" data-target="/tools/imggen">Open as Image</button>
                <button class="builder">Open in Builder</button>
                <input type="hidden" name="_token" value="" autocomplete="off">            </div>
        </div>
    </div>

    <div  class="hidden"  data-image-decklist>
        <div class="decklist-visual embed light">
            <div class="card-grid">
                                    <div class="decklist-visual-card">
                        <a href="/cards/TEF/109">
                            <img class="card-picture card" src="https://limitlesstcg.nyc3.digitaloceanspaces.com/tpci/TEF/TEF_109_R_EN_XS.png" alt="Roaring Moon" loading="lazy" width=136 height=189>
                                                            <img class="card-count" src="/images/decklist/4.png" alt="4" loading="lazy">
                                                    </a>
                    </div>
                                    <div class="decklist-visual-card">
                        <a href="/cards/TEF/119">
                            <img class="card-picture card" src="https://limitlesstcg.nyc3.digitaloceanspaces.com/tpci/TEF/TEF_119_R_EN_XS.png" alt="Koraidon" loading="lazy" width=136 height=189>
                                                            <img class="card-count" src="/images/decklist/4.png" alt="4" loading="lazy">
                                                    </a>
                    </div>
                                    <div class="decklist-visual-card">
                        <a href="/cards/TEF/78">
                            <img class="card-picture card" src="https://limitlesstcg.nyc3.digitaloceanspaces.com/tpci/TEF/TEF_078_R_EN_XS.png" alt="Flutter Mane" loading="lazy" width=136 height=189>
                                                            <img class="card-count" src="/images/decklist/4.png" alt="4" loading="lazy">
                                                    </a>
                    </div>
                                    <div class="decklist-visual-card">
                        <a href="/cards/ASR/46">
                            <img class="card-picture card" src="https://limitlesstcg.nyc3.digitaloceanspaces.com/tpci/ASR/ASR_046_R_EN_XS.png" alt="Radiant Greninja" loading="lazy" width=136 height=189>
                                                            <img class="card-count" src="/images/decklist/1.png" alt="1" loading="lazy">
                                                    </a>
                    </div>
                                    <div class="decklist-visual-card">
                        <a href="/cards/PAR/170">
                            <img class="card-picture card" src="https://limitlesstcg.nyc3.digitaloceanspaces.com/tpci/PAR/PAR_170_R_EN_XS.png" alt="Professor Sada&#039;s Vitality" loading="lazy" width=136 height=189>
                                                            <img class="card-count" src="/images/decklist/4.png" alt="4" loading="lazy">
                                                    </a>
                    </div>
                                    <div class="decklist-visual-card">
                        <a href="/cards/TEF/147">
                            <img class="card-picture card" src="https://limitlesstcg.nyc3.digitaloceanspaces.com/tpci/TEF/TEF_147_R_EN_XS.png" alt="Explorer&#039;s Guidance" loading="lazy" width=136 height=189>
                                                            <img class="card-count" src="/images/decklist/4.png" alt="4" loading="lazy">
                                                    </a>
                    </div>
                                    <div class="decklist-visual-card">
                        <a href="/cards/PAR/163">
                            <img class="card-picture card" src="https://limitlesstcg.nyc3.digitaloceanspaces.com/tpci/PAR/PAR_163_R_EN_XS.png" alt="Earthen Vessel" loading="lazy" width=136 height=189>
                                                            <img class="card-count" src="/images/decklist/4.png" alt="4" loading="lazy">
                                                    </a>
                    </div>
                                    <div class="decklist-visual-card">
                        <a href="/cards/SVI/186">
                            <img class="card-picture card" src="https://limitlesstcg.nyc3.digitaloceanspaces.com/tpci/SVI/SVI_186_R_EN_XS.png" alt="Pokégear 3.0" loading="lazy" width=136 height=189>
                                                            <img class="card-count" src="/images/decklist/4.png" alt="4" loading="lazy">
                                                    </a>
                    </div>
                                    <div class="decklist-visual-card">
                        <a href="/cards/PAF/84">
                            <img class="card-picture card" src="https://limitlesstcg.nyc3.digitaloceanspaces.com/tpci/PAF/PAF_084_R_EN_XS.png" alt="Nest Ball" loading="lazy" width=136 height=189>
                                                            <img class="card-count" src="/images/decklist/4.png" alt="4" loading="lazy">
                                                    </a>
                    </div>
                                    <div class="decklist-visual-card">
                        <a href="/cards/PAF/91">
                            <img class="card-picture card" src="https://limitlesstcg.nyc3.digitaloceanspaces.com/tpci/PAF/PAF_091_R_EN_XS.png" alt="Ultra Ball" loading="lazy" width=136 height=189>
                                                            <img class="card-count" src="/images/decklist/4.png" alt="4" loading="lazy">
                                                    </a>
                    </div>
                                    <div class="decklist-visual-card">
                        <a href="/cards/PAR/160">
                            <img class="card-picture card" src="https://limitlesstcg.nyc3.digitaloceanspaces.com/tpci/PAR/PAR_160_R_EN_XS.png" alt="Counter Catcher" loading="lazy" width=136 height=189>
                                                            <img class="card-count" src="/images/decklist/3.png" alt="3" loading="lazy">
                                                    </a>
                    </div>
                                    <div class="decklist-visual-card">
                        <a href="/cards/PAL/188">
                            <img class="card-picture card" src="https://limitlesstcg.nyc3.digitaloceanspaces.com/tpci/PAL/PAL_188_R_EN_XS.png" alt="Super Rod" loading="lazy" width=136 height=189>
                                                            <img class="card-count" src="/images/decklist/2.png" alt="2" loading="lazy">
                                                    </a>
                    </div>
                                    <div class="decklist-visual-card">
                        <a href="/cards/PAL/189">
                            <img class="card-picture card" src="https://limitlesstcg.nyc3.digitaloceanspaces.com/tpci/PAL/PAL_189_R_EN_XS.png" alt="Superior Energy Retrieval" loading="lazy" width=136 height=189>
                                                            <img class="card-count" src="/images/decklist/1.png" alt="1" loading="lazy">
                                                    </a>
                    </div>
                                    <div class="decklist-visual-card">
                        <a href="/cards/SVI/182">
                            <img class="card-picture card" src="https://limitlesstcg.nyc3.digitaloceanspaces.com/tpci/SVI/SVI_182_R_EN_XS.png" alt="Pal Pad" loading="lazy" width=136 height=189>
                                                            <img class="card-count" src="/images/decklist/1.png" alt="1" loading="lazy">
                                                    </a>
                    </div>
                                    <div class="decklist-visual-card">
                        <a href="/cards/TEF/141">
                            <img class="card-picture card" src="https://limitlesstcg.nyc3.digitaloceanspaces.com/tpci/TEF/TEF_141_R_EN_XS.png" alt="Awakening Drum" loading="lazy" width=136 height=189>
                                                            <img class="card-count" src="/images/decklist/1.png" alt="1" loading="lazy">
                                                    </a>
                    </div>
                                    <div class="decklist-visual-card">
                        <a href="/cards/TEF/140">
                            <img class="card-picture card" src="https://limitlesstcg.nyc3.digitaloceanspaces.com/tpci/TEF/TEF_140_R_EN_XS.png" alt="Ancient Booster Energy Capsule" loading="lazy" width=136 height=189>
                                                            <img class="card-count" src="/images/decklist/4.png" alt="4" loading="lazy">
                                                    </a>
                    </div>
                                    <div class="decklist-visual-card">
                        <a href="/cards/PGO/68">
                            <img class="card-picture card" src="https://limitlesstcg.nyc3.digitaloceanspaces.com/tpci/PGO/PGO_068_R_EN_XS.png" alt="PokéStop" loading="lazy" width=136 height=189>
                                                            <img class="card-count" src="/images/decklist/3.png" alt="3" loading="lazy">
                                                    </a>
                    </div>
                                    <div class="decklist-visual-card">
                        <a href="/cards/SVE/7">
                            <img class="card-picture card" src="https://limitlesstcg.nyc3.digitaloceanspaces.com/tpci/SVE/SVE_007_R_EN_XS.png" alt="Darkness Energy" loading="lazy" width=136 height=189>
                                                            <img class="card-count" src="/images/decklist/6.png" alt="6" loading="lazy">
                                                    </a>
                    </div>
                                    <div class="decklist-visual-card">
                        <a href="/cards/SVE/6">
                            <img class="card-picture card" src="https://limitlesstcg.nyc3.digitaloceanspaces.com/tpci/SVE/SVE_006_R_EN_XS.png" alt="Fighting Energy" loading="lazy" width=136 height=189>
                                                            <img class="card-count" src="/images/decklist/2.png" alt="2" loading="lazy">
                                                    </a>
                    </div>
                            </div>
        </div>
    </div>

</div>

<div class="decklist-results">
    <h3>Decklist played by</h3>
    <ul>
            <li>1st Place <a href="/tournaments/436">Indonesia Regional League Vol.3</a> - <a href="/players/4609">Christian </a></li>
        </ul>
</div>

</div>
    </main>

            <div class="container dont-print">
            <div id="nn_mobile_lb2_sticky"></div>
            <div id="nn_mobile_mpu1"></div>
        </div>

    <div class="after-content"></div>

    <footer>
    <div class="container footer">
        <div class="menus">
            <div class="column">
                <h2><a href="https://limitlesstcg.com">Limitless TCG</a></h2>
                <ul>
                    <li><a href="/about">About</a></li>
                    <li><a href="/contact">Contact</a></li>
                    <li><a href="/privacy">Privacy Policy</a></li>
                    <li><a href="/legal">Site Notice</a></li>
                </ul>
            </div>
            <div class="column">
                <h2>Our Other Sites</h2>
                <ul>
                    <li><a href="https://my.limitlesstcg.com">Deck Builder</a></li>
                    <li><a href="https://play.limitlesstcg.com">Tournament Platform</a></li>
                    <li><a href="https://onepiece.limitlesstcg.com">One Piece Database</a></li>
                    <li><a href="https://limitlessvgc.com">Limitless VGC</a></li>
                </ul>
            </div>
            <div class="column">
                <h2>Follow</h2>
                <ul>
                    <li><a href="https://twitter.com/limitlesstcg"><i class="fab fa-twitter"></i> Twitter</a></li>
                    <li><a href="https://facebook.com/LimitlessTCG/"><i class="fab fa-facebook"></i> Facebook</a></li>
                    <li><a href="https://twitch.tv/limitless_tcg"><i class="fab fa-twitch"></i> Twitch</a></li>
                    <li><a href="https://www.patreon.com/limitlesstcg"><i class="fab fa-patreon"></i> Patreon</a></li>
                </ul>
            </div>
            <div class="column full">
                                                    <h2>TCGplayer</h2>
                    <a class="promo" href="https://tcgplayer.pxf.io/LIMITLESS?u=https%3A%2F%2Fwww.tcgplayer.com%2Fsearch%2Fpokemon%2Fproduct" target="_blank">
                        <img src="/images/tcgplayer.png" alt="tcgplayer logo" />
                        <p>You can support us by using our Affiliate Link when buying on TCGplayer!</p>
                    </a>
                            </div>

            <a href="#" class="cookie-link nn-cmp-show">Manage Cookies</a>
        </div>

        <div class="disclaimer">The literal and graphical information presented on this website about the Pokémon Trading Card Game, including card images and text, is copyright The Pokémon Company (Pokémon), Nintendo, Game Freak and/or Creatures. This website is not produced by, endorsed by, supported by, or affiliated with Pokémon, Nintendo, Game Freak or Creatures.</div>
    </div>
</footer>

    <div id="jsg">
        <div class="overlay"></div>
    </div>
</body>

</html>

"""
