(defn expression [f]
  (fn [& nodes]
    (fn [vars]
      (apply f (mapv #(% vars) nodes)))))

(def add (expression +))
(def subtract (expression -))
(def multiply (expression *))
(def negate (expression -))
(def divide
  (expression #(/ (double %1) (double %2))))

(def arcTan (expression #(Math/atan %)))
(def atan arcTan)

(def arcTan2 (expression #(Math/atan2 %1 %2)))
(def atan2 arcTan2)

(def constant constantly)

(defn variable [str]
  (fn [vars] (vars str)))

(def operations
  {'+        add,
   '-        subtract,
   '*        multiply,
   '/        divide,
   'negate   negate,
   'atan     arcTan,
   'atan2    arcTan2,
   :constant constant,
   :variable variable})

; ---------------Object---------------
(defn proto-get [obj key default]
  (cond
    (contains? obj key) (get obj key)
    (contains? obj :proto) (recur (get obj :proto) key default)
    :else default))
(defn proto-call [obj key & args]
  (apply (proto-get obj key nil) obj args))
(defn method [key]
  (fn [obj & args] (apply proto-call obj key args)))
(defn field
  ([key] (field key nil))
  ([key default] (fn [obj] (proto-get obj key default))))
(defn constructor [constr proto]
  (fn [& args] (apply constr {:proto proto} args)))


(def _value (field :value))
(def _name (field :name))
(def _nodes (field :nodes))
(def _func (field :func))
(def evaluate (method :evaluate))
(def toString (method :toString))
(def toStringPostfix (method :toStringPostfix))


(def ConstantProto
  {:evaluate        (fn [this _] (_value this))
   :toString        (fn [this] (str (_value this)))
   :toStringPostfix (fn [this] (str (_value this)))})
(defn CConstant [this value]
  (assoc this :value value))
(def Constant (constructor CConstant ConstantProto))


(def VariableProto
  {:evaluate        (fn [this vars] (vars (str (Character/toLowerCase (first (_name this))))))
   :toString        (fn [this] (_name this))
   :toStringPostfix (fn [this] (_name this))})
(defn CVariable [this name]
  (assoc this :name name))
(def Variable (constructor CVariable VariableProto))


(def OperationProto
  {:evaluate        (fn [this vars] (apply (_func this) (mapv #(evaluate % vars) (_nodes this))))
   :toString        (fn [this] (str "(" (_name this) " " (clojure.string/join " " (mapv toString (_nodes this))) ")"))
   :toStringPostfix (fn [this] (str "(" (clojure.string/join " " (mapv toStringPostfix (_nodes this))) " " (_name this) ")"))})
(defn CreateOperation [name func]
  (constructor
    (fn [this & nodes]
      (assoc this :name name :func func :nodes nodes))
    OperationProto))


(def Add (CreateOperation "+" +))
(def Subtract (CreateOperation "-" -))
(def Multiply (CreateOperation "*" *))
(def Divide (CreateOperation "/" #(/ (double %1) (double %2))))
(def Negate (CreateOperation "negate" -))
(def Sinh (CreateOperation "sinh" #(Math/sinh %)))
(def Cosh (CreateOperation "cosh" #(Math/cosh %)))
(def UPow (CreateOperation "**" #(Math/exp %)))
(def ULog (CreateOperation "//" #(Math/log %)))


(def object-operations
  {
   '**           UPow
   (symbol "//") ULog
   '+            Add
   '-            Subtract
   '*            Multiply
   '/            Divide
   'negate       Negate
   'sinh         Sinh
   'cosh         Cosh
   :variable     Variable
   :constant     Constant})


(defn abstractParse [operations-map input-str]
  (letfn [(parseImpl [expr]
            (cond (list? expr)
                  (let [op-symbol (first expr)
                        args (rest expr)
                        op (operations-map op-symbol (resolve op-symbol))]
                    (apply op (mapv parseImpl args)))
                  (symbol? expr) ((operations-map :variable) (str expr))
                  :else ((operations-map :constant) expr)))]
    (parseImpl (read-string input-str))
    ))

; ---------------Comb---------------
(load-file "parser.clj")

(defn get-symbol-keys-as-strings [m]
  (mapv str (filter symbol? (keys m))))

(defn +string [s] (apply
                    (partial +seqf (constantly s))
                    (mapv #(+char (str %)) s)))

(defn abstractPostfixParse [operations-map]
  (defparser parserImpl
             *all-chars (mapv char (range 0 128))
             (*chars [p] (+char (apply str (filter p *all-chars))))
             *letter (*chars #(Character/isLetter %))
             *digit (*chars #(Character/isDigit %))
             *space (*chars #(Character/isWhitespace %))
             *ws (+ignore (+star *space))

             *unsigned-int (+str (+plus *digit))
             *fraction-part (+seqf str (+char ".") *unsigned-int)
             *real (+map read-string (+seqf str (+opt (+char "-")) *unsigned-int (+opt *fraction-part)))

             *var (+str (+plus (+char "xyzXYZ")))

             operations-strings (get-symbol-keys-as-strings operations-map)
             sorted-ops (sort-by #(- (count %)) operations-strings)
             *op (apply +or (mapv +string sorted-ops))

             *number-parse (+map (operations-map :constant) *real)
             *var-parse (+map #((operations-map :variable) %) *var)

             *op-parse (+map #((comp object-operations symbol str) %) *op)

             *closure-begin (+ignore (+char "("))
             *closure-end (+seqn 0 *op-parse *ws (+ignore (+char ")")))

             (*closure-args [parser] (+opt (+seqf cons *ws parser (+star (+seqn 0 *ws parser)))))
             *closure (+seqf #(apply %2 %1) *closure-begin *ws (*closure-args (delay *value)) *ws *closure-end)
             *value (+or *closure *number-parse *var-parse)

             *parserImpl (+seqn 0 *ws *value *ws)
             )
  parserImpl)

; HW 10
(def parseFunction (partial abstractParse operations))

; HW 11
(def parseObject (partial abstractParse object-operations))

; HW 12
(def parseObjectPostfix (abstractPostfixParse object-operations))
