lookup(K, [(K, V) | _], V).
lookup(K, [_ | T], V) :- lookup(K, T, V).

variable(Name, variable(Name)).
const(Value, const(Value)).

operation(op_add, A, B, R) :- R is A + B.
operation(op_subtract, A, B, R) :- R is A - B.
operation(op_multiply, A, B, R) :- R is A * B.
operation(op_divide, A, B, R) :- R is A / B.

operation(op_negate, A, R) :- R is -1 * A.
operation(op_sinh, A, R) :- A1 is -1 * A, R is (exp(A) - exp(A1)) / 2.
operation(op_cosh, A, R) :- A1 is -1 * A, R is (exp(A) + exp(A1)) / 2.

op_p(op_add) --> ['+'].
op_p(op_subtract) --> ['-'].
op_p(op_multiply) --> ['*'].
op_p(op_divide) --> ['/'].

op_un_p(op_negate) --> ['n', 'e', 'g', 'a', 't', 'e'].
op_un_p(op_sinh) --> ['s', 'i', 'n', 'h'].
op_un_p(op_cosh) --> ['c', 'o', 's', 'h'].

evaluate(const(Value), _, Value).
evaluate(variable(Name), Vars, R) :- lookup(Name, Vars, R).
evaluate(operation(Op, A, B), Vars, R) :-
    evaluate(A, Vars, AV),
    evaluate(B, Vars, BV),
    operation(Op, AV, BV, R).

evaluate(operation(Op, A), Vars, R) :-
    evaluate(A, Vars, AV),
    operation(Op, AV, R).

all_member([], _).
all_member([H | T], Values) :- member(H, Values), all_member(T, Values).

nonvar(V, T) :- var(V).
nonvar(V, T) :- nonvar(V), call(T).

:- load_library('alice.tuprolog.lib.DCGLibrary').

digits_p([]) --> [].

digits_p([H | T]) -->
  { member(H, ['-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'])},
  [H],
  digits_p(T).

whitespaces --> [].
whitespaces --> [' '], whitespaces.

expr_p(variable(Name)) --> whitespaces, [Name], whitespaces, { member(Name, [x, y, z]) }.

expr_p(const(Value)) -->
  { nonvar(Value, number_chars(Value, Chars)) },
  whitespaces, digits_p(Chars), whitespaces,
  { Chars = [_ | _], \= (Chars, ['-']), number_chars(Value, Chars)}.

expr_p(operation(Op, A)) -->
  whitespaces, op_un_p(Op), [' '], whitespaces, expr_p(A), whitespaces.

expr_p(operation(Op, A, B)) -->
  whitespaces, op_p(Op), [' '], whitespaces , expr_p(A), [' '], whitespaces, expr_p(B), whitespaces.

polish_str(E, A) :- ground(E), phrase(expr_p(E), C), atom_chars(A, C), !.
polish_str(E, A) :- atom(A), atom_chars(A, C), phrase(expr_p(E), C), !.
