package com.spencerwi.either;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Either<L, R> {
    protected L leftValue;
    protected R rightValue;

    public static <L,R> Either<L,R> either(Supplier<L> leftSupplier, Supplier<R> rightSupplier){
        R rightValue = rightSupplier.get();
        if (rightValue != null){
            return Either.<L,R>right(rightValue);
        } else {
            return Either.<L,R>left(leftSupplier.get());
        }
    }

    public static <L,R> Either<L,R> left(L left){ return new Left<>(left); }
    public static <L,R> Either<L,R> right(R right){ return new Right<>(right); }

    public abstract L getLeft();
    public abstract R getRight();

    public abstract boolean isLeft();
    public abstract boolean isRight();

    public abstract <T> T fold(Function<L,T> transformLeft, Function<R,T> transformRight);
    public abstract <T,U> Either<T,U> map(Function<L,T> transformLeft, Function<R,U> transformRight);

    public static class Left<L,R> extends Either<L, R> {
        private Left(L left) {
            this.leftValue = left;
            this.rightValue = null;
        }

        @Override
        public L getLeft() { return this.leftValue; }
        @Override
        public R getRight() { throw new NoSuchElementException("Tried to getRight from a Left"); }

        @Override
        public boolean isLeft() { return true; }
        @Override
        public boolean isRight() { return false; }

        @Override
        public <T> T fold(Function<L, T> transformLeft, Function<R, T> transformRight) {
            return transformLeft.apply(this.leftValue);
        }

        @Override
        public <T, U> Either<T, U> map(Function<L, T> transformLeft, Function<R, U> transformRight) {
            return Either.<T,U>left(transformLeft.apply(this.leftValue));
        }

        @Override
        public int hashCode(){ return this.leftValue.hashCode(); }
        @Override
        public boolean equals(Object other){
            if (other instanceof Left<?,?>){
                final Left<?, ?> otherAsLeft = (Left<?, ?>)other;
                return this.leftValue.equals(otherAsLeft.leftValue);
            } else {
                return false;
            }
        }

    }
    public static class Right<L,R> extends Either<L, R> {
        private Right(R right) {
            this.leftValue = null;
            this.rightValue = right;
        }

        @Override
        public L getLeft() { throw new NoSuchElementException("Tried to getLeft from a Right"); }
        @Override
        public R getRight() { return rightValue; }

        @Override
        public boolean isLeft() { return false; }
        @Override
        public boolean isRight() { return true; }

        @Override
        public <T> T fold(Function<L, T> transformLeft, Function<R, T> transformRight) {
            return transformRight.apply(this.rightValue);
        }
        @Override
        public int hashCode(){ return this.rightValue.hashCode(); }
        @Override
        public boolean equals(Object other){
            if (other instanceof Right<?,?>){
                final Right<?, ?> otherAsRight = (Right<?, ?>)other;
                return this.rightValue.equals(otherAsRight.rightValue);
            } else {
                return false;
            }
        }

        @Override
        public <T, U> Either<T, U> map(Function<L, T> transformLeft, Function<R, U> transformRight) {
            return Either.<T,U>right(transformRight.apply(this.rightValue));
        }
    }
}
