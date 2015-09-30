package com.kogecoo.scalaad.graph

import com.kogecoo.scalaad.rule.{ContainerValue, MathRule, NonContainerValue, Value}

import scala.language.higherKinds


class sin[U[_], T](v: Node[U, T])(implicit vr: MathRule[U, T]) extends UnaryOp[U, T] {
  override def toString: String = s"sin(${ v })"
  override def apply(): Value[U, T] = sin(v())
  override def deriv(wrt: Node[U, T]): Value[U, T] = cos(v()) * v.deriv(wrt)
  override def propagate(g: Value[U, T]): Value[U, T] = g * cos(v())
}

class cos[U[_], T](v: Node[U, T])(implicit vr: MathRule[U, T]) extends UnaryOp[U, T] {
  override def toString: String = s"cos(${ v })"
  override def apply(): Value[U, T] = cos(v())
  override def deriv(wrt: Node[U, T]): Value[U, T] = -sin(v()) * v.deriv(wrt)
  override def propagate(g: Value[U, T]): Value[U, T] = -sin(v()) * g
}

class tan[U[_], T](v: Node[U, T])(implicit vr: MathRule[U, T]) extends UnaryOp[U, T] {
  override def toString: String = s"tan(${ v })"
  override def apply(): Value[U, T] = tan(v())
  override def deriv(wrt: Node[U, T]): Value[U, T] = {
    val tan_v_val = tan(v())
    v.deriv(wrt) * (vr.zeroMul + tan_v_val * tan_v_val)
  }

  override def propagate(g: Value[U, T]): Value[U, T] = {
    val tan_v_val = tan(v())
    v.propagate(g * (vr.zeroMul + tan_v_val * tan_v_val))
  }
}

class ln[U[_], T](v: Node[U, T])(implicit r: MathRule[U, T]) extends UnaryOp[U, T] {
  override def toString: String = s"ln(${ v })"
  override def apply(): Value[U, T] = ln(v())
  override def deriv(wrt: Node[U, T]): Value[U, T] = v.deriv(wrt) / v()
  override def propagate(g: Value[U, T]): Value[U, T] = v.propagate(g / v())
}

class exp[U[_], T](v: Node[U, T])(implicit r: MathRule[U, T]) extends UnaryOp[U, T] {
  override def toString: String = s"exp(${ v })"
  override def apply(): Value[U, T] = exp(v())
  override def deriv(wrt: Node[U, T]): Value[U, T] = v.deriv(wrt) * v()
  override def propagate(g: Value[U, T]): Value[U, T] = v.propagate(g * v())
}


object sin {
  def apply[U[_], T](v: Node[U, T])(implicit vr: MathRule[U, T]): sin[U, T] = new sin(v)
  def apply[U[_], T](v: Value[U, T])(implicit mr: MathRule[U, T]): Value[U, T] = v match {
    case v: NonContainerValue[U, T] => NonContainerValue[U, T](mr.sinM(v.data))
    case v: ContainerValue[U, T]    => ContainerValue[U, T](mr.sinS(v.data))
  }
}

object cos {
  def apply[U[_], T](v: Node[U, T])(implicit vr: MathRule[U, T]): cos[U, T] = new cos(v)
  def apply[U[_], T](v: Value[U, T])(implicit mr: MathRule[U, T]): Value[U, T] = v match {
    case v: NonContainerValue[U, T] => NonContainerValue[U, T](mr.cosM(v.data))
    case v: ContainerValue[U, T]    => ContainerValue[U, T](mr.cosS(v.data))
  }
}

object tan {
  def apply[U[_], T](v: Node[U, T])(implicit vr: MathRule[U, T]): tan[U, T] = new tan(v)
  def apply[U[_], T](v: Value[U, T])(implicit mr: MathRule[U, T]): Value[U, T] = v match {
    case v: NonContainerValue[U, T] => NonContainerValue[U, T](mr.tanM(v.data))
    case v: ContainerValue[U, T]    => ContainerValue[U, T](mr.tanS(v.data))
  }
}

object ln {
  def apply[U[_], T](v: Node[U, T])(implicit vr: MathRule[U, T]): ln[U, T] = new ln(v)
  def apply[U[_], T](v: Value[U, T])(implicit mr: MathRule[U, T]): Value[U, T] = v match {
    case v: NonContainerValue[U, T] => NonContainerValue[U, T](mr.lnM(v.data))
    case v: ContainerValue[U, T]    => ContainerValue[U, T](mr.lnS(v.data))
  }
}

object exp {
  def apply[U[_], T](v: Node[U, T])(implicit vr: MathRule[U, T]): ln[U, T] = new ln(v)
  def apply[U[_], T](v: Value[U, T])(implicit mr: MathRule[U, T]): Value[U, T] = v match {
    case v: NonContainerValue[U, T] => NonContainerValue[U, T](mr.expM(v.data))
    case v: ContainerValue[U, T]    => ContainerValue[U, T](mr.expS(v.data))
  }
}

