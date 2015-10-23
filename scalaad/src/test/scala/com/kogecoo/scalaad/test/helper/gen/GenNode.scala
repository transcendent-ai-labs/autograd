package com.kogecoo.scalaad.test.helper.gen

import com.kogecoo.scalaad.graph.{Node, ContainerConst, Var, ScalarConst}
import org.scalacheck.Gen
import org.scalacheck.Gen.oneOf

import scala.language.higherKinds


abstract class GenNode[U[_], T] {

  lazy val defaultRestriction = (_: T) => true

  final def genNode(restrict: T => Boolean = defaultRestriction): Gen[Node[U, T]] = {
    oneOf(genScalarConst(restrict), genContainerConst(restrict), genVar(restrict))
  }

  final def genVar(restrict: T => Boolean = defaultRestriction): Gen[Var[U, T]] = {
    genVarWithSource(restrict).map(_.node)
  }

  final def genScalarConst(restrict: T => Boolean = defaultRestriction): Gen[ScalarConst[U, T]] = {
    genScalarConstWithSource(restrict).map(_.node)
  }

  final def genContainerConst(restrict: T => Boolean = defaultRestriction): Gen[ContainerConst[U, T]] = {
    genContainerConstWithSource(restrict).map(_.node)
  }

  def genVarWithSource(restrict: T => Boolean = defaultRestriction): Gen[VarSample[U, T]]

  def genScalarConstWithSource(restrict: T => Boolean = defaultRestriction): Gen[ScalarConstSample[U, T]]

  def genContainerConstWithSource(restrict: T => Boolean = defaultRestriction): Gen[ContainerConstSample[U, T]]

}

class VarSample[U[_], T](val node: Var[U, T], val src: U[T])

class ScalarConstSample[U[_], T](val node: ScalarConst[U, T], val src: T)

class ContainerConstSample[U[_], T](val node: ContainerConst[U, T], val src: U[T])

