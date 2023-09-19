"use strict";

class Const {
    constructor(value) {
        this._value = value;
    }

    evaluate() {
        return this._value;
    }

    toString() {
        return this._value.toString();
    }

    prefix() {
        return this.toString();
    }

    static fromToken(token) {
        return !isNaN(token) && new Const(+token);
    }
}

class Variable {
    constructor(name) {
        this._name = name;
    }

    evaluate(...vars) {
        return vars[Variable.index.get(this._name)];
    }

    toString() {
        return this._name.toString();
    }

    prefix() {
        return this.toString();
    }

    static fromToken(token) {
        return this.index.has(token) && new Variable(token);
    }

    static index = new Map([['x', 0], ['y', 1], ['z', 2]]);
}

class Operation {
    constructor(symbol, nodes) {
        this._symbol = symbol;
        this._nodes = nodes;
    }

    _compute() {
        throw new Error('Unimplemented method');
    }

    evaluate(...vars) {
        return this._compute(...this._nodes.map(node => node.evaluate(...vars)));
    }

    toString() {
        return `${this._nodes.join(' ')} ${this._symbol}`;
    }

    prefix() {
        return `(${this._symbol} ${this._nodes.map(n => n.prefix()).join(' ')})`;
    }

    static _supported = new Map();

    static fromToken(token) {
        return this._supported.get(token);
    }

    static create(symbol, operationFunc, minArgc = operationFunc.length, maxArgc = minArgc) {
        class NewOperation extends Operation {
            constructor(...nodes) {
                super(symbol, nodes);
            }

            _compute = operationFunc;
            static minArgc = minArgc;
            static maxArgc = maxArgc;
        }

        Operation._supported.set(symbol, NewOperation);
        return NewOperation;
    }
}

const Add = Operation.create('+', (a, b) => a + b);
const Subtract = Operation.create('-', (a, b) => a - b);
const Multiply = Operation.create('*', (a, b) => a * b);
const Divide = Operation.create('/', (a, b) => a / b);
const Negate = Operation.create('negate', a => -a);
const ArcTan = Operation.create('atan', Math.atan);
const ArcTan2 = Operation.create('atan2', Math.atan2);

const reduceSum = (...args) => args.reduce((a, b) => a + b, 0)
const Sum = Operation.create("sum", reduceSum, 0, Infinity);
const Avg = Operation.create("avg", (...args) => reduceSum(...args) / args.length, 0, Infinity)

const parseFold = (stack, token) => {
    const operation = Operation.fromToken(token);
    const node = operation && new operation(...stack.splice(-operation.minArgc));
    stack.push(node || Variable.fromToken(token) || Const.fromToken(token));
    return stack;
}
const parse = string => string.trim().split(/\s+/).reduce(parseFold, [])[0];


class ParseError extends Error {
    constructor(message, token) {
        if (!token || !token.input || !token[0] || !token.index) {
            super(message);
            return;
        }

        const showRange = 20;
        const maxTokenLength = 50;
        const tokenLength = Math.min(token[0].length, maxTokenLength);
        const tokenEnd = token.index + tokenLength;

        const description = `${message} for token "${token[0]}" at pos: ${token.index}`;
        const details = `
        ${token.input.substring(token.index - showRange, tokenEnd + showRange)}
        ${' '.repeat(Math.min(token.index, showRange))}${'~'.repeat(tokenLength)}`;

        super(description + details);
    }
}

// ParseError: Expected operation for token "*+" at pos: 17
//         (+ (* x y) (/ 5 (*+ z 3)))
//                          ~~

class StringParser {
    constructor(string, tokenRegex) {
        if(string === null) {
            throw new ParseError("String is null");
        }
        this.string = string;
        this.tokenRegex = tokenRegex;
    }

    nextToken() {
        this.token = this.tokenRegex.exec(this.string);
        return this.currentToken();
    }

    currentToken() {
        return this.token?.at(0) || '';
    }

    error(message) {
        throw new ParseError(message, this.token);
    }

    expect(token, what = token) {
        this.nextToken() === token || this.error(`Expected "${what}"`);
    }
}

class PrefixParser extends StringParser {
    constructor(string) {
        super(string, /[()]|[^\s()]+/g);
    }

    parseFactor(token = this.nextToken()) {
        if (token === '(') {
            return this.parsePrefixOperation();
        }
        return Variable.fromToken(token) || Const.fromToken(token)
            || this.error('Expected "(" or const or variable');
    }

    parsePrefixOperation() {
        const operation = Operation.fromToken(this.nextToken()) || this.error('Expected operation');
        const args = Array.from({length: operation.minArgc}, this.parseFactor.bind(this));
        while (this.nextToken() !== ')') {
            if (args.push(this.parseFactor(this.currentToken())) > operation.maxArgc) {
                this.error('Too many arguments for operation');
            }
        }
        return new operation(...args);
    }

    parse() {
        if(this.nextToken() === '') {
            throw new ParseError("Empty expression");
        }
        const expr = this.parseFactor(this.currentToken());
        this.expect('', 'end of expression');
        return expr;
    }
}

const parsePrefix = string => new PrefixParser(string).parse();
