(ns rusty-fisher.utils.antiban
  (:require [rusty-fisher.utils.antiban :as antiban]
            [rusty-fisher.constants :as constants]))

(import [java.util Random]
        [java.awt Point]
        [org.dreambot.api.input Mouse]
        [org.dreambot.api.methods.input Camera]
        [org.dreambot.api Client]
        [org.dreambot.api.methods.map Area]
        [org.dreambot.api.methods MethodProvider])

(defn pollingTime
  "Generates a normally distrubted polling time along a guasian distribution"
  ([mean stdev]
   (let [pollTime (+ (* (.nextGaussian (Random.)) stdev) mean)] ;; .nextGausian*stddev+mean to adjust the number to the desired mean & stdev
     (if (< 200 pollTime) ;; Polltimes < 200 milliseconds are probably too fast for people
       (int pollTime)
       (recur mean stdev)))))

(defn seededGuassianNumber
  "Returns a seeded number from a guassian curve with the supplied mean and stdev"
  [mean stdev]
  (+ (* (.nextGaussian (Random. (Integer/parseInt
                                 (apply str (filter #(Character/isDigit %) (Client/getPlayerHash)))))) stdev) mean))

(defn playerReactionTimeAvgActive
  "Returns the fast average reaction time for current player"
  []
  (seededGuassianNumber constants/activeReactionTimeAvg constants/activeReactionTimeStdDev))

(defn reactionDelayActive
  "Returns an active/fast reaction/delay time for the current player"
  []
  (pollingTime (playerReactionTimeAvgActive) constants/activeReactionTimeStdDev))

(defn reactionDelayAFK
  "Returns an AFK reaction/delay time for the current player"
  []
  (pollingTime 300000 200000))

(defn reactionDelay
  "Returns an reaction/delay time that is active active% of time with default 80"
  ([]
   (reactionDelay 80))

  ([active%Int]
   (if (> (rand-int 100) active%Int)
     (reactionDelayActive)
     (reactionDelayAFK))))

(defn moveMouseOutOfScreen
  "Moves the mouse outside of the screen"
  []
  (if (Mouse/isMouseInScreen)
    (Mouse/moveMouseOutsideScreen)
    true))

(defn randomCameraSpin
  "Spins the camera to a random tile"
  []
  (let [newArea (Area. (+ (.getX (Client/getLocalPlayer)) 10) (+ (.getY (Client/getLocalPlayer)) 10)
                       (- (.getX (Client/getLocalPlayer)) 10) (- (.getY (Client/getLocalPlayer)) 10))]
    (Camera/rotateToTile (.getRandomTile newArea))))

(defn randomMouseMovement
  "Makes a random mouse movement"
  []
  (Mouse/move (Point. (rand-int 760) (rand-int 500)))
  ;; Need a mechanism to clear a right click before using this.
  ;; (when (> 89 (rand-int 100)) ;; Sometimes right click
  ;;   (Mouse/click true)))
  )

(defn antiLogout
  "Performs an action to prevent logging out"
  []
  (MethodProvider/log "Performing anti logout action...")
  (let [rand_val (rand-int 100)]
    (cond
      (< 80 rand_val) (randomMouseMovement)
      (>= 80 rand_val) (randomCameraSpin)))
    ;; TODO: add more potential methods
    ;; either right click
    ;; spin
    ;; check a skill
    ;; swap tabs
    ;; and then maybe move mouse outside of screen
  ;;
  )
