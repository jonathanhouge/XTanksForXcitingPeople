# XTanksForXcitingPeople
The third assignment for CS 335, implemented from 10/20/22 to 11/10/22.

Hey! Why don't you read me? 
Basic controls: WASD or the arrow keys for movement. Spacebar for shooting!

Some quick notices:
* Those running as 'Client' should set their IP address to be the same as the one running the 'Server'. [this needs to be done manually at the top of the Client code]
* If XTankServer fails to connect, usually running XTank once (which will also fail) will fix running XTankServer.

* Bounds work perfectly fine with every resolution - but "Courtyard" and "Fortress" have walls within the map that are set specifically for one resolution.
  * Changing one's resolution to 1366 x 768 will allow for playing on "Courtyard" and "Fortress" as intended.
  * Not everyone's resolution is the same! For quality play between machines, make sure all clients have the same resolution.

* There are three different kinds of tanks - each with different movement and health.
* There are two different modes: Standard and Freemode. 
  * Standard: Game ends when there's one or less people alive. [does not work with one player!]
  * Freemode: A debugging / trial mode that lets you get aquainted with the software. Ideal with one player but allows multiplayer.

* Name's are max ten characters. If the client doesn't enter a name, a random name in the format '[emoticon]' is given.
  * There are only a set amount of these so there's a rare chance where two players that don't set a unique name could be named the same!

* If you join before the last player, close the waiting dialog ASAP!
  * If you don't close the dialog, there is catch-up!
    Ex: Two players are playing. The first one joined gets a waiting dialog, the second doesn't.
    The second player can begin playing the game immediately, while the first player needs to dismiss their waiting dialog (by hitting the "Okay!" button) to start their
    UI. If the first player doesn't click this button and the second player begins play, the first player catches up to the second player in real time - you're able to
    see everything that player two did before you entered the game. Yet, the game was going and is still going! So, technically, leaving the waiting dialog up for too
    long can result in immediate death by pesky player two! Or, just weird stuff in general. It's cool to see and a neat feature, but can be alarming without notice!

* To play again, hit the play again button in the game over dialog: there's a catch! You have to hit it twice and you can only play again once.
  * In general, this is very wonky. Shooting diagonally at this time causes damage to the player who's shooting - but everything else seems to work fine.
  * If you try to play again a second time, the game will load and it's possible - but it won't game over properly.
