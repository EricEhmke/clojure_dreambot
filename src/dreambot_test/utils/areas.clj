(ns dreambot-test.utils.areas)

(import [org.dreambot.api.methods.map Area Tile])

(def fishingAreas {:Catherby
                   [(new Area 2835 3435 2849 3428)
                    (new Area 2850 3430 2861 3424)]
                   :Draynor
                   [(new Area
                         (into-array [(new Tile 3079 3240 0)
                                      (new Tile 3083 3236 0)
                                      (new Tile 3086 3231 0)
                                      (new Tile 3087 3225 0)
                                      (new Tile 3092 3229 0)
                                      (new Tile 3087 3239 0)]))]
                   :Lumbridge_Swamp
                   [(new Area
                         (into-array [(new Tile 3241 3155 0)
                                      (new Tile 3240 3152 0)
                                      (new Tile 3237 3145 0)
                                      (new Tile 3239 3141 0)
                                      (new Tile 3241 3142 0)
                                      (new Tile 3247 3160 0)
                                      (new Tile 3241 3160 0)]))]
                   :Barbarian_Village
                   [(new Area
                         (into-array [(new Tile 3100 3426 0)
                                      (new Tile 3100 3435 0)
                                      (new Tile 3110 3435 0)
                                      (new Tile 3108 3430 0)
                                      (new Tile 3104 3426 0)
                                      (new Tile 3104 3423 0)]))]
                   :Fishing_Guild
                   [(new Area
                         (into-array [(new Tile 2605 3426 0)
                                      (new Tile 2605 3420 0)
                                      (new Tile 2595 3420 0)
                                      (new Tile 2595 3422 0)
                                      (new Tile 2598 3422 0)
                                      (new Tile 2598 3422 0)
                                      (new Tile 2599 3426 0)]))
                    (new Area
                         (into-array [(new Tile 2603 3416 0)
                                      (new Tile 2612 3416 0)
                                      (new Tile 2612 3411 0)
                                      (new Tile 2605 3411 0)
                                      (new Tile 2605 3406 0)
                                      (new Tile 2603 3406 0)]))]})

(def cookingAreas {:Catherby (new Area 2815 3444 2818 3439)})
