(ns dreambot-test.utils.antiban)

(import [java.util Random]
        [java.awt Point]
        [org.dreambot.api.input Mouse]
        [org.dreambot.api.methods.input Camera]
        [org.dreambot.api Client]
        [org.dreambot.api.methods.map Area]
        [org.dreambot.api.methods MethodProvider])

(defn pollingTime
  "Generates a normally distrubted polling time along a guasian distribution"
  ([]
   (pollingTime 3000 1000)) ;; milliseconds
  ([mean stdev]
   (let [pollTime (+ (* (.nextGaussian (Random.)) stdev) mean)] ;; .nextGausian*stddev+mean to adjust the number to the desired mean & stdev
     (if (< 500 pollTime) ;; Polltimes < half a second are too fast
       (int pollTime)
       (recur mean stdev)))))

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