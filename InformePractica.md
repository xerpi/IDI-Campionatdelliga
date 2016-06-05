# Informació sobre l'aplicació
Per a la creació d'aquesta aplicació s'ha utilitzat la biblioteca [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) per tal de poder dibuixar gràfiques.  
S'han implementat totes les funcionalitats addicionals descrites en l'enunciat de la pràctica.

# Pantalla inicial
En la pantalla inicial hi trobem els botons principals per navegar per l'aplicació.
A més a més també hi ha dues gràfiques, una ens mostra el top 3 de la classificació dels equips per punts, i l'altre el top 3 de jugadors que més gols han marcat.  
Si premem sobre la icona que hi ha a la part dreta de la barra superior, ens sortirà un menú amb les opcions d'"About" i d'ajuda.  
En l'opció "About" hi trobem la informació relacionada amb aquesta aplicació.
Si polsem ajuda, ens surt un diàleg on si acceptem s'obrirà el navegador d'internet a una pàgina que conté aquest document.

# Equips
Des de la pantalla inicial podem accedir a la llista de tots els equips que competeixen en la lliga polsant el botó "Equips". Per veure les dades d'un equip en concret, hem de polsar sobre un equip de la llista.
Un cop estiguem en la pantalla de la informació d'un equip en concret, podem canviar el seu escut prement durant uns segons la imatge de l'escut actual. També ens apareix la ciutat (és a dir, seu) de l'equip.
A sota podem veure totes les dades de l'equip relacionades amb la lliga. Aquestes dades són:
* Partits guanyats (text de color verd)
* Partits empatats (text de color verd)
* Partits perduts (text de color vermell)
* Punts (text de color groc)

A sota d'aquestes dades hi tenim dos botons: el botó ["Mostra Jugadors"](#mostra-jugadors), per accedir a la llista de jugadors de l'equip i el botó ["Mostra Evolució"](#mostra-evolució-de-lequip), que ens envia a una pantalla amb informació sobre l'evolució de l'equip.  

Podem eliminar un equip, per representar que ha baixat de categoria, per exemple si premem durant uns quants segons sobre un equip de la llista i seleccionem ["Eliminar Equip"](#eliminar-equip).

# Eliminar Equip
Eliminar un equip correspon amb crear-ne un de nou, per tant en aquesta pantalla hem d'entrar la informació bàsica de l'equip nou que es crearà: el nom i la seu de l'equip.  
Un cop acceptem la creació del nou equip, es generaran automàticament tots els jugadors d'aquests nou equip.

# Mostra Jugadors
En aquesta pantalla hi trobem la llista de jugadors de l'equip, amb la informació de si cada jugador és titular o és reserva.  
Si es prem un jugador durant uns segons, apareix un menú on podem realitzar accions sobre el jugador triat.
La primera opció és passar el jugador de titular a reserva (si és titular) o de reserva a titular (si és reserva). Si seleccionem aquesta opció, ens apareixerà una llista amb la resta de jugadors de l'equip, on hem de triar un per fer l'intercanvi de titular a reserva o al revés.  
L'altra opció que tenim és la d'eliminar el jugador, si la seleccionem i confirmem, el jugador quedarà eliminat (es continuarà mostrant en la classificació dels jugadors per gols marcats) i es crearà un jugador nou que ocuparà el seu lloc.

# Mostra evolució de l'equip
Aquí hi trobarem una pantalla amb dues pestanyes, amb una gràfica en cada pestanya.
La primera pestanya, anomenada "Gols", conté els gols marcats (línia verda) i encaixats (línia vermella) de l'equip en cada jornada jugada.  
La segona pestanya, anomenada "Partits", conté els partits guanyats (línia verda), empatats (línia blava) i perduts (línia verda) de l'equip en cada jornada jugada.

# Partits
Des de la pantalla inicial podem accedir a la llista de tots els partits realitzats en la lliga polsant el botó "Partits".
Un cop hem accedit en aquesta pantalla, podem veure la informació d'un partit en concret (polsant sobre el partit que desitgem) o també podem crear un partit nou, si polsem sobre la icona ["+"](#creació-dun-nou-partit) que hi ha a la part dreta de la barra superior.

# Informació d'un partit
En aquesta pantalla podem veure els equips que han disputat el partit, el resultat d'aquest, i la jornada en la qual es va disputar.  
A més a més també hi trobem la llista dels gols marcats en el partit, amb informació de quin jugador va marcar el gol i en quin minut va fer-ho.

# Jornades
Des de la pantalla inicial podem accedir a la llista de tots els partits realitzats en la lliga polsant el botó "Jornades".
Aquí hi trobem una llista de les jornades de la lliga, tant les que s'han acabat com les que estan en curs. Podem veure informació més detallada de la jornada si polsem sobre una.

# Informació d'una jornada
En aquesta pantalla veiem la llista dels partits disputats en la jornada.
Si polsem sobre un partit, passarem a la pantalla d'informació del partit.
Podem crear un partit nou en la jornada si polsem sobre la icona ["+"](#creació-dun-nou-partit) que hi ha a la part dreta de la barra superior.

# Creació d'un nou partit
Per accedir en aquesta pantalla es pot fer tant des de la pantalla ["Partits"](#partits), com des de la d'informació d'una jornada.  
Hem de triar els equips (local i visitant) que volem que disputin el partit, i la jornada en la qual volem que es disputi aquest.  
Si triem dos equips que ja han disputat un partit en la jornada triada, quan li donem al botó acceptar ens avisarà d'això.
Si polsem el botó "Afegir Gol", podem triar el jugador que ha marcat el gol i el minut en el qual s'ha marcat.
Cada cop que afegim un gol ens apareixerà aquest en la llista de gols. Podem eliminar un gol si pitgem durant uns quants segons sobre aquest.
Un cop tinguem les dades desitjades introduïdes, podem realitzar la confirmació de la creació del nou partit polsant el botó "Acceptar".  
Si volem tirar enrere (i no crear el nou partit), podem prémer el botó "Cancel·lar", en cas que hàgim introduït algun gol sortirà un diàleg demanant-nos confirmació de si estem segurs que volem tirar enrere.

# Classificació dels equips
En aquesta pantalla hi podem trobar la classificació dels equips de la lliga en funció dels punts de cada equip.
Es mostra una gràfica de barres, en la qual podem fer zoom si fem doble-click. Si polsem sobre una barra ens surt l'equip i els seus punts (en cas que s'hagi fet massa zoom-out).

# Classificació dels jugadors
En aquesta pantalla hi podem trobar la classificació de tots els jugadors que han participat en la lliga (encara que no hagin marcat cap gol), en funció dels gols marcats.
Es mostra una gràfica de barres, en la qual podem fer zoom si fem doble-click. Si polsem sobre una barra ens surt el jugador i el nombre de gols marcats (en cas que s'hagi fet massa zoom-out).
