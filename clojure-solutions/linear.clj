(defn component-operation [f] #(apply mapv f %&))

(def v+ (component-operation +))
(def v- (component-operation -))
(def v* (component-operation *))
(def vd (component-operation /))

(def m+ (component-operation v+))
(def m- (component-operation v-))
(def m* (component-operation v*))
(def md (component-operation vd))

(defn scalar [& v] (apply + (apply v* v)))
(defn transpose [m] (apply mapv vector m))

(defn v*s [v s] (mapv #(* % s) v))
(defn m*s [m s] (mapv #(v*s % s) m))

(defn m*v [m v] (mapv #(scalar % v) m))

(defn m*m [m1 m2]
  (mapv #(m*v (transpose m2) %) m1))

(defn vect [[x y z] v]
  (m*v [[0, (- z), y], [z, 0, (- x)], [(- y), x, 0]] v))

; Shapeless
(defn shapeless-operation [f]
  (fn [s1 s2]
    (if (vector? s1)
      (mapv (shapeless-operation f) s1 s2)
      (f s1 s2))))

(def s+ (shapeless-operation +))
(def s- (shapeless-operation -))
(def s* (shapeless-operation *))
(def sd (shapeless-operation /))
