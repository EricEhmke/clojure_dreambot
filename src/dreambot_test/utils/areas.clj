(ns dreambot-test.utils.areas)

(import [org.dreambot.api.methods.map Area])

(def fishingAreas {:Catherby
                   [(new Area 2835 3435 2849 3428)
                    (new Area 2850 3430 2861 3424)]})
;; Maybe just move to a tile within 10 of the fishing spot?

(def cookingAreas {:Catherby (new Area 2815 3444 2818 3439)})

;; (defn createCompositeArea
;;   "Creates a new Area which encompases all Areas in areas"
;;   [& areas]
;;   ;; (smallest x1, largest y1) (largest x2, smallest y2)
;;   ;; for each area (.getBoundingBox Area)
;;   )
