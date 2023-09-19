"use strict";

const operation = f => (...nodes) => (...vars) => f(...nodes.map(node => node(...vars)));

const add = operation((a, b) => a + b);
const subtract = operation((a, b) => a - b);
const multiply = operation((a, b) => a * b);
const divide = operation((a, b) => a / b);

const madd = operation((a, b, c) => a * b + c);

const negate = operation(a => -a);
const floor = operation(Math.floor);
const ceil = operation(Math.ceil);
const sin = operation(Math.sin);
const cos = operation(Math.cos);
const sinh = operation(Math.sinh);
const cosh = operation(Math.cosh);
const argIndex = f => (...args) => (...vars) => {
    const values = args.map(a => a(...vars));
    return values.indexOf(f(...values));
};
const argMin = argIndex(Math.min);
const argMin3 = argMin;
const argMin5 = argMin;

const argMax = argIndex(Math.max);
const argMax3 = argMax;
const argMax5 = argMax;

const variable = name => supportedVariables[name];
const cnst = val => () => val;

const supportedVariables = {
    'x': (...xyz) => xyz[0],
    'y': (...xyz) => xyz[1],
    'z': (...xyz) => xyz[2]
};

const supportedOperations = {
    '-': [subtract, 2],
    '+': [add, 2],
    '*': [multiply, 2],
    '/': [divide, 2],
    'negate': [negate, 1],
    '^': [ceil, 1],
    '_': [floor, 1],
    '*+': [madd, 3],
    'sin': [sin, 1],
    'cos': [cos, 1],
    'sinh': [sinh, 1],
    'cosh': [cosh, 1],
    'argMin3': [argMin, 3],
    'argMin5': [argMin, 5],
    'argMax3': [argMax, 3],
    'argMax5': [argMax, 5]
};

const one = cnst(1);
const two = cnst(2);

const supportedConstants = {one, two};

const parseFoldLeft = (stack, token) => {
    const tuple = supportedOperations[token];
    const op = tuple && tuple[0](...stack.splice(-tuple[1]));
    stack.push(op || supportedVariables[token] || supportedConstants[token] || cnst(+token))
    return stack;
};

const parse = string => string.trim().split(/\s+/).reduce(parseFoldLeft, [])[0];
