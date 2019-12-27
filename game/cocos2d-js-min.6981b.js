define("cocos2d-js-min.6981b.js", function(require, module, exports, process) {
 (function(t, e, i) {
  function n(i, r) {
   var s = e[i];
   if (!s) {
    var a = t[i];
    if (!a) return;
    var o = {};
    s = e[i] = {
     exports: o
    }, a[0]((function(t) {
     return n(a[1][t] || t)
    }), s, o)
   }
   return s.exports
  }
  for (var r = 0; r < i.length; r++) n(i[r])
 })({
  1: [(function(t, e, i) {
   t("../core/platform/CCClass");
   var n = t("../core/utils/misc");
   cc.Action = cc.Class({
    name: "cc.Action",
    ctor: function() {
     this.originalTarget = null, this.target = null, this.tag = cc.Action.TAG_INVALID
    },
    clone: function() {
     var t = new cc.Action;
     return t.originalTarget = null, t.target = null, t.tag = this.tag, t
    },
    isDone: function() {
     return !0
    },
    startWithTarget: function(t) {
     this.originalTarget = t, this.target = t
    },
    stop: function() {
     this.target = null
    },
    step: function(t) {
     cc.logID(1006)
    },
    update: function(t) {
     cc.logID(1007)
    },
    getTarget: function() {
     return this.target
    },
    setTarget: function(t) {
     this.target = t
    },
    getOriginalTarget: function() {
     return this.originalTarget
    },
    setOriginalTarget: function(t) {
     this.originalTarget = t
    },
    getTag: function() {
     return this.tag
    },
    setTag: function(t) {
     this.tag = t
    },
    retain: function() {},
    release: function() {}
   }), cc.Action.TAG_INVALID = -1, cc.FiniteTimeAction = cc.Class({
    name: "cc.FiniteTimeAction",
    extends: cc.Action,
    ctor: function() {
     this._duration = 0
    },
    getDuration: function() {
     return this._duration * (this._timesForRepeat || 1)
    },
    setDuration: function(t) {
     this._duration = t
    },
    reverse: function() {
     return cc.logID(1008), null
    },
    clone: function() {
     return new cc.FiniteTimeAction
    }
   }), cc.Speed = cc.Class({
    name: "cc.Speed",
    extends: cc.Action,
    ctor: function(t, e) {
     this._speed = 0, this._innerAction = null, t && this.initWithAction(t, e)
    },
    getSpeed: function() {
     return this._speed
    },
    setSpeed: function(t) {
     this._speed = t
    },
    initWithAction: function(t, e) {
     return t ? (this._innerAction = t, this._speed = e, !0) : (cc.errorID(1021), !1)
    },
    clone: function() {
     var t = new cc.Speed;
     return t.initWithAction(this._innerAction.clone(), this._speed), t
    },
    startWithTarget: function(t) {
     cc.Action.prototype.startWithTarget.call(this, t), this._innerAction.startWithTarget(t)
    },
    stop: function() {
     this._innerAction.stop(), cc.Action.prototype.stop.call(this)
    },
    step: function(t) {
     this._innerAction.step(t * this._speed)
    },
    isDone: function() {
     return this._innerAction.isDone()
    },
    reverse: function() {
     return new cc.Speed(this._innerAction.reverse(), this._speed)
    },
    setInnerAction: function(t) {
     this._innerAction !== t && (this._innerAction = t)
    },
    getInnerAction: function() {
     return this._innerAction
    }
   }), cc.speed = function(t, e) {
    return new cc.Speed(t, e)
   }, cc.Follow = cc.Class({
    name: "cc.Follow",
    extends: cc.Action,
    ctor: function(t, e) {
     this._followedNode = null, this._boundarySet = !1, this._boundaryFullyCovered = !1, this._halfScreenSize = null, this._fullScreenSize = null, this.leftBoundary = 0, this.rightBoundary = 0, this.topBoundary = 0, this.bottomBoundary = 0, this._worldRect = cc.rect(0, 0, 0, 0), t && (e ? this.initWithTarget(t, e) : this.initWithTarget(t))
    },
    clone: function() {
     var t = new cc.Follow,
      e = this._worldRect,
      i = new cc.Rect(e.x, e.y, e.width, e.height);
     return t.initWithTarget(this._followedNode, i), t
    },
    isBoundarySet: function() {
     return this._boundarySet
    },
    setBoudarySet: function(t) {
     this._boundarySet = t
    },
    initWithTarget: function(t, e) {
     if (!t) return cc.errorID(1022), !1;
     e = e || cc.rect(0, 0, 0, 0), this._followedNode = t, this._worldRect = e, this._boundarySet = !(0 === e.width && 0 === e.height), this._boundaryFullyCovered = !1;
     var i = cc.winSize;
     return this._fullScreenSize = cc.v2(i.width, i.height), this._halfScreenSize = this._fullScreenSize.mul(.5), this._boundarySet && (this.leftBoundary = -(e.x + e.width - this._fullScreenSize.x), this.rightBoundary = -e.x, this.topBoundary = -e.y, this.bottomBoundary = -(e.y + e.height - this._fullScreenSize.y), this.rightBoundary < this.leftBoundary && (this.rightBoundary = this.leftBoundary = (this.leftBoundary + this.rightBoundary) / 2), this.topBoundary < this.bottomBoundary && (this.topBoundary = this.bottomBoundary = (this.topBoundary + this.bottomBoundary) / 2), this.topBoundary === this.bottomBoundary && this.leftBoundary === this.rightBoundary && (this._boundaryFullyCovered = !0)), !0
    },
    step: function(t) {
     var e = this.target.convertToWorldSpaceAR(cc.Vec2.ZERO),
      i = this._followedNode.convertToWorldSpaceAR(cc.Vec2.ZERO),
      r = e.sub(i),
      s = this.target.parent.convertToNodeSpaceAR(r.add(this._halfScreenSize));
     if (this._boundarySet) {
      if (this._boundaryFullyCovered) return;
      this.target.setPosition(n.clampf(s.x, this.leftBoundary, this.rightBoundary), n.clampf(s.y, this.bottomBoundary, this.topBoundary))
     } else this.target.setPosition(s.x, s.y)
    },
    isDone: function() {
     return !this._followedNode.activeInHierarchy
    },
    stop: function() {
     this.target = null, cc.Action.prototype.stop.call(this)
    }
   }), cc.follow = function(t, e) {
    return new cc.Follow(t, e)
   }
  }), {
   "../core/platform/CCClass": 108,
   "../core/utils/misc": 186
  }],
  2: [(function(t, e, i) {
   function n(t, e) {
    return t[Math.min(t.length - 1, Math.max(e, 0))]
   }

   function r(t) {
    for (var e = [], i = t.length - 1; i >= 0; i--) e.push(cc.v2(t[i].x, t[i].y));
    return e
   }

   function s(t) {
    for (var e = [], i = 0; i < t.length; i++) e.push(cc.v2(t[i].x, t[i].y));
    return e
   }
   cc.CardinalSplineTo = cc.Class({
    name: "cc.CardinalSplineTo",
    extends: cc.ActionInterval,
    ctor: function(t, e, i) {
     this._points = [], this._deltaT = 0, this._tension = 0, this._previousPosition = null, this._accumulatedDiff = null, void 0 !== i && cc.CardinalSplineTo.prototype.initWithDuration.call(this, t, e, i)
    },
    initWithDuration: function(t, e, i) {
     return e && 0 !== e.length ? !!cc.ActionInterval.prototype.initWithDuration.call(this, t) && (this.setPoints(e), this._tension = i, !0) : (cc.errorID(1024), !1)
    },
    clone: function() {
     var t = new cc.CardinalSplineTo;
     return t.initWithDuration(this._duration, s(this._points), this._tension), t
    },
    startWithTarget: function(t) {
     cc.ActionInterval.prototype.startWithTarget.call(this, t), this._deltaT = 1 / (this._points.length - 1), this._previousPosition = cc.v2(this.target.x, this.target.y), this._accumulatedDiff = cc.v2(0, 0)
    },
    update: function(t) {
     var e, i;
     t = this._computeEaseTime(t);
     var r = this._points;
     if (1 === t) e = r.length - 1, i = 1;
     else {
      var s = this._deltaT;
      i = (t - s * (e = 0 | t / s)) / s
     }
     var a, o, c = (function(t, e, i, n, r, s) {
      var a = s * s,
       o = a * s,
       c = (1 - r) / 2,
       h = c * (2 * a - o - s),
       u = c * (-o + a) + (2 * o - 3 * a + 1),
       l = c * (o - 2 * a + s) + (-2 * o + 3 * a),
       _ = c * (o - a),
       d = t.x * h + e.x * u + i.x * l + n.x * _,
       f = t.y * h + e.y * u + i.y * l + n.y * _;
      return cc.v2(d, f)
     })(n(r, e - 1), n(r, e - 0), n(r, e + 1), n(r, e + 2), this._tension, i);
     if (cc.macro.ENABLE_STACKABLE_ACTIONS && (a = this.target.x - this._previousPosition.x, o = this.target.y - this._previousPosition.y, 0 !== a || 0 !== o)) {
      var h = this._accumulatedDiff;
      a = h.x + a, o = h.y + o, h.x = a, h.y = o, c.x += a, c.y += o
     }
     this.updatePosition(c)
    },
    reverse: function() {
     var t = r(this._points);
     return cc.cardinalSplineTo(this._duration, t, this._tension)
    },
    updatePosition: function(t) {
     this.target.setPosition(t), this._previousPosition = t
    },
    getPoints: function() {
     return this._points
    },
    setPoints: function(t) {
     this._points = t
    }
   }), cc.cardinalSplineTo = function(t, e, i) {
    return new cc.CardinalSplineTo(t, e, i)
   }, cc.CardinalSplineBy = cc.Class({
    name: "cc.CardinalSplineBy",
    extends: cc.CardinalSplineTo,
    ctor: function(t, e, i) {
     this._startPosition = cc.v2(0, 0), void 0 !== i && this.initWithDuration(t, e, i)
    },
    startWithTarget: function(t) {
     cc.CardinalSplineTo.prototype.startWithTarget.call(this, t), this._startPosition.x = t.x, this._startPosition.y = t.y
    },
    reverse: function() {
     for (var t, e = this._points.slice(), i = e[0], n = 1; n < e.length; ++n) t = e[n], e[n] = t.sub(i), i = t;
     var s = r(e);
     i = s[s.length - 1], s.pop(), i.x = -i.x, i.y = -i.y, s.unshift(i);
     for (n = 1; n < s.length; ++n)(t = s[n]).x = -t.x, t.y = -t.y, t.x += i.x, t.y += i.y, s[n] = t, i = t;
     return cc.cardinalSplineBy(this._duration, s, this._tension)
    },
    updatePosition: function(t) {
     var e = this._startPosition,
      i = t.x + e.x,
      n = t.y + e.y;
     this._previousPosition.x = i, this._previousPosition.y = n, this.target.setPosition(i, n)
    },
    clone: function() {
     var t = new cc.CardinalSplineBy;
     return t.initWithDuration(this._duration, s(this._points), this._tension), t
    }
   }), cc.cardinalSplineBy = function(t, e, i) {
    return new cc.CardinalSplineBy(t, e, i)
   }, cc.CatmullRomTo = cc.Class({
    name: "cc.CatmullRomTo",
    extends: cc.CardinalSplineTo,
    ctor: function(t, e) {
     e && this.initWithDuration(t, e)
    },
    initWithDuration: function(t, e) {
     return cc.CardinalSplineTo.prototype.initWithDuration.call(this, t, e, .5)
    },
    clone: function() {
     var t = new cc.CatmullRomTo;
     return t.initWithDuration(this._duration, s(this._points)), t
    }
   }), cc.catmullRomTo = function(t, e) {
    return new cc.CatmullRomTo(t, e)
   }, cc.CatmullRomBy = cc.Class({
    name: "cc.CatmullRomBy",
    extends: cc.CardinalSplineBy,
    ctor: function(t, e) {
     e && this.initWithDuration(t, e)
    },
    initWithDuration: function(t, e) {
     return cc.CardinalSplineTo.prototype.initWithDuration.call(this, t, e, .5)
    },
    clone: function() {
     var t = new cc.CatmullRomBy;
     return t.initWithDuration(this._duration, s(this._points)), t
    }
   }), cc.catmullRomBy = function(t, e) {
    return new cc.CatmullRomBy(t, e)
   }
  }), {}],
  3: [(function(t, e, i) {
   cc.easeIn = function(t) {
    return {
     _rate: t,
     easing: function(t) {
      return Math.pow(t, this._rate)
     },
     reverse: function() {
      return cc.easeIn(1 / this._rate)
     }
    }
   }, cc.easeOut = function(t) {
    return {
     _rate: t,
     easing: function(t) {
      return Math.pow(t, 1 / this._rate)
     },
     reverse: function() {
      return cc.easeOut(1 / this._rate)
     }
    }
   }, cc.easeInOut = function(t) {
    return {
     _rate: t,
     easing: function(t) {
      return (t *= 2) < 1 ? .5 * Math.pow(t, this._rate) : 1 - .5 * Math.pow(2 - t, this._rate)
     },
     reverse: function() {
      return cc.easeInOut(this._rate)
     }
    }
   };
   var n = {
    easing: function(t) {
     return 0 === t ? 0 : Math.pow(2, 10 * (t - 1))
    },
    reverse: function() {
     return r
    }
   };
   cc.easeExponentialIn = function() {
    return n
   };
   var r = {
    easing: function(t) {
     return 1 === t ? 1 : 1 - Math.pow(2, -10 * t)
    },
    reverse: function() {
     return n
    }
   };
   cc.easeExponentialOut = function() {
    return r
   };
   var s = {
    easing: function(t) {
     return 1 !== t && 0 !== t ? (t *= 2) < 1 ? .5 * Math.pow(2, 10 * (t - 1)) : .5 * (2 - Math.pow(2, -10 * (t - 1))) : t
    },
    reverse: function() {
     return s
    }
   };
   cc.easeExponentialInOut = function() {
    return s
   };
   var a = {
    easing: function(t) {
     return 0 === t || 1 === t ? t : -1 * Math.cos(t * Math.PI / 2) + 1
    },
    reverse: function() {
     return o
    }
   };
   cc.easeSineIn = function() {
    return a
   };
   var o = {
    easing: function(t) {
     return 0 === t || 1 === t ? t : Math.sin(t * Math.PI / 2)
    },
    reverse: function() {
     return a
    }
   };
   cc.easeSineOut = function() {
    return o
   };
   var c = {
    easing: function(t) {
     return 0 === t || 1 === t ? t : -.5 * (Math.cos(Math.PI * t) - 1)
    },
    reverse: function() {
     return c
    }
   };
   cc.easeSineInOut = function() {
    return c
   };
   var h = {
    easing: function(t) {
     return 0 === t || 1 === t ? t : (t -= 1, -Math.pow(2, 10 * t) * Math.sin((t - .075) * Math.PI * 2 / .3))
    },
    reverse: function() {
     return u
    }
   };
   cc.easeElasticIn = function(t) {
    return t && .3 !== t ? {
     _period: t,
     easing: function(t) {
      return 0 === t || 1 === t ? t : (t -= 1, -Math.pow(2, 10 * t) * Math.sin((t - this._period / 4) * Math.PI * 2 / this._period))
     },
     reverse: function() {
      return cc.easeElasticOut(this._period)
     }
    } : h
   };
   var u = {
    easing: function(t) {
     return 0 === t || 1 === t ? t : Math.pow(2, -10 * t) * Math.sin((t - .075) * Math.PI * 2 / .3) + 1
    },
    reverse: function() {
     return h
    }
   };

   function l(t) {
    return t < 1 / 2.75 ? 7.5625 * t * t : t < 2 / 2.75 ? 7.5625 * (t -= 1.5 / 2.75) * t + .75 : t < 2.5 / 2.75 ? 7.5625 * (t -= 2.25 / 2.75) * t + .9375 : 7.5625 * (t -= 2.625 / 2.75) * t + .984375
   }
   cc.easeElasticOut = function(t) {
    return t && .3 !== t ? {
     _period: t,
     easing: function(t) {
      return 0 === t || 1 === t ? t : Math.pow(2, -10 * t) * Math.sin((t - this._period / 4) * Math.PI * 2 / this._period) + 1
     },
     reverse: function() {
      return cc.easeElasticIn(this._period)
     }
    } : u
   }, cc.easeElasticInOut = function(t) {
    return {
     _period: t = t || .3,
     easing: function(t) {
      var e = 0,
       i = this._period;
      if (0 === t || 1 === t) e = t;
      else {
       t *= 2, i || (i = this._period = .3 * 1.5);
       var n = i / 4;
       e = (t -= 1) < 0 ? -.5 * Math.pow(2, 10 * t) * Math.sin((t - n) * Math.PI * 2 / i) : Math.pow(2, -10 * t) * Math.sin((t - n) * Math.PI * 2 / i) * .5 + 1
      }
      return e
     },
     reverse: function() {
      return cc.easeElasticInOut(this._period)
     }
    }
   };
   var _ = {
    easing: function(t) {
     return 1 - l(1 - t)
    },
    reverse: function() {
     return d
    }
   };
   cc.easeBounceIn = function() {
    return _
   };
   var d = {
    easing: function(t) {
     return l(t)
    },
    reverse: function() {
     return _
    }
   };
   cc.easeBounceOut = function() {
    return d
   };
   var f = {
    easing: function(t) {
     return t < .5 ? .5 * (1 - l(1 - (t *= 2))) : .5 * l(2 * t - 1) + .5
    },
    reverse: function() {
     return f
    }
   };
   cc.easeBounceInOut = function() {
    return f
   };
   var p = {
    easing: function(t) {
     return 0 === t || 1 === t ? t : t * t * (2.70158 * t - 1.70158)
    },
    reverse: function() {
     return m
    }
   };
   cc.easeBackIn = function() {
    return p
   };
   var m = {
    easing: function(t) {
     return (t -= 1) * t * (2.70158 * t + 1.70158) + 1
    },
    reverse: function() {
     return p
    }
   };
   cc.easeBackOut = function() {
    return m
   };
   var v = {
    easing: function(t) {
     return (t *= 2) < 1 ? t * t * (3.5949095 * t - 2.5949095) / 2 : (t -= 2) * t * (3.5949095 * t + 2.5949095) / 2 + 1
    },
    reverse: function() {
     return v
    }
   };
   cc.easeBackInOut = function() {
    return v
   }, cc.easeBezierAction = function(t, e, i, n) {
    return {
     easing: function(r) {
      return Math.pow(1 - r, 3) * t + 3 * r * Math.pow(1 - r, 2) * e + 3 * Math.pow(r, 2) * (1 - r) * i + Math.pow(r, 3) * n
     },
     reverse: function() {
      return cc.easeBezierAction(n, i, e, t)
     }
    }
   };
   var g = {
    easing: function(t) {
     return Math.pow(t, 2)
    },
    reverse: function() {
     return g
    }
   };
   cc.easeQuadraticActionIn = function() {
    return g
   };
   var y = {
    easing: function(t) {
     return -t * (t - 2)
    },
    reverse: function() {
     return y
    }
   };
   cc.easeQuadraticActionOut = function() {
    return y
   };
   var T = {
    easing: function(t) {
     return (t *= 2) < 1 ? t * t * .5 : -.5 * (--t * (t - 2) - 1)
    },
    reverse: function() {
     return T
    }
   };
   cc.easeQuadraticActionInOut = function() {
    return T
   };
   var E = {
    easing: function(t) {
     return t * t * t * t
    },
    reverse: function() {
     return E
    }
   };
   cc.easeQuarticActionIn = function() {
    return E
   };
   var x = {
    easing: function(t) {
     return -((t -= 1) * t * t * t - 1)
    },
    reverse: function() {
     return x
    }
   };
   cc.easeQuarticActionOut = function() {
    return x
   };
   var C = {
    easing: function(t) {
     return (t *= 2) < 1 ? .5 * t * t * t * t : -.5 * ((t -= 2) * t * t * t - 2)
    },
    reverse: function() {
     return C
    }
   };
   cc.easeQuarticActionInOut = function() {
    return C
   };
   var A = {
    easing: function(t) {
     return t * t * t * t * t
    },
    reverse: function() {
     return A
    }
   };
   cc.easeQuinticActionIn = function() {
    return A
   };
   var b = {
    easing: function(t) {
     return (t -= 1) * t * t * t * t + 1
    },
    reverse: function() {
     return b
    }
   };
   cc.easeQuinticActionOut = function() {
    return b
   };
   var S = {
    easing: function(t) {
     return (t *= 2) < 1 ? .5 * t * t * t * t * t : .5 * ((t -= 2) * t * t * t * t + 2)
    },
    reverse: function() {
     return S
    }
   };
   cc.easeQuinticActionInOut = function() {
    return S
   };
   var w = {
    easing: function(t) {
     return -1 * (Math.sqrt(1 - t * t) - 1)
    },
    reverse: function() {
     return w
    }
   };
   cc.easeCircleActionIn = function() {
    return w
   };
   var D = {
    easing: function(t) {
     return t -= 1, Math.sqrt(1 - t * t)
    },
    reverse: function() {
     return D
    }
   };
   cc.easeCircleActionOut = function() {
    return D
   };
   var R = {
    easing: function(t) {
     return (t *= 2) < 1 ? -.5 * (Math.sqrt(1 - t * t) - 1) : (t -= 2, .5 * (Math.sqrt(1 - t * t) + 1))
    },
    reverse: function() {
     return R
    }
   };
   cc.easeCircleActionInOut = function() {
    return R
   };
   var M = {
    easing: function(t) {
     return t * t * t
    },
    reverse: function() {
     return M
    }
   };
   cc.easeCubicActionIn = function() {
    return M
   };
   var I = {
    easing: function(t) {
     return (t -= 1) * t * t + 1
    },
    reverse: function() {
     return I
    }
   };
   cc.easeCubicActionOut = function() {
    return I
   };
   var L = {
    easing: function(t) {
     return (t *= 2) < 1 ? .5 * t * t * t : .5 * ((t -= 2) * t * t + 2)
    },
    reverse: function() {
     return L
    }
   };
   cc.easeCubicActionInOut = function() {
    return L
   }
  }), {}],
  4: [(function(t, e, i) {
   cc.ActionInstant = cc.Class({
    name: "cc.ActionInstant",
    extends: cc.FiniteTimeAction,
    isDone: function() {
     return !0
    },
    step: function(t) {
     this.update(1)
    },
    update: function(t) {},
    reverse: function() {
     return this.clone()
    },
    clone: function() {
     return new cc.ActionInstant
    }
   }), cc.Show = cc.Class({
    name: "cc.Show",
    extends: cc.ActionInstant,
    update: function(t) {
     for (var e = this.target.getComponentsInChildren(cc.RenderComponent), i = 0; i < e.length; ++i) {
      e[i].enabled = !0
     }
    },
    reverse: function() {
     return new cc.Hide
    },
    clone: function() {
     return new cc.Show
    }
   }), cc.show = function() {
    return new cc.Show
   }, cc.Hide = cc.Class({
    name: "cc.Hide",
    extends: cc.ActionInstant,
    update: function(t) {
     for (var e = this.target.getComponentsInChildren(cc.RenderComponent), i = 0; i < e.length; ++i) {
      e[i].enabled = !1
     }
    },
    reverse: function() {
     return new cc.Show
    },
    clone: function() {
     return new cc.Hide
    }
   }), cc.hide = function() {
    return new cc.Hide
   }, cc.ToggleVisibility = cc.Class({
    name: "cc.ToggleVisibility",
    extends: cc.ActionInstant,
    update: function(t) {
     for (var e = this.target.getComponentsInChildren(cc.RenderComponent), i = 0; i < e.length; ++i) {
      var n = e[i];
      n.enabled = !n.enabled
     }
    },
    reverse: function() {
     return new cc.ToggleVisibility
    },
    clone: function() {
     return new cc.ToggleVisibility
    }
   }), cc.toggleVisibility = function() {
    return new cc.ToggleVisibility
   }, cc.RemoveSelf = cc.Class({
    name: "cc.RemoveSelf",
    extends: cc.ActionInstant,
    ctor: function(t) {
     this._isNeedCleanUp = !0, void 0 !== t && this.init(t)
    },
    update: function(t) {
     this.target.removeFromParent(this._isNeedCleanUp)
    },
    init: function(t) {
     return this._isNeedCleanUp = t, !0
    },
    reverse: function() {
     return new cc.RemoveSelf(this._isNeedCleanUp)
    },
    clone: function() {
     return new cc.RemoveSelf(this._isNeedCleanUp)
    }
   }), cc.removeSelf = function(t) {
    return new cc.RemoveSelf(t)
   }, cc.FlipX = cc.Class({
    name: "cc.FlipX",
    extends: cc.ActionInstant,
    ctor: function(t) {
     this._flippedX = !1, void 0 !== t && this.initWithFlipX(t)
    },
    initWithFlipX: function(t) {
     return this._flippedX = t, !0
    },
    update: function(t) {
     this.target.scaleX = Math.abs(this.target.scaleX) * (this._flippedX ? -1 : 1)
    },
    reverse: function() {
     return new cc.FlipX(!this._flippedX)
    },
    clone: function() {
     var t = new cc.FlipX;
     return t.initWithFlipX(this._flippedX), t
    }
   }), cc.flipX = function(t) {
    return new cc.FlipX(t)
   }, cc.FlipY = cc.Class({
    name: "cc.FlipY",
    extends: cc.ActionInstant,
    ctor: function(t) {
     this._flippedY = !1, void 0 !== t && this.initWithFlipY(t)
    },
    initWithFlipY: function(t) {
     return this._flippedY = t, !0
    },
    update: function(t) {
     this.target.scaleY = Math.abs(this.target.scaleY) * (this._flippedY ? -1 : 1)
    },
    reverse: function() {
     return new cc.FlipY(!this._flippedY)
    },
    clone: function() {
     var t = new cc.FlipY;
     return t.initWithFlipY(this._flippedY), t
    }
   }), cc.flipY = function(t) {
    return new cc.FlipY(t)
   }, cc.Place = cc.Class({
    name: "cc.Place",
    extends: cc.ActionInstant,
    ctor: function(t, e) {
     this._x = 0, this._y = 0, void 0 !== t && (void 0 !== t.x && (e = t.y, t = t.x), this.initWithPosition(t, e))
    },
    initWithPosition: function(t, e) {
     return this._x = t, this._y = e, !0
    },
    update: function(t) {
     this.target.setPosition(this._x, this._y)
    },
    clone: function() {
     var t = new cc.Place;
     return t.initWithPosition(this._x, this._y), t
    }
   }), cc.place = function(t, e) {
    return new cc.Place(t, e)
   }, cc.CallFunc = cc.Class({
    name: "cc.CallFunc",
    extends: cc.ActionInstant,
    ctor: function(t, e, i) {
     this._selectorTarget = null, this._function = null, this._data = null, this.initWithFunction(t, e, i)
    },
    initWithFunction: function(t, e, i) {
     return t && (this._function = t), e && (this._selectorTarget = e), void 0 !== i && (this._data = i), !0
    },
    execute: function() {
     this._function && this._function.call(this._selectorTarget, this.target, this._data)
    },
    update: function(t) {
     this.execute()
    },
    getTargetCallback: function() {
     return this._selectorTarget
    },
    setTargetCallback: function(t) {
     t !== this._selectorTarget && (this._selectorTarget && (this._selectorTarget = null), this._selectorTarget = t)
    },
    clone: function() {
     var t = new cc.CallFunc;
     return t.initWithFunction(this._function, this._selectorTarget, this._data), t
    }
   }), cc.callFunc = function(t, e, i) {
    return new cc.CallFunc(t, e, i)
   }
  }), {}],
  5: [(function(t, e, i) {
   function n(t, e, i, n, r) {
    return Math.pow(1 - r, 3) * t + 3 * r * Math.pow(1 - r, 2) * e + 3 * Math.pow(r, 2) * (1 - r) * i + Math.pow(r, 3) * n
   }
   cc.ActionInterval = cc.Class({
    name: "cc.ActionInterval",
    extends: cc.FiniteTimeAction,
    ctor: function(t) {
     this.MAX_VALUE = 2, this._elapsed = 0, this._firstTick = !1, this._easeList = null, this._speed = 1, this._timesForRepeat = 1, this._repeatForever = !1, this._repeatMethod = !1, this._speedMethod = !1, void 0 !== t && cc.ActionInterval.prototype.initWithDuration.call(this, t)
    },
    getElapsed: function() {
     return this._elapsed
    },
    initWithDuration: function(t) {
     return this._duration = 0 === t ? cc.macro.FLT_EPSILON : t, this._elapsed = 0, this._firstTick = !0, !0
    },
    isDone: function() {
     return this._elapsed >= this._duration
    },
    _cloneDecoration: function(t) {
     t._repeatForever = this._repeatForever, t._speed = this._speed, t._timesForRepeat = this._timesForRepeat, t._easeList = this._easeList, t._speedMethod = this._speedMethod, t._repeatMethod = this._repeatMethod
    },
    _reverseEaseList: function(t) {
     if (this._easeList) {
      t._easeList = [];
      for (var e = 0; e < this._easeList.length; e++) t._easeList.push(this._easeList[e].reverse())
     }
    },
    clone: function() {
     var t = new cc.ActionInterval(this._duration);
     return this._cloneDecoration(t), t
    },
    easing: function(t) {
     this._easeList ? this._easeList.length = 0 : this._easeList = [];
     for (var e = 0; e < arguments.length; e++) this._easeList.push(arguments[e]);
     return this
    },
    _computeEaseTime: function(t) {
     var e = this._easeList;
     if (!e || 0 === e.length) return t;
     for (var i = 0, n = e.length; i < n; i++) t = e[i].easing(t);
     return t
    },
    step: function(t) {
     this._firstTick ? (this._firstTick = !1, this._elapsed = 0) : this._elapsed += t;
     var e = this._elapsed / (this._duration > 1.192092896e-7 ? this._duration : 1.192092896e-7);
     e = 1 > e ? e : 1, this.update(e > 0 ? e : 0), this._repeatMethod && this._timesForRepeat > 1 && this.isDone() && (this._repeatForever || this._timesForRepeat--, this.startWithTarget(this.target), this.step(this._elapsed - this._duration))
    },
    startWithTarget: function(t) {
     cc.Action.prototype.startWithTarget.call(this, t), this._elapsed = 0, this._firstTick = !0
    },
    reverse: function() {
     return cc.logID(1010), null
    },
    setAmplitudeRate: function(t) {
     cc.logID(1011)
    },
    getAmplitudeRate: function() {
     return cc.logID(1012), 0
    },
    speed: function(t) {
     return t <= 0 ? (cc.logID(1013), this) : (this._speedMethod = !0, this._speed *= t, this)
    },
    getSpeed: function() {
     return this._speed
    },
    setSpeed: function(t) {
     return this._speed = t, this
    },
    repeat: function(t) {
     return t = Math.round(t), isNaN(t) || t < 1 ? (cc.logID(1014), this) : (this._repeatMethod = !0, this._timesForRepeat *= t, this)
    },
    repeatForever: function() {
     return this._repeatMethod = !0, this._timesForRepeat = this.MAX_VALUE, this._repeatForever = !0, this
    }
   }), cc.actionInterval = function(t) {
    return new cc.ActionInterval(t)
   }, cc.Sequence = cc.Class({
    name: "cc.Sequence",
    extends: cc.ActionInterval,
    ctor: function(t) {
     this._actions = [], this._split = null, this._last = 0, this._reversed = !1;
     var e = t instanceof Array ? t : arguments;
     if (1 !== e.length) {
      var i = e.length - 1;
      if (i >= 0 && null == e[i] && cc.logID(1015), i >= 0) {
       for (var n, r = e[0], s = 1; s < i; s++) e[s] && (n = r, r = cc.Sequence._actionOneTwo(n, e[s]));
       this.initWithTwoActions(r, e[i])
      }
     } else cc.errorID(1019)
    },
    initWithTwoActions: function(t, e) {
     if (!t || !e) return cc.errorID(1025), !1;
     var i = t._duration,
      n = e._duration,
      r = (i *= t._repeatMethod ? t._timesForRepeat : 1) + (n *= e._repeatMethod ? e._timesForRepeat : 1);
     return this.initWithDuration(r), this._actions[0] = t, this._actions[1] = e, !0
    },
    clone: function() {
     var t = new cc.Sequence;
     return this._cloneDecoration(t), t.initWithTwoActions(this._actions[0].clone(), this._actions[1].clone()), t
    },
    startWithTarget: function(t) {
     cc.ActionInterval.prototype.startWithTarget.call(this, t), this._split = this._actions[0]._duration / this._duration, this._split *= this._actions[0]._repeatMethod ? this._actions[0]._timesForRepeat : 1, this._last = -1
    },
    stop: function() {
     -1 !== this._last && this._actions[this._last].stop(), cc.Action.prototype.stop.call(this)
    },
    update: function(t) {
     var e, i, n = 0,
      r = this._split,
      s = this._actions,
      a = this._last;
     (t = this._computeEaseTime(t)) < r ? (e = 0 !== r ? t / r : 1, 0 === n && 1 === a && this._reversed && (s[1].update(0), s[1].stop())) : (n = 1, e = 1 === r ? 1 : (t - r) / (1 - r), -1 === a && (s[0].startWithTarget(this.target), s[0].update(1), s[0].stop()), 0 === a && (s[0].update(1), s[0].stop())), i = s[n], a === n && i.isDone() || (a !== n && i.startWithTarget(this.target), e *= i._timesForRepeat, i.update(e > 1 ? e % 1 : e), this._last = n)
    },
    reverse: function() {
     var t = cc.Sequence._actionOneTwo(this._actions[1].reverse(), this._actions[0].reverse());
     return this._cloneDecoration(t), this._reverseEaseList(t), t._reversed = !0, t
    }
   }), cc.sequence = function(t) {
    var e = t instanceof Array ? t : arguments;
    if (1 === e.length) return cc.errorID(1019), null;
    var i = e.length - 1;
    i >= 0 && null == e[i] && cc.logID(1015);
    var n = null;
    if (i >= 0) {
     n = e[0];
     for (var r = 1; r <= i; r++) e[r] && (n = cc.Sequence._actionOneTwo(n, e[r]))
    }
    return n
   }, cc.Sequence._actionOneTwo = function(t, e) {
    var i = new cc.Sequence;
    return i.initWithTwoActions(t, e), i
   }, cc.Repeat = cc.Class({
    name: "cc.Repeat",
    extends: cc.ActionInterval,
    ctor: function(t, e) {
     this._times = 0, this._total = 0, this._nextDt = 0, this._actionInstant = !1, this._innerAction = null, void 0 !== e && this.initWithAction(t, e)
    },
    initWithAction: function(t, e) {
     var i = t._duration * e;
     return !!this.initWithDuration(i) && (this._times = e, this._innerAction = t, t instanceof cc.ActionInstant && (this._actionInstant = !0, this._times -= 1), this._total = 0, !0)
    },
    clone: function() {
     var t = new cc.Repeat;
     return this._cloneDecoration(t), t.initWithAction(this._innerAction.clone(), this._times), t
    },
    startWithTarget: function(t) {
     this._total = 0, this._nextDt = this._innerAction._duration / this._duration, cc.ActionInterval.prototype.startWithTarget.call(this, t), this._innerAction.startWithTarget(t)
    },
    stop: function() {
     this._innerAction.stop(), cc.Action.prototype.stop.call(this)
    },
    update: function(t) {
     t = this._computeEaseTime(t);
     var e = this._innerAction,
      i = this._duration,
      n = this._times,
      r = this._nextDt;
     if (t >= r) {
      for (; t > r && this._total < n;) e.update(1), this._total++, e.stop(), e.startWithTarget(this.target), r += e._duration / i, this._nextDt = r > 1 ? 1 : r;
      t >= 1 && this._total < n && (e.update(1), this._total++), this._actionInstant || (this._total === n ? e.stop() : e.update(t - (r - e._duration / i)))
     } else e.update(t * n % 1)
    },
    isDone: function() {
     return this._total === this._times
    },
    reverse: function() {
     var t = new cc.Repeat(this._innerAction.reverse(), this._times);
     return this._cloneDecoration(t), this._reverseEaseList(t), t
    },
    setInnerAction: function(t) {
     this._innerAction !== t && (this._innerAction = t)
    },
    getInnerAction: function() {
     return this._innerAction
    }
   }), cc.repeat = function(t, e) {
    return new cc.Repeat(t, e)
   }, cc.RepeatForever = cc.Class({
    name: "cc.RepeatForever",
    extends: cc.ActionInterval,
    ctor: function(t) {
     this._innerAction = null, t && this.initWithAction(t)
    },
    initWithAction: function(t) {
     return t ? (this._innerAction = t, !0) : (cc.errorID(1026), !1)
    },
    clone: function() {
     var t = new cc.RepeatForever;
     return this._cloneDecoration(t), t.initWithAction(this._innerAction.clone()), t
    },
    startWithTarget: function(t) {
     cc.ActionInterval.prototype.startWithTarget.call(this, t), this._innerAction.startWithTarget(t)
    },
    step: function(t) {
     var e = this._innerAction;
     e.step(t), e.isDone() && (e.startWithTarget(this.target), e.step(e.getElapsed() - e._duration))
    },
    isDone: function() {
     return !1
    },
    reverse: function() {
     var t = new cc.RepeatForever(this._innerAction.reverse());
     return this._cloneDecoration(t), this._reverseEaseList(t), t
    },
    setInnerAction: function(t) {
     this._innerAction !== t && (this._innerAction = t)
    },
    getInnerAction: function() {
     return this._innerAction
    }
   }), cc.repeatForever = function(t) {
    return new cc.RepeatForever(t)
   }, cc.Spawn = cc.Class({
    name: "cc.Spawn",
    extends: cc.ActionInterval,
    ctor: function(t) {
     this._one = null, this._two = null;
     var e = t instanceof Array ? t : arguments;
     if (1 !== e.length) {
      var i = e.length - 1;
      if (i >= 0 && null == e[i] && cc.logID(1015), i >= 0) {
       for (var n, r = e[0], s = 1; s < i; s++) e[s] && (n = r, r = cc.Spawn._actionOneTwo(n, e[s]));
       this.initWithTwoActions(r, e[i])
      }
     } else cc.errorID(1020)
    },
    initWithTwoActions: function(t, e) {
     if (!t || !e) return cc.errorID(1027), !1;
     var i = !1,
      n = t._duration,
      r = e._duration;
     return this.initWithDuration(Math.max(n, r)) && (this._one = t, this._two = e, n > r ? this._two = cc.Sequence._actionOneTwo(e, cc.delayTime(n - r)) : n < r && (this._one = cc.Sequence._actionOneTwo(t, cc.delayTime(r - n))), i = !0), i
    },
    clone: function() {
     var t = new cc.Spawn;
     return this._cloneDecoration(t), t.initWithTwoActions(this._one.clone(), this._two.clone()), t
    },
    startWithTarget: function(t) {
     cc.ActionInterval.prototype.startWithTarget.call(this, t), this._one.startWithTarget(t), this._two.startWithTarget(t)
    },
    stop: function() {
     this._one.stop(), this._two.stop(), cc.Action.prototype.stop.call(this)
    },
    update: function(t) {
     t = this._computeEaseTime(t), this._one && this._one.update(t), this._two && this._two.update(t)
    },
    reverse: function() {
     var t = cc.Spawn._actionOneTwo(this._one.reverse(), this._two.reverse());
     return this._cloneDecoration(t), this._reverseEaseList(t), t
    }
   }), cc.spawn = function(t) {
    var e = t instanceof Array ? t : arguments;
    if (1 === e.length) return cc.errorID(1020), null;
    e.length > 0 && null == e[e.length - 1] && cc.logID(1015);
    for (var i = e[0], n = 1; n < e.length; n++) null != e[n] && (i = cc.Spawn._actionOneTwo(i, e[n]));
    return i
   }, cc.Spawn._actionOneTwo = function(t, e) {
    var i = new cc.Spawn;
    return i.initWithTwoActions(t, e), i
   }, cc.RotateTo = cc.Class({
    name: "cc.RotateTo",
    extends: cc.ActionInterval,
    ctor: function(t, e, i) {
     this._dstAngleX = 0, this._startAngleX = 0, this._diffAngleX = 0, this._dstAngleY = 0, this._startAngleY = 0, this._diffAngleY = 0, void 0 !== e && this.initWithDuration(t, e, i)
    },
    initWithDuration: function(t, e, i) {
     return !!cc.ActionInterval.prototype.initWithDuration.call(this, t) && (this._dstAngleX = e || 0, this._dstAngleY = void 0 !== i ? i : this._dstAngleX, !0)
    },
    clone: function() {
     var t = new cc.RotateTo;
     return this._cloneDecoration(t), t.initWithDuration(this._duration, this._dstAngleX, this._dstAngleY), t
    },
    startWithTarget: function(t) {
     cc.ActionInterval.prototype.startWithTarget.call(this, t);
     var e = t.rotationX % 360,
      i = this._dstAngleX - e;
     i > 180 && (i -= 360), i < -180 && (i += 360), this._startAngleX = e, this._diffAngleX = i, this._startAngleY = t.rotationY % 360;
     var n = this._dstAngleY - this._startAngleY;
     n > 180 && (n -= 360), n < -180 && (n += 360), this._diffAngleY = n
    },
    reverse: function() {
     cc.logID(1016)
    },
    update: function(t) {
     t = this._computeEaseTime(t), this.target && (this.target.rotationX = this._startAngleX + this._diffAngleX * t, this.target.rotationY = this._startAngleY + this._diffAngleY * t)
    }
   }), cc.rotateTo = function(t, e, i) {
    return new cc.RotateTo(t, e, i)
   }, cc.RotateBy = cc.Class({
    name: "cc.RotateBy",
    extends: cc.ActionInterval,
    ctor: function(t, e, i) {
     this._angleX = 0, this._startAngleX = 0, this._angleY = 0, this._startAngleY = 0, void 0 !== e && this.initWithDuration(t, e, i)
    },
    initWithDuration: function(t, e, i) {
     return !!cc.ActionInterval.prototype.initWithDuration.call(this, t) && (this._angleX = e || 0, this._angleY = void 0 !== i ? i : this._angleX, !0)
    },
    clone: function() {
     var t = new cc.RotateBy;
     return this._cloneDecoration(t), t.initWithDuration(this._duration, this._angleX, this._angleY), t
    },
    startWithTarget: function(t) {
     cc.ActionInterval.prototype.startWithTarget.call(this, t), this._startAngleX = t.rotationX, this._startAngleY = t.rotationY
    },
    update: function(t) {
     t = this._computeEaseTime(t), this.target && (this.target.rotationX = this._startAngleX + this._angleX * t, this.target.rotationY = this._startAngleY + this._angleY * t)
    },
    reverse: function() {
     var t = new cc.RotateBy(this._duration, -this._angleX, -this._angleY);
     return this._cloneDecoration(t), this._reverseEaseList(t), t
    }
   }), cc.rotateBy = function(t, e, i) {
    return new cc.RotateBy(t, e, i)
   }, cc.MoveBy = cc.Class({
    name: "cc.MoveBy",
    extends: cc.ActionInterval,
    ctor: function(t, e, i) {
     this._positionDelta = cc.v2(0, 0), this._startPosition = cc.v2(0, 0), this._previousPosition = cc.v2(0, 0), void 0 !== e && cc.MoveBy.prototype.initWithDuration.call(this, t, e, i)
    },
    initWithDuration: function(t, e, i) {
     return !!cc.ActionInterval.prototype.initWithDuration.call(this, t) && (void 0 !== e.x && (i = e.y, e = e.x), this._positionDelta.x = e, this._positionDelta.y = i, !0)
    },
    clone: function() {
     var t = new cc.MoveBy;
     return this._cloneDecoration(t), t.initWithDuration(this._duration, this._positionDelta), t
    },
    startWithTarget: function(t) {
     cc.ActionInterval.prototype.startWithTarget.call(this, t);
     var e = t.x,
      i = t.y;
     this._previousPosition.x = e, this._previousPosition.y = i, this._startPosition.x = e, this._startPosition.y = i
    },
    update: function(t) {
     if (t = this._computeEaseTime(t), this.target) {
      var e = this._positionDelta.x * t,
       i = this._positionDelta.y * t,
       n = this._startPosition;
      if (cc.macro.ENABLE_STACKABLE_ACTIONS) {
       var r = this.target.x,
        s = this.target.y,
        a = this._previousPosition;
       n.x = n.x + r - a.x, n.y = n.y + s - a.y, e += n.x, i += n.y, a.x = e, a.y = i, this.target.setPosition(e, i)
      } else this.target.setPosition(n.x + e, n.y + i)
     }
    },
    reverse: function() {
     var t = new cc.MoveBy(this._duration, cc.v2(-this._positionDelta.x, -this._positionDelta.y));
     return this._cloneDecoration(t), this._reverseEaseList(t), t
    }
   }), cc.moveBy = function(t, e, i) {
    return new cc.MoveBy(t, e, i)
   }, cc.MoveTo = cc.Class({
    name: "cc.MoveTo",
    extends: cc.MoveBy,
    ctor: function(t, e, i) {
     this._endPosition = cc.v2(0, 0), void 0 !== e && this.initWithDuration(t, e, i)
    },
    initWithDuration: function(t, e, i) {
     return !!cc.MoveBy.prototype.initWithDuration.call(this, t, e, i) && (void 0 !== e.x && (i = e.y, e = e.x), this._endPosition.x = e, this._endPosition.y = i, !0)
    },
    clone: function() {
     var t = new cc.MoveTo;
     return this._cloneDecoration(t), t.initWithDuration(this._duration, this._endPosition), t
    },
    startWithTarget: function(t) {
     cc.MoveBy.prototype.startWithTarget.call(this, t), this._positionDelta.x = this._endPosition.x - t.x, this._positionDelta.y = this._endPosition.y - t.y
    }
   }), cc.moveTo = function(t, e, i) {
    return new cc.MoveTo(t, e, i)
   }, cc.SkewTo = cc.Class({
    name: "cc.SkewTo",
    extends: cc.ActionInterval,
    ctor: function(t, e, i) {
     this._skewX = 0, this._skewY = 0, this._startSkewX = 0, this._startSkewY = 0, this._endSkewX = 0, this._endSkewY = 0, this._deltaX = 0, this._deltaY = 0, void 0 !== i && cc.SkewTo.prototype.initWithDuration.call(this, t, e, i)
    },
    initWithDuration: function(t, e, i) {
     var n = !1;
     return cc.ActionInterval.prototype.initWithDuration.call(this, t) && (this._endSkewX = e, this._endSkewY = i, n = !0), n
    },
    clone: function() {
     var t = new cc.SkewTo;
     return this._cloneDecoration(t), t.initWithDuration(this._duration, this._endSkewX, this._endSkewY), t
    },
    startWithTarget: function(t) {
     cc.ActionInterval.prototype.startWithTarget.call(this, t), this._startSkewX = t.skewX % 180, this._deltaX = this._endSkewX - this._startSkewX, this._deltaX > 180 && (this._deltaX -= 360), this._deltaX < -180 && (this._deltaX += 360), this._startSkewY = t.skewY % 360, this._deltaY = this._endSkewY - this._startSkewY, this._deltaY > 180 && (this._deltaY -= 360), this._deltaY < -180 && (this._deltaY += 360)
    },
    update: function(t) {
     t = this._computeEaseTime(t), this.target.skewX = this._startSkewX + this._deltaX * t, this.target.skewY = this._startSkewY + this._deltaY * t
    }
   }), cc.skewTo = function(t, e, i) {
    return new cc.SkewTo(t, e, i)
   }, cc.SkewBy = cc.Class({
    name: "cc.SkewBy",
    extends: cc.SkewTo,
    ctor: function(t, e, i) {
     void 0 !== i && this.initWithDuration(t, e, i)
    },
    initWithDuration: function(t, e, i) {
     var n = !1;
     return cc.SkewTo.prototype.initWithDuration.call(this, t, e, i) && (this._skewX = e, this._skewY = i, n = !0), n
    },
    clone: function() {
     var t = new cc.SkewBy;
     return this._cloneDecoration(t), t.initWithDuration(this._duration, this._skewX, this._skewY), t
    },
    startWithTarget: function(t) {
     cc.SkewTo.prototype.startWithTarget.call(this, t), this._deltaX = this._skewX, this._deltaY = this._skewY, this._endSkewX = this._startSkewX + this._deltaX, this._endSkewY = this._startSkewY + this._deltaY
    },
    reverse: function() {
     var t = new cc.SkewBy(this._duration, -this._skewX, -this._skewY);
     return this._cloneDecoration(t), this._reverseEaseList(t), t
    }
   }), cc.skewBy = function(t, e, i) {
    return new cc.SkewBy(t, e, i)
   }, cc.JumpBy = cc.Class({
    name: "cc.JumpBy",
    extends: cc.ActionInterval,
    ctor: function(t, e, i, n, r) {
     this._startPosition = cc.v2(0, 0), this._previousPosition = cc.v2(0, 0), this._delta = cc.v2(0, 0), this._height = 0, this._jumps = 0, void 0 !== n && cc.JumpBy.prototype.initWithDuration.call(this, t, e, i, n, r)
    },
    initWithDuration: function(t, e, i, n, r) {
     return !!cc.ActionInterval.prototype.initWithDuration.call(this, t) && (void 0 === r && (r = n, n = i, i = e.y, e = e.x), this._delta.x = e, this._delta.y = i, this._height = n, this._jumps = r, !0)
    },
    clone: function() {
     var t = new cc.JumpBy;
     return this._cloneDecoration(t), t.initWithDuration(this._duration, this._delta, this._height, this._jumps), t
    },
    startWithTarget: function(t) {
     cc.ActionInterval.prototype.startWithTarget.call(this, t);
     var e = t.x,
      i = t.y;
     this._previousPosition.x = e, this._previousPosition.y = i, this._startPosition.x = e, this._startPosition.y = i
    },
    update: function(t) {
     if (t = this._computeEaseTime(t), this.target) {
      var e = t * this._jumps % 1,
       i = 4 * this._height * e * (1 - e);
      i += this._delta.y * t;
      var n = this._delta.x * t,
       r = this._startPosition;
      if (cc.macro.ENABLE_STACKABLE_ACTIONS) {
       var s = this.target.x,
        a = this.target.y,
        o = this._previousPosition;
       r.x = r.x + s - o.x, r.y = r.y + a - o.y, n += r.x, i += r.y, o.x = n, o.y = i, this.target.setPosition(n, i)
      } else this.target.setPosition(r.x + n, r.y + i)
     }
    },
    reverse: function() {
     var t = new cc.JumpBy(this._duration, cc.v2(-this._delta.x, -this._delta.y), this._height, this._jumps);
     return this._cloneDecoration(t), this._reverseEaseList(t), t
    }
   }), cc.jumpBy = function(t, e, i, n, r) {
    return new cc.JumpBy(t, e, i, n, r)
   }, cc.JumpTo = cc.Class({
    name: "cc.JumpTo",
    extends: cc.JumpBy,
    ctor: function(t, e, i, n, r) {
     this._endPosition = cc.v2(0, 0), void 0 !== n && this.initWithDuration(t, e, i, n, r)
    },
    initWithDuration: function(t, e, i, n, r) {
     return !!cc.JumpBy.prototype.initWithDuration.call(this, t, e, i, n, r) && (void 0 === r && (i = e.y, e = e.x), this._endPosition.x = e, this._endPosition.y = i, !0)
    },
    startWithTarget: function(t) {
     cc.JumpBy.prototype.startWithTarget.call(this, t), this._delta.x = this._endPosition.x - this._startPosition.x, this._delta.y = this._endPosition.y - this._startPosition.y
    },
    clone: function() {
     var t = new cc.JumpTo;
     return this._cloneDecoration(t), t.initWithDuration(this._duration, this._endPosition, this._height, this._jumps), t
    }
   }), cc.jumpTo = function(t, e, i, n, r) {
    return new cc.JumpTo(t, e, i, n, r)
   }, cc.BezierBy = cc.Class({
    name: "cc.BezierBy",
    extends: cc.ActionInterval,
    ctor: function(t, e) {
     this._config = [], this._startPosition = cc.v2(0, 0), this._previousPosition = cc.v2(0, 0), e && cc.BezierBy.prototype.initWithDuration.call(this, t, e)
    },
    initWithDuration: function(t, e) {
     return !!cc.ActionInterval.prototype.initWithDuration.call(this, t) && (this._config = e, !0)
    },
    clone: function() {
     var t = new cc.BezierBy;
     this._cloneDecoration(t);
     for (var e = [], i = 0; i < this._config.length; i++) {
      var n = this._config[i];
      e.push(cc.v2(n.x, n.y))
     }
     return t.initWithDuration(this._duration, e), t
    },
    startWithTarget: function(t) {
     cc.ActionInterval.prototype.startWithTarget.call(this, t);
     var e = t.x,
      i = t.y;
     this._previousPosition.x = e, this._previousPosition.y = i, this._startPosition.x = e, this._startPosition.y = i
    },
    update: function(t) {
     if (t = this._computeEaseTime(t), this.target) {
      var e = this._config,
       i = e[0].x,
       r = e[1].x,
       s = e[2].x,
       a = e[0].y,
       o = e[1].y,
       c = e[2].y,
       h = n(0, i, r, s, t),
       u = n(0, a, o, c, t),
       l = this._startPosition;
      if (cc.macro.ENABLE_STACKABLE_ACTIONS) {
       var _ = this.target.x,
        d = this.target.y,
        f = this._previousPosition;
       l.x = l.x + _ - f.x, l.y = l.y + d - f.y, h += l.x, u += l.y, f.x = h, f.y = u, this.target.setPosition(h, u)
      } else this.target.setPosition(l.x + h, l.y + u)
     }
    },
    reverse: function() {
     var t = this._config,
      e = t[0].x,
      i = t[0].y,
      n = t[1].x,
      r = t[1].y,
      s = t[2].x,
      a = t[2].y,
      o = [cc.v2(n - s, r - a), cc.v2(e - s, i - a), cc.v2(-s, -a)],
      c = new cc.BezierBy(this._duration, o);
     return this._cloneDecoration(c), this._reverseEaseList(c), c
    }
   }), cc.bezierBy = function(t, e) {
    return new cc.BezierBy(t, e)
   }, cc.BezierTo = cc.Class({
    name: "cc.BezierTo",
    extends: cc.BezierBy,
    ctor: function(t, e) {
     this._toConfig = [], e && this.initWithDuration(t, e)
    },
    initWithDuration: function(t, e) {
     return !!cc.ActionInterval.prototype.initWithDuration.call(this, t) && (this._toConfig = e, !0)
    },
    clone: function() {
     var t = new cc.BezierTo;
     return this._cloneDecoration(t), t.initWithDuration(this._duration, this._toConfig), t
    },
    startWithTarget: function(t) {
     cc.BezierBy.prototype.startWithTarget.call(this, t);
     var e = this._startPosition,
      i = this._toConfig,
      n = this._config;
     n[0] = i[0].sub(e), n[1] = i[1].sub(e), n[2] = i[2].sub(e)
    }
   }), cc.bezierTo = function(t, e) {
    return new cc.BezierTo(t, e)
   }, cc.ScaleTo = cc.Class({
    name: "cc.ScaleTo",
    extends: cc.ActionInterval,
    ctor: function(t, e, i) {
     this._scaleX = 1, this._scaleY = 1, this._startScaleX = 1, this._startScaleY = 1, this._endScaleX = 0, this._endScaleY = 0, this._deltaX = 0, this._deltaY = 0, void 0 !== e && cc.ScaleTo.prototype.initWithDuration.call(this, t, e, i)
    },
    initWithDuration: function(t, e, i) {
     return !!cc.ActionInterval.prototype.initWithDuration.call(this, t) && (this._endScaleX = e, this._endScaleY = null != i ? i : e, !0)
    },
    clone: function() {
     var t = new cc.ScaleTo;
     return this._cloneDecoration(t), t.initWithDuration(this._duration, this._endScaleX, this._endScaleY), t
    },
    startWithTarget: function(t) {
     cc.ActionInterval.prototype.startWithTarget.call(this, t), this._startScaleX = t.scaleX, this._startScaleY = t.scaleY, this._deltaX = this._endScaleX - this._startScaleX, this._deltaY = this._endScaleY - this._startScaleY
    },
    update: function(t) {
     t = this._computeEaseTime(t), this.target && (this.target.scaleX = this._startScaleX + this._deltaX * t, this.target.scaleY = this._startScaleY + this._deltaY * t)
    }
   }), cc.scaleTo = function(t, e, i) {
    return new cc.ScaleTo(t, e, i)
   }, cc.ScaleBy = cc.Class({
    name: "cc.ScaleBy",
    extends: cc.ScaleTo,
    startWithTarget: function(t) {
     cc.ScaleTo.prototype.startWithTarget.call(this, t), this._deltaX = this._startScaleX * this._endScaleX - this._startScaleX, this._deltaY = this._startScaleY * this._endScaleY - this._startScaleY
    },
    reverse: function() {
     var t = new cc.ScaleBy(this._duration, 1 / this._endScaleX, 1 / this._endScaleY);
     return this._cloneDecoration(t), this._reverseEaseList(t), t
    },
    clone: function() {
     var t = new cc.ScaleBy;
     return this._cloneDecoration(t), t.initWithDuration(this._duration, this._endScaleX, this._endScaleY), t
    }
   }), cc.scaleBy = function(t, e, i) {
    return new cc.ScaleBy(t, e, i)
   }, cc.Blink = cc.Class({
    name: "cc.Blink",
    extends: cc.ActionInterval,
    ctor: function(t, e) {
     this._times = 0, this._originalState = !1, void 0 !== e && this.initWithDuration(t, e)
    },
    initWithDuration: function(t, e) {
     return !!cc.ActionInterval.prototype.initWithDuration.call(this, t) && (this._times = e, !0)
    },
    clone: function() {
     var t = new cc.Blink;
     return this._cloneDecoration(t), t.initWithDuration(this._duration, this._times), t
    },
    update: function(t) {
     if (t = this._computeEaseTime(t), this.target && !this.isDone()) {
      var e = 1 / this._times,
       i = t % e;
      this.target.opacity = i > e / 2 ? 255 : 0
     }
    },
    startWithTarget: function(t) {
     cc.ActionInterval.prototype.startWithTarget.call(this, t), this._originalState = t.opacity
    },
    stop: function() {
     this.target.opacity = this._originalState, cc.ActionInterval.prototype.stop.call(this)
    },
    reverse: function() {
     var t = new cc.Blink(this._duration, this._times);
     return this._cloneDecoration(t), this._reverseEaseList(t), t
    }
   }), cc.blink = function(t, e) {
    return new cc.Blink(t, e)
   }, cc.FadeTo = cc.Class({
    name: "cc.FadeTo",
    extends: cc.ActionInterval,
    ctor: function(t, e) {
     this._toOpacity = 0, this._fromOpacity = 0, void 0 !== e && cc.FadeTo.prototype.initWithDuration.call(this, t, e)
    },
    initWithDuration: function(t, e) {
     return !!cc.ActionInterval.prototype.initWithDuration.call(this, t) && (this._toOpacity = e, !0)
    },
    clone: function() {
     var t = new cc.FadeTo;
     return this._cloneDecoration(t), t.initWithDuration(this._duration, this._toOpacity), t
    },
    update: function(t) {
     t = this._computeEaseTime(t);
     var e = void 0 !== this._fromOpacity ? this._fromOpacity : 255;
     this.target.opacity = e + (this._toOpacity - e) * t
    },
    startWithTarget: function(t) {
     cc.ActionInterval.prototype.startWithTarget.call(this, t), this._fromOpacity = t.opacity
    }
   }), cc.fadeTo = function(t, e) {
    return new cc.FadeTo(t, e)
   }, cc.FadeIn = cc.Class({
    name: "cc.FadeIn",
    extends: cc.FadeTo,
    ctor: function(t) {
     null == t && (t = 0), this._reverseAction = null, this.initWithDuration(t, 255)
    },
    reverse: function() {
     var t = new cc.FadeOut;
     return t.initWithDuration(this._duration, 0), this._cloneDecoration(t), this._reverseEaseList(t), t
    },
    clone: function() {
     var t = new cc.FadeIn;
     return this._cloneDecoration(t), t.initWithDuration(this._duration, this._toOpacity), t
    },
    startWithTarget: function(t) {
     this._reverseAction && (this._toOpacity = this._reverseAction._fromOpacity), cc.FadeTo.prototype.startWithTarget.call(this, t)
    }
   }), cc.fadeIn = function(t) {
    return new cc.FadeIn(t)
   }, cc.FadeOut = cc.Class({
    name: "cc.FadeOut",
    extends: cc.FadeTo,
    ctor: function(t) {
     null == t && (t = 0), this._reverseAction = null, this.initWithDuration(t, 0)
    },
    reverse: function() {
     var t = new cc.FadeIn;
     return t._reverseAction = this, t.initWithDuration(this._duration, 255), this._cloneDecoration(t), this._reverseEaseList(t), t
    },
    clone: function() {
     var t = new cc.FadeOut;
     return this._cloneDecoration(t), t.initWithDuration(this._duration, this._toOpacity), t
    }
   }), cc.fadeOut = function(t) {
    return new cc.FadeOut(t)
   }, cc.TintTo = cc.Class({
    name: "cc.TintTo",
    extends: cc.ActionInterval,
    ctor: function(t, e, i, n) {
     this._to = cc.color(0, 0, 0), this._from = cc.color(0, 0, 0), e instanceof cc.Color && (n = e.b, i = e.g, e = e.r), void 0 !== n && this.initWithDuration(t, e, i, n)
    },
    initWithDuration: function(t, e, i, n) {
     return !!cc.ActionInterval.prototype.initWithDuration.call(this, t) && (this._to = cc.color(e, i, n), !0)
    },
    clone: function() {
     var t = new cc.TintTo;
     this._cloneDecoration(t);
     var e = this._to;
     return t.initWithDuration(this._duration, e.r, e.g, e.b), t
    },
    startWithTarget: function(t) {
     cc.ActionInterval.prototype.startWithTarget.call(this, t), this._from = this.target.color
    },
    update: function(t) {
     t = this._computeEaseTime(t);
     var e = this._from,
      i = this._to;
     e && (this.target.color = cc.color(e.r + (i.r - e.r) * t, e.g + (i.g - e.g) * t, e.b + (i.b - e.b) * t))
    }
   }), cc.tintTo = function(t, e, i, n) {
    return new cc.TintTo(t, e, i, n)
   }, cc.TintBy = cc.Class({
    name: "cc.TintBy",
    extends: cc.ActionInterval,
    ctor: function(t, e, i, n) {
     this._deltaR = 0, this._deltaG = 0, this._deltaB = 0, this._fromR = 0, this._fromG = 0, this._fromB = 0, void 0 !== n && this.initWithDuration(t, e, i, n)
    },
    initWithDuration: function(t, e, i, n) {
     return !!cc.ActionInterval.prototype.initWithDuration.call(this, t) && (this._deltaR = e, this._deltaG = i, this._deltaB = n, !0)
    },
    clone: function() {
     var t = new cc.TintBy;
     return this._cloneDecoration(t), t.initWithDuration(this._duration, this._deltaR, this._deltaG, this._deltaB), t
    },
    startWithTarget: function(t) {
     cc.ActionInterval.prototype.startWithTarget.call(this, t);
     var e = t.color;
     this._fromR = e.r, this._fromG = e.g, this._fromB = e.b
    },
    update: function(t) {
     t = this._computeEaseTime(t), this.target.color = cc.color(this._fromR + this._deltaR * t, this._fromG + this._deltaG * t, this._fromB + this._deltaB * t)
    },
    reverse: function() {
     var t = new cc.TintBy(this._duration, -this._deltaR, -this._deltaG, -this._deltaB);
     return this._cloneDecoration(t), this._reverseEaseList(t), t
    }
   }), cc.tintBy = function(t, e, i, n) {
    return new cc.TintBy(t, e, i, n)
   }, cc.DelayTime = cc.Class({
    name: "cc.DelayTime",
    extends: cc.ActionInterval,
    update: function(t) {},
    reverse: function() {
     var t = new cc.DelayTime(this._duration);
     return this._cloneDecoration(t), this._reverseEaseList(t), t
    },
    clone: function() {
     var t = new cc.DelayTime;
     return this._cloneDecoration(t), t.initWithDuration(this._duration), t
    }
   }), cc.delayTime = function(t) {
    return new cc.DelayTime(t)
   }, cc.ReverseTime = cc.Class({
    name: "cc.ReverseTime",
    extends: cc.ActionInterval,
    ctor: function(t) {
     this._other = null, t && this.initWithAction(t)
    },
    initWithAction: function(t) {
     return t ? t === this._other ? (cc.errorID(1029), !1) : !!cc.ActionInterval.prototype.initWithDuration.call(this, t._duration) && (this._other = t, !0) : (cc.errorID(1028), !1)
    },
    clone: function() {
     var t = new cc.ReverseTime;
     return this._cloneDecoration(t), t.initWithAction(this._other.clone()), t
    },
    startWithTarget: function(t) {
     cc.ActionInterval.prototype.startWithTarget.call(this, t), this._other.startWithTarget(t)
    },
    update: function(t) {
     t = this._computeEaseTime(t), this._other && this._other.update(1 - t)
    },
    reverse: function() {
     return this._other.clone()
    },
    stop: function() {
     this._other.stop(), cc.Action.prototype.stop.call(this)
    }
   }), cc.reverseTime = function(t) {
    return new cc.ReverseTime(t)
   }, cc.TargetedAction = cc.Class({
    name: "cc.TargetedAction",
    extends: cc.ActionInterval,
    ctor: function(t, e) {
     this._action = null, this._forcedTarget = null, e && this.initWithTarget(t, e)
    },
    initWithTarget: function(t, e) {
     return !!this.initWithDuration(e._duration) && (this._forcedTarget = t, this._action = e, !0)
    },
    clone: function() {
     var t = new cc.TargetedAction;
     return this._cloneDecoration(t), t.initWithTarget(this._forcedTarget, this._action.clone()), t
    },
    startWithTarget: function(t) {
     cc.ActionInterval.prototype.startWithTarget.call(this, t), this._action.startWithTarget(this._forcedTarget)
    },
    stop: function() {
     this._action.stop()
    },
    update: function(t) {
     t = this._computeEaseTime(t), this._action.update(t)
    },
    getForcedTarget: function() {
     return this._forcedTarget
    },
    setForcedTarget: function(t) {
     this._forcedTarget !== t && (this._forcedTarget = t)
    }
   }), cc.targetedAction = function(t, e) {
    return new cc.TargetedAction(t, e)
   }
  }), {}],
  6: [(function(t, e, i) {
   t("../core/platform/CCClass");
   var n = t("../core/platform/js");
   cc.ActionManager = function() {
    this._hashTargets = n.createMap(!0), this._arrayTargets = [], this._currentTarget = null, cc.director._scheduler && cc.director._scheduler.enableForTarget(this)
   }, cc.ActionManager.prototype = {
    constructor: cc.ActionManager,
    _elementPool: [],
    _searchElementByTarget: function(t, e) {
     for (var i = 0; i < t.length; i++)
      if (e === t[i].target) return t[i];
     return null
    },
    _getElement: function(t, e) {
     var i = this._elementPool.pop();
     return i || (i = new function() {
      this.actions = [], this.target = null, this.actionIndex = 0, this.currentAction = null, this.paused = !1, this.lock = !1
     }), i.target = t, i.paused = !!e, i
    },
    _putElement: function(t) {
     t.actions.length = 0, t.actionIndex = 0, t.currentAction = null, t.paused = !1, t.target = null, t.lock = !1, this._elementPool.push(t)
    },
    addAction: function(t, e, i) {
     if (t && e) {
      var n = this._hashTargets[e._id];
      n ? n.actions || (n.actions = []) : (n = this._getElement(e, i), this._hashTargets[e._id] = n, this._arrayTargets.push(n)), n.actions.push(t), t.startWithTarget(e)
     } else cc.errorID(1e3)
    },
    removeAllActions: function() {
     for (var t = this._arrayTargets, e = 0; e < t.length; e++) {
      var i = t[e];
      i && this._putElement(i)
     }
     this._arrayTargets.length = 0, this._hashTargets = n.createMap(!0)
    },
    removeAllActionsFromTarget: function(t, e) {
     if (null != t) {
      var i = this._hashTargets[t._id];
      i && (i.actions.length = 0, this._deleteHashElement(i))
     }
    },
    removeAction: function(t) {
     if (null != t) {
      var e = t.getOriginalTarget(),
       i = this._hashTargets[e._id];
      if (i) {
       for (var n = 0; n < i.actions.length; n++)
        if (i.actions[n] === t) {
         i.actions.splice(n, 1), i.actionIndex >= n && i.actionIndex--;
         break
        }
      } else cc.logID(1001)
     }
    },
    removeActionByTag: function(t, e) {
     t === cc.Action.TAG_INVALID && cc.logID(1002), cc.assertID(e, 1003);
     var i = this._hashTargets[e._id];
     if (i)
      for (var n = i.actions.length, r = 0; r < n; ++r) {
       var s = i.actions[r];
       if (s && s.getTag() === t && s.getOriginalTarget() === e) {
        this._removeActionAtIndex(r, i);
        break
       }
      }
    },
    getActionByTag: function(t, e) {
     t === cc.Action.TAG_INVALID && cc.logID(1004);
     var i = this._hashTargets[e._id];
     if (i) {
      if (null != i.actions)
       for (var n = 0; n < i.actions.length; ++n) {
        var r = i.actions[n];
        if (r && r.getTag() === t) return r
       }
      cc.logID(1005, t)
     }
     return null
    },
    getNumberOfRunningActionsInTarget: function(t) {
     var e = this._hashTargets[t._id];
     return e && e.actions ? e.actions.length : 0
    },
    pauseTarget: function(t) {
     var e = this._hashTargets[t._id];
     e && (e.paused = !0)
    },
    resumeTarget: function(t) {
     var e = this._hashTargets[t._id];
     e && (e.paused = !1)
    },
    pauseAllRunningActions: function() {
     for (var t = [], e = this._arrayTargets, i = 0; i < e.length; i++) {
      var n = e[i];
      n && !n.paused && (n.paused = !0, t.push(n.target))
     }
     return t
    },
    resumeTargets: function(t) {
     if (t)
      for (var e = 0; e < t.length; e++) t[e] && this.resumeTarget(t[e])
    },
    pauseTargets: function(t) {
     if (t)
      for (var e = 0; e < t.length; e++) t[e] && this.pauseTarget(t[e])
    },
    purgeSharedManager: function() {
     cc.director.getScheduler().unscheduleUpdate(this)
    },
    _removeActionAtIndex: function(t, e) {
     e.actions[t];
     e.actions.splice(t, 1), e.actionIndex >= t && e.actionIndex--, 0 === e.actions.length && this._deleteHashElement(e)
    },
    _deleteHashElement: function(t) {
     var e = !1;
     if (t && !t.lock && this._hashTargets[t.target._id]) {
      delete this._hashTargets[t.target._id];
      for (var i = this._arrayTargets, n = 0, r = i.length; n < r; n++)
       if (i[n] === t) {
        i.splice(n, 1);
        break
       } this._putElement(t), e = !0
     }
     return e
    },
    update: function(t) {
     for (var e, i = this._arrayTargets, n = 0; n < i.length; n++) {
      if (this._currentTarget = i[n], !(e = this._currentTarget).paused && e.actions) {
       for (e.lock = !0, e.actionIndex = 0; e.actionIndex < e.actions.length; e.actionIndex++)
        if (e.currentAction = e.actions[e.actionIndex], e.currentAction) {
         if (e.currentAction.step(t * (e.currentAction._speedMethod ? e.currentAction._speed : 1)), e.currentAction && e.currentAction.isDone()) {
          e.currentAction.stop();
          var r = e.currentAction;
          e.currentAction = null, this.removeAction(r)
         }
         e.currentAction = null
        } e.lock = !1
      }
      0 === e.actions.length && this._deleteHashElement(e) && n--
     }
    }
   }
  }), {
   "../core/platform/CCClass": 108,
   "../core/platform/js": 128
  }],
  7: [(function(t, e, i) {
   t("./CCActionManager"), t("./CCAction"), t("./CCActionInterval"), t("./CCActionInstant"), t("./CCActionEase"), t("./CCActionCatmullRom")
  }), {
   "./CCAction": 1,
   "./CCActionCatmullRom": 2,
   "./CCActionEase": 3,
   "./CCActionInstant": 4,
   "./CCActionInterval": 5,
   "./CCActionManager": 6
  }],
  8: [(function(t, e, i) {
   var n = cc.js,
    r = t("./playable"),
    s = t("./animation-curves").DynamicAnimCurve,
    a = t("./animation-curves").quickFindIndex,
    o = t("./motion-path-helper").sampleMotionPaths,
    c = t("./animation-curves").EventAnimCurve,
    h = t("./animation-curves").EventInfo,
    u = t("./types").WrapModeMask,
    l = t("../core/utils/binary-search").binarySearchEpsilon;

   function _(t, e) {
    r.call(this), this.target = t, this.animation = e, this._anims = new n.array.MutableForwardIterator([])
   }
   n.extend(_, r);
   var d = _.prototype;

   function f(t, e) {
    var i = e.clip,
     n = e.curves;

    function r(t) {
     if (!Array.isArray(t)) return !1;
     for (var e = 0, i = t.length; e < i; e++) {
      var n = t[e];
      if (!Array.isArray(n) || 6 !== n.length) return !1
     }
     return !0
    }

    function _(t, n, c) {
     var h, u = t instanceof cc.Node && "position" === n && c[0] && Array.isArray(c[0].value),
      _ = [],
      d = new s;
     d.target = t;
     var f = n.indexOf("."); - 1 !== f ? t[h = n.slice(0, f)] : h = n, d.prop = h, d.subProps = (function(t) {
      var e = t.split(".");
      return e.shift(), e.length > 0 ? e : null
     })(n);
     for (var p = 0, m = c.length; p < m; p++) {
      var v = c[p],
       g = v.frame / e.duration;
      if (d.ratios.push(g), u) {
       var y = v.motionPath;
       y && !r(y) && (cc.errorID(3904, t.name, n, p), y = null), _.push(y)
      }
      var T = v.value;
      d.values.push(T);
      var E = v.curve;
      if (E) {
       if ("string" == typeof E) {
        d.types.push(E);
        continue
       }
       if (Array.isArray(E)) {
        E[0] === E[1] && E[2] === E[3] ? d.types.push(s.Linear) : d.types.push(s.Bezier(E));
        continue
       }
      }
      d.types.push(s.Linear)
     }
     u && o(_, d, i.duration, i.sample);
     for (var x, C, A = d.ratios, b = !0, S = 1, w = A.length; S < w; S++)
      if (x = A[S] - A[S - 1], 1 === S) C = x;
      else if (Math.abs(x - C) > 1e-6) {
      b = !1;
      break
     }
     return d._findFrameIndex = b ? a : l, d
    }

    function d(t, e) {
     var i = e.props,
      r = e.comps;
     if (i)
      for (var s in i) {
       var a = _(t, s, i[s]);
       n.push(a)
      }
     if (r)
      for (var o in r) {
       var c = t.getComponent(o);
       if (c) {
        var h = r[o];
        for (var s in h) {
         a = _(c, s, h[s]);
         n.push(a)
        }
       }
      }
    }
    n.length = 0, e.duration = i.duration, e.speed = i.speed, e.wrapMode = i.wrapMode, e.frameRate = i.sample, (e.wrapMode & u.Loop) === u.Loop ? e.repeatCount = 1 / 0 : e.repeatCount = 1;
    var f = i.curveData,
     p = f.paths;
    for (var m in d(t, f), p) {
     var v = cc.find(m, t);
     if (v) d(v, p[m])
    }
    var g = i.events;
    if (g)
     for (var y, T = 0, E = g.length; T < E; T++) {
      y || ((y = new c).target = t, n.push(y));
      var x, C = g[T],
       A = C.frame / e.duration,
       b = l(y.ratios, A);
      b >= 0 ? x = y.events[b] : (x = new h, y.ratios.push(A), y.events.push(x)), x.add(C.func, C.params)
     }
   }
   d.playState = function(t, e) {
    t.clip && (t.curveLoaded || f(this.target, t), t.animator = this, t.play(), "number" == typeof e && t.setTime(e), this.play())
   }, d.stopStatesExcept = function(t) {
    var e = this._anims,
     i = e.array;
    for (e.i = 0; e.i < i.length; ++e.i) {
     var n = i[e.i];
     n !== t && this.stopState(n)
    }
   }, d.addAnimation = function(t) {
    -1 === this._anims.array.indexOf(t) && this._anims.push(t), t._setEventTarget(this.animation)
   }, d.removeAnimation = function(t) {
    var e = this._anims.array.indexOf(t);
    e >= 0 ? (this._anims.fastRemoveAt(e), 0 === this._anims.array.length && this.stop()) : cc.errorID(3908), t.animator = null
   }, d.sample = function() {
    var t = this._anims,
     e = t.array;
    for (t.i = 0; t.i < e.length; ++t.i) {
     e[t.i].sample()
    }
   }, d.stopState = function(t) {
    t && t.stop()
   }, d.pauseState = function(t) {
    t && t.pause()
   }, d.resumeState = function(t) {
    t && t.resume(), this.isPaused && this.resume()
   }, d.setStateTime = function(t, e) {
    if (void 0 !== e) t && (t.setTime(e), t.sample());
    else {
     e = t;
     for (var i = this._anims.array, n = 0; n < i.length; ++n) {
      var r = i[n];
      r.setTime(e), r.sample()
     }
    }
   }, d.onStop = function() {
    var t = this._anims,
     e = t.array;
    for (t.i = 0; t.i < e.length; ++t.i) {
     e[t.i].stop()
    }
   }, d.onPause = function() {
    for (var t = this._anims.array, e = 0; e < t.length; ++e) {
     var i = t[e];
     i.pause(), i.animator = null
    }
   }, d.onResume = function() {
    for (var t = this._anims.array, e = 0; e < t.length; ++e) {
     var i = t[e];
     i.animator = this, i.resume()
    }
   }, d._reloadClip = function(t) {
    f(this.target, t)
   }, e.exports = _
  }), {
   "../core/utils/binary-search": 181,
   "./animation-curves": 10,
   "./motion-path-helper": 16,
   "./playable": 17,
   "./types": 18
  }],
  9: [(function(t, e, i) {
   var n = cc.Class({
    name: "cc.AnimationClip",
    extends: cc.Asset,
    properties: {
     _duration: {
      default: 0,
      type: "Float"
     },
     duration: {
      get: function() {
       return this._duration
      }
     },
     sample: {
      default: 60
     },
     speed: {
      default: 1
     },
     wrapMode: {
      default: cc.WrapMode.Normal
     },
     curveData: {
      default: {},
      visible: !1
     },
     events: {
      default: [],
      visible: !1
     }
    },
    statics: {
     createWithSpriteFrames: function(t, e) {
      if (!Array.isArray(t)) return cc.errorID(3905), null;
      var i = new n;
      i.sample = e || i.sample, i._duration = t.length / i.sample;
      for (var r = [], s = 1 / i.sample, a = 0, o = t.length; a < o; a++) r[a] = {
       frame: a * s,
       value: t[a]
      };
      return i.curveData = {
       comps: {
        "cc.Sprite": {
         spriteFrame: r
        }
       }
      }, i
     }
    }
   });
   cc.AnimationClip = e.exports = n
  }), {}],
  10: [(function(t, e, i) {
   var n = t("./bezier").bezierByTime,
    r = t("../core/utils/binary-search").binarySearchEpsilon,
    s = t("./types").WrapModeMask,
    a = t("./types").WrappedInfo;

   function o(t, e) {
    if ("string" == typeof e) {
     var i = cc.Easing[e];
     i ? t = i(t) : cc.errorID(3906, e)
    } else Array.isArray(e) && (t = n(e, t));
    return t
   }
   var c = cc.Class({
    name: "cc.AnimCurve",
    sample: function(t, e, i) {},
    onTimeChangedManually: void 0
   });

   function h(t, e) {
    var i = t.length - 1;
    if (0 === i) return 0;
    var n = t[0];
    if (e < n) return 0;
    var r = t[i];
    if (e > r) return i;
    var s = (e = (e - n) / (r - n)) / (1 / i),
     a = 0 | s;
    return s - a < 1e-6 ? a : a + 1 - s < 1e-6 ? a + 1 : ~(a + 1)
   }
   var u = cc.Class({
    name: "cc.DynamicAnimCurve",
    extends: c,
    properties: {
     target: null,
     prop: "",
     values: [],
     ratios: [],
     types: [],
     subProps: null
    },
    _findFrameIndex: r,
    sample: function(t, e, i) {
     var n = this.values,
      r = this.ratios,
      s = r.length;
     if (0 !== s) {
      var a, c = this._findFrameIndex(r, e);
      if (c < 0)
       if ((c = ~c) <= 0) a = n[0];
       else if (c >= s) a = n[s - 1];
      else {
       var h = n[c - 1],
        u = "number" == typeof h,
        l = h && h.lerp;
       if (u || l) {
        var _ = r[c - 1],
         d = r[c],
         f = this.types[c - 1],
         p = (e - _) / (d - _);
        f && (p = o(p, f));
        var m = n[c];
        u ? a = h + (m - h) * p : l && (a = h.lerp(m, p))
       } else a = h
      } else a = n[c];
      var v = this.subProps;
      if (v) {
       for (var g = this.target[this.prop], y = g, T = 0; T < v.length - 1; T++) {
        var E = v[T];
        if (!y) return;
        y = y[E]
       }
       var x = v[v.length - 1];
       if (!y) return;
       y[x] = a, a = g
      }
      this.target[this.prop] = a
     }
    }
   });
   u.Linear = null, u.Bezier = function(t) {
    return t
   };
   var l = function() {
    this.events = []
   };
   l.prototype.add = function(t, e) {
    this.events.push({
     func: t || "",
     params: e || []
    })
   };
   var _ = cc.Class({
    name: "cc.EventAnimCurve",
    extends: c,
    properties: {
     target: null,
     ratios: [],
     events: [],
     _wrappedInfo: {
      default: function() {
       return new a
      }
     },
     _lastWrappedInfo: null,
     _ignoreIndex: NaN
    },
    _wrapIterations: function(t) {
     return t - (0 | t) == 0 && (t -= 1), 0 | t
    },
    sample: function(t, e, i) {
     var n = this.ratios.length,
      o = i.getWrappedInfo(i.time, this._wrappedInfo),
      c = o.direction,
      h = r(this.ratios, o.ratio);
     if (h < 0 && (h = ~h - 1, c < 0 && (h += 1)), this._ignoreIndex !== h && (this._ignoreIndex = NaN), o.frameIndex = h, !this._lastWrappedInfo) return this._fireEvent(h), void(this._lastWrappedInfo = new a(o));
     var u = i.wrapMode,
      l = this._wrapIterations(o.iterations),
      _ = this._lastWrappedInfo,
      d = this._wrapIterations(_.iterations),
      f = _.frameIndex,
      p = _.direction,
      m = -1 !== d && l !== d;
     if (f === h && m && 1 === n) this._fireEvent(0);
     else if (f !== h || m) {
      c = p;
      do {
       if (f !== h) {
        if (-1 === c && 0 === f && h > 0 ? ((u & s.PingPong) === s.PingPong ? c *= -1 : f = n, d++) : 1 === c && f === n - 1 && h < n - 1 && ((u & s.PingPong) === s.PingPong ? c *= -1 : f = -1, d++), f === h) break;
        if (d > l) break
       }
       f += c, cc.director.getAnimationManager().pushDelayEvent(this, "_fireEvent", [f])
      } while (f !== h && f > -1 && f < n)
     }
     this._lastWrappedInfo.set(o)
    },
    _fireEvent: function(t) {
     if (!(t < 0 || t >= this.events.length || this._ignoreIndex === t)) {
      var e = this.events[t].events;
      if (this.target.isValid)
       for (var i = this.target._components, n = 0; n < e.length; n++)
        for (var r = e[n], s = r.func, a = 0; a < i.length; a++) {
         var o = i[a],
          c = o[s];
         c && c.apply(o, r.params)
        }
     }
    },
    onTimeChangedManually: function(t, e) {
     this._lastWrappedInfo = null, this._ignoreIndex = NaN;
     var i = e.getWrappedInfo(t, this._wrappedInfo),
      n = i.direction,
      s = r(this.ratios, i.ratio);
     s < 0 && (s = ~s - 1, n < 0 && (s += 1), this._ignoreIndex = s)
    }
   });
   e.exports = {
    AnimCurve: c,
    DynamicAnimCurve: u,
    EventAnimCurve: _,
    EventInfo: l,
    computeRatioByType: o,
    quickFindIndex: h
   }
  }), {
   "../core/utils/binary-search": 181,
   "./bezier": 13,
   "./types": 18
  }],
  11: [(function(t, e, i) {
   var n = cc.js,
    r = cc.Class({
     ctor: function() {
      this._anims = new n.array.MutableForwardIterator([]), this._delayEvents = [], cc.director._scheduler && cc.director._scheduler.enableForTarget(this)
     },
     update: function(t) {
      var e = this._anims,
       i = e.array;
      for (e.i = 0; e.i < i.length; ++e.i) {
       var n = i[e.i];
       n._isPlaying && !n._isPaused && n.update(t)
      }
      for (var r = this._delayEvents, s = 0, a = r.length; s < a; s++) {
       var o = r[s];
       o.target[o.func].apply(o.target, o.args)
      }
      r.length = 0
     },
     destruct: function() {},
     addAnimation: function(t) {
      -1 === this._anims.array.indexOf(t) && this._anims.push(t)
     },
     removeAnimation: function(t) {
      var e = this._anims.array.indexOf(t);
      e >= 0 ? this._anims.fastRemoveAt(e) : cc.errorID(3907)
     },
     pushDelayEvent: function(t, e, i) {
      this._delayEvents.push({
       target: t,
       func: e,
       args: i
      })
     }
    });
   cc.AnimationManager = e.exports = r
  }), {}],
  12: [(function(t, e, i) {
   var n = cc.js,
    r = t("./playable"),
    s = t("./types"),
    a = s.WrappedInfo,
    o = s.WrapMode,
    c = s.WrapModeMask;

   function h(t, e) {
    r.call(this), this._currentFramePlayed = !1, this._delay = 0, this._delayTime = 0, this._wrappedInfo = new a, this._lastWrappedInfo = null, this._process = l, this._clip = t, this._name = e || t && t.name, this.animator = null, this.curves = [], this.delay = 0, this.repeatCount = 1, this.duration = 1, this.speed = 1, this.wrapMode = o.Normal, this.time = 0, this._target = null, this._lastframeEventOn = !1, this.emit = function() {
     for (var t = new Array(arguments.length), e = 0, i = t.length; e < i; e++) t[e] = arguments[e];
     cc.director.getAnimationManager().pushDelayEvent(this, "_emit", t)
    }
   }
   n.extend(h, r);
   var u = h.prototype;

   function l() {
    var t, e = this.sample();
    this._lastframeEventOn && (t = this._lastWrappedInfo ? this._lastWrappedInfo : this._lastWrappedInfo = new a(e), this.repeatCount > 1 && (0 | e.iterations) > (0 | t.iterations) && this.emit("lastframe", this), t.set(e));
    e.stopped && (this.stop(), this.emit("finished", this))
   }

   function _() {
    var t = this.time,
     e = this.duration;
    t > e ? 0 === (t %= e) && (t = e) : t < 0 && 0 !== (t %= e) && (t += e);
    for (var i = t / e, n = this.curves, r = 0, s = n.length; r < s; r++) {
     n[r].sample(t, i, this)
    }
    this._lastframeEventOn && (void 0 === this._lastIterations && (this._lastIterations = i), (this.time > 0 && this._lastIterations > i || this.time < 0 && this._lastIterations < i) && this.emit("lastframe", this), this._lastIterations = i)
   }
   u._emit = function(t, e) {
    this._target && this._target.isValid && this._target.emit(t, t, e)
   }, u.on = function(t, e, i) {
    return this._target && this._target.isValid ? ("lastframe" === t && (this._lastframeEventOn = !0), this._target.on(t, e, i)) : null
   }, u.once = function(t, e, i) {
    var n = this;
    if (!this._target || !this._target.isValid) return null;
    var r = (function() {
     "lastframe" === t && (n._lastframeEventOn = !0);
     var r = n;
     return {
      v: n._target.once(t, (function(t) {
       e.call(i, t), r._lastframeEventOn = !1
      }))
     }
    })();
    return "object" == typeof r ? r.v : void 0
   }, u.off = function(t, e, i) {
    this._target && this._target.isValid && ("lastframe" === t && (this._target.hasEventListener(t) || (this._lastframeEventOn = !1)), this._target.off(t, e, i))
   }, u._setEventTarget = function(t) {
    this._target = t
   }, u.onPlay = function() {
    this.setTime(0), this._delayTime = this._delay, cc.director.getAnimationManager().addAnimation(this), this.animator && this.animator.addAnimation(this), this.emit("play", this)
   }, u.onStop = function() {
    this.isPaused || cc.director.getAnimationManager().removeAnimation(this), this.animator && this.animator.removeAnimation(this), this.emit("stop", this)
   }, u.onResume = function() {
    cc.director.getAnimationManager().addAnimation(this), this.emit("resume", this)
   }, u.onPause = function() {
    cc.director.getAnimationManager().removeAnimation(this), this.emit("pause", this)
   }, u.setTime = function(t) {
    this._currentFramePlayed = !1, this.time = t || 0;
    for (var e = this.curves, i = 0, n = e.length; i < n; i++) {
     var r = e[i];
     r.onTimeChangedManually && r.onTimeChangedManually(t, this)
    }
   }, u.update = function(t) {
    this._delayTime > 0 && (this._delayTime -= t, this._delayTime > 0) || (this._currentFramePlayed ? this.time += t * this.speed : this._currentFramePlayed = !0, this._process())
   }, u._needRevers = function(t) {
    var e = this.wrapMode,
     i = !1;
    (e & c.PingPong) === c.PingPong && (t - (0 | t) == 0 && t > 0 && (t -= 1), 1 & t && (i = !i));
    return (e & c.Reverse) === c.Reverse && (i = !i), i
   }, u.getWrappedInfo = function(t, e) {
    e = e || new a;
    var i = !1,
     n = this.duration,
     r = this.repeatCount,
     s = t > 0 ? t / n : -t / n;
    if (s >= r) {
     s = r, i = !0;
     var o = r - (0 | r);
     0 === o && (o = 1), t = o * n * (t > 0 ? 1 : -1)
    }
    if (t > n) {
     var h = t % n;
     t = 0 === h ? n : h
    } else t < 0 && 0 !== (t %= n) && (t += n);
    var u = !1,
     l = this._wrapMode & c.ShouldWrap;
    l && (u = this._needRevers(s));
    var _ = u ? -1 : 1;
    return this.speed < 0 && (_ *= -1), l && u && (t = n - t), e.ratio = t / n, e.time = t, e.direction = _, e.stopped = i, e.iterations = s, e
   }, u.sample = function() {
    for (var t = this.getWrappedInfo(this.time, this._wrappedInfo), e = this.curves, i = 0, n = e.length; i < n; i++) {
     e[i].sample(t.time, t.ratio, this)
    }
    return t
   }, n.get(u, "clip", (function() {
    return this._clip
   })), n.get(u, "name", (function() {
    return this._name
   })), n.obsolete(u, "AnimationState.length", "duration"), n.getset(u, "curveLoaded", (function() {
    return this.curves.length > 0
   }), (function() {
    this.curves.length = 0
   })), n.getset(u, "wrapMode", (function() {
    return this._wrapMode
   }), (function(t) {
    this._wrapMode = t, this.time = 0, t & c.Loop ? this.repeatCount = 1 / 0 : this.repeatCount = 1
   })), n.getset(u, "repeatCount", (function() {
    return this._repeatCount
   }), (function(t) {
    this._repeatCount = t;
    var e = this._wrapMode & c.ShouldWrap,
     i = (this.wrapMode & c.Reverse) === c.Reverse;
    this._process = t !== 1 / 0 || e || i ? l : _
   })), n.getset(u, "delay", (function() {
    return this._delay
   }), (function(t) {
    this._delayTime = this._delay = t
   })), cc.AnimationState = e.exports = h
  }), {
   "./playable": 17,
   "./types": 18
  }],
  13: [(function(t, e, i) {
   function n(t, e, i, n, r) {
    var s = 1 - r;
    return t * s * s * s + 3 * e * s * s * r + 3 * i * s * r * r + n * r * r * r
   }
   var r = Math.cos,
    s = Math.acos,
    a = Math.max,
    o = 2 * Math.PI,
    c = Math.sqrt;

   function h(t) {
    return t < 0 ? -Math.pow(-t, 1 / 3) : Math.pow(t, 1 / 3)
   }

   function u(t, e) {
    var i = (function(t, e) {
      var i, n, u, l, _ = e - 0,
       d = e - t[0],
       f = 3 * _,
       p = 3 * d,
       m = 3 * (e - t[2]),
       v = 1 / (-_ + p - m + (e - 1)),
       g = (f - 6 * d + m) * v,
       y = g * (1 / 3),
       T = (-f + p) * v,
       E = 1 / 3 * (3 * T - g * g),
       x = E * (1 / 3),
       C = (2 * g * g * g - 9 * g * T + _ * v * 27) / 27,
       A = C / 2,
       b = A * A + x * x * x;
      if (b < 0) {
       var S = 1 / 3 * -E,
        w = c(S * S * S),
        D = -C / (2 * w),
        R = s(D < -1 ? -1 : D > 1 ? 1 : D),
        M = 2 * h(w);
       return n = M * r(R * (1 / 3)) - y, u = M * r((R + o) * (1 / 3)) - y, l = M * r((R + 2 * o) * (1 / 3)) - y, 0 <= n && n <= 1 ? 0 <= u && u <= 1 ? 0 <= l && l <= 1 ? a(n, u, l) : a(n, u) : 0 <= l && l <= 1 ? a(n, l) : n : 0 <= u && u <= 1 ? 0 <= l && l <= 1 ? a(u, l) : u : l
      }
      if (0 === b) return u = -(i = A < 0 ? h(-A) : -h(A)) - y, 0 <= (n = 2 * i - y) && n <= 1 ? 0 <= u && u <= 1 ? a(n, u) : n : u;
      var I = c(b);
      return n = (i = h(-A + I)) - h(A + I) - y
     })(t, e),
     n = 1 - i;
    return 0 * n * n * n + 3 * t[1] * i * n * n + 3 * t[3] * i * i * n + 1 * i * i * i
   }
   e.exports = {
    bezier: n,
    bezierByTime: u
   }
  }), {}],
  14: [(function(t, e, i) {
   var n = {
    constant: function() {
     return 0
    },
    linear: function(t) {
     return t
    },
    quadIn: function(t) {
     return t * t
    },
    quadOut: function(t) {
     return t * (2 - t)
    },
    quadInOut: function(t) {
     return (t *= 2) < 1 ? .5 * t * t : -.5 * (--t * (t - 2) - 1)
    },
    cubicIn: function(t) {
     return t * t * t
    },
    cubicOut: function(t) {
     return --t * t * t + 1
    },
    cubicInOut: function(t) {
     return (t *= 2) < 1 ? .5 * t * t * t : .5 * ((t -= 2) * t * t + 2)
    },
    quartIn: function(t) {
     return t * t * t * t
    },
    quartOut: function(t) {
     return 1 - --t * t * t * t
    },
    quartInOut: function(t) {
     return (t *= 2) < 1 ? .5 * t * t * t * t : -.5 * ((t -= 2) * t * t * t - 2)
    },
    quintIn: function(t) {
     return t * t * t * t * t
    },
    quintOut: function(t) {
     return --t * t * t * t * t + 1
    },
    quintInOut: function(t) {
     return (t *= 2) < 1 ? .5 * t * t * t * t * t : .5 * ((t -= 2) * t * t * t * t + 2)
    },
    sineIn: function(t) {
     return 1 - Math.cos(t * Math.PI / 2)
    },
    sineOut: function(t) {
     return Math.sin(t * Math.PI / 2)
    },
    sineInOut: function(t) {
     return .5 * (1 - Math.cos(Math.PI * t))
    },
    expoIn: function(t) {
     return 0 === t ? 0 : Math.pow(1024, t - 1)
    },
    expoOut: function(t) {
     return 1 === t ? 1 : 1 - Math.pow(2, -10 * t)
    },
    expoInOut: function(t) {
     return 0 === t ? 0 : 1 === t ? 1 : (t *= 2) < 1 ? .5 * Math.pow(1024, t - 1) : .5 * (2 - Math.pow(2, -10 * (t - 1)))
    },
    circIn: function(t) {
     return 1 - Math.sqrt(1 - t * t)
    },
    circOut: function(t) {
     return Math.sqrt(1 - --t * t)
    },
    circInOut: function(t) {
     return (t *= 2) < 1 ? -.5 * (Math.sqrt(1 - t * t) - 1) : .5 * (Math.sqrt(1 - (t -= 2) * t) + 1)
    },
    elasticIn: function(t) {
     var e, i = .1;
     return 0 === t ? 0 : 1 === t ? 1 : (!i || i < 1 ? (i = 1, e = .1) : e = .4 * Math.asin(1 / i) / (2 * Math.PI), -i * Math.pow(2, 10 * (t -= 1)) * Math.sin((t - e) * (2 * Math.PI) / .4))
    },
    elasticOut: function(t) {
     var e, i = .1;
     return 0 === t ? 0 : 1 === t ? 1 : (!i || i < 1 ? (i = 1, e = .1) : e = .4 * Math.asin(1 / i) / (2 * Math.PI), i * Math.pow(2, -10 * t) * Math.sin((t - e) * (2 * Math.PI) / .4) + 1)
    },
    elasticInOut: function(t) {
     var e, i = .1;
     return 0 === t ? 0 : 1 === t ? 1 : (!i || i < 1 ? (i = 1, e = .1) : e = .4 * Math.asin(1 / i) / (2 * Math.PI), (t *= 2) < 1 ? i * Math.pow(2, 10 * (t -= 1)) * Math.sin((t - e) * (2 * Math.PI) / .4) * -.5 : i * Math.pow(2, -10 * (t -= 1)) * Math.sin((t - e) * (2 * Math.PI) / .4) * .5 + 1)
    },
    backIn: function(t) {
     var e = 1.70158;
     return t * t * ((e + 1) * t - e)
    },
    backOut: function(t) {
     var e = 1.70158;
     return --t * t * ((e + 1) * t + e) + 1
    },
    backInOut: function(t) {
     var e = 2.5949095;
     return (t *= 2) < 1 ? t * t * ((e + 1) * t - e) * .5 : .5 * ((t -= 2) * t * ((e + 1) * t + e) + 2)
    },
    bounceOut: function(t) {
     return t < 1 / 2.75 ? 7.5625 * t * t : t < 2 / 2.75 ? 7.5625 * (t -= 1.5 / 2.75) * t + .75 : t < 2.5 / 2.75 ? 7.5625 * (t -= 2.25 / 2.75) * t + .9375 : 7.5625 * (t -= 2.625 / 2.75) * t + .984375
    },
    smooth: function(t) {
     return t <= 0 ? 0 : t >= 1 ? 1 : t * t * (3 - 2 * t)
    },
    fade: function(t) {
     return t <= 0 ? 0 : t >= 1 ? 1 : t * t * t * (t * (6 * t - 15) + 10)
    }
   };

   function r(t, e) {
    return function(i) {
     return i < .5 ? e(2 * i) / 2 : t(2 * i - 1) / 2 + .5
    }
   }
   n.quadOutIn = r(n.quadIn, n.quadOut), n.cubicOutIn = r(n.cubicIn, n.cubicOut), n.quartOutIn = r(n.quartIn, n.quartOut), n.quintOutIn = r(n.quintIn, n.quintOut), n.sineOutIn = r(n.sineIn, n.sineOut), n.expoOutIn = r(n.expoIn, n.expoOut), n.circOutIn = r(n.circIn, n.circOut), n.backOutIn = r(n.backIn, n.backOut), n.backOutIn = r(n.backIn, n.backOut), n.bounceIn = function(t) {
    return 1 - n.bounceOut(1 - t)
   }, n.bounceInOut = function(t) {
    return t < .5 ? .5 * n.bounceIn(2 * t) : .5 * n.bounceOut(2 * t - 1) + .5
   }, n.bounceOutIn = r(n.bounceIn, n.bounceOut), cc.Easing = e.exports = n
  }), {}],
  15: [(function(t, e, i) {
   t("./bezier"), t("./easing"), t("./types"), t("./motion-path-helper"), t("./animation-curves"), t("./animation-clip"), t("./animation-manager"), t("./animation-state"), t("./animation-animator")
  }), {
   "./animation-animator": 8,
   "./animation-clip": 9,
   "./animation-curves": 10,
   "./animation-manager": 11,
   "./animation-state": 12,
   "./bezier": 13,
   "./easing": 14,
   "./motion-path-helper": 16,
   "./types": 18
  }],
  16: [(function(t, e, i) {
   var n = t("./animation-curves").DynamicAnimCurve,
    r = t("./animation-curves").computeRatioByType,
    s = t("./bezier").bezier,
    a = t("../core/utils/binary-search").binarySearchEpsilon,
    o = cc.v2;

   function c(t) {
    this.points = t || [], this.beziers = [], this.ratios = [], this.progresses = [], this.length = 0, this.computeBeziers()
   }

   function h() {
    this.start = o(), this.end = o(), this.startCtrlPoint = o(), this.endCtrlPoint = o()
   }

   function u(t, e, i, s) {
    function h(t) {
     return t instanceof cc.Vec2 ? {
      in: t,
      pos: t,
      out: t
     } : Array.isArray(t) && 6 === t.length ? {
      in: o(t[2], t[3]),
      pos: o(t[0], t[1]),
      out: o(t[4], t[5])
     } : {
      in: cc.Vec2.ZERO,
      pos: cc.Vec2.ZERO,
      out: cc.Vec2.ZERO
     }
    }
    var u = e.values;
    if (0 !== t.length && 0 !== u.length)
     if (1 !== (u = u.map((function(t) {
       return o(t[0], t[1])
      }))).length) {
      for (var l = e.types, _ = e.ratios, d = e.values = [], f = e.types = [], p = e.ratios = [], m = 0, v = n.Linear, g = 0, y = t.length; g < y - 1; g++) {
       var T, E = t[g],
        x = _[g],
        C = _[g + 1] - x,
        A = u[g],
        b = u[g + 1],
        S = l[g],
        w = [],
        D = m / C,
        R = 1 / (C * i * s);
       if (E && E.length > 0) {
        var M = [];
        M.push(h(A));
        for (var I = 0, L = E.length; I < L; I++) {
         var O = h(E[I]);
         M.push(O)
        }
        M.push(h(b));
        var P = new c(M);
        P.computeBeziers();
        for (var F = P.progresses; 1 - D > 1e-6;) {
         var N, B, k, z;
         if ((T = r(T = D, S)) < 0) z = (0 - T) * (B = P.beziers[0]).getLength(), k = B.start.sub(B.endCtrlPoint).normalize(), N = B.start.add(k.mul(z));
         else if (T > 1) z = (T - 1) * (B = P.beziers[P.beziers.length - 1]).getLength(), k = B.end.sub(B.startCtrlPoint).normalize(), N = B.end.add(k.mul(z));
         else {
          var U = a(F, T);
          U < 0 && (U = ~U), T -= U > 0 ? F[U - 1] : 0, T /= P.ratios[U], N = P.beziers[U].getPointAt(T)
         }
         w.push(N), D += R
        }
       } else
        for (; 1 - D > 1e-6;) T = r(T = D, S), w.push(A.lerp(b, T)), D += R;
       v = "constant" === S ? S : n.Linear;
       for (I = 0, L = w.length; I < L; I++) {
        var W = x + m + R * I * C;
        H(w[I], v, W)
       }
       m = Math.abs(D - 1) > 1e-6 ? (D - 1) * C : 0
      }
      _[_.length - 1] !== p[p.length - 1] && H(u[u.length - 1], v, _[_.length - 1])
     } else e.values = u;

    function H(t, e, i) {
     d.push(t), f.push(e), p.push(i)
    }
   }
   c.prototype.computeBeziers = function() {
    var t;
    this.beziers.length = 0, this.ratios.length = 0, this.progresses.length = 0, this.length = 0;
    for (var e = 1; e < this.points.length; e++) {
     var i = this.points[e - 1],
      n = this.points[e];
     (t = new h).start = i.pos, t.startCtrlPoint = i.out, t.end = n.pos, t.endCtrlPoint = n.in, this.beziers.push(t), this.length += t.getLength()
    }
    var r = 0;
    for (e = 0; e < this.beziers.length; e++) t = this.beziers[e], this.ratios[e] = t.getLength() / this.length, this.progresses[e] = r += this.ratios[e];
    return this.beziers
   }, h.prototype.getPointAt = function(t) {
    var e = this.getUtoTmapping(t);
    return this.getPoint(e)
   }, h.prototype.getPoint = function(t) {
    var e = s(this.start.x, this.startCtrlPoint.x, this.endCtrlPoint.x, this.end.x, t),
     i = s(this.start.y, this.startCtrlPoint.y, this.endCtrlPoint.y, this.end.y, t);
    return new o(e, i)
   }, h.prototype.getLength = function() {
    var t = this.getLengths();
    return t[t.length - 1]
   }, h.prototype.getLengths = function(t) {
    if (t || (t = this.__arcLengthDivisions ? this.__arcLengthDivisions : 200), this.cacheArcLengths && this.cacheArcLengths.length === t + 1) return this.cacheArcLengths;
    var e, i, n = [],
     r = this.getPoint(0),
     s = o(),
     a = 0;
    for (n.push(0), i = 1; i <= t; i++) e = this.getPoint(i / t), s.x = r.x - e.x, s.y = r.y - e.y, a += s.mag(), n.push(a), r = e;
    return this.cacheArcLengths = n, n
   }, h.prototype.getUtoTmapping = function(t, e) {
    var i, n = this.getLengths(),
     r = 0,
     s = n.length;
    i = e || t * n[s - 1];
    for (var a, o = 0, c = s - 1; o <= c;)
     if ((a = n[r = Math.floor(o + (c - o) / 2)] - i) < 0) o = r + 1;
     else {
      if (!(a > 0)) {
       c = r;
       break
      }
      c = r - 1
     } if (n[r = c] === i) return r / (s - 1);
    var h = n[r];
    return (r + (i - h) / (n[r + 1] - h)) / (s - 1)
   }, e.exports = {
    sampleMotionPaths: u,
    Curve: c,
    Bezier: h
   }
  }), {
   "../core/utils/binary-search": 181,
   "./animation-curves": 10,
   "./bezier": 13
  }],
  17: [(function(t, e, i) {
   var n = cc.js,
    r = t("../core/CCDebug");

   function s() {
    this._isPlaying = !1, this._isPaused = !1, this._stepOnce = !1
   }
   var a = s.prototype;
   n.get(a, "isPlaying", (function() {
    return this._isPlaying
   }), !0), n.get(a, "isPaused", (function() {
    return this._isPaused
   }), !0);
   var o = function() {};
   a.onPlay = o, a.onPause = o, a.onResume = o, a.onStop = o, a.onError = o, a.play = function() {
    this._isPlaying ? this._isPaused ? (this._isPaused = !1, this.onResume()) : this.onError(r.getError(3912)) : (this._isPlaying = !0, this.onPlay())
   }, a.stop = function() {
    this._isPlaying && (this._isPlaying = !1, this.onStop(), this._isPaused = !1)
   }, a.pause = function() {
    this._isPlaying && !this._isPaused && (this._isPaused = !0, this.onPause())
   }, a.resume = function() {
    this._isPlaying && this._isPaused && (this._isPaused = !1, this.onResume())
   }, a.step = function() {
    this.pause(), this._stepOnce = !0, this._isPlaying || this.play()
   }, e.exports = s
  }), {
   "../core/CCDebug": 21
  }],
  18: [(function(t, e, i) {
   var n = {
     Loop: 2,
     ShouldWrap: 4,
     PingPong: 22,
     Reverse: 36
    },
    r = cc.Enum({
     Default: 0,
     Normal: 1,
     Reverse: n.Reverse,
     Loop: n.Loop,
     LoopReverse: n.Loop | n.Reverse,
     PingPong: n.PingPong,
     PingPongReverse: n.PingPong | n.Reverse
    });

   function s(t) {
    t ? this.set(t) : (this.ratio = 0, this.time = 0, this.direction = 1, this.stopped = !0, this.iterations = 0, this.frameIndex = void 0)
   }
   cc.WrapMode = r, s.prototype.set = function(t) {
    this.ratio = t.ratio, this.time = t.time, this.direction = t.direction, this.stopped = t.stopped, this.iterations = t.iterations, this.frameIndex = t.frameIndex
   }, e.exports = {
    WrapModeMask: n,
    WrapMode: r,
    WrappedInfo: s
   }
  }), {}],
  19: [(function(t, e, i) {
   var n = t("../core/event/event-target"),
    r = t("../core/platform/CCSys"),
    s = (t("../core/assets/CCAudioClip").LoadMode, []),
    a = function(t) {
     n.call(this), this._src = t, this._element = null, this.id = 0, this._volume = 1, this._loop = !1, this._nextTime = 0, this._state = a.State.INITIALZING, this._onended = function() {
      this._state = a.State.STOPPED, this.emit("ended")
     }.bind(this)
    };
   cc.js.extend(a, n), a.State = {
    ERROR: -1,
    INITIALZING: 0,
    PLAYING: 1,
    PAUSED: 2,
    STOPPED: 3
   }, (function(t) {
    t._bindEnded = function(t) {
     t = t || this._onended;
     var e = this._element;
     this._src && e instanceof HTMLAudioElement ? e.addEventListener("ended", t) : e.onended = t
    }, t._unbindEnded = function() {
     var t = this._element;
     t instanceof HTMLAudioElement ? t.removeEventListener("ended", this._onended) : t && (t.onended = null)
    }, t._onLoaded = function() {
     var t = this._src._nativeAsset;
     t instanceof HTMLAudioElement ? (this._element || (this._element = document.createElement("audio")), this._element.src = t.src) : this._element = new o(t, this), this.setVolume(this._volume), this.setLoop(this._loop), 0 !== this._nextTime && this.setCurrentTime(this._nextTime), this._state === a.State.PLAYING ? this.play() : this._state = a.State.INITIALZING
    }, t.play = function() {
     this._state = a.State.PLAYING, this._element && (this._bindEnded(), this._element.play())
    }, t.destroy = function() {
     this._element && this._element.destroy(), this._element = null
    }, t.pause = function() {
     this._element && this._state === a.State.PLAYING && (this._unbindEnded(), this._element.pause(), this._state = a.State.PAUSED)
    }, t.resume = function() {
     this._element && this._state === a.State.PAUSED && (this._bindEnded(), this._element.play(), this._state = a.State.PLAYING)
    }, t.stop = function() {
     if (this._element) {
      this._element.pause();
      try {
       this._element.currentTime = 0
      } catch (t) {}
      for (var t = 0; t < s.length; t++)
       if (s[t].instance === this) {
        s.splice(t, 1);
        break
       } this._unbindEnded(), this.emit("stop"), this._state = a.State.STOPPED
     }
    }, t.setLoop = function(t) {
     this._loop = t, this._element && (this._element.loop = t)
    }, t.getLoop = function() {
     return this._loop
    }, t.setVolume = function(t) {
     this._volume = t, this._element && (this._element.volume = t)
    }, t.getVolume = function() {
     return this._volume
    }, t.setCurrentTime = function(t) {
     var e = this;
     if (this._element) {
      this._nextTime = 0;
      try {
       this._element.currentTime = t
      } catch (i) {
       (function() {
        var i = e._element;
        i.addEventListener && (function() {
         var e = function() {
          i.removeEventListener("loadedmetadata", e), i.currentTime = t
         };
         i.addEventListener("loadedmetadata", e)
        })()
       })()
      }
     } else this._nextTime = t
    }, t.getCurrentTime = function() {
     return this._element ? this._element.currentTime : 0
    }, t.getDuration = function() {
     return this._element ? this._element.duration : 0
    }, t.getState = function() {
     return this._state
    }, t.__defineGetter__("src", (function() {
     return this._src
    })), t.__defineSetter__("src", (function(t) {
     var e = this;
     return this._unbindEnded(), t ? (this._src = t, t.loaded ? this._onLoaded() : (function() {
      var i = e;
      t.once("load", (function() {
       t === i._src && i._onLoaded()
      })), cc.loader.load({
       url: t.nativeUrl,
       skips: ["Loader"]
      }, (function(e, i) {
       e ? cc.error(e) : t.loaded || (t._nativeAsset = i)
      }))
     })()) : (this._src = null, this._element instanceof HTMLAudioElement ? this._element.src = "" : this._element = null, this._state = a.State.INITIALZING), t
    })), t.__defineGetter__("paused", (function() {
     return !this._element || this._element.paused
    }))
   })(a.prototype);
   var o = function(t, e) {
    this._audio = e, this._context = r.__audioSupport.context, this._buffer = t, this._gainObj = this._context.createGain(), this._volume = 1, this._gainObj.gain.setTargetAtTime ? this._gainObj.gain.setTargetAtTime(this._volume, this._context.currentTime, .01) : this._gainObj.gain.value = 1, this._gainObj.connect(this._context.destination), this._loop = !1, this._startTime = -1, this._currentSource = null, this.playedLength = 0, this._currextTimer = null, this._endCallback = function() {
     this.onended && this.onended(this)
    }.bind(this)
   };
   (function(t) {
    t.play = function(t) {
     var e = this;
     this._currentSource && !this.paused && (this._currentSource.onended = null, this._currentSource.stop(0), this.playedLength = 0);
     var i = this._context.createBufferSource();
     i.buffer = this._buffer, i.connect(this._gainObj), i.loop = this._loop, this._startTime = this._context.currentTime, (t = t || this.playedLength) && (this._startTime -= t);
     var n = this._buffer.duration,
      r = t,
      s = void 0;
     this._loop ? i.start ? i.start(0, r) : i.notoGrainOn ? i.noteGrainOn(0, r) : i.noteOn(0, r) : (s = n - t, i.start ? i.start(0, r, s) : i.notoGrainOn ? i.noteGrainOn(0, r, s) : i.noteOn(0, r, s)), this._currentSource = i, i.onended = this._endCallback, i.context.state && "suspended" !== i.context.state || 0 !== this._context.currentTime || (clearTimeout(e._currextTimer), e._currextTimer = setTimeout((function() {}), 10))
    }, t.pause = function() {
     if (clearTimeout(this._currextTimer), !this.paused) {
      this.playedLength = this._context.currentTime - this._startTime, this.playedLength %= this._buffer.duration;
      var t = this._currentSource;
      this._currentSource = null, this._startTime = -1, t && t.stop(0)
     }
    }, t.__defineGetter__("paused", (function() {
     return (!this._currentSource || !this._currentSource.loop) && (-1 === this._startTime || this._context.currentTime - this._startTime > this._buffer.duration)
    })), t.__defineGetter__("loop", (function() {
     return this._loop
    })), t.__defineSetter__("loop", (function(t) {
     return this._currentSource && (this._currentSource.loop = t), this._loop = t
    })), t.__defineGetter__("volume", (function() {
     return this._volume
    })), t.__defineSetter__("volume", (function(t) {
     return this._volume = t, this._gainObj.gain.setTargetAtTime ? this._gainObj.gain.setTargetAtTime(this._volume, this._context.currentTime, .01) : this._volume.gain.value = t, r.os === r.OS_IOS && !this.paused && this._currentSource && (this._currentSource.onended = null, this.pause(), this.play()), t
    })), t.__defineGetter__("currentTime", (function() {
     return this.paused ? this.playedLength : (this.playedLength = this._context.currentTime - this._startTime, this.playedLength %= this._buffer.duration, this.playedLength)
    })), t.__defineSetter__("currentTime", (function(t) {
     return this.paused ? this.playedLength = t : (this.pause(), this.playedLength = t, this.play()), t
    })), t.__defineGetter__("duration", (function() {
     return this._buffer.duration
    }))
   })(o.prototype), e.exports = cc.Audio = a
  }), {
   "../core/assets/CCAudioClip": 29,
   "../core/event/event-target": 77,
   "../core/platform/CCSys": 117
  }],
  20: [(function(t, e, i) {
   var n = t("./CCAudio"),
    r = t("../core/assets/CCAudioClip"),
    s = cc.js,
    a = 0,
    o = s.createMap(!0),
    c = {},
    h = [],
    u = function(t) {
     var e = a++,
      i = c[t];
     if (i || (i = c[t] = []), d._maxAudioInstance <= i.length) {
      var r = i.shift();
      l(r).stop()
     }
     var s = h.pop() || new n,
      u = function() {
       if (l(this.id)) {
        delete o[this.id];
        var t = i.indexOf(this.id);
        cc.js.array.fastRemoveAt(i, t)
       }(function(t) {
        t._finishCallback = null, h.length < 32 ? (t.off("ended"), t.off("stop"), t.src = null, h.push(t)) : t.destroy()
       })(this)
      };
     return s.on("ended", (function() {
      this._finishCallback && this._finishCallback(), u.call(this)
     }), s), s.on("stop", u, s), s.id = e, o[e] = s, i.push(e), s
    },
    l = function(t) {
     return o[t]
    },
    _ = function(t) {
     return void 0 === t ? t = 1 : "string" == typeof t && (t = Number.parseFloat(t)), t
    },
    d = {
     AudioState: n.State,
     _maxWebAudioSize: 2097152,
     _maxAudioInstance: 24,
     _id2audio: o,
     play: function(t, e, i) {
      var n, s = t;
      if ("string" == typeof t) cc.warnID(8401, "cc.audioEngine", "cc.AudioClip", "AudioClip", "cc.AudioClip", "audio"), n = u(s = t), r._loadByUrl(s, (function(t, e) {
       e && (n.src = e)
      }));
      else {
       if (!t) return;
       s = t.nativeUrl, (n = u(s)).src = t
      }
      return n.setLoop(e || !1), i = _(i), n.setVolume(i), n.play(), n.id
     },
     setLoop: function(t, e) {
      var i = l(t);
      i && i.setLoop && i.setLoop(e)
     },
     isLoop: function(t) {
      var e = l(t);
      return !(!e || !e.getLoop) && e.getLoop()
     },
     setVolume: function(t, e) {
      var i = l(t);
      i && i.setVolume(e)
     },
     getVolume: function(t) {
      var e = l(t);
      return e ? e.getVolume() : 1
     },
     setCurrentTime: function(t, e) {
      var i = l(t);
      return !!i && (i.setCurrentTime(e), !0)
     },
     getCurrentTime: function(t) {
      var e = l(t);
      return e ? e.getCurrentTime() : 0
     },
     getDuration: function(t) {
      var e = l(t);
      return e ? e.getDuration() : 0
     },
     getState: function(t) {
      var e = l(t);
      return e ? e.getState() : this.AudioState.ERROR
     },
     setFinishCallback: function(t, e) {
      var i = l(t);
      i && (i._finishCallback = e)
     },
     pause: function(t) {
      var e = l(t);
      return !!e && (e.pause(), !0)
     },
     _pauseIDCache: [],
     pauseAll: function() {
      for (var t in o) {
       var e = o[t];
       e.getState() === n.State.PLAYING && (this._pauseIDCache.push(t), e.pause())
      }
     },
     resume: function(t) {
      var e = l(t);
      e && e.resume()
     },
     resumeAll: function() {
      for (var t = 0; t < this._pauseIDCache.length; ++t) {
       var e = this._pauseIDCache[t],
        i = l(e);
       i && i.resume()
      }
      this._pauseIDCache.length = 0
     },
     stop: function(t) {
      var e = l(t);
      return !!e && (e.stop(), !0)
     },
     stopAll: function() {
      for (var t in o) {
       var e = o[t];
       e && e.stop()
      }
     },
     setMaxAudioInstance: function(t) {
      this._maxAudioInstance = t
     },
     getMaxAudioInstance: function() {
      return this._maxAudioInstance
     },
     uncache: function(t) {
      var e = t;
      if ("string" == typeof t) cc.warnID(8401, "cc.audioEngine", "cc.AudioClip", "AudioClip", "cc.AudioClip", "audio"), e = t;
      else {
       if (!t) return;
       e = t.nativeUrl
      }
      var i = c[e];
      if (i)
       for (; i.length > 0;) {
        var n = i.pop(),
         r = o[n];
        r && (r.stop(), delete o[n])
       }
     },
     uncacheAll: function() {
      this.stopAll();
      var t = void 0;
      for (var e in o)(t = o[e]) && t.destroy();
      for (; t = h.pop();) t.destroy();
      o = s.createMap(!0), c = {}
     },
     getProfile: function(t) {},
     preload: function(t, e) {
      cc.loader.load(t, e && function(t) {
       t || e()
      })
     },
     setMaxWebAudioSize: function(t) {
      this._maxWebAudioSize = 1024 * t
     },
     _breakCache: null,
     _break: function() {
      for (var t in this._breakCache = [], o) {
       var e = o[t];
       e.getState() === n.State.PLAYING && (this._breakCache.push(t), e.pause())
      }
     },
     _restore: function() {
      if (this._breakCache) {
       for (; this._breakCache.length > 0;) {
        var t = this._breakCache.pop(),
         e = l(t);
        e && e.resume && e.resume()
       }
       this._breakCache = null
      }
     },
     _music: {
      id: -1,
      loop: !1,
      volume: 1
     },
     _effect: {
      volume: 1,
      pauseCache: []
     },
     playMusic: function(t, e) {
      var i = this._music;
      return this.stop(i.id), i.id = this.play(t, e, i.volume), i.loop = e, i.id
     },
     stopMusic: function() {
      this.stop(this._music.id)
     },
     pauseMusic: function() {
      return this.pause(this._music.id), this._music.id
     },
     resumeMusic: function() {
      return this.resume(this._music.id), this._music.id
     },
     getMusicVolume: function() {
      return this._music.volume
     },
     setMusicVolume: function(t) {
      t = _(t);
      var e = this._music;
      return e.volume = t, this.setVolume(e.id, e.volume), e.volume
     },
     isMusicPlaying: function() {
      return this.getState(this._music.id) === this.AudioState.PLAYING
     },
     playEffect: function(t, e) {
      return this.play(t, e || !1, this._effect.volume)
     },
     setEffectsVolume: function(t) {
      t = _(t);
      var e = this._music.id;
      for (var i in this._effect.volume = t, o) {
       var n = o[i];
       n && n.id !== e && d.setVolume(i, t)
      }
     },
     getEffectsVolume: function() {
      return this._effect.volume
     },
     pauseEffect: function(t) {
      return this.pause(t)
     },
     pauseAllEffects: function() {
      var t = this._music.id,
       e = this._effect;
      for (var i in e.pauseCache.length = 0, o) {
       var n = o[i];
       if (n && n.id !== t) n.getState() === this.AudioState.PLAYING && (e.pauseCache.push(i), n.pause())
      }
     },
     resumeEffect: function(t) {
      this.resume(t)
     },
     resumeAllEffects: function() {
      for (var t = this._effect.pauseCache, e = 0; e < t.length; ++e) {
       var i = t[e],
        n = o[i];
       n && n.resume()
      }
     },
     stopEffect: function(t) {
      return this.stop(t)
     },
     stopAllEffects: function() {
      var t = this._music.id;
      for (var e in o) {
       var i = o[e];
       if (i && i.id !== t) i.getState() === d.AudioState.PLAYING && i.stop()
      }
     }
    };
   e.exports = cc.audioEngine = d
  }), {
   "../core/assets/CCAudioClip": 29,
   "./CCAudio": 19
  }],
  21: [(function(t, e, i) {
   t("../../DebugInfos");
   var n = "https://github.com/cocos-creator/engine/blob/master/EngineErrorMap.md",
    r = void 0;
   cc.log = cc.warn = cc.error = cc.assert = console.log;

   function s(t) {
    return function() {
     var e = arguments[0],
      i = t + " " + e + ", please go to " + n + "#" + e + " to see details.";
     if (1 === arguments.length) return i;
     if (2 === arguments.length) return i + " Arguments: " + arguments[1];
     var r = cc.js.shiftArguments.apply(null, arguments);
     return i + " Arguments: " + r.join(", ")
    }
   }
   cc._throw = function(t) {
    var e = t.stack;
    e ? cc.error(e) : cc.error(t)
   };
   var a = s("Log");
   cc.logID = function() {
    cc.log(a.apply(null, arguments))
   };
   var o = s("Warning");
   cc.warnID = function() {
    cc.warn(o.apply(null, arguments))
   };
   var c = s("Error");
   cc.errorID = function() {
    cc.error(c.apply(null, arguments))
   };
   var h = s("Assert");
   cc.assertID = function(t) {
    "use strict";
    t || cc.assert(!1, h.apply(null, cc.js.shiftArguments.apply(null, arguments)))
   };
   var u = cc.Enum({
    NONE: 0,
    INFO: 1,
    WARN: 2,
    ERROR: 3,
    INFO_FOR_WEB_PAGE: 4,
    WARN_FOR_WEB_PAGE: 5,
    ERROR_FOR_WEB_PAGE: 6
   });
   e.exports = cc.debug = {
    DebugMode: u,
    _resetDebugSetting: function(t) {
     cc.log = cc.warn = cc.error = cc.assert = function() {}, t !== u.NONE && (t > u.ERROR ? (function() {
      function e(t) {
       if (cc.game.canvas) {
        if (!r) {
         var e = document.createElement("Div");
         e.setAttribute("id", "logInfoDiv"), e.setAttribute("width", "200"), e.setAttribute("height", cc.game.canvas.height);
         var i = e.style;
         i.zIndex = "99999", i.position = "absolute", i.top = i.left = "0", (r = document.createElement("textarea")).setAttribute("rows", "20"), r.setAttribute("cols", "30"), r.setAttribute("disabled", "true");
         var n = r.style;
         n.backgroundColor = "transparent", n.borderBottom = "1px solid #cccccc", n.borderTopWidth = n.borderLeftWidth = n.borderRightWidth = "0px", n.borderTopStyle = n.borderLeftStyle = n.borderRightStyle = "none", n.padding = "0px", n.margin = 0, e.appendChild(r), cc.game.canvas.parentNode.appendChild(e)
        }
        r.value = r.value + t + "\r\n", r.scrollTop = r.scrollHeight
       }
      }
      cc.error = function() {
       e("ERROR :  " + cc.js.formatStr.apply(null, arguments))
      }, cc.assert = function(t, i) {
       "use strict";
       !t && i && e("ASSERT: " + (i = cc.js.formatStr.apply(null, cc.js.shiftArguments.apply(null, arguments))))
      }, t !== u.ERROR_FOR_WEB_PAGE && (cc.warn = function() {
       e("WARN :  " + cc.js.formatStr.apply(null, arguments))
      }), t === u.INFO_FOR_WEB_PAGE && (cc.log = function() {
       e(cc.js.formatStr.apply(null, arguments))
      })
     })() : console && console.log.apply && (console.error || (console.error = console.log), console.warn || (console.warn = console.log), console.error.bind ? cc.error = console.error.bind(console) : cc.error = function() {
      return console.error.apply(console, arguments)
     }, cc.assert = function(t, e) {
      if (!t) throw e && (e = cc.js.formatStr.apply(null, cc.js.shiftArguments.apply(null, arguments))), new Error(e)
     }), t !== u.ERROR && (console.warn.bind ? cc.warn = console.warn.bind(console) : cc.warn = function() {
      return console.warn.apply(console, arguments)
     }), t === u.INFO && (console.log.bind ? cc.log = console.log.bind(console) : cc.log = function() {
      return console.log.apply(console, arguments)
     }))
    },
    getError: s("ERROR"),
    isDisplayStats: function() {
     return !!cc.profiler && cc.profiler.isShowingStats()
    },
    setDisplayStats: function(t) {
     cc.profiler && (t ? cc.profiler.showStats() : cc.profiler.hideStats(), cc.game.config.showFPS = !!t)
    }
   }
  }), {
   "../../DebugInfos": void 0
  }],
  22: [(function(t, e, i) {
   var n = t("./event/event-target"),
    r = t("./load-pipeline/auto-release-utils"),
    s = t("./component-scheduler"),
    a = t("./node-activator"),
    o = t("./platform/CCObject"),
    c = t("./CCGame"),
    h = t("./renderer"),
    u = t("./event-manager"),
    l = t("./CCScheduler");
   cc.Director = function() {
    n.call(this), this.invalid = !1, this._paused = !1, this._purgeDirectorInNextLoop = !1, this._winSizeInPoints = null, this._loadingScene = "", this._scene = null, this._totalFrames = 0, this._lastUpdate = 0, this._deltaTime = 0, this._scheduler = null, this._compScheduler = null, this._nodeActivator = null, this._actionManager = null;
    var t = this;
    c.on(c.EVENT_SHOW, (function() {
     t._lastUpdate = performance.now()
    })), c.once(c.EVENT_ENGINE_INITED, this.init, this)
   }, cc.Director.prototype = {
    constructor: cc.Director,
    init: function() {
     return this._totalFrames = 0, this._lastUpdate = performance.now(), this._paused = !1, this._purgeDirectorInNextLoop = !1, this._winSizeInPoints = cc.size(0, 0), this._scheduler = new l, cc.ActionManager ? (this._actionManager = new cc.ActionManager, this._scheduler.scheduleUpdate(this._actionManager, l.PRIORITY_SYSTEM, !1)) : this._actionManager = null, this.sharedInit(), !0
    },
    sharedInit: function() {
     this._compScheduler = new s, this._nodeActivator = new a, u && u.setEnabled(!0), cc.AnimationManager ? (this._animationManager = new cc.AnimationManager, this._scheduler.scheduleUpdate(this._animationManager, l.PRIORITY_SYSTEM, !1)) : this._animationManager = null, cc.CollisionManager ? (this._collisionManager = new cc.CollisionManager, this._scheduler.scheduleUpdate(this._collisionManager, l.PRIORITY_SYSTEM, !1)) : this._collisionManager = null, cc.PhysicsManager ? (this._physicsManager = new cc.PhysicsManager, this._scheduler.scheduleUpdate(this._physicsManager, l.PRIORITY_SYSTEM, !1)) : this._physicsManager = null, cc._widgetManager && cc._widgetManager.init(this), cc.loader.init(this)
    },
    calculateDeltaTime: function() {
     var t = performance.now();
     this._deltaTime = (t - this._lastUpdate) / 1e3, this._lastUpdate = t
    },
    convertToGL: function(t) {
     var e = c.container,
      i = cc.view,
      n = e.getBoundingClientRect(),
      r = n.left + window.pageXOffset - e.clientLeft,
      s = n.top + window.pageYOffset - e.clientTop,
      a = i._devicePixelRatio * (t.x - r),
      o = i._devicePixelRatio * (s + n.height - t.y);
     return i._isRotated ? cc.v2(i._viewportRect.width - o, a) : cc.v2(a, o)
    },
    convertToUI: function(t) {
     var e = c.container,
      i = cc.view,
      n = e.getBoundingClientRect(),
      r = n.left + window.pageXOffset - e.clientLeft,
      s = n.top + window.pageYOffset - e.clientTop,
      a = cc.v2(0, 0);
     return i._isRotated ? (a.x = r + t.y / i._devicePixelRatio, a.y = s + n.height - (i._viewportRect.width - t.x) / i._devicePixelRatio) : (a.x = r + t.x * i._devicePixelRatio, a.y = s + n.height - t.y * i._devicePixelRatio), a
    },
    end: function() {
     this._purgeDirectorInNextLoop = !0
    },
    getWinSize: function() {
     return cc.size(cc.winSize)
    },
    getWinSizeInPixels: function() {
     return cc.size(cc.winSize)
    },
    pause: function() {
     this._paused || (this._paused = !0)
    },
    purgeCachedData: function() {
     cc.loader.releaseAll()
    },
    purgeDirector: function() {
     this._scheduler.unscheduleAll(), this._compScheduler.unscheduleAll(), this._nodeActivator.reset(), u && u.setEnabled(!1), cc.renderer.clear(), cc.isValid(this._scene) && this._scene.destroy(), this._scene = null, this.stopAnimation(), cc.loader.releaseAll()
    },
    reset: function() {
     this.purgeDirector(), u && u.setEnabled(!0), this._actionManager && this._scheduler.scheduleUpdate(this._actionManager, cc.Scheduler.PRIORITY_SYSTEM, !1), this._animationManager && this._scheduler.scheduleUpdate(this._animationManager, cc.Scheduler.PRIORITY_SYSTEM, !1), this._collisionManager && this._scheduler.scheduleUpdate(this._collisionManager, cc.Scheduler.PRIORITY_SYSTEM, !1), this._physicsManager && this._scheduler.scheduleUpdate(this._physicsManager, cc.Scheduler.PRIORITY_SYSTEM, !1), this.startAnimation()
    },
    runSceneImmediate: function(t, e, i) {
     cc.assertID(t instanceof cc.Scene, 1216), t._load();
     for (var n = Object.keys(c._persistRootNodes).map((function(t) {
       return c._persistRootNodes[t]
      })), s = 0; s < n.length; s++) {
      var a = n[s],
       h = t.getChildByUuid(a.uuid);
      if (h) {
       var u = h.getSiblingIndex();
       h._destroyImmediate(), t.insertChild(a, u)
      } else a.parent = t
     }
     var l = this._scene,
      _ = l && l.autoReleaseAssets && l.dependAssets;
     r.autoRelease(_, t.dependAssets, n), cc.isValid(l) && l.destroy(), this._scene = null, o._deferredDestroy(), e && e(), this.emit(cc.Director.EVENT_BEFORE_SCENE_LAUNCH, t), this._scene = t, t._activate(), this.startAnimation(), i && i(null, t), this.emit(cc.Director.EVENT_AFTER_SCENE_LAUNCH, t)
    },
    runScene: function(t, e, i) {
     cc.assertID(t, 1205), cc.assertID(t instanceof cc.Scene, 1216), t._load(), this.once(cc.Director.EVENT_AFTER_UPDATE, (function() {
      this.runSceneImmediate(t, e, i)
     }), this)
    },
    _getSceneUuid: function(t) {
     var e = c._sceneInfos;
     if ("string" == typeof t) {
      t.endsWith(".fire") || (t += ".fire"), "/" === t[0] || t.startsWith("db://") || (t = "/" + t);
      for (var i = 0; i < e.length; i++) {
       var n = e[i];
       if (n.url.endsWith(t)) return n
      }
     } else if ("number" == typeof t) {
      if (0 <= t && t < e.length) return e[t];
      cc.errorID(1206, t)
     } else cc.errorID(1207, t);
     return null
    },
    loadScene: function(t, e, i) {
     if (this._loadingScene) return cc.errorID(1208, t, this._loadingScene), !1;
     var n = this._getSceneUuid(t);
     if (n) {
      var r = n.uuid;
      return this.emit(cc.Director.EVENT_BEFORE_SCENE_LOADING, t), this._loadingScene = t, this._loadSceneByUuid(r, e, i), !0
     }
     return cc.errorID(1209, t), !1
    },
    preloadScene: function(t, e, i) {
     void 0 === i && (i = e, e = null);
     var n = this._getSceneUuid(t);
     if (n) this.emit(cc.Director.EVENT_BEFORE_SCENE_LOADING, t), cc.loader.load({
      uuid: n.uuid,
      type: "uuid"
     }, e, (function(e, n) {
      e && cc.errorID(1210, t, e.message), i && i(e, n)
     }));
     else {
      var r = 'Can not preload the scene "' + t + '" because it is not in the build settings.';
      i(new Error(r)), cc.error("preloadScene: " + r)
     }
    },
    _loadSceneByUuid: function(t, e, i, n) {
     console.time("LoadScene " + t), cc.AssetLibrary.loadAsset(t, (function(n, r) {
      console.timeEnd("LoadScene " + t);
      var s = cc.director;
      if (s._loadingScene = "", n) n = "Failed to load scene: " + n, cc.error(n);
      else {
       if (r instanceof cc.SceneAsset) {
        var a = r.scene;
        return a._id = r._uuid, a._name = r._name, void s.runSceneImmediate(a, i, e)
       }
       n = "The asset " + t + " is not a scene", cc.error(n)
      }
      e && e(n)
     }))
    },
    resume: function() {
     this._paused && (this._lastUpdate = performance.now(), this._lastUpdate || cc.logID(1200), this._paused = !1, this._deltaTime = 0)
    },
    setDepthTest: function(t) {
     cc.Camera.main && (cc.Camera.main.depth = !!t)
    },
    setClearColor: function(t) {
     cc.Camera.main && (cc.Camera.main.backgroundColor = t)
    },
    getRunningScene: function() {
     return this._scene
    },
    getScene: function() {
     return this._scene
    },
    getAnimationInterval: function() {
     return 1e3 / c.getFrameRate()
    },
    setAnimationInterval: function(t) {
     c.setFrameRate(Math.round(1e3 / t))
    },
    getDeltaTime: function() {
     return this._deltaTime
    },
    getTotalFrames: function() {
     return this._totalFrames
    },
    isPaused: function() {
     return this._paused
    },
    getScheduler: function() {
     return this._scheduler
    },
    setScheduler: function(t) {
     this._scheduler !== t && (this._scheduler = t)
    },
    getActionManager: function() {
     return this._actionManager
    },
    setActionManager: function(t) {
     this._actionManager !== t && (this._actionManager && this._scheduler.unscheduleUpdate(this._actionManager), this._actionManager = t, this._scheduler.scheduleUpdate(this._actionManager, cc.Scheduler.PRIORITY_SYSTEM, !1))
    },
    getAnimationManager: function() {
     return this._animationManager
    },
    getCollisionManager: function() {
     return this._collisionManager
    },
    getPhysicsManager: function() {
     return this._physicsManager
    },
    startAnimation: function() {
     this.invalid = !1, this._lastUpdate = performance.now()
    },
    stopAnimation: function() {
     this.invalid = !0
    },
    mainLoop: function() {
     this._purgeDirectorInNextLoop ? (this._purgeDirectorInNextLoop = !1, this.purgeDirector()) : this.invalid || (this.calculateDeltaTime(), this._paused || (this.emit(cc.Director.EVENT_BEFORE_UPDATE), this._compScheduler.startPhase(), this._compScheduler.updatePhase(this._deltaTime), this._scheduler.update(this._deltaTime), this._compScheduler.lateUpdatePhase(this._deltaTime), this.emit(cc.Director.EVENT_AFTER_UPDATE), o._deferredDestroy()), this.emit(cc.Director.EVENT_BEFORE_DRAW), h.render(this._scene), this.emit(cc.Director.EVENT_AFTER_DRAW), u.frameUpdateListeners(), this._totalFrames++)
    },
    __fastOn: function(t, e, i) {
     this.add(t, e, i)
    },
    __fastOff: function(t, e, i) {
     this.remove(t, e, i)
    }
   }, cc.js.addon(cc.Director.prototype, n.prototype), cc.Director.EVENT_PROJECTION_CHANGED = "director_projection_changed", cc.Director.EVENT_BEFORE_SCENE_LOADING = "director_before_scene_loading", cc.Director.EVENT_BEFORE_SCENE_LAUNCH = "director_before_scene_launch", cc.Director.EVENT_AFTER_SCENE_LAUNCH = "director_after_scene_launch", cc.Director.EVENT_BEFORE_UPDATE = "director_before_update", cc.Director.EVENT_AFTER_UPDATE = "director_after_update", cc.Director.EVENT_BEFORE_VISIT = "director_before_draw", cc.Director.EVENT_AFTER_VISIT = "director_before_draw", cc.Director.EVENT_BEFORE_DRAW = "director_before_draw", cc.Director.EVENT_AFTER_DRAW = "director_after_draw", cc.Director.PROJECTION_2D = 0, cc.Director.PROJECTION_3D = 1, cc.Director.PROJECTION_CUSTOM = 3, cc.Director.PROJECTION_DEFAULT = cc.Director.PROJECTION_2D, cc.director = new cc.Director, e.exports = cc.director
  }), {
   "./CCGame": 23,
   "./CCScheduler": 27,
   "./component-scheduler": 48,
   "./event-manager": 75,
   "./event/event-target": 77,
   "./load-pipeline/auto-release-utils": 90,
   "./node-activator": 105,
   "./platform/CCObject": 114,
   "./renderer": 149
  }],
  23: [(function(t, e, i) {
   var n = t("./event/event-target");
   t("../audio/CCAudioEngine");
   var r = t("./CCDebug"),
    s = t("./renderer/index.js"),
    a = t("./platform/CCInputManager"),
    o = t("../core/renderer/utils/dynamic-atlas/manager"),
    c = {
     EVENT_HIDE: "game_on_hide",
     EVENT_SHOW: "game_on_show",
     EVENT_RESTART: "game_on_restart",
     EVENT_GAME_INITED: "game_inited",
     EVENT_ENGINE_INITED: "engine_inited",
     EVENT_RENDERER_INITED: "engine_inited",
     RENDER_TYPE_CANVAS: 0,
     RENDER_TYPE_WEBGL: 1,
     RENDER_TYPE_OPENGL: 2,
     _persistRootNodes: {},
     _paused: !0,
     _configLoaded: !1,
     _isCloning: !1,
     _prepared: !1,
     _rendererInitialized: !1,
     _renderContext: null,
     _intervalId: null,
     _lastTime: null,
     _frameTime: null,
     _sceneInfos: [],
     frame: null,
     container: null,
     canvas: null,
     renderType: -1,
     config: null,
     onStart: null,
     setFrameRate: function(t) {
      this.config.frameRate = t, this._intervalId && window.cancelAnimFrame(this._intervalId), this._intervalId = 0, this._paused = !0, this._setAnimFrame(), this._runMainLoop()
     },
     getFrameRate: function() {
      return this.config.frameRate
     },
     step: function() {
      cc.director.mainLoop()
     },
     pause: function() {
      this._paused || (this._paused = !0, cc.audioEngine && cc.audioEngine._break(), cc.director.stopAnimation(), this._intervalId && window.cancelAnimFrame(this._intervalId), this._intervalId = 0)
     },
     resume: function() {
      this._paused && (this._paused = !1, cc.audioEngine && cc.audioEngine._restore(), cc.director.startAnimation(), this._runMainLoop())
     },
     isPaused: function() {
      return this._paused
     },
     restart: function() {
      cc.director.once(cc.Director.EVENT_AFTER_DRAW, (function() {
       for (var t in c._persistRootNodes) c.removePersistRootNode(c._persistRootNodes[t]);
       cc.director.getScene().destroy(), cc.Object._deferredDestroy(), cc.director.purgeDirector(), cc.audioEngine && cc.audioEngine.uncacheAll(), cc.director.reset(), c.onStart(), c.emit(c.EVENT_RESTART)
      }))
     },
     end: function() {
      close()
     },
     _initEngine: function() {
      this._rendererInitialized || (this._initRenderer(), this._initEvents(), this.emit(this.EVENT_ENGINE_INITED))
     },
     _prepareFinished: function(t) {
      this._prepared = !0, this._initEngine(), console.log("Cocos Creator v" + cc.ENGINE_VERSION), this._setAnimFrame(), this._runMainLoop(), this.emit(this.EVENT_GAME_INITED), t && t()
     },
     eventTargetOn: n.prototype.on,
     eventTargetOnce: n.prototype.once,
     on: function(t, e, i) {
      this._prepared && t === this.EVENT_ENGINE_INITED || !this._pause && t === this.EVENT_GAME_INITED ? e.call(i) : this.eventTargetOn(t, e, i)
     },
     once: function(t, e, i) {
      this._prepared && t === this.EVENT_ENGINE_INITED || !this._pause && t === this.EVENT_GAME_INITED ? e.call(i) : this.eventTargetOnce(t, e, i)
     },
     prepare: function(t) {
      if (this._prepared) t && t();
      else {
       var e = this.config.jsList;
       if (e && e.length > 0) {
        var i = this;
        cc.loader.load(e, (function(e) {
         if (e) throw new Error(JSON.stringify(e));
         i._prepareFinished(t)
        }))
       } else this._prepareFinished(t)
      }
     },
     run: function(t, e) {
      this._initConfig(t), this.onStart = e, this.prepare(c.onStart && c.onStart.bind(c))
     },
     addPersistRootNode: function(t) {
      if (cc.Node.isNode(t) && t.uuid) {
       var e = t.uuid;
       if (!this._persistRootNodes[e]) {
        var i = cc.director._scene;
        if (cc.isValid(i))
         if (t.parent) {
          if (!(t.parent instanceof cc.Scene)) return void cc.warnID(3801);
          if (t.parent !== i) return void cc.warnID(3802)
         } else t.parent = i;
        this._persistRootNodes[e] = t, t._persistNode = !0
       }
      } else cc.warnID(3800)
     },
     removePersistRootNode: function(t) {
      var e = t.uuid || "";
      t === this._persistRootNodes[e] && (delete this._persistRootNodes[e], t._persistNode = !1)
     },
     isPersistRootNode: function(t) {
      return t._persistNode
     },
     _setAnimFrame: function() {
      this._lastTime = new Date;
      var t = c.config.frameRate;
      this._frameTime = 1e3 / t, 60 !== t && 30 !== t ? (window.requestAnimFrame = this._stTime, window.cancelAnimFrame = this._ctTime) : (window.requestAnimFrame = window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.oRequestAnimationFrame || window.msRequestAnimationFrame || this._stTime, window.cancelAnimFrame = window.cancelAnimationFrame || window.cancelRequestAnimationFrame || window.msCancelRequestAnimationFrame || window.mozCancelRequestAnimationFrame || window.oCancelRequestAnimationFrame || window.webkitCancelRequestAnimationFrame || window.msCancelAnimationFrame || window.mozCancelAnimationFrame || window.webkitCancelAnimationFrame || window.oCancelAnimationFrame || this._ctTime)
     },
     _stTime: function(t) {
      var e = (new Date).getTime(),
       i = Math.max(0, c._frameTime - (e - c._lastTime)),
       n = window.setTimeout((function() {
        t()
       }), i);
      return c._lastTime = e + i, n
     },
     _ctTime: function(t) {
      window.clearTimeout(t)
     },
     _runMainLoop: function() {
      var t, e = this,
       i = e.config,
       n = cc.director,
       s = !0,
       a = i.frameRate;
      r.setDisplayStats(i.showFPS), t = function() {
       if (!e._paused) {
        if (e._intervalId = window.requestAnimFrame(t), 30 === a && (s = !s)) return;
        n.mainLoop()
       }
      }, e._intervalId = window.requestAnimFrame(t), e._paused = !1
     },
     _initConfig: function(t) {
      "number" != typeof t.debugMode && (t.debugMode = 0), t.exposeClassName = !!t.exposeClassName, "number" != typeof t.frameRate && (t.frameRate = 60);
      var e = t.renderMode;
      ("number" != typeof e || e > 2 || e < 0) && (t.renderMode = 0), "boolean" != typeof t.registerSystemEvent && (t.registerSystemEvent = !0), t.showFPS = !!t.showFPS, this._sceneInfos = t.scenes || [], this.collisionMatrix = t.collisionMatrix || [], this.groupList = t.groupList || [], r._resetDebugSetting(t.debugMode), this.config = t, this._configLoaded = !0
     },
     _determineRenderType: function() {
      var t = this.config,
       e = parseInt(t.renderMode) || 0;
      this.renderType = this.RENDER_TYPE_CANVAS;
      var i = !1;
      if (0 === e ? cc.sys.capabilities.opengl ? (this.renderType = this.RENDER_TYPE_WEBGL, i = !0) : cc.sys.capabilities.canvas && (this.renderType = this.RENDER_TYPE_CANVAS, i = !0) : 1 === e && cc.sys.capabilities.canvas ? (this.renderType = this.RENDER_TYPE_CANVAS, i = !0) : 2 === e && cc.sys.capabilities.opengl && (this.renderType = this.RENDER_TYPE_WEBGL, i = !0), !i) throw new Error(r.getError(3820, e))
     },
     _initRenderer: function() {
      if (!this._rendererInitialized) {
       this.config.id;
       var t = void 0,
        e = void 0;
       if (this.container = e = document.createElement("DIV"), this.frame = e.parentNode === document.body ? document.documentElement : e.parentNode, t = cc.sys.browserType === cc.sys.BROWSER_TYPE_WECHAT_GAME_SUB ? window.sharedCanvas || wx.getSharedCanvas() : canvas, this.canvas = t, this._determineRenderType(), this.renderType === this.RENDER_TYPE_WEBGL) {
        var i = {
         stencil: !0,
         antialias: cc.macro.ENABLE_WEBGL_ANTIALIAS,
         alpha: cc.macro.ENABLE_TRANSPARENT_CANVAS,
         preserveDrawingBuffer: !1
        };
      if(!window.__isAndroid ||
        mgc.ext_compareVersion(window.__letoSys.version, '7.0') > 0) {
        i["preserveDrawingBuffer"] = true;
      }
        s.initWebGL(t, i), this._renderContext = s.device._gl, !cc.macro.CLEANUP_IMAGE_CACHE && o && (o.enabled = !0), cc.sys.browserType == cc.sys.BROWSER_TYPE_CHROME && parseFloat(cc.sys.browserVersion) >= 69 && (o.enabled = !1)
       }
       this._renderContext || (this.renderType = this.RENDER_TYPE_CANVAS, s.initCanvas(t), this._renderContext = s.device._ctx), this.canvas.oncontextmenu = function() {
        if (!cc._isContextMenuEnable) return !1
       }, this._rendererInitialized = !0
      }
     },
     _initEvents: function() {
      var t, e = window;
      this.config.registerSystemEvent && a.registerSystemEvent(this.canvas), void 0 !== document.hidden ? t = "hidden" : void 0 !== document.mozHidden ? t = "mozHidden" : void 0 !== document.msHidden ? t = "msHidden" : void 0 !== document.webkitHidden && (t = "webkitHidden");
      var i = !1;

      function n() {
       i || (i = !0, c.emit(c.EVENT_HIDE))
      }

      function r(t, e, n, r, s) {
       i && (i = !1, c.emit(c.EVENT_SHOW, t, e, n, r, s))
      }
      if (t)
       for (var s = ["visibilitychange", "mozvisibilitychange", "msvisibilitychange", "webkitvisibilitychange", "qbrowserVisibilityChange"], o = 0; o < s.length; o++) document.addEventListener(s[o], (function(e) {
        var i = document[t];
        (i = i || e.hidden) ? n(): r()
       }));
      else e.addEventListener("blur", n), e.addEventListener("focus", r);
      navigator.userAgent.indexOf("MicroMessenger") > -1 && (e.onfocus = r), cc.sys.browserType !== cc.sys.BROWSER_TYPE_WECHAT_GAME_SUB && (wx.onShow && wx.onShow(r), wx.onHide && wx.onHide(n)), "onpageshow" in window && "onpagehide" in window && (e.addEventListener("pagehide", n), e.addEventListener("pageshow", r), document.addEventListener("pagehide", n), document.addEventListener("pageshow", r)), this.on(c.EVENT_HIDE, (function() {
       c.pause()
      })), this.on(c.EVENT_SHOW, (function() {
       c.resume()
      }))
     }
    };
   n.call(c), cc.js.addon(c, n.prototype), cc.game = e.exports = c
  }), {
   "../audio/CCAudioEngine": 20,
   "../core/renderer/utils/dynamic-atlas/manager": 153,
   "./CCDebug": 21,
   "./event/event-target": 77,
   "./platform/BKInputManager": 106,
   "./platform/CCInputManager": 112,
   "./renderer/index.js": 149
  }],
  24: [(function(t, e, i) {
   "use strict";
   var n = t("./utils/base-node"),
    r = t("./utils/prefab-helper"),
    s = t("./utils/math-pools"),
    a = t("./renderer/render-engine").math,
    o = t("./utils/affine-transform"),
    c = t("./event-manager"),
    h = t("./platform/CCMacro"),
    u = t("./utils/misc"),
    l = t("./platform/js"),
    _ = (t("./event/event"), t("./event/event-target")),
    d = t("./renderer/render-flow"),
    f = cc.Object.Flags.Destroying,
    p = Math.PI / 180,
    m = !!cc.ActionManager,
    v = function() {},
    g = cc.v2(),
    y = cc.v2(),
    T = a.mat4.create(),
    E = a.vec3.create(),
    x = a.quat.create(),
    C = 1,
    A = new Array(16);
   A.length = 0;
   var b = cc.Enum({
     DEBUG: 31
    }),
    S = cc.Enum({
     POSITION: 1,
     SCALE: 2,
     ROTATION: 4,
     SKEW: 8,
     RT: 7,
     ALL: 65535
    }),
    w = cc.Enum({
     TOUCH_START: "touchstart",
     TOUCH_MOVE: "touchmove",
     TOUCH_END: "touchend",
     TOUCH_CANCEL: "touchcancel",
     MOUSE_DOWN: "mousedown",
     MOUSE_MOVE: "mousemove",
     MOUSE_ENTER: "mouseenter",
     MOUSE_LEAVE: "mouseleave",
     MOUSE_UP: "mouseup",
     MOUSE_WHEEL: "mousewheel",
     POSITION_CHANGED: "position-changed",
     ROTATION_CHANGED: "rotation-changed",
     SCALE_CHANGED: "scale-changed",
     SIZE_CHANGED: "size-changed",
     ANCHOR_CHANGED: "anchor-changed",
     COLOR_CHANGED: "color-changed",
     CHILD_ADDED: "child-added",
     CHILD_REMOVED: "child-removed",
     CHILD_REORDER: "child-reorder",
     GROUP_CHANGED: "group-changed"
    }),
    D = [w.TOUCH_START, w.TOUCH_MOVE, w.TOUCH_END, w.TOUCH_CANCEL],
    R = [w.MOUSE_DOWN, w.MOUSE_ENTER, w.MOUSE_MOVE, w.MOUSE_LEAVE, w.MOUSE_UP, w.MOUSE_WHEEL],
    M = null,
    I = function(t, e) {
     var i = t.getLocation(),
      n = this.owner;
     return !!n._hitTest(i, this) && (e.type = w.TOUCH_START, e.touch = t, e.bubbles = !0, n.dispatchEvent(e), !0)
    },
    L = function(t, e) {
     var i = this.owner;
     e.type = w.TOUCH_MOVE, e.touch = t, e.bubbles = !0, i.dispatchEvent(e)
    },
    O = function(t, e) {
     var i = t.getLocation(),
      n = this.owner;
     n._hitTest(i, this) ? e.type = w.TOUCH_END : e.type = w.TOUCH_CANCEL, e.touch = t, e.bubbles = !0, n.dispatchEvent(e)
    },
    P = function(t, e) {
     t.getLocation();
     var i = this.owner;
     e.type = w.TOUCH_CANCEL, e.touch = t, e.bubbles = !0, i.dispatchEvent(e)
    },
    F = function(t) {
     var e = t.getLocation(),
      i = this.owner;
     i._hitTest(e, this) && (t.type = w.MOUSE_DOWN, t.bubbles = !0, i.dispatchEvent(t))
    },
    N = function(t) {
     var e = t.getLocation(),
      i = this.owner;
     if (i._hitTest(e, this)) this._previousIn || (M && M._mouseListener && (t.type = w.MOUSE_LEAVE, M.dispatchEvent(t), M._mouseListener._previousIn = !1), M = this.owner, t.type = w.MOUSE_ENTER, i.dispatchEvent(t), this._previousIn = !0), t.type = w.MOUSE_MOVE, t.bubbles = !0, i.dispatchEvent(t);
     else {
      if (!this._previousIn) return;
      t.type = w.MOUSE_LEAVE, i.dispatchEvent(t), this._previousIn = !1, M = null
     }
     t.stopPropagation()
    },
    B = function(t) {
     var e = t.getLocation(),
      i = this.owner;
     i._hitTest(e, this) && (t.type = w.MOUSE_UP, t.bubbles = !0, i.dispatchEvent(t), t.stopPropagation())
    },
    k = function(t) {
     var e = t.getLocation(),
      i = this.owner;
     i._hitTest(e, this) && (t.type = w.MOUSE_WHEEL, t.bubbles = !0, i.dispatchEvent(t), t.stopPropagation())
    };

   function z(t) {
    var e = cc.Mask;
    if (e)
     for (var i = 0, n = t; n && cc.Node.isNode(n); n = n._parent, ++i)
      if (n.getComponent(e)) return {
       index: i,
       node: n
      };
    return null
   }

   function U(t, e) {
    if (!(t._objFlags & f)) {
     var i = 0;
     if (t._bubblingListeners)
      for (; i < e.length; ++i)
       if (t._bubblingListeners.hasEventListener(e[i])) return !0;
     if (t._capturingListeners)
      for (; i < e.length; ++i)
       if (t._capturingListeners.hasEventListener(e[i])) return !0;
     return !1
    }
    return !0
   }

   function W(t) {
    var e = t.groupIndex;
    return 0 === e && t.parent && (e = W(t.parent)), e
   }

   function H(t) {
    var e = W(t);
    t._cullingMask = 1 << e;
    for (var i = 0; i < t._children.length; i++) H(t._children[i])
   }
   var G = cc.Class({
    name: "cc.Node",
    extends: n,
    properties: {
     _opacity: 255,
     _color: cc.Color.WHITE,
     _contentSize: cc.Size,
     _anchorPoint: cc.v2(.5, .5),
     _position: cc.Vec3,
     _scaleX: {
      default: void 0,
      type: cc.Float
     },
     _scaleY: {
      default: void 0,
      type: cc.Float
     },
     _scale: cc.Vec3,
     _rotationX: 0,
     _rotationY: 0,
     _quat: cc.Quat,
     _skewX: 0,
     _skewY: 0,
     _localZOrder: {
      default: 0,
      serializable: !1
     },
     _zIndex: 0,
     groupIndex: {
      default: 0,
      type: cc.Integer
     },
     group: {
      get: function() {
       return cc.game.groupList[this.groupIndex] || ""
      },
      set: function(t) {
       this.groupIndex = cc.game.groupList.indexOf(t), H(this), this.emit(w.GROUP_CHANGED, this)
      }
     },
     x: {
      get: function() {
       return this._position.x
      },
      set: function(t) {
       var e = this._position;
       t !== e.x && (e.x = t, this.setLocalDirty(S.POSITION), this._renderFlag |= d.FLAG_WORLD_TRANSFORM, 1 & this._eventMask && this.emit(w.POSITION_CHANGED))
      }
     },
     y: {
      get: function() {
       return this._position.y
      },
      set: function(t) {
       var e = this._position;
       t !== e.y && (e.y = t, this.setLocalDirty(S.POSITION), this._renderFlag |= d.FLAG_WORLD_TRANSFORM, 1 & this._eventMask && this.emit(w.POSITION_CHANGED))
      }
     },
     z: {
      get: function() {
       return this._position.z
      },
      set: function(t) {
       var e = this._position;
       t !== e.z && (e.z = t, this.setLocalDirty(S.POSITION), this._renderFlag |= d.FLAG_WORLD_TRANSFORM, 1 & this._eventMask && this.emit(w.POSITION_CHANGED))
      }
     },
     rotation: {
      get: function() {
       return this._rotationX
      },
      set: function(t) {
       this._rotationX === t && this._rotationY === t || (this._rotationX = this._rotationY = t, a.quat.fromEuler(this._quat, 0, 0, -t), this.setLocalDirty(S.ROTATION), this._renderFlag |= d.FLAG_TRANSFORM, 4 & this._eventMask && this.emit(w.ROTATION_CHANGED))
      }
     },
     rotationX: {
      get: function() {
       return this._rotationX
      },
      set: function(t) {
       this._rotationX !== t && (this._rotationX = t, this._rotationX === this._rotationY ? a.quat.fromEuler(this._quat, 0, 0, -t) : a.quat.fromEuler(this._quat, t, this._rotationY, 0), this.setLocalDirty(S.ROTATION), this._renderFlag |= d.FLAG_TRANSFORM, 4 & this._eventMask && this.emit(w.ROTATION_CHANGED))
      }
     },
     rotationY: {
      get: function() {
       return this._rotationY
      },
      set: function(t) {
       this._rotationY !== t && (this._rotationY = t, this._rotationX === this._rotationY ? a.quat.fromEuler(this._quat, 0, 0, -t) : a.quat.fromEuler(this._quat, this._rotationX, t, 0), this.setLocalDirty(S.ROTATION), this._renderFlag |= d.FLAG_TRANSFORM, 4 & this._eventMask && this.emit(w.ROTATION_CHANGED))
      }
     },
     scaleX: {
      get: function() {
       return this._scale.x
      },
      set: function(t) {
       this._scale.x !== t && (this._scale.x = t, this.setLocalDirty(S.SCALE), this._renderFlag |= d.FLAG_TRANSFORM, 2 & this._eventMask && this.emit(w.SCALE_CHANGED))
      }
     },
     scaleY: {
      get: function() {
       return this._scale.y
      },
      set: function(t) {
       this._scale.y !== t && (this._scale.y = t, this.setLocalDirty(S.SCALE), this._renderFlag |= d.FLAG_TRANSFORM, 2 & this._eventMask && this.emit(w.SCALE_CHANGED))
      }
     },
     skewX: {
      get: function() {
       return this._skewX
      },
      set: function(t) {
       this._skewX = t, this.setLocalDirty(S.SKEW), this._renderFlag |= d.FLAG_TRANSFORM
      }
     },
     skewY: {
      get: function() {
       return this._skewY
      },
      set: function(t) {
       this._skewY = t, this.setLocalDirty(S.SKEW), this._renderFlag |= d.FLAG_TRANSFORM
      }
     },
     opacity: {
      get: function() {
       return this._opacity
      },
      set: function(t) {
       this._opacity !== t && (this._opacity = t, this._renderFlag |= d.FLAG_OPACITY | d.FLAG_COLOR)
      },
      range: [0, 255]
     },
     color: {
      get: function() {
       return this._color.clone()
      },
      set: function(t) {
       this._color.equals(t) || (this._color.set(t), this._renderComponent && (this._renderFlag |= d.FLAG_COLOR), 32 & this._eventMask && this.emit(w.COLOR_CHANGED, t))
      }
     },
     anchorX: {
      get: function() {
       return this._anchorPoint.x
      },
      set: function(t) {
       var e = this._anchorPoint;
       e.x !== t && (e.x = t, 16 & this._eventMask && this.emit(w.ANCHOR_CHANGED))
      }
     },
     anchorY: {
      get: function() {
       return this._anchorPoint.y
      },
      set: function(t) {
       var e = this._anchorPoint;
       e.y !== t && (e.y = t, 16 & this._eventMask && this.emit(w.ANCHOR_CHANGED))
      }
     },
     width: {
      get: function() {
       return this._contentSize.width
      },
      set: function(t) {
       t !== this._contentSize.width && (this._contentSize.width = t, 8 & this._eventMask && this.emit(w.SIZE_CHANGED))
      }
     },
     height: {
      get: function() {
       return this._contentSize.height
      },
      set: function(t) {
       t !== this._contentSize.height && (this._contentSize.height = t, 8 & this._eventMask && this.emit(w.SIZE_CHANGED))
      }
     },
     zIndex: {
      get: function() {
       return this._zIndex
      },
      set: function(t) {
       t > h.MAX_ZINDEX ? (cc.warnID(1636), t = h.MAX_ZINDEX) : t < h.MIN_ZINDEX && (cc.warnID(1637), t = h.MIN_ZINDEX), this._zIndex !== t && (this._zIndex = t, this._localZOrder = 65535 & this._localZOrder | t << 16, this._parent && this._parent._delaySort())
      }
     }
    },
    ctor: function() {
     this._reorderChildDirty = !1, this._widget = null, this._renderComponent = null, this._capturingListeners = null, this._bubblingListeners = null, this._touchListener = null, this._mouseListener = null, this._scale.x = 1, this._scale.y = 1, this._scale.z = 1, this._matrix = s.mat4.get(), this._worldMatrix = s.mat4.get(), this._localMatDirty = S.ALL, this._worldMatDirty = !0, this._eventMask = 0, this._cullingMask = 1
    },
    statics: {
     EventType: w,
     _LocalDirtyFlag: S,
     isNode: function(t) {
      return t instanceof G && (t.constructor === G || !(t instanceof cc.Scene))
     },
     BuiltinGroupIndex: b
    },
    _onSiblingIndexChanged: function(t) {
     for (var e, i = this._parent, n = i._children, r = 0, s = n.length; r < s; r++)(e = n[r])._updateOrderOfArrival(), c._setDirtyForNode(e);
     i._delaySort()
    },
    _onPreDestroy: function() {
     this._onPreDestroyBase();
     m && cc.director.getActionManager().removeAllActionsFromTarget(this), M === this && (M = null), (this._touchListener || this._mouseListener) && (c.removeListeners(this), this._touchListener && (this._touchListener.owner = null, this._touchListener.mask = null, this._touchListener = null), this._mouseListener && (this._mouseListener.owner = null, this._mouseListener.mask = null, this._mouseListener = null)), s.mat4.put(this._matrix), s.mat4.put(this._worldMatrix), this._matrix = this._worldMatrix = null, this._reorderChildDirty && cc.director.__fastOff(cc.Director.EVENT_AFTER_UPDATE, this.sortAllChildren, this)
    },
    _onPostActivated: function(t) {
     var e = m ? cc.director.getActionManager() : null;
     if (t)
      if (this._renderFlag |= d.FLAG_WORLD_TRANSFORM, e && e.resumeTarget(this), c.resumeTarget(this), this._touchListener) {
       var i = this._touchListener.mask = z(this);
       this._mouseListener && (this._mouseListener.mask = i)
      } else this._mouseListener && (this._mouseListener.mask = z(this));
     else e && e.pauseTarget(this), c.pauseTarget(this)
    },
    _onHierarchyChanged: function(t) {
     this._updateOrderOfArrival(), H(this), this._parent && this._parent._delaySort(), this._renderFlag |= d.FLAG_WORLD_TRANSFORM, this._onHierarchyChangedBase(t), cc._widgetManager && (cc._widgetManager._nodesOrderDirty = !0)
    },
    _upgrade_1x_to_2x: function() {
     if (void 0 !== this._scaleX && (this._scale.x = this._scaleX, this._scaleX = void 0), void 0 !== this._scaleY && (this._scale.y = this._scaleY, this._scaleY = void 0), 0 !== this._localZOrder && (this._zIndex = (4294901760 & this._localZOrder) >> 16), 0 !== this._rotationX || 0 !== this._rotationY) this._rotationX === this._rotationY ? a.quat.fromEuler(this._quat, 0, 0, -this._rotationX) : a.quat.fromEuler(this._quat, this._rotationX, this._rotationY, 0);
     else {
      var t = this._quat.getRoll(),
       e = this._quat.getPitch();
      0 === t && 0 === e ? this._rotationX = this._rotationY = -this._quat.getYaw() : (this._rotationX = t, this._rotationY = e)
     }
     this._color.a < 255 && 255 === this._opacity && (this._opacity = this._color.a, this._color.a = 255)
    },
    _onBatchCreated: function() {
     this._upgrade_1x_to_2x(), this._updateOrderOfArrival(), this._cullingMask = 1 << W(this);
     var t = this._prefab;
     t && t.sync && t.root === this && r.syncWithPrefab(this), this._activeInHierarchy || (m && cc.director.getActionManager().pauseTarget(this), c.pauseTarget(this));
     for (var e = this._children, i = 0, n = e.length; i < n; i++) e[i]._onBatchCreated();
     e.length > 0 && (this._renderFlag |= d.FLAG_CHILDREN)
    },
    _onBatchRestored: function() {
     this._upgrade_1x_to_2x(), this._cullingMask = 1 << W(this), this._activeInHierarchy || (m && cc.director.getActionManager().pauseTarget(this), c.pauseTarget(this));
     for (var t = this._children, e = 0, i = t.length; e < i; e++) t[e]._onBatchRestored();
     t.length > 0 && (this._renderFlag |= d.FLAG_CHILDREN)
    },
    _checknSetupSysEvent: function(t) {
     var e = !1,
      i = !1;
     return -1 !== D.indexOf(t) ? (this._touchListener || (this._touchListener = cc.EventListener.create({
      event: cc.EventListener.TOUCH_ONE_BY_ONE,
      swallowTouches: !0,
      owner: this,
      mask: z(this),
      onTouchBegan: I,
      onTouchMoved: L,
      onTouchEnded: O,
      onTouchCancelled: P
     }), c.addListener(this._touchListener, this), e = !0), i = !0) : -1 !== R.indexOf(t) && (this._mouseListener || (this._mouseListener = cc.EventListener.create({
      event: cc.EventListener.MOUSE,
      _previousIn: !1,
      owner: this,
      mask: z(this),
      onMouseDown: F,
      onMouseMove: N,
      onMouseUp: B,
      onMouseScroll: k
     }), c.addListener(this._mouseListener, this), e = !0), i = !0), e && !this._activeInHierarchy && cc.director.getScheduler().schedule((function() {
      this._activeInHierarchy || c.pauseTarget(this)
     }), this, 0, 0, 0, !1), i
    },
    on: function(t, e, i, n) {
     if (this._checknSetupSysEvent(t)) return this._onDispatch(t, e, i, n);
     switch (t) {
      case w.POSITION_CHANGED:
       this._eventMask |= 1;
       break;
      case w.SCALE_CHANGED:
       this._eventMask |= 2;
       break;
      case w.ROTATION_CHANGED:
       this._eventMask |= 4;
       break;
      case w.SIZE_CHANGED:
       this._eventMask |= 8;
       break;
      case w.ANCHOR_CHANGED:
       this._eventMask |= 16;
       break;
      case w.COLOR_CHANGED:
       this._eventMask |= 32
     }
     return this._bubblingListeners || (this._bubblingListeners = new _), this._bubblingListeners.on(t, e, i)
    },
    once: function(t, e, i, n) {
     var r = this,
      s = this._checknSetupSysEvent(t),
      a = "__ONCE_FLAG:" + t,
      o = null;
     (o = s && n ? this._capturingListeners = this._capturingListeners || new _ : this._bubblingListeners = this._bubblingListeners || new _).hasEventListener(a, e, i) || (function() {
      var n = r,
       s = function(r, c, h, u, l) {
        n.off(t, s, i), o.remove(a, e, i), e.call(this, r, c, h, u, l)
       };
      r.on(t, s, i), o.add(a, e, i)
     })()
    },
    _onDispatch: function(t, e, i, n) {
     if ("boolean" == typeof i ? (n = i, i = void 0) : n = !!n, e) {
      var r = null;
      return (r = n ? this._capturingListeners = this._capturingListeners || new _ : this._bubblingListeners = this._bubblingListeners || new _).hasEventListener(t, e, i) || (r.add(t, e, i), i && i.__eventTargets && i.__eventTargets.push(this)), e
     }
     cc.errorID(6800)
    },
    off: function(t, e, i, n) {
     var r = -1 !== D.indexOf(t),
      s = !r && -1 !== R.indexOf(t);
     if (r || s) this._offDispatch(t, e, i, n), r ? this._touchListener && !U(this, D) && (c.removeListener(this._touchListener), this._touchListener = null) : s && this._mouseListener && !U(this, R) && (c.removeListener(this._mouseListener), this._mouseListener = null);
     else if (this._bubblingListeners) {
      if (this._bubblingListeners.off(t, e, i), !this._bubblingListeners.hasEventListener(t)) switch (t) {
       case w.POSITION_CHANGED:
        this._eventMask &= -2;
        break;
       case w.SCALE_CHANGED:
        this._eventMask &= -3;
        break;
       case w.ROTATION_CHANGED:
        this._eventMask &= -5;
        break;
       case w.SIZE_CHANGED:
        this._eventMask &= -9;
        break;
       case w.ANCHOR_CHANGED:
        this._eventMask &= -17;
        break;
       case w.COLOR_CHANGED:
        this._eventMask &= -33
      }
     }
    },
    _offDispatch: function(t, e, i, n) {
     if ("boolean" == typeof i ? (n = i, i = void 0) : n = !!n, e) {
      var r = n ? this._capturingListeners : this._bubblingListeners;
      r && (r.remove(t, e, i), i && i.__eventTargets && l.array.fastRemove(i.__eventTargets, this))
     } else this._capturingListeners && this._capturingListeners.removeAll(t), this._bubblingListeners && this._bubblingListeners.removeAll(t)
    },
    targetOff: function(t) {
     var e = this._bubblingListeners;
     e && (e.targetOff(t), 1 & this._eventMask && !e.hasEventListener(w.POSITION_CHANGED) && (this._eventMask &= -2), 2 & this._eventMask && !e.hasEventListener(w.SCALE_CHANGED) && (this._eventMask &= -3), 4 & this._eventMask && !e.hasEventListener(w.ROTATION_CHANGED) && (this._eventMask &= -5), 8 & this._eventMask && !e.hasEventListener(w.SIZE_CHANGED) && (this._eventMask &= -9), 16 & this._eventMask && !e.hasEventListener(w.ANCHOR_CHANGED) && (this._eventMask &= -17), 32 & this._eventMask && !e.hasEventListener(w.COLOR_CHANGED) && (this._eventMask &= -33)), this._capturingListeners && this._capturingListeners.targetOff(t), this._touchListener && !U(this, D) && (c.removeListener(this._touchListener), this._touchListener = null), this._mouseListener && !U(this, R) && (c.removeListener(this._mouseListener), this._mouseListener = null)
    },
    hasEventListener: function(t) {
     var e = !1;
     return this._bubblingListeners && (e = this._bubblingListeners.hasEventListener(t)), !e && this._capturingListeners && (e = this._capturingListeners.hasEventListener(t)), e
    },
    emit: function(t, e, i, n, r, s) {
     this._bubblingListeners && this._bubblingListeners.emit(t, e, i, n, r, s)
    },
    dispatchEvent: function(t) {
     (function(t, e) {
      var i, n;
      for (e.target = t, A.length = 0, t._getCapturingTargets(e.type, A), e.eventPhase = 1, n = A.length - 1; n >= 0; --n)
       if ((i = A[n])._capturingListeners && (e.currentTarget = i, i._capturingListeners.emit(e.type, e, A), e._propagationStopped)) return void(A.length = 0);
      if (A.length = 0, e.eventPhase = 2, e.currentTarget = t, t._capturingListeners && t._capturingListeners.emit(e.type, e), !e._propagationImmediateStopped && t._bubblingListeners && t._bubblingListeners.emit(e.type, e), !e._propagationStopped && e.bubbles)
       for (t._getBubblingTargets(e.type, A), e.eventPhase = 3, n = 0; n < A.length; ++n)
        if ((i = A[n])._bubblingListeners && (e.currentTarget = i, i._bubblingListeners.emit(e.type, e), e._propagationStopped)) return void(A.length = 0);
      A.length = 0
     })(this, t), A.length = 0
    },
    pauseSystemEvents: function(t) {
     c.pauseTarget(this, t)
    },
    resumeSystemEvents: function(t) {
     c.resumeTarget(this, t)
    },
    _hitTest: function(t, e) {
     var i = this._contentSize.width,
      n = this._contentSize.height,
      r = g,
      s = y,
      o = cc.Camera.findCamera(this);
     if (o ? o.getCameraToWorldPoint(t, r) : r.set(t), this._updateWorldMatrix(), a.mat4.invert(T, this._worldMatrix), a.vec2.transformMat4(s, r, T), s.x += this._anchorPoint.x * i, s.y += this._anchorPoint.y * n, s.x >= 0 && s.y >= 0 && s.x <= i && s.y <= n) {
      if (e && e.mask) {
       for (var c = e.mask, h = this, u = 0; h && u < c.index; ++u, h = h.parent);
       if (h === c.node) {
        var l = h.getComponent(cc.Mask);
        return !l || !l.enabledInHierarchy || l._hitTest(r)
       }
       return e.mask = null, !0
      }
      return !0
     }
     return !1
    },
    _getCapturingTargets: function(t, e) {
     for (var i = this.parent; i;) i._capturingListeners && i._capturingListeners.hasEventListener(t) && e.push(i), i = i.parent
    },
    _getBubblingTargets: function(t, e) {
     for (var i = this.parent; i;) i._bubblingListeners && i._bubblingListeners.hasEventListener(t) && e.push(i), i = i.parent
    },
    runAction: m ? function(t) {
     if (this.active) return cc.assertID(t, 1618), cc.director.getActionManager().addAction(t, this, !1), t
    } : v,
    pauseAllActions: m ? function() {
     cc.director.getActionManager().pauseTarget(this)
    } : v,
    resumeAllActions: m ? function() {
     cc.director.getActionManager().resumeTarget(this)
    } : v,
    stopAllActions: m ? function() {
     cc.director.getActionManager().removeAllActionsFromTarget(this)
    } : v,
    stopAction: m ? function(t) {
     cc.director.getActionManager().removeAction(t)
    } : v,
    stopActionByTag: m ? function(t) {
     t !== cc.Action.TAG_INVALID ? cc.director.getActionManager().removeActionByTag(t, this) : cc.logID(1612)
    } : v,
    getActionByTag: m ? function(t) {
     return t === cc.Action.TAG_INVALID ? (cc.logID(1613), null) : cc.director.getActionManager().getActionByTag(t, this)
    } : function() {
     return null
    },
    getNumberOfRunningActions: m ? function() {
     return cc.director.getActionManager().getNumberOfRunningActionsInTarget(this)
    } : function() {
     return 0
    },
    getPosition: function() {
     return new cc.Vec2(this._position)
    },
    setPosition: function(t, e) {
     var i;
     void 0 === e ? (i = t.x, e = t.y) : i = t;
     var n = this._position;
     n.x === i && n.y === e || (n.x = i, n.y = e, this.setLocalDirty(S.POSITION), this._renderFlag |= d.FLAG_WORLD_TRANSFORM, 1 & this._eventMask && this.emit(w.POSITION_CHANGED))
    },
    getScale: function() {
     return this._scale.x !== this._scale.y && cc.logID(1603), this._scale.x
    },
    setScale: function(t, e) {
     t && "number" != typeof t ? (e = t.y, t = t.x) : void 0 === e && (e = t), this._scale.x === t && this._scale.y === e || (this._scale.x = t, this._scale.y = e, this.setLocalDirty(S.SCALE), this._renderFlag |= d.FLAG_TRANSFORM, 2 & this._eventMask && this.emit(w.SCALE_CHANGED))
    },
    getContentSize: function() {
     return cc.size(this._contentSize.width, this._contentSize.height)
    },
    setContentSize: function(t, e) {
     var i = this._contentSize;
     if (void 0 === e) {
      if (t.width === i.width && t.height === i.height) return;
      0, i.width = t.width, i.height = t.height
     } else {
      if (t === i.width && e === i.height) return;
      0, i.width = t, i.height = e
     }
     8 & this._eventMask && this.emit(w.SIZE_CHANGED)
    },
    getAnchorPoint: function() {
     return cc.v2(this._anchorPoint)
    },
    setAnchorPoint: function(t, e) {
     var i = this._anchorPoint;
     if (void 0 === e) {
      if (t.x === i.x && t.y === i.y) return;
      i.x = t.x, i.y = t.y
     } else {
      if (t === i.x && e === i.y) return;
      i.x = t, i.y = e
     }
     this.setLocalDirty(S.POSITION), 16 & this._eventMask && this.emit(w.ANCHOR_CHANGED)
    },
    _invTransformPoint: function(t, e) {
     return this._parent ? this._parent._invTransformPoint(t, e) : a.vec3.copy(t, e), a.vec3.sub(t, t, this._position), a.quat.conjugate(x, this._quat), a.vec3.transformQuat(t, t, x), a.vec3.inverseSafe(E, this._scale), a.vec3.mul(t, t, E), t
    },
    getWorldPos: function(t) {
     a.vec3.copy(t, this._position);
     for (var e = this._parent; e;) a.vec3.mul(t, t, e._scale), a.vec3.transformQuat(t, t, e._quat), a.vec3.add(t, t, e._position), e = e._parent;
     return t
    },
    setWorldPos: function(t) {
     this._parent ? this._parent._invTransformPoint(this._position, t) : a.vec3.copy(this._position, t), this.setLocalDirty(S.POSITION), 1 & this._eventMask && this.emit(w.POSITION_CHANGED)
    },
    getWorldRot: function(t) {
     a.quat.copy(t, this._quat);
     for (var e = this._parent; e;) a.quat.mul(t, e._quat, t), e = e._parent;
     return t
    },
    setWorldRot: function(t) {
     this._parent ? (this._parent.getWorldRot(this._quat), a.quat.conjugate(this._quat, this._quat), a.quat.mul(this._quat, this._quat, t)) : a.quat.copy(this._quat, t), this.setLocalDirty(S.ROTATION)
    },
    getWorldRT: function(t) {
     var e = E,
      i = x;
     a.vec3.copy(e, this._position), a.quat.copy(i, this._quat);
     for (var n = this._parent; n;) a.vec3.mul(e, e, n._scale), a.vec3.transformQuat(e, e, n._quat), a.vec3.add(e, e, n._position), a.quat.mul(i, n._quat, i), n = n._parent;
     return a.mat4.fromRT(t, i, e), t
    },
    lookAt: function(t, e) {
     this.getWorldPos(E), a.vec3.sub(E, E, t), a.vec3.normalize(E, E), a.quat.fromViewUp(x, E, e), this.setWorldRot(x)
    },
    _updateLocalMatrix: function() {
     var t = this._localMatDirty;
     if (t) {
      var e = this._matrix;
      if (t & (S.RT | S.SKEW)) {
       var i = this._rotationX || this._rotationY,
        n = this._skewX || this._skewY,
        r = this._scale.x,
        s = this._scale.y;
       if (i || n) {
        var a = 1,
         o = 0,
         c = 0,
         h = 1;
        if (i) {
         var u = this._rotationX * p;
         if (c = Math.sin(u), h = Math.cos(u), this._rotationY === this._rotationX) a = h, o = -c;
         else {
          var l = this._rotationY * p;
          a = Math.cos(l), o = -Math.sin(l)
         }
        }
        if (e.m00 = a *= r, e.m01 = o *= r, e.m04 = c *= s, e.m05 = h *= s, n) {
         var _ = e.m00,
          d = e.m01,
          f = e.m04,
          m = e.m05,
          v = Math.tan(this._skewX * p),
          g = Math.tan(this._skewY * p);
         v === 1 / 0 && (v = 99999999), g === 1 / 0 && (g = 99999999), e.m00 = _ + f * g, e.m01 = d + m * g, e.m04 = f + _ * v, e.m05 = m + d * v
        }
       } else e.m00 = r, e.m01 = 0, e.m04 = 0, e.m05 = s
      }
      e.m12 = this._position.x, e.m13 = this._position.y, this._localMatDirty = 0, this._worldMatDirty = !0
     }
    },
    _calculWorldMatrix: function() {
     this._localMatDirty && this._updateLocalMatrix();
     var t = this._parent;
     if (t) {
      var e = t._worldMatrix,
       i = this._matrix,
       n = this._worldMatrix,
       r = i.m00,
       s = i.m01,
       o = i.m04,
       c = i.m05,
       h = i.m12,
       u = i.m13,
       l = e.m00,
       _ = e.m01,
       d = e.m04,
       f = e.m05,
       p = e.m12,
       m = e.m13;
      0 !== _ || 0 !== d ? (n.m00 = r * l + s * d, n.m01 = r * _ + s * f, n.m04 = o * l + c * d, n.m05 = o * _ + c * f, n.m12 = l * h + d * u + p, n.m13 = _ * h + f * u + m) : (n.m00 = r * l, n.m01 = s * f, n.m04 = o * l, n.m05 = c * f, n.m12 = l * h + p, n.m13 = f * u + m)
     } else a.mat4.copy(this._worldMatrix, this._matrix);
     this._worldMatDirty = !1
    },
    _updateWorldMatrix: function() {
     if (this._parent && this._parent._updateWorldMatrix(), this._worldMatDirty) {
      this._calculWorldMatrix();
      for (var t = this._children, e = 0, i = t.length; e < i; e++) t[e]._worldMatDirty = !0
     }
    },
    setLocalDirty: function(t) {
     this._localMatDirty = this._localMatDirty | t, this._worldMatDirty = !0
    },
    setWorldDirty: function() {
     this._worldMatDirty = !0
    },
    getLocalMatrix: function(t) {
     return this._updateLocalMatrix(), a.mat4.copy(t, this._matrix)
    },
    getWorldMatrix: function(t) {
     return this._updateWorldMatrix(), a.mat4.copy(t, this._worldMatrix)
    },
    convertToNodeSpace: function(t) {
     this._updateWorldMatrix(), a.mat4.invert(T, this._worldMatrix);
     var e = new cc.Vec2;
     return a.vec2.transformMat4(e, t, T), e.x += this._anchorPoint.x * this._contentSize.width, e.y += this._anchorPoint.y * this._contentSize.height, e
    },
    convertToWorldSpace: function(t) {
     this._updateWorldMatrix();
     var e = new cc.Vec2(t.x - this._anchorPoint.x * this._contentSize.width, t.y - this._anchorPoint.y * this._contentSize.height);
     return a.vec2.transformMat4(e, e, this._worldMatrix)
    },
    convertToNodeSpaceAR: function(t) {
     this._updateWorldMatrix(), a.mat4.invert(T, this._worldMatrix);
     var e = new cc.Vec2;
     return a.vec2.transformMat4(e, t, T)
    },
    convertToWorldSpaceAR: function(t) {
     this._updateWorldMatrix();
     var e = new cc.Vec2;
     return a.vec2.transformMat4(e, t, this._worldMatrix)
    },
    getNodeToParentTransform: function(t) {
     t || (t = o.identity()), this._updateLocalMatrix();
     var e = this._contentSize;
     return E.x = -this._anchorPoint.x * e.width, E.y = -this._anchorPoint.y * e.height, a.mat4.copy(T, this._matrix), a.mat4.translate(T, T, E), o.fromMat4(t, T)
    },
    getNodeToParentTransformAR: function(t) {
     return t || (t = o.identity()), this._updateLocalMatrix(), o.fromMat4(t, this._matrix)
    },
    getNodeToWorldTransform: function(t) {
     t || (t = o.identity()), this._updateWorldMatrix();
     var e = this._contentSize;
     return E.x = -this._anchorPoint.x * e.width, E.y = -this._anchorPoint.y * e.height, a.mat4.copy(T, this._worldMatrix), a.mat4.translate(T, T, E), o.fromMat4(t, T)
    },
    getNodeToWorldTransformAR: function(t) {
     return t || (t = o.identity()), this._updateWorldMatrix(), o.fromMat4(t, this._worldMatrix)
    },
    getParentToNodeTransform: function(t) {
     return t || (t = o.identity()), this._updateLocalMatrix(), a.mat4.invert(T, this._matrix), o.fromMat4(t, T)
    },
    getWorldToNodeTransform: function(t) {
     return t || (t = o.identity()), this._updateWorldMatrix(), a.mat4.invert(T, this._worldMatrix), o.fromMat4(t, T)
    },
    convertTouchToNodeSpace: function(t) {
     return this.convertToNodeSpace(t.getLocation())
    },
    convertTouchToNodeSpaceAR: function(t) {
     return this.convertToNodeSpaceAR(t.getLocation())
    },
    getBoundingBox: function() {
     this._updateLocalMatrix();
     var t = this._contentSize.width,
      e = this._contentSize.height,
      i = cc.rect(-this._anchorPoint.x * t, -this._anchorPoint.y * e, t, e);
     return i.transformMat4(i, this._matrix)
    },
    getBoundingBoxToWorld: function() {
     return this._parent ? (this._parent._updateWorldMatrix(), this._getBoundingBoxTo(this._parent._worldMatrix)) : this.getBoundingBox()
    },
    _getBoundingBoxTo: function(t) {
     this._updateLocalMatrix();
     var e = this._contentSize.width,
      i = this._contentSize.height,
      n = cc.rect(-this._anchorPoint.x * e, -this._anchorPoint.y * i, e, i);
     t = a.mat4.mul(this._worldMatrix, t, this._matrix);
     if (n.transformMat4(n, t), !this._children) return n;
     for (var r = this._children, s = 0; s < r.length; s++) {
      var o = r[s];
      if (o && o.active) {
       var c = o._getBoundingBoxTo(t);
       c && n.union(n, c)
      }
     }
     return n
    },
    _updateOrderOfArrival: function() {
     var t = ++C;
     this._localZOrder = 4294901760 & this._localZOrder | t
    },
    addChild: function(t, e, i) {
     cc.assertID(t, 1606), cc.assertID(null === t._parent, 1605), t.parent = this, void 0 !== e && (t.zIndex = e), void 0 !== i && (t.name = i)
    },
    cleanup: function() {
     m && cc.director.getActionManager().removeAllActionsFromTarget(this), c.removeListeners(this);
     var t, e, i = this._children.length;
     for (t = 0; t < i; ++t)(e = this._children[t]) && e.cleanup()
    },
    sortAllChildren: function() {
     if (this._reorderChildDirty) {
      this._reorderChildDirty = !1;
      var t = this._children;
      if (t.length > 1) {
       var e, i, n, r = t.length;
       for (e = 1; e < r; e++) {
        for (n = t[e], i = e - 1; i >= 0 && n._localZOrder < t[i]._localZOrder;) t[i + 1] = t[i], i--;
        t[i + 1] = n
       }
       this.emit(w.CHILD_REORDER, this)
      }
      cc.director.__fastOff(cc.Director.EVENT_AFTER_UPDATE, this.sortAllChildren, this)
     }
    },
    _delaySort: function() {
     this._reorderChildDirty || (this._reorderChildDirty = !0, cc.director.__fastOn(cc.Director.EVENT_AFTER_UPDATE, this.sortAllChildren, this))
    },
    _restoreProperties: !1,
    onRestore: !1
   });
   u.propertyDefine(G, ["position", "scale", "rotation"]), cc.Node = e.exports = G
  }), {
   "./event-manager": 75,
   "./event/event": 78,
   "./event/event-target": 77,
   "./platform/CCMacro": 113,
   "./platform/js": 128,
   "./renderer/render-engine": 150,
   "./renderer/render-flow": 151,
   "./utils/affine-transform": 179,
   "./utils/base-node": 180,
   "./utils/math-pools": 185,
   "./utils/misc": 186,
   "./utils/prefab-helper": 189
  }],
  25: [(function(t, e, i) {
   "use strict";
   var n = t("./CCNode"),
    r = t("./renderer/render-flow"),
    s = t("./renderer/render-engine").math,
    a = n._LocalDirtyFlag,
    o = (s.vec3.create(), cc.Class({
     name: "cc.PrivateNode",
     extends: n,
     properties: {
      x: {
       get: function() {
        return this._originPos.x
       },
       set: function(t) {
        var e = this._originPos;
        t !== e.x && (e.x = t, this._posDirty(!0))
       },
       override: !0
      },
      y: {
       get: function() {
        return this._originPos.y
       },
       set: function(t) {
        var e = this._originPos;
        t !== e.y && (e.y = t, this._posDirty(!0))
       },
       override: !0
      },
      zIndex: {
       get: function() {
        return cc.macro.MIN_ZINDEX
       },
       set: function() {
        cc.warnID(1638)
       },
       override: !0
      }
     },
     ctor: function(t) {
      this._localZOrder = cc.macro.MIN_ZINDEX << 16, this._originPos = cc.v2()
     },
     _posDirty: function(t) {
      this.setLocalDirty(a.POSITION), this._renderFlag |= r.FLAG_TRANSFORM, !0 === t && 1 & this._eventMask && this.emit(n.EventType.POSITION_CHANGED)
     },
     _updateLocalMatrix: function() {
      if (this._localMatDirty) {
       var t = this.parent;
       t && (this._position.x = this._originPos.x - (t._anchorPoint.x - .5) * t._contentSize.width, this._position.y = this._originPos.y - (t._anchorPoint.y - .5) * t._contentSize.height), this._super()
      }
     },
     getPosition: function() {
      return new cc.Vec2(this._originPos)
     },
     setPosition: function(t, e) {
      void 0 === e && (e = (t = t.x).y);
      var i = this._originPos;
      i.x === t && i.y === e || (i.x = t, i.y = e, this._posDirty(!0))
     },
     setParent: function(t) {
      var e = this._parent;
      this._super(t), e !== t && (e && e.off(n.EventType.ANCHOR_CHANGED, this._posDirty, this), t && t.on(n.EventType.ANCHOR_CHANGED, this._posDirty, this))
     },
     _updateOrderOfArrival: function() {}
    }));
   cc.js.getset(o.prototype, "parent", o.prototype.getParent, o.prototype.setParent), cc.js.getset(o.prototype, "position", o.prototype.getPosition, o.prototype.setPosition), cc.PrivateNode = e.exports = o
  }), {
   "./CCNode": 24,
   "./renderer/render-engine": 150,
   "./renderer/render-flow": 151
  }],
  26: [(function(t, e, i) {
   cc.Scene = cc.Class({
    name: "cc.Scene",
    extends: t("./CCNode"),
    properties: {
     autoReleaseAssets: {
      default: void 0,
      type: cc.Boolean
     }
    },
    ctor: function() {
     this._anchorPoint.x = 0, this._anchorPoint.y = 0, this._activeInHierarchy = !1, this._inited = !cc.game._isCloning, this.dependAssets = null
    },
    destroy: function() {
     if (cc.Object.prototype.destroy.call(this))
      for (var t = this._children, e = 0; e < t.length; ++e) t[e].active = !1;
     this._active = !1, this._activeInHierarchy = !1
    },
    _onHierarchyChanged: function() {},
    _instantiate: null,
    _load: function() {
     this._inited || (this._onBatchCreated(), this._inited = !0)
    },
    _activate: function(t) {
     t = !1 !== t, cc.director._nodeActivator.activateNode(this, t)
    }
   }), e.exports = cc.Scene
  }), {
   "./CCNode": 24
  }],
  27: [(function(t, e, i) {
   var n = t("./platform/js"),
    r = new(t("./platform/id-generater"))("Scheduler"),
    s = function(t, e, i, n) {
     this.target = t, this.priority = e, this.paused = i, this.markedForDeletion = n
    },
    a = [];
   s.get = function(t, e, i, n) {
    var r = a.pop();
    return r ? (r.target = t, r.priority = e, r.paused = i, r.markedForDeletion = n) : r = new s(t, e, i, n), r
   }, s.put = function(t) {
    a.length < 20 && (t.target = null, a.push(t))
   };
   var o = function(t, e, i, n) {
     this.list = t, this.entry = e, this.target = i, this.callback = n
    },
    c = [];
   o.get = function(t, e, i, n) {
    var r = c.pop();
    return r ? (r.list = t, r.entry = e, r.target = i, r.callback = n) : r = new o(t, e, i, n), r
   }, o.put = function(t) {
    c.length < 20 && (t.list = t.entry = t.target = t.callback = null, c.push(t))
   };
   var h = function(t, e, i, n, r, s) {
     var a = this;
     a.timers = t, a.target = e, a.timerIndex = i, a.currentTimer = n, a.currentTimerSalvaged = r, a.paused = s
    },
    u = [];

   function l() {
    this._lock = !1, this._scheduler = null, this._elapsed = -1, this._runForever = !1, this._useDelay = !1, this._timesExecuted = 0, this._repeat = 0, this._delay = 0, this._interval = 0, this._target = null, this._callback = null
   }
   h.get = function(t, e, i, n, r, s) {
    var a = u.pop();
    return a ? (a.timers = t, a.target = e, a.timerIndex = i, a.currentTimer = n, a.currentTimerSalvaged = r, a.paused = s) : a = new h(t, e, i, n, r, s), a
   }, h.put = function(t) {
    u.length < 20 && (t.timers = t.target = t.currentTimer = null, u.push(t))
   };
   var _ = l.prototype;
   _.initWithCallback = function(t, e, i, n, r, s) {
    return this._lock = !1, this._scheduler = t, this._target = i, this._callback = e, this._elapsed = -1, this._interval = n, this._delay = s, this._useDelay = this._delay > 0, this._repeat = r, this._runForever = this._repeat === cc.macro.REPEAT_FOREVER, !0
   }, _.getInterval = function() {
    return this._interval
   }, _.setInterval = function(t) {
    this._interval = t
   }, _.update = function(t) {
    -1 === this._elapsed ? (this._elapsed = 0, this._timesExecuted = 0) : (this._elapsed += t, this._runForever && !this._useDelay ? this._elapsed >= this._interval && (this.trigger(), this._elapsed = 0) : (this._useDelay ? this._elapsed >= this._delay && (this.trigger(), this._elapsed -= this._delay, this._timesExecuted += 1, this._useDelay = !1) : this._elapsed >= this._interval && (this.trigger(), this._elapsed = 0, this._timesExecuted += 1), this._callback && !this._runForever && this._timesExecuted > this._repeat && this.cancel()))
   }, _.getCallback = function() {
    return this._callback
   }, _.trigger = function() {
    this._target && this._callback && (this._lock = !0, this._callback.call(this._target, this._elapsed), this._lock = !1)
   }, _.cancel = function() {
    this._scheduler.unschedule(this._callback, this._target)
   };
   var d = [];
   l.get = function() {
    return d.pop() || new l
   }, l.put = function(t) {
    d.length < 20 && !t._lock && (t._scheduler = t._target = t._callback = null, d.push(t))
   }, cc.Scheduler = function() {
    this._timeScale = 1, this._updatesNegList = [], this._updates0List = [], this._updatesPosList = [], this._hashForUpdates = n.createMap(!0), this._hashForTimers = n.createMap(!0), this._currentTarget = null, this._currentTargetSalvaged = !1, this._updateHashLocked = !1, this._arrayForTimers = []
   }, cc.Scheduler.prototype = {
    constructor: cc.Scheduler,
    _removeHashElement: function(t) {
     delete this._hashForTimers[t.target._id];
     for (var e = this._arrayForTimers, i = 0, n = e.length; i < n; i++)
      if (e[i] === t) {
       e.splice(i, 1);
       break
      } h.put(t)
    },
    _removeUpdateFromHash: function(t) {
     var e = t.target._id,
      i = this._hashForUpdates[e];
     if (i) {
      for (var n = i.list, r = i.entry, a = 0, c = n.length; a < c; a++)
       if (n[a] === r) {
        n.splice(a, 1);
        break
       } delete this._hashForUpdates[e], s.put(r), o.put(i)
     }
    },
    _priorityIn: function(t, e, i) {
     for (var n = 0; n < t.length; n++)
      if (i < t[n].priority) return void t.splice(n, 0, e);
     t.push(e)
    },
    _appendIn: function(t, e) {
     t.push(e)
    },
    enableForTarget: function(t) {
     t._id || (t.__instanceId ? cc.warnID(1513) : t._id = r.getNewId())
    },
    setTimeScale: function(t) {
     this._timeScale = t
    },
    getTimeScale: function() {
     return this._timeScale
    },
    update: function(t) {
     var e, i, n, r;
     for (this._updateHashLocked = !0, 1 !== this._timeScale && (t *= this._timeScale), e = 0, n = (i = this._updatesNegList).length; e < n; e++)(r = i[e]).paused || r.markedForDeletion || r.target.update(t);
     for (e = 0, n = (i = this._updates0List).length; e < n; e++)(r = i[e]).paused || r.markedForDeletion || r.target.update(t);
     for (e = 0, n = (i = this._updatesPosList).length; e < n; e++)(r = i[e]).paused || r.markedForDeletion || r.target.update(t);
     var s, a = this._arrayForTimers;
     for (e = 0; e < a.length; e++) {
      if (s = a[e], this._currentTarget = s, this._currentTargetSalvaged = !1, !s.paused)
       for (s.timerIndex = 0; s.timerIndex < s.timers.length; ++s.timerIndex) s.currentTimer = s.timers[s.timerIndex], s.currentTimerSalvaged = !1, s.currentTimer.update(t), s.currentTimer = null;
      this._currentTargetSalvaged && 0 === this._currentTarget.timers.length && (this._removeHashElement(this._currentTarget), --e)
     }
     for (e = 0, i = this._updatesNegList; e < i.length;)(r = i[e]).markedForDeletion ? this._removeUpdateFromHash(r) : e++;
     for (e = 0, i = this._updates0List; e < i.length;)(r = i[e]).markedForDeletion ? this._removeUpdateFromHash(r) : e++;
     for (e = 0, i = this._updatesPosList; e < i.length;)(r = i[e]).markedForDeletion ? this._removeUpdateFromHash(r) : e++;
     this._updateHashLocked = !1, this._currentTarget = null
    },
    schedule: function(t, e, i, n, r, s) {
     "use strict";
     if ("function" != typeof t) {
      var a = t;
      t = e, e = a
     }
     4 !== arguments.length && 5 !== arguments.length || (s = !!n, n = cc.macro.REPEAT_FOREVER, r = 0), cc.assertID(e, 1502);
     var o = e._id;
     o || (e.__instanceId ? (cc.warnID(1513), o = e._id = e.__instanceId) : cc.errorID(1510));
     var c, u, _ = this._hashForTimers[o];
     if (_ ? _.paused !== s && cc.warnID(1511) : (_ = h.get(null, e, 0, null, null, s), this._arrayForTimers.push(_), this._hashForTimers[o] = _), null == _.timers) _.timers = [];
     else
      for (u = 0; u < _.timers.length; ++u)
       if ((c = _.timers[u]) && t === c._callback) return cc.logID(1507, c.getInterval(), i), void(c._interval = i);
     (c = l.get()).initWithCallback(this, t, e, i, n, r), _.timers.push(c), this._currentTarget === _ && this._currentTargetSalvaged && (this._currentTargetSalvaged = !1)
    },
    scheduleUpdate: function(t, e, i) {
     var n = t._id;
     n || (t.__instanceId ? (cc.warnID(1513), n = t._id = t.__instanceId) : cc.errorID(1510));
     var r = this._hashForUpdates[n];
     if (r && r.entry) {
      if (r.entry.priority === e) return r.entry.markedForDeletion = !1, void(r.entry.paused = i);
      if (this._updateHashLocked) return cc.logID(1506), r.entry.markedForDeletion = !1, void(r.entry.paused = i);
      this.unscheduleUpdate(t)
     }
     var a, c = s.get(t, e, i, !1);
     0 === e ? (a = this._updates0List, this._appendIn(a, c)) : (a = e < 0 ? this._updatesNegList : this._updatesPosList, this._priorityIn(a, c, e)), this._hashForUpdates[n] = o.get(a, c, t, null)
    },
    unschedule: function(t, e) {
     if (e && t) {
      var i = e._id;
      i || (e.__instanceId ? (cc.warnID(1513), i = e._id = e.__instanceId) : cc.errorID(1510));
      var n = this._hashForTimers[i];
      if (n)
       for (var r = n.timers, s = 0, a = r.length; s < a; s++) {
        var o = r[s];
        if (t === o._callback) return o !== n.currentTimer || n.currentTimerSalvaged || (n.currentTimerSalvaged = !0), r.splice(s, 1), l.put(o), n.timerIndex >= s && n.timerIndex--, void(0 === r.length && (this._currentTarget === n ? this._currentTargetSalvaged = !0 : this._removeHashElement(n)))
       }
     }
    },
    unscheduleUpdate: function(t) {
     if (t) {
      var e = t._id;
      e || (t.__instanceId ? (cc.warnID(1513), e = t._id = t.__instanceId) : cc.errorID(1510));
      var i = this._hashForUpdates[e];
      i && (this._updateHashLocked ? i.entry.markedForDeletion = !0 : this._removeUpdateFromHash(i.entry))
     }
    },
    unscheduleAllForTarget: function(t) {
     if (t) {
      var e = t._id;
      e || (t.__instanceId ? (cc.warnID(1513), e = t._id = t.__instanceId) : cc.errorID(1510));
      var i = this._hashForTimers[e];
      if (i) {
       var n = i.timers;
       n.indexOf(i.currentTimer) > -1 && !i.currentTimerSalvaged && (i.currentTimerSalvaged = !0);
       for (var r = 0, s = n.length; r < s; r++) l.put(n[r]);
       n.length = 0, this._currentTarget === i ? this._currentTargetSalvaged = !0 : this._removeHashElement(i)
      }
      this.unscheduleUpdate(t)
     }
    },
    unscheduleAll: function() {
     this.unscheduleAllWithMinPriority(cc.Scheduler.PRIORITY_SYSTEM)
    },
    unscheduleAllWithMinPriority: function(t) {
     var e, i, n, r = this._arrayForTimers;
     for (e = r.length - 1; e >= 0; e--) i = r[e], this.unscheduleAllForTarget(i.target);
     var s = 0;
     if (t < 0)
      for (e = 0; e < this._updatesNegList.length;) s = this._updatesNegList.length, (n = this._updatesNegList[e]) && n.priority >= t && this.unscheduleUpdate(n.target), s == this._updatesNegList.length && e++;
     if (t <= 0)
      for (e = 0; e < this._updates0List.length;) s = this._updates0List.length, (n = this._updates0List[e]) && this.unscheduleUpdate(n.target), s == this._updates0List.length && e++;
     for (e = 0; e < this._updatesPosList.length;) s = this._updatesPosList.length, (n = this._updatesPosList[e]) && n.priority >= t && this.unscheduleUpdate(n.target), s == this._updatesPosList.length && e++
    },
    isScheduled: function(t, e) {
     cc.assertID(t, 1508), cc.assertID(e, 1509);
     var i = e._id;
     i || (e.__instanceId ? (cc.warnID(1513), i = e._id = e.__instanceId) : cc.errorID(1510));
     var n = this._hashForTimers[i];
     if (!n) return !1;
     if (null == n.timers) return !1;
     for (var r = n.timers, s = 0; s < r.length; ++s) {
      if (t === r[s]._callback) return !0
     }
     return !1
    },
    pauseAllTargets: function() {
     return this.pauseAllTargetsWithMinPriority(cc.Scheduler.PRIORITY_SYSTEM)
    },
    pauseAllTargetsWithMinPriority: function(t) {
     var e, i, n, r, s = [],
      a = this._arrayForTimers;
     for (i = 0, n = a.length; i < n; i++)(e = a[i]) && (e.paused = !0, s.push(e.target));
     if (t < 0)
      for (i = 0; i < this._updatesNegList.length; i++)(r = this._updatesNegList[i]) && r.priority >= t && (r.paused = !0, s.push(r.target));
     if (t <= 0)
      for (i = 0; i < this._updates0List.length; i++)(r = this._updates0List[i]) && (r.paused = !0, s.push(r.target));
     for (i = 0; i < this._updatesPosList.length; i++)(r = this._updatesPosList[i]) && r.priority >= t && (r.paused = !0, s.push(r.target));
     return s
    },
    resumeTargets: function(t) {
     if (t)
      for (var e = 0; e < t.length; e++) this.resumeTarget(t[e])
    },
    pauseTarget: function(t) {
     cc.assertID(t, 1503);
     var e = t._id;
     e || (t.__instanceId ? (cc.warnID(1513), e = t._id = t.__instanceId) : cc.errorID(1510));
     var i = this._hashForTimers[e];
     i && (i.paused = !0);
     var n = this._hashForUpdates[e];
     n && (n.entry.paused = !0)
    },
    resumeTarget: function(t) {
     cc.assertID(t, 1504);
     var e = t._id;
     e || (t.__instanceId ? (cc.warnID(1513), e = t._id = t.__instanceId) : cc.errorID(1510));
     var i = this._hashForTimers[e];
     i && (i.paused = !1);
     var n = this._hashForUpdates[e];
     n && (n.entry.paused = !1)
    },
    isTargetPaused: function(t) {
     cc.assertID(t, 1505);
     var e = t._id;
     e || (t.__instanceId ? (cc.warnID(1513), e = t._id = t.__instanceId) : cc.errorID(1510));
     var i = this._hashForTimers[e];
     if (i) return i.paused;
     var n = this._hashForUpdates[e];
     return !!n && n.entry.paused
    }
   }, cc.Scheduler.PRIORITY_SYSTEM = 1 << 31, cc.Scheduler.PRIORITY_NON_SYSTEM = cc.Scheduler.PRIORITY_SYSTEM + 1, e.exports = cc.Scheduler
  }), {
   "./platform/id-generater": 124,
   "./platform/js": 128
  }],
  28: [(function(t, e, i) {
   var n = t("./CCRawAsset");
   cc.Asset = cc.Class({
    name: "cc.Asset",
    extends: n,
    ctor: function() {
     this.loaded = !0
    },
    properties: {
     nativeUrl: {
      get: function() {
       if (this._native) {
        var t = this._native;
        if (47 === t.charCodeAt(0)) return t.slice(1);
        if (cc.AssetLibrary) {
         var e = cc.AssetLibrary.getLibUrlNoExt(this._uuid, !0);
         return 46 === t.charCodeAt(0) ? e + t : e + "/" + t
        }
        cc.errorID(6400)
       }
       return ""
      },
      visible: !1
     },
     _native: "",
     _nativeAsset: {
      get: function() {},
      set: function(t) {}
     }
    },
    statics: {
     deserialize: !1,
     preventDeferredLoadDependents: !1,
     preventPreloadNativeObject: !1
    },
    toString: function() {
     return this.nativeUrl
    },
    serialize: !1,
    createNode: null,
    _setRawAsset: function(t, e) {
     this._native = !1 !== e ? t || void 0 : "/" + t
    }
   }), e.exports = cc.Asset
  }), {
   "./CCRawAsset": 36
  }],
  29: [(function(t, e, i) {
   var n = t("./CCAsset"),
    r = t("../event/event-target"),
    s = cc.Enum({
     WEB_AUDIO: 0,
     DOM_AUDIO: 1
    }),
    a = cc.Class({
     name: "cc.AudioClip",
     extends: n,
     mixins: [r],
     ctor: function() {
      this.loaded = !1, this._audio = null
     },
     properties: {
      loadMode: {
       default: s.WEB_AUDIO,
       type: s
      },
      _nativeAsset: {
       get: function() {
        return this._audio
       },
       set: function(t) {
        t instanceof cc.AudioClip ? this._audio = t._nativeAsset : this._audio = t, this._audio && (this.loaded = !0, this.emit("load"))
       },
       override: !0
      }
     },
     statics: {
      LoadMode: s,
      _loadByUrl: function(t, e) {
       var i = cc.loader.getItem(t) || cc.loader.getItem(t + "?useDom=1");
       i && i.complete ? i._owner instanceof a ? e(null, i._owner) : e(null, i.content) : cc.loader.load(t, (function(n, r) {
        if (n) return e(n);
        i = cc.loader.getItem(t) || cc.loader.getItem(t + "?useDom=1"), e(null, i.content)
       }))
      }
     },
     destroy: function() {
      cc.audioEngine.uncache(this), this._super()
     }
    });
   cc.AudioClip = a, e.exports = a
  }), {
   "../event/event-target": 77,
   "./CCAsset": 28
  }],
  30: [(function(t, e, i) {
   var n = cc.Class({
    name: "cc.BitmapFont",
    extends: cc.Font,
    properties: {
     fntDataStr: {
      default: ""
     },
     spriteFrame: {
      default: null,
      type: cc.SpriteFrame
     },
     fontSize: {
      default: -1
     },
     _fntConfig: null
    }
   });
   cc.BitmapFont = n, e.exports = n
  }), {}],
  31: [(function(t, e, i) {
   var n = cc.Class({
    name: "cc.BufferAsset",
    extends: cc.Asset,
    ctor: function() {
     this._buffer = null
    },
    properties: {
     _nativeAsset: {
      get: function() {
       return this._buffer
      },
      set: function(t) {
       this._buffer = t.buffer || t
      },
      override: !0
     }
    }
   });
   cc.BufferAsset = e.exports = n
  }), {}],
  32: [(function(t, e, i) {
   var n = cc.Class({
    name: "cc.Font",
    extends: cc.Asset
   });
   cc.Font = e.exports = n
  }), {}],
  33: [(function(t, e, i) {
   var n = cc.Class({
    name: "cc.JsonAsset",
    extends: cc.Asset,
    properties: {
     json: null
    }
   });
   e.exports = cc.JsonAsset = n
  }), {}],
  34: [(function(t, e, i) {
   var n = cc.Class({
    name: "cc.LabelAtlas",
    extends: cc.BitmapFont
   });
   cc.LabelAtlas = n, e.exports = n
  }), {}],
  35: [(function(t, e, i) {
   var n = cc.Enum({
     AUTO: 0,
     SINGLE_INSTANCE: 1,
     MULTI_INSTANCE: 2
    }),
    r = cc.Class({
     name: "cc.Prefab",
     extends: cc.Asset,
     ctor: function() {
      this._createFunction = null, this._instantiatedTimes = 0
     },
     properties: {
      data: null,
      optimizationPolicy: n.AUTO,
      asyncLoadAssets: !1
     },
     statics: {
      OptimizationPolicy: n,
      OptimizationPolicyThreshold: 3
     },
     createNode: !1,
     compileCreateFunction: function() {
      var e = t("../platform/instantiate-jit");
      this._createFunction = e.compile(this.data)
     },
     _doInstantiate: function(t) {
      return this.data._prefab ? this.data._prefab._synced = !0 : cc.warnID(3700), this._createFunction || this.compileCreateFunction(), this._createFunction(t)
     },
     _instantiate: function() {
      var t, e = !1;
      return e ? (t = this._doInstantiate(), this.data._instantiate(t)) : (this.data._prefab._synced = !0, t = this.data._instantiate()), ++this._instantiatedTimes, t
     }
    });
   cc.Prefab = e.exports = r, cc.js.obsolete(cc, "cc._Prefab", "Prefab")
  }), {
   "../platform/instantiate-jit": 126
  }],
  36: [(function(t, e, i) {
   var n = t("../platform/CCObject"),
    r = t("../platform/js");
   cc.RawAsset = cc.Class({
    name: "cc.RawAsset",
    extends: n,
    ctor: function() {
     Object.defineProperty(this, "_uuid", {
      value: "",
      writable: !0
     })
    }
   }), r.value(cc.RawAsset, "isRawAssetType", (function(t) {
    return r.isChildClassOf(t, cc.RawAsset) && !r.isChildClassOf(t, cc.Asset)
   })), r.value(cc.RawAsset, "wasRawAssetType", (function(t) {
    return t === cc.Texture2D || t === cc.AudioClip || t === cc.ParticleAsset || t === cc.Asset
   })), e.exports = cc.RawAsset
  }), {
   "../platform/CCObject": 114,
   "../platform/js": 128
  }],
  37: [(function(t, e, i) {
   var n = t("../renderer"),
    r = t("../renderer/render-engine"),
    s = r.gfx,
    a = t("./CCTexture2D"),
    o = cc.Class({
     name: "cc.RenderTexture",
     extends: a,
     ctor: function() {
      this._framebuffer = null
     },
     initWithSize: function(t, e, i) {
      this.width = Math.floor(t || cc.visibleRect.width), this.height = Math.floor(e || cc.visibleRect.height);
      var o = {};
      if (o.format = this._format, o.width = t, o.height = e, o.images = void 0, o.wrapS = this._wrapS, o.wrapT = this._wrapT, o.premultiplyAlpha = this._premultiplyAlpha, o.minFilter = a._FilterIndex[this._minFilter], o.magFilter = a._FilterIndex[this._magFilter], this._texture ? this._texture.update(o) : this._texture = new n.Texture2D(n.device, o), o = {
        colors: [this._texture]
       }, i) {
       var c = new s.RenderBuffer(n.device, i, t, e);
       i === s.RB_FMT_D24S8 ? o.depth = o.stencil = c : i === s.RB_FMT_S8 ? o.stencil = c : i === gl.RB_FMT_D16 && (o.depth = c)
      }
      this._framebuffer && this._framebuffer.destroy(), this._framebuffer = new r.gfx.FrameBuffer(n.device, t, e, o), this.loaded = !0, this.emit("load")
     },
     drawTextureAt: function(t, e, i) {
      t._image && this._texture.updateSubImage({
       x: e,
       y: i,
       image: t._image,
       width: t.width,
       height: t.height,
       level: 0,
       flipY: !1,
       premultiplyAlpha: t._premultiplyAlpha
      })
     },
     readPixels: function(t, e, i, r, s) {
      if (!this._framebuffer || !this._texture) return t;
      e = e || 0, i = i || 0;
      var a = r || this.width,
       o = s || this.height;
      t = t || new Uint8Array(a * o * 4);
      var c = n._forward._device._gl,
       h = c.getParameter(c.FRAMEBUFFER_BINDING);
      return c.bindFramebuffer(c.FRAMEBUFFER, this._framebuffer._glID), c.framebufferTexture2D(c.FRAMEBUFFER, c.COLOR_ATTACHMENT0, c.TEXTURE_2D, this._texture._glID, 0), c.readPixels(e, i, a, o, c.RGBA, c.UNSIGNED_BYTE, t), c.bindFramebuffer(c.FRAMEBUFFER, h), t
     },
     destroy: function() {
      this._super(), this._framebuffer && this._framebuffer.destroy()
     }
    });
   cc.RenderTexture = e.exports = o
  }), {
   "../renderer": 149,
   "../renderer/render-engine": 150,
   "./CCTexture2D": 44
  }],
  38: [(function(t, e, i) {
   var n = cc.Class({
    name: "cc.SceneAsset",
    extends: cc.Asset,
    properties: {
     scene: null,
     asyncLoadAssets: void 0
    }
   });
   cc.SceneAsset = n, e.exports = n
  }), {}],
  39: [(function(t, e, i) {
   var n = cc.Class({
    name: "cc.Script",
    extends: cc.Asset
   });
   cc._Script = n;
   var r = cc.Class({
    name: "cc.JavaScript",
    extends: n
   });
   cc._JavaScript = r;
   var s = cc.Class({
    name: "cc.CoffeeScript",
    extends: n
   });
   cc._CoffeeScript = s;
   var a = cc.Class({
    name: "cc.TypeScript",
    extends: n
   });
   cc._TypeScript = a
  }), {}],
  40: [(function(t, e, i) {
   var n = cc.Class({
    name: "cc.SpriteAtlas",
    extends: cc.Asset,
    properties: {
     _spriteFrames: {
      default: {}
     }
    },
    getTexture: function() {
     var t = Object.keys(this._spriteFrames);
     if (t.length > 0) {
      var e = this._spriteFrames[t[0]];
      return e ? e.getTexture() : null
     }
     return null
    },
    getSpriteFrame: function(t) {
     var e = this._spriteFrames[t];
     return e ? (e.name || (e.name = t), e) : null
    },
    getSpriteFrames: function() {
     var t = [],
      e = this._spriteFrames;
     for (var i in e) t.push(this.getSpriteFrame(i));
     return t
    }
   });
   cc.SpriteAtlas = n, e.exports = n
  }), {}],
  41: [(function(t, e, i) {
   var n = t("../event/event-target"),
    r = t("../utils/texture-util"),
    s = [{
     u: 0,
     v: 0
    }, {
     u: 0,
     v: 0
    }, {
     u: 0,
     v: 0
    }, {
     u: 0,
     v: 0
    }],
    a = cc.Class({
     name: "cc.SpriteFrame",
     extends: t("../assets/CCAsset"),
     mixins: [n],
     properties: {
      _textureSetter: {
       set: function(t) {
        t && (this._texture !== t && this._refreshTexture(t), this._textureFilename = t.url)
       }
      },
      insetTop: {
       get: function() {
        return this._capInsets[1]
       },
       set: function(t) {
        this._capInsets[1] = t, this._texture && this._calculateSlicedUV()
       }
      },
      insetBottom: {
       get: function() {
        return this._capInsets[3]
       },
       set: function(t) {
        this._capInsets[3] = t, this._texture && this._calculateSlicedUV()
       }
      },
      insetLeft: {
       get: function() {
        return this._capInsets[0]
       },
       set: function(t) {
        this._capInsets[0] = t, this._texture && this._calculateSlicedUV()
       }
      },
      insetRight: {
       get: function() {
        return this._capInsets[2]
       },
       set: function(t) {
        this._capInsets[2] = t, this._texture && this._calculateSlicedUV()
       }
      }
     },
     ctor: function() {
      n.call(this);
      var t = arguments[0],
       e = arguments[1],
       i = arguments[2],
       r = arguments[3],
       s = arguments[4];
      this._rect = null, this.uv = [], this._texture = null, this._original = null, this._offset = null, this._originalSize = null, this._rotated = !1, this.vertices = null, this._capInsets = [0, 0, 0, 0], this.uvSliced = [], this._textureFilename = "", void 0 !== t && this.setTexture(t, e, i, r, s)
     },
     textureLoaded: function() {
      return this._texture && this._texture.loaded
     },
     isRotated: function() {
      return this._rotated
     },
     setRotated: function(t) {
      this._rotated = t, this._texture && this._calculateUV()
     },
     getRect: function() {
      return cc.rect(this._rect)
     },
     setRect: function(t) {
      this._rect = t, this._texture && this._calculateUV()
     },
     getOriginalSize: function() {
      return cc.size(this._originalSize)
     },
     setOriginalSize: function(t) {
      this._originalSize ? (this._originalSize.width = t.width, this._originalSize.height = t.height) : this._originalSize = cc.size(t)
     },
     getTexture: function() {
      return this._texture
     },
     _textureLoadedCallback: function() {
      var t = this._texture;
      if (t) {
       var e = t.width,
        i = t.height;
       this._rotated && cc.game.renderType === cc.game.RENDER_TYPE_CANVAS && (this._rotated = !1, e = this._texture.width, i = this._texture.height, this._rect = cc.rect(0, 0, e, i)), this._rect ? this._checkRect(this._texture) : this._rect = cc.rect(0, 0, e, i), this._originalSize || this.setOriginalSize(cc.size(e, i)), this._offset || this.setOffset(cc.v2(0, 0)), this._calculateUV(), this.emit("load")
      }
     },
     _refreshTexture: function(t) {
      this._texture = t, t.loaded ? this._textureLoadedCallback() : t.once("load", this._textureLoadedCallback, this)
     },
     getOffset: function() {
      return cc.v2(this._offset)
     },
     setOffset: function(t) {
      this._offset = cc.v2(t)
     },
     clone: function() {
      return new a(this._texture || this._textureFilename, this._rect, this._rotated, this._offset, this._originalSize)
     },
     setTexture: function(t, e, i, n, r) {
      this._rect = e || null, n ? this.setOffset(n) : this._offset = null, r ? this.setOriginalSize(r) : this._originalSize = null, this._rotated = i || !1;
      var s = t;
      return "string" == typeof s && s && (this._textureFilename = s, this._loadTexture()), s instanceof cc.Texture2D && this._texture !== s && this._refreshTexture(s), !0
     },
     _loadTexture: function() {
      if (this._textureFilename) {
       var t = r.loadImage(this._textureFilename);
       this._refreshTexture(t)
      }
     },
     ensureLoadTexture: function() {
      this._texture ? this._texture.loaded || (this._refreshTexture(this._texture), r.postLoadTexture(this._texture)) : this._textureFilename && this._loadTexture()
     },
     clearTexture: function() {
      this._texture = null
     },
     _checkRect: function(t) {
      var e = this._rect,
       i = e.x,
       n = e.y;
      this._rotated ? (i += e.height, n += e.width) : (i += e.width, n += e.height), i > t.width && cc.errorID(3300, t.url + "/" + this.name, i, t.width), n > t.height && cc.errorID(3400, t.url + "/" + this.name, n, t.height)
     },
     _calculateSlicedUV: function() {
      var t = this._rect,
       e = this._texture.width,
       i = this._texture.height,
       n = this._capInsets[0],
       r = this._capInsets[2],
       a = t.width - n - r,
       o = this._capInsets[1],
       c = this._capInsets[3],
       h = t.height - o - c,
       u = this.uvSliced;
      if (u.length = 0, this._rotated) {
       s[0].u = t.x / e, s[1].u = (t.x + c) / e, s[2].u = (t.x + c + h) / e, s[3].u = (t.x + t.height) / e, s[3].v = t.y / i, s[2].v = (t.y + n) / i, s[1].v = (t.y + n + a) / i, s[0].v = (t.y + t.width) / i;
       for (var l = 0; l < 4; ++l)
        for (var _ = s[l], d = 0; d < 4; ++d) {
         var f = s[3 - d];
         u.push({
          u: _.u,
          v: f.v
         })
        }
      } else {
       s[0].u = t.x / e, s[1].u = (t.x + n) / e, s[2].u = (t.x + n + a) / e, s[3].u = (t.x + t.width) / e, s[3].v = t.y / i, s[2].v = (t.y + o) / i, s[1].v = (t.y + o + h) / i, s[0].v = (t.y + t.height) / i;
       for (var p = 0; p < 4; ++p)
        for (var m = s[p], v = 0; v < 4; ++v) {
         var g = s[v];
         u.push({
          u: g.u,
          v: m.v
         })
        }
      }
     },
     _setDynamicAtlasFrame: function(t) {
      t && (this._original = {
       _texture: this._texture,
       _x: this._rect.x,
       _y: this._rect.y
      }, this._texture = t.texture, this._rect.x = t.x, this._rect.y = t.y, this._calculateUV())
     },
     _resetDynamicAtlasFrame: function() {
      this._original && (this._rect.x = this._original._x, this._rect.y = this._original._y, this._texture = this._original._texture, this._original = null, this._calculateUV())
     },
     _calculateUV: function() {
      var t = this._rect,
       e = this._texture,
       i = this.uv,
       n = e.width,
       r = e.height;
      if (this._rotated) {
       var s = 0 === n ? 0 : t.x / n,
        a = 0 === n ? 0 : (t.x + t.height) / n,
        o = 0 === r ? 0 : (t.y + t.width) / r,
        c = 0 === r ? 0 : t.y / r;
       i[0] = s, i[1] = c, i[2] = s, i[3] = o, i[4] = a, i[5] = c, i[6] = a, i[7] = o
      } else {
       var h = 0 === n ? 0 : t.x / n,
        u = 0 === n ? 0 : (t.x + t.width) / n,
        l = 0 === r ? 0 : (t.y + t.height) / r,
        _ = 0 === r ? 0 : t.y / r;
       i[0] = h, i[1] = l, i[2] = u, i[3] = l, i[4] = h, i[5] = _, i[6] = u, i[7] = _
      }
      var d = this.vertices;
      if (d) {
       d.nu.length = 0, d.nv.length = 0;
       for (var f = 0; f < d.u.length; f++) d.nu[f] = d.u[f] / n, d.nv[f] = d.v[f] / r
      }
      this._calculateSlicedUV()
     },
     _serialize: !1,
     _deserialize: function(t, e) {
      var i = t.rect;
      i && (this._rect = new cc.Rect(i[0], i[1], i[2], i[3])), t.offset && this.setOffset(new cc.Vec2(t.offset[0], t.offset[1])), t.originalSize && this.setOriginalSize(new cc.Size(t.originalSize[0], t.originalSize[1])), this._rotated = 1 === t.rotated, this._name = t.name;
      var n = t.capInsets;
      n && (this._capInsets[0] = n[0], this._capInsets[1] = n[1], this._capInsets[2] = n[2], this._capInsets[3] = n[3]), this.vertices = t.vertices, this.vertices && (this.vertices.nu = [], this.vertices.nv = []);
      var r = t.texture;
      r && e.result.push(this, "_textureSetter", r)
     }
    }),
    o = a.prototype;
   o.copyWithZone = o.clone, o.copy = o.clone, o.initWithTexture = o.setTexture, cc.SpriteFrame = a, e.exports = a
  }), {
   "../assets/CCAsset": 28,
   "../event/event-target": 77,
   "../utils/texture-util": 194
  }],
  42: [(function(t, e, i) {
   var n = t("./CCFont"),
    r = cc.Class({
     name: "cc.TTFFont",
     extends: n,
     properties: {
      _fontFamily: null,
      _nativeAsset: {
       type: cc.String,
       get: function() {
        return this._fontFamily
       },
       set: function(t) {
        this._fontFamily = t || "Arial"
       },
       override: !0
      }
     }
    });
   cc.TTFFont = e.exports = r
  }), {
   "./CCFont": 32
  }],
  43: [(function(t, e, i) {
   var n = cc.Class({
    name: "cc.TextAsset",
    extends: cc.Asset,
    properties: {
     text: ""
    },
    toString: function() {
     return this.text
    }
   });
   e.exports = cc.TextAsset = n
  }), {}],
  44: [(function(t, e, i) {
   var n = t("../event/event-target"),
    r = t("../renderer/render-engine"),
    s = t("../renderer");
   t("../platform/CCClass");
   var a = r.gfx,
    o = new(t("../platform/id-generater"))("Tex"),
    c = cc.Enum({
     RGB565: a.TEXTURE_FMT_R5_G6_B5,
     RGB5A1: a.TEXTURE_FMT_R5_G5_B5_A1,
     RGBA4444: a.TEXTURE_FMT_R4_G4_B4_A4,
     RGB888: a.TEXTURE_FMT_RGB8,
     RGBA8888: a.TEXTURE_FMT_RGBA8,
     A8: a.TEXTURE_FMT_A8,
     I8: a.TEXTURE_FMT_L8,
     AI8: a.TEXTURE_FMT_L8_A8
    }),
    h = cc.Enum({
     REPEAT: 10497,
     CLAMP_TO_EDGE: 33071,
     MIRRORED_REPEAT: 33648
    }),
    u = cc.Enum({
     LINEAR: 9729,
     NEAREST: 9728
    }),
    l = {
     9728: 0,
     9729: 1
    },
    _ = [],
    d = {
     width: void 0,
     height: void 0,
     minFilter: void 0,
     magFilter: void 0,
     wrapS: void 0,
     wrapT: void 0,
     format: void 0,
     mipmap: void 0,
     images: void 0,
     image: void 0,
     flipY: void 0,
     premultiplyAlpha: void 0
    };

   function f() {
    for (var t in d) d[t] = void 0;
    return _.length = 0, d.images = _, d.flipY = !1, d
   }
   var p = cc.Class({
    name: "cc.Texture2D",
    extends: t("../assets/CCAsset"),
    mixins: [n],
    properties: {
     _nativeAsset: {
      get: function() {
       return this._image
      },
      set: function(t) {
       this.initWithElement(t)
      },
      override: !0
     },
     _hasMipmap: !1,
     _format: c.RGBA8888,
     _premultiplyAlpha: !1,
     _flipY: !1,
     _minFilter: u.LINEAR,
     _magFilter: u.LINEAR,
     _wrapS: h.CLAMP_TO_EDGE,
     _wrapT: h.CLAMP_TO_EDGE
    },
    statics: {
     PixelFormat: c,
     WrapMode: h,
     Filter: u,
     _FilterIndex: l,
     extnames: [".png", ".jpg", ".jpeg", ".bmp", ".webp"]
    },
    ctor: function() {
     this._id = o.getNewId(), this.url = "", this.loaded = !1, this.width = 0, this.height = 0, this._hashDirty = !0, this._hash = 0, this._texture = null
    },
    getImpl: function() {
     return this._texture
    },
    getId: function() {
     return this._id
    },
    toString: function() {
     return this.url || ""
    },
    update: function(t) {
     if (t) {
      var e = !1;
      void 0 !== t.width && (this.width = t.width), void 0 !== t.height && (this.height = t.height), void 0 !== t.minFilter && (this._minFilter = t.minFilter, t.minFilter = l[t.minFilter]), void 0 !== t.magFilter && (this._magFilter = t.magFilter, t.magFilter = l[t.magFilter]), void 0 !== t.wrapS && (this._wrapS = t.wrapS), void 0 !== t.wrapT && (this._wrapT = t.wrapT), void 0 !== t.format && (this._format = t.format), void 0 !== t.flipY && (this._flipY = t.flipY, e = !0), void 0 !== t.premultiplyAlpha && (this._premultiplyAlpha = t.premultiplyAlpha, e = !0), void 0 !== t.mipmap && (this._hasMipmap = t.mipmap), e && this._image && (t.image = this._image), t.images && t.images.length > 0 ? this._image = t.images[0] : void 0 !== t.image && (this._image = t.image, t.images || (_.length = 0, t.images = _), t.images.push(t.image)), t.images && t.images.length > 0 && this._texture.update(t), this._hashDirty = !0
     }
    },
    initWithElement: function(t) {
     t && (this._image = t, this.handleLoadedTexture())
    },
    initWithData: function(t, e, i, n) {
     var r = f();
     return r.image = t, r.images = [r.image], r.hasMipmap = this._hasMipmap, r.premultiplyAlpha = this._premultiplyAlpha, r.flipY = this._flipY, r.minFilter = l[this._minFilter], r.magFilter = l[this._magFilter], r.wrapS = this._wrapS, r.wrapT = this._wrapT, r.format = e, r.width = i, r.height = n, this._texture ? this.update(r) : this._texture = new s.Texture2D(s.device, r), this.width = i, this.height = n, this.loaded = !0, this.emit("load"), !0
    },
    getHtmlElementObj: function() {
     return this._image
    },
    destroy: function() {
     this._image = null, this._texture && this._texture.destroy(), this._super()
    },
    getPixelFormat: function() {
     return this._format
    },
    hasPremultipliedAlpha: function() {
     return this._premultiplyAlpha || !1
    },
    hasMipmap: function() {
     return this._hasMipmap || !1
    },
    handleLoadedTexture: function() {
     if (this._image && this._image.width && this._image.height) {
      this.width = this._image.width, this.height = this._image.height;
      var t = f();
      t.image = this._image, t.images = [t.image], t.width = this.width, t.height = this.height, t.hasMipmap = this._hasMipmap, t.format = this._format, t.premultiplyAlpha = this._premultiplyAlpha, t.flipY = this._flipY, t.minFilter = l[this._minFilter], t.magFilter = l[this._magFilter], t.wrapS = this._wrapS, t.wrapT = this._wrapT, this._texture ? this._texture.update(t) : this._texture = new s.Texture2D(s.device, t), this.loaded = !0, this.emit("load"), cc.macro.CLEANUP_IMAGE_CACHE && this._image instanceof HTMLImageElement && (this._image.src = "", cc.loader.removeItem(this._image.id))
     }
    },
    description: function() {
     return "<cc.Texture2D | Name = " + this.url + " | Dimensions = " + this.width + " x " + this.height + ">"
    },
    releaseTexture: function() {
     this._image = null, this._texture && this._texture.destroy()
    },
    setWrapMode: function(t, e) {
     if (this._wrapS !== t || this._wrapT !== e) {
      var i = f();
      i.wrapS = t, i.wrapT = e, this.update(i)
     }
    },
    setFilters: function(t, e) {
     if (this._minFilter !== t || this._magFilter !== e) {
      var i = f();
      i.minFilter = t, i.magFilter = e, this.update(i)
     }
    },
    setFlipY: function(t) {
     if (this._flipY !== t) {
      var e = f();
      e.flipY = t, this.update(e)
     }
    },
    setPremultiplyAlpha: function(t) {
     if (this._premultiplyAlpha !== t) {
      var e = f();
      e.premultiplyAlpha = t, this.update(e)
     }
    },
    setMipmap: function(t) {
     if (this._hasMipmap !== t) {
      var e = f();
      e.hasMipmap = t, this.update(e)
     }
    },
    _serialize: !1,
    _deserialize: function(t, e) {
     var i = t.split(","),
      n = i[0];
     if (n) {
      var r = n.charCodeAt(0) - 48,
       s = p.extnames[r];
      this._setRawAsset(s || n);
      var a = e.customEnv,
       o = a && a.uuid;
      if (o) {
       this._uuid = o;
       var c = this.nativeUrl;
       this.url = c
      }
     }
     6 === i.length && (this._minFilter = parseInt(i[1]), this._magFilter = parseInt(i[2]), this._wrapS = parseInt(i[3]), this._wrapT = parseInt(i[4]), this._premultiplyAlpha = 49 === i[5].charCodeAt(0))
    },
    _getHash: function() {
     if (!this._hashDirty) return this._hash;
     var t = this._hasMipmap ? 1 : 0,
      e = this._premultiplyAlpha ? 1 : 0,
      i = this._flipY ? 1 : 0,
      n = this._minFilter === u.LINEAR ? 1 : 2,
      r = this._magFilter === u.LINEAR ? 1 : 2,
      s = this._wrapS === h.REPEAT ? 1 : this._wrapS === h.CLAMP_TO_EDGE ? 2 : 3,
      a = this._wrapT === h.REPEAT ? 1 : this._wrapT === h.CLAMP_TO_EDGE ? 2 : 3,
      o = this._format;
     return this._hash = parseInt("" + n + r + o + s + a + t + e + i), this._hashDirty = !1, this._hash
    }
   });
   cc.Texture2D = e.exports = p
  }), {
   "../assets/CCAsset": 28,
   "../event/event-target": 77,
   "../platform/CCClass": 108,
   "../platform/id-generater": 124,
   "../renderer": 149,
   "../renderer/render-engine": 150
  }],
  45: [(function(t, e, i) {
   t("./CCRawAsset"), t("./CCAsset"), t("./CCFont"), t("./CCPrefab"), t("./CCAudioClip"), t("./CCScripts"), t("./CCSceneAsset"), t("./CCSpriteFrame"), t("./CCTexture2D"), t("./CCRenderTexture"), t("./CCTTFFont"), t("./CCSpriteAtlas"), t("./CCBitmapFont"), t("./CCLabelAtlas"), t("./CCTextAsset"), t("./CCJsonAsset"), t("./CCBufferAsset")
  }), {
   "./CCAsset": 28,
   "./CCAudioClip": 29,
   "./CCBitmapFont": 30,
   "./CCBufferAsset": 31,
   "./CCFont": 32,
   "./CCJsonAsset": 33,
   "./CCLabelAtlas": 34,
   "./CCPrefab": 35,
   "./CCRawAsset": 36,
   "./CCRenderTexture": 37,
   "./CCSceneAsset": 38,
   "./CCScripts": 39,
   "./CCSpriteAtlas": 40,
   "./CCSpriteFrame": 41,
   "./CCTTFFont": 42,
   "./CCTextAsset": 43,
   "./CCTexture2D": 44
  }],
  46: [(function(t, e, i) {
   var n = t("../utils/affine-transform"),
    r = t("../renderer/render-engine"),
    s = t("../renderer/index"),
    a = t("../CCGame"),
    o = cc.vmath.mat4,
    c = cc.vmath.vec2,
    h = cc.vmath.vec3,
    u = o.create(),
    l = o.create(),
    _ = h.create(),
    d = [],
    f = null;

   function p() {
    if (f) {
     var t = f._node,
      e = cc.visibleRect;
     t.z = e.height / 1.1566, t.x = _.x = e.width / 2, t.y = _.y = e.height / 2, _.z = 0, t.lookAt(_)
    }
   }
   var m = cc.Enum({
     COLOR: 1,
     DEPTH: 2,
     STENCIL: 4
    }),
    v = cc.Class({
     name: "cc.Camera",
     extends: cc.Component,
     ctor: function() {
      if (a.renderType !== a.RENDER_TYPE_CANVAS) {
       var t = new r.Camera;
       t.setStages(["transparent"]), this._fov = 60 * Math.PI / 180, t.setFov(this._fov), t.setNear(.1), t.setFar(4096);
       var e = new r.View;
       t.view = e, t.dirty = !0, this._matrixDirty = !0, this._inited = !1, this._camera = t
      } else this._inited = !0
     },
     editor: !1,
     properties: {
      _cullingMask: 4294967295,
      _clearFlags: m.DEPTH | m.STENCIL,
      _backgroundColor: cc.color(0, 0, 0, 255),
      _depth: 0,
      _zoomRatio: 1,
      _targetTexture: null,
      zoomRatio: {
       get: function() {
        return this._zoomRatio
       },
       set: function(t) {
        this._zoomRatio = t, this._matrixDirty = !0
       }
      },
      cullingMask: {
       get: function() {
        return this._cullingMask
       },
       set: function(t) {
        this._cullingMask = t, this._updateCameraMask()
       }
      },
      clearFlags: {
       get: function() {
        return this._clearFlags
       },
       set: function(t) {
        this._clearFlags = t, this._camera && this._camera.setClearFlags(t)
       }
      },
      backgroundColor: {
       get: function() {
        return this._backgroundColor
       },
       set: function(t) {
        this._backgroundColor = t, this._updateBackgroundColor()
       }
      },
      depth: {
       get: function() {
        return this._depth
       },
       set: function(t) {
        this._depth = t, this._camera && this._camera.setDepth(t)
       }
      },
      targetTexture: {
       get: function() {
        return this._targetTexture
       },
       set: function(t) {
        this._targetTexture = t, this._updateTargetTexture()
       }
      }
     },
     statics: {
      main: null,
      cameras: d,
      ClearFlags: m,
      findCamera: function(t) {
       for (var e = 0, i = d.length; e < i; e++) {
        var n = d[e];
        if (n.containsNode(t)) return n
       }
       return null
      },
      _setupDebugCamera: function() {
       if (!f && a.renderType !== a.RENDER_TYPE_CANVAS) {
        var t = new r.Camera;
        f = t, t.setStages(["transparent"]), t.setFov(60 * Math.PI / 180), t.setNear(.1), t.setFar(4096);
        var e = new r.View;
        t.view = e, t.dirty = !0, t._cullingMask = t.view._cullingMask = 1 << cc.Node.BuiltinGroupIndex.DEBUG, t.setDepth(cc.macro.MAX_ZINDEX), t.setClearFlags(0), t.setColor(0, 0, 0, 0);
        var i = new cc.Node;
        t.setNode(i), p(), cc.view.on("design-resolution-changed", p), s.scene.addCamera(t)
       }
      }
     },
     _updateCameraMask: function() {
      if (this._camera) {
       var t = this._cullingMask & ~(1 << cc.Node.BuiltinGroupIndex.DEBUG);
       this._camera._cullingMask = t, this._camera.view._cullingMask = t
      }
     },
     _updateBackgroundColor: function() {
      if (this._camera) {
       var t = this._backgroundColor;
       this._camera.setColor(t.r / 255, t.g / 255, t.b / 255, t.a / 255)
      }
     },
     _updateTargetTexture: function() {
      var t = this._targetTexture;
      this._camera && (this._camera._framebuffer = t ? t._framebuffer : null)
     },
     _onMatrixDirty: function() {
      this._matrixDirty = !0
     },
     _init: function() {
      this._inited || (this._inited = !0, this._camera && (this._camera.setNode(this.node), this._camera.setClearFlags(this._clearFlags), this._camera.setDepth(this._depth), this._updateBackgroundColor(), this._updateCameraMask(), this._updateTargetTexture()))
     },
     onLoad: function() {
      this._init()
     },
     onEnable: function() {
      this._matrixDirty = !0, a.renderType !== a.RENDER_TYPE_CANVAS && (cc.director.on(cc.Director.EVENT_BEFORE_DRAW, this.beforeDraw, this), s.scene.addCamera(this._camera)), d.push(this)
     },
     onDisable: function() {
      a.renderType !== a.RENDER_TYPE_CANVAS && (cc.director.off(cc.Director.EVENT_BEFORE_DRAW, this.beforeDraw, this), s.scene.removeCamera(this._camera)), cc.js.array.remove(d, this)
     },
     getNodeToCameraTransform: function(t) {
      var e = n.identity();
      return t.getWorldMatrix(l), this.containsNode(t) && (this.getWorldToCameraMatrix(u), o.mul(l, l, u)), n.fromMat4(e, l), e
     },
     getCameraToWorldPoint: function(t, e) {
      return e = e || cc.v2(), this.getCameraToWorldMatrix(u), c.transformMat4(e, t, u), e
     },
     getWorldToCameraPoint: function(t, e) {
      return e = e || cc.v2(), this.getWorldToCameraMatrix(u), c.transformMat4(e, t, u), e
     },
     getCameraToWorldMatrix: function(t) {
      return this.getWorldToCameraMatrix(t), o.invert(t, t), t
     },
     getWorldToCameraMatrix: function(t) {
      this.node.getWorldRT(u);
      var e = this.zoomRatio;
      u.m00 *= e, u.m01 *= e, u.m04 *= e, u.m05 *= e;
      var i = u.m12,
       n = u.m13,
       r = cc.visibleRect.center;
      return u.m12 = r.x - (u.m00 * i + u.m04 * n), u.m13 = r.y - (u.m01 * i + u.m05 * n), t !== u && o.copy(t, u), t
     },
     containsNode: function(t) {
      return t._cullingMask & this.cullingMask
     },
     render: function(t) {
      if (!(t = t || cc.director.getScene())) return null;
      this.node.getWorldMatrix(u), this.beforeDraw(), s._walker.visit(t), s._forward.renderCamera(this._camera, s.scene)
     },
     beforeDraw: function() {
      var t = this.node;
      if (this._matrixDirty || t._worldMatDirty) {
       var e = this._camera,
        i = 2 * Math.atan(Math.tan(this._fov / 2) / this.zoomRatio);
       e.setFov(i);
       var n = cc.game.canvas.height / cc.view._scaleY,
        r = this._targetTexture;
       r && (n = r.height), t._updateWorldMatrix(), _.x = t._worldMatrix.m12, _.y = t._worldMatrix.m13, _.z = 0, t.z = n / (2 * Math.tan(this._fov / 2)), t.lookAt(_), this._matrixDirty = !1, e.dirty = !0
      }
     }
    });
   e.exports = cc.Camera = v
  }), {
   "../CCGame": 23,
   "../renderer/index": 149,
   "../renderer/render-engine": 150,
   "../utils/affine-transform": 179
  }],
  47: [(function(t, e, i) {
   var n = {};

   function r(t, e, i, n) {
    var r = (n.x - i.x) * (t.y - i.y) - (n.y - i.y) * (t.x - i.x),
     s = (e.x - t.x) * (t.y - i.y) - (e.y - t.y) * (t.x - i.x),
     a = (n.y - i.y) * (e.x - t.x) - (n.x - i.x) * (e.y - t.y);
    if (0 !== a) {
     var o = r / a,
      c = s / a;
     if (0 <= o && o <= 1 && 0 <= c && c <= 1) return !0
    }
    return !1
   }

   function s(t, e, i) {
    for (var n = i.length, s = 0; s < n; ++s) {
     if (r(t, e, i[s], i[(s + 1) % n])) return !0
    }
    return !1
   }

   function a(t, e) {
    for (var i = !1, n = t.x, r = t.y, s = e.length, a = 0, o = s - 1; a < s; o = a++) {
     var c = e[a].x,
      h = e[a].y,
      u = e[o].x,
      l = e[o].y;
     h > r != l > r && n < (u - c) * (r - h) / (l - h) + c && (i = !i)
    }
    return i
   }

   function o(t, e, i, n) {
    var r, s = i.x - e.x,
     a = i.y - e.y,
     o = s * s + a * a,
     c = ((t.x - e.x) * s + (t.y - e.y) * a) / o;
    return r = n ? o ? c < 0 ? e : c > 1 ? i : cc.v2(e.x + c * s, e.y + c * a) : e : cc.v2(e.x + c * s, e.y + c * a), s = t.x - r.x, a = t.y - r.y, Math.sqrt(s * s + a * a)
   }
   n.lineLine = r, n.lineRect = function(t, e, i) {
    var n = new cc.Vec2(i.x, i.y),
     s = new cc.Vec2(i.x, i.yMax),
     a = new cc.Vec2(i.xMax, i.yMax),
     o = new cc.Vec2(i.xMax, i.y);
    return !!(r(t, e, n, s) || r(t, e, s, a) || r(t, e, a, o) || r(t, e, o, n))
   }, n.linePolygon = s, n.rectRect = function(t, e) {
    var i = t.x,
     n = t.y,
     r = t.x + t.width,
     s = t.y + t.height,
     a = e.x,
     o = e.y,
     c = e.x + e.width,
     h = e.y + e.height;
    return i <= c && r >= a && n <= h && s >= o
   }, n.rectPolygon = function(t, e) {
    var i, n, r = new cc.Vec2(t.x, t.y),
     o = new cc.Vec2(t.x, t.yMax),
     c = new cc.Vec2(t.xMax, t.yMax),
     h = new cc.Vec2(t.xMax, t.y);
    if (s(r, o, e)) return !0;
    if (s(o, c, e)) return !0;
    if (s(c, h, e)) return !0;
    if (s(h, r, e)) return !0;
    for (i = 0, n = e.length; i < n; ++i)
     if (a(e[i], t)) return !0;
    return !!(a(r, e) || a(o, e) || a(c, e) || a(h, e))
   }, n.polygonPolygon = function(t, e) {
    var i, n;
    for (i = 0, n = t.length; i < n; ++i)
     if (s(t[i], t[(i + 1) % n], e)) return !0;
    for (i = 0, n = e.length; i < n; ++i)
     if (a(e[i], t)) return !0;
    for (i = 0, n = t.length; i < n; ++i)
     if (a(t[i], e)) return !0;
    return !1
   }, n.circleCircle = function(t, e) {
    return t.position.sub(e.position).mag() < t.radius + e.radius
   }, n.polygonCircle = function(t, e) {
    var i = e.position;
    if (a(i, t)) return !0;
    for (var n = 0, r = t.length; n < r; n++)
     if (o(i, 0 === n ? t[t.length - 1] : t[n - 1], t[n], !0) < e.radius) return !0;
    return !1
   }, n.pointInPolygon = a, n.pointLineDistance = o, cc.Intersection = e.exports = n
  }), {}],
  48: [(function(t, e, i) {
   t("./platform/CCClass");
   var n = t("./platform/CCObject").Flags,
    r = t("./platform/js").array,
    s = n.IsStartCalled,
    a = n.IsOnEnableCalled,
    o = (n.IsEditorOnEnableCalled, function(t) {
     t.start(), t._objFlags |= s
    }),
    c = function(t, e) {
     t.update(e)
    },
    h = function(t, e) {
     t.lateUpdate(e)
    };

   function u(t, e) {
    for (var i = e.constructor._executionOrder, n = e._id, r = 0, s = t.length - 1, a = s >>> 1; r <= s; a = r + s >>> 1) {
     var o = t[a],
      c = o.constructor._executionOrder;
     if (c > i) s = a - 1;
     else if (c < i) r = a + 1;
     else {
      var h = o._id;
      if (h > n) s = a - 1;
      else {
       if (!(h < n)) return a;
       r = a + 1
      }
     }
    }
    return ~r
   }

   function l(t, e) {
    for (var i = t.array, n = t.i + 1; n < i.length;) {
     var r = i[n];
     r._enabled && r.node._activeInHierarchy ? ++n : (t.removeAt(n), e && (r._objFlags &= ~e))
    }
   }
   var _ = cc.Class({
    __ctor__: function(t) {
     var e = r.MutableForwardIterator;
     this._zero = new e([]), this._neg = new e([]), this._pos = new e([]), this._invoke = t
    },
    statics: {
     stableRemoveInactive: l
    },
    add: null,
    remove: null,
    invoke: null
   });

   function d(t, e) {
    return t.constructor._executionOrder - e.constructor._executionOrder
   }
   var f = cc.Class({
     extends: _,
     add: function(t) {
      var e = t.constructor._executionOrder;
      (0 === e ? this._zero : e < 0 ? this._neg : this._pos).array.push(t)
     },
     remove: function(t) {
      var e = t.constructor._executionOrder;
      (0 === e ? this._zero : e < 0 ? this._neg : this._pos).fastRemove(t)
     },
     cancelInactive: function(t) {
      l(this._zero, t), l(this._neg, t), l(this._pos, t)
     },
     invoke: function() {
      var t = this._neg;
      t.array.length > 0 && (t.array.sort(d), this._invoke(t), t.array.length = 0), this._invoke(this._zero), this._zero.array.length = 0;
      var e = this._pos;
      e.array.length > 0 && (e.array.sort(d), this._invoke(e), e.array.length = 0)
     }
    }),
    p = cc.Class({
     extends: _,
     add: function(t) {
      var e = t.constructor._executionOrder;
      if (0 === e) this._zero.array.push(t);
      else {
       var i = e < 0 ? this._neg.array : this._pos.array,
        n = u(i, t);
       n < 0 && i.splice(~n, 0, t)
      }
     },
     remove: function(t) {
      var e = t.constructor._executionOrder;
      if (0 === e) this._zero.fastRemove(t);
      else {
       var i = e < 0 ? this._neg : this._pos,
        n = u(i.array, t);
       n >= 0 && i.removeAt(n)
      }
     },
     invoke: function(t) {
      this._neg.array.length > 0 && this._invoke(this._neg, t), this._invoke(this._zero, t), this._pos.array.length > 0 && this._invoke(this._pos, t)
     }
    });

   function m(t, e) {
    if ("function" == typeof t) return e ? function(e, i) {
     var n = e.array;
     for (e.i = 0; e.i < n.length; ++e.i) {
      var r = n[e.i];
      t(r, i)
     }
    } : function(e) {
     var i = e.array;
     for (e.i = 0; e.i < i.length; ++e.i) {
      var n = i[e.i];
      t(n)
     }
    };
    var i = "var a=it.array;for(it.i=0;it.i<a.length;++it.i){var c=a[it.i];" + t + "}";
    return e ? Function("it", "dt", i) : Function("it", i)
   }

   function v() {
    this.startInvoker = new f(m(o)), this.updateInvoker = new p(m(c, !0)), this.lateUpdateInvoker = new p(m(h, !0)), this.scheduleInNextFrame = [], this._updating = !1
   }
   var g = cc.Class({
    ctor: v,
    unscheduleAll: v,
    statics: {
     LifeCycleInvoker: _,
     OneOffInvoker: f,
     createInvokeImpl: m,
     invokeOnEnable: function(t) {
      var e = cc.director._compScheduler,
       i = t.array;
      for (t.i = 0; t.i < i.length; ++t.i) {
       var n = i[t.i];
       if (n._enabled) n.onEnable(), !n.node._activeInHierarchy || e._onEnabled(n)
      }
     }
    },
    _onEnabled: function(t) {
     cc.director.getScheduler().resumeTarget(t), t._objFlags |= a, this._updating ? this.scheduleInNextFrame.push(t) : this._scheduleImmediate(t)
    },
    _onDisabled: function(t) {
     cc.director.getScheduler().pauseTarget(t), t._objFlags &= ~a;
     var e = this.scheduleInNextFrame.indexOf(t);
     e >= 0 ? r.fastRemoveAt(this.scheduleInNextFrame, e) : (!t.start || t._objFlags & s || this.startInvoker.remove(t), t.update && this.updateInvoker.remove(t), t.lateUpdate && this.lateUpdateInvoker.remove(t))
    },
    enableComp: function(t, e) {
     if (!(t._objFlags & a)) {
      if (t.onEnable) {
       if (e) return void e.add(t);
       if (t.onEnable(), !t.node._activeInHierarchy) return
      }
      this._onEnabled(t)
     }
    },
    disableComp: function(t) {
     t._objFlags & a && (t.onDisable && t.onDisable(), this._onDisabled(t))
    },
    _scheduleImmediate: function(t) {
     !t.start || t._objFlags & s || this.startInvoker.add(t), t.update && this.updateInvoker.add(t), t.lateUpdate && this.lateUpdateInvoker.add(t)
    },
    _deferredSchedule: function() {
     for (var t = this.scheduleInNextFrame, e = 0, i = t.length; e < i; e++) {
      var n = t[e];
      this._scheduleImmediate(n)
     }
     t.length = 0
    },
    startPhase: function() {
     this._updating = !0, this.scheduleInNextFrame.length > 0 && this._deferredSchedule(), this.startInvoker.invoke()
    },
    updatePhase: function(t) {
     this.updateInvoker.invoke(t)
    },
    lateUpdatePhase: function(t) {
     this.lateUpdateInvoker.invoke(t), this._updating = !1
    }
   });
   e.exports = g
  }), {
   "./platform/CCClass": 108,
   "./platform/CCObject": 114,
   "./platform/js": 128,
   "./utils/misc": 186
  }],
  49: [(function(t, e, i) {
   var n = t("../../animation/animation-animator"),
    r = t("../../animation/animation-clip"),
    s = t("../event/event-target"),
    a = t("../platform/js");

   function o(t, e) {
    return t === e || t && e && (t.name === e.name || t._uuid === e._uuid)
   }
   var c = cc.Enum({
     PLAY: "play",
     STOP: "stop",
     PAUSE: "pause",
     RESUME: "resume",
     LASTFRAME: "lastframe",
     FINISHED: "finished"
    }),
    h = cc.Class({
     name: "cc.Animation",
     extends: t("./CCComponent"),
     mixins: [s],
     editor: !1,
     statics: {
      EventType: c
     },
     ctor: function() {
      cc.EventTarget.call(this), this._animator = null, this._nameToState = a.createMap(!0), this._didInit = !1, this._currentClip = null
     },
     properties: {
      _defaultClip: {
       default: null,
       type: r
      },
      defaultClip: {
       type: r,
       get: function() {
        return this._defaultClip
       },
       set: function(t) {},
       tooltip: !1
      },
      currentClip: {
       get: function() {
        return this._currentClip
       },
       set: function(t) {
        this._currentClip = t
       },
       type: r,
       visible: !1
      },
      _clips: {
       default: [],
       type: [r],
       tooltip: !1,
       visible: !0
      },
      playOnLoad: {
       default: !1,
       tooltip: !1
      }
     },
     start: function() {
      if (this.playOnLoad && this._defaultClip && !(this._animator && this._animator.isPlaying)) {
       var t = this.getAnimationState(this._defaultClip.name);
       this._animator.playState(t)
      }
     },
     onEnable: function() {
      this._animator && this._animator.resume()
     },
     onDisable: function() {
      this._animator && this._animator.pause()
     },
     onDestroy: function() {
      this.stop()
     },
     getClips: function() {
      return this._clips
     },
     play: function(t, e) {
      var i = this.playAdditive(t, e);
      return this._animator.stopStatesExcept(i), i
     },
     playAdditive: function(t, e) {
      this._init();
      var i = this.getAnimationState(t || this._defaultClip && this._defaultClip.name);
      if (i) {
       this.enabled = !0;
       var n = this._animator;
       n.isPlaying && i.isPlaying ? i.isPaused ? n.resumeState(i) : (n.stopState(i), n.playState(i, e)) : n.playState(i, e), this.enabledInHierarchy || n.pause(), this.currentClip = i.clip
      }
      return i
     },
     stop: function(t) {
      if (this._didInit)
       if (t) {
        var e = this._nameToState[t];
        e && this._animator.stopState(e)
       } else this._animator.stop()
     },
     pause: function(t) {
      if (this._didInit)
       if (t) {
        var e = this._nameToState[t];
        e && this._animator.pauseState(e)
       } else this.enabled = !1
     },
     resume: function(t) {
      if (this._didInit)
       if (t) {
        var e = this._nameToState[t];
        e && this._animator.resumeState(e)
       } else this.enabled = !0
     },
     setCurrentTime: function(t, e) {
      if (this._init(), e) {
       var i = this._nameToState[e];
       i && this._animator.setStateTime(i, t)
      } else this._animator.setStateTime(t)
     },
     getAnimationState: function(t) {
      this._init();
      var e = this._nameToState[t];
      return e && !e.curveLoaded && this._animator._reloadClip(e), e || null
     },
     addClip: function(t, e) {
      if (t) {
       this._init(), cc.js.array.contains(this._clips, t) || this._clips.push(t), e = e || t.name;
       var i = this._nameToState[e];
       if (i) {
        if (i.clip === t) return i;
        var n = this._clips.indexOf(i.clip); - 1 !== n && this._clips.splice(n, 1)
       }
       var r = new cc.AnimationState(t, e);
       return this._nameToState[e] = r, r
      }
      cc.warnID(3900)
     },
     removeClip: function(t, e) {
      if (t) {
       this._init();
       var i = void 0;
       for (var n in this._nameToState) {
        if ((i = this._nameToState[n]).clip === t) break
       }
       if (t === this._defaultClip) {
        if (!e) return void cc.warnID(3902);
        this._defaultClip = null
       }
       if (i && i.isPlaying) {
        if (!e) return void cc.warnID(3903);
        this.stop(i.name)
       }
       this._clips = this._clips.filter((function(e) {
        return e !== t
       })), i && delete this._nameToState[i.name]
      } else cc.warnID(3901)
     },
     sample: function(t) {
      if (this._init(), t) {
       var e = this._nameToState[t];
       e && e.sample()
      } else this._animator.sample()
     },
     on: function(t, e, i, n) {
      this._init();
      var r = this._EventTargetOn(t, e, i, n);
      if ("lastframe" === t)
       for (var s = this._animator._anims.array, a = 0; a < s.length; ++a) {
        s[a]._lastframeEventOn = !0
       }
      return r
     },
     off: function(t, e, i, n) {
      if (this._init(), "lastframe" === t) {
       var r = this._nameToState;
       for (var s in r) {
        r[s]._lastframeEventOn = !1
       }
      }
      this._EventTargetOff(t, e, i, n)
     },
     _init: function() {
      this._didInit || (this._didInit = !0, this._animator = new n(this.node, this), this._createStates())
     },
     _createStates: function() {
      this._nameToState = a.createMap(!0);
      for (var t = null, e = !1, i = 0; i < this._clips.length; ++i) {
       var n = this._clips[i];
       n && (t = new cc.AnimationState(n), this._nameToState[t.name] = t, o(this._defaultClip, n) && (e = t))
      }
      this._defaultClip && !e && (t = new cc.AnimationState(this._defaultClip), this._nameToState[t.name] = t)
     }
    });
   h.prototype._EventTargetOn = s.prototype.on, h.prototype._EventTargetOff = s.prototype.off, cc.Animation = e.exports = h
  }), {
   "../../animation/animation-animator": 8,
   "../../animation/animation-clip": 9,
   "../event/event-target": 77,
   "../platform/js": 128,
   "./CCComponent": 54
  }],
  50: [(function(t, e, i) {
   var n = t("../utils/misc"),
    r = t("./CCComponent"),
    s = t("../assets/CCAudioClip"),
    a = cc.Class({
     name: "cc.AudioSource",
     extends: r,
     editor: !1,
     ctor: function() {
  if(window.LetoAudio) {
    this.audio = new window.LetoAudio()
  } else {
    this.audio = new cc.Audio
  }
     },
     properties: {
      _clip: {
       default: null,
       type: s
      },
      _volume: 1,
      _mute: !1,
      _loop: !1,
      _pausedFlag: {
       default: !1,
       serializable: !1
      },
      isPlaying: {
       get: function() {
        return this.audio.getState() === cc.Audio.State.PLAYING
       },
       visible: !1
      },
      clip: {
       get: function() {
        return this._clip
       },
       set: function(t) {
        var e = this;
        if ("string" == typeof t) {
         var i = (function() {
          cc.warnID(8401, "cc.AudioSource", "cc.AudioClip", "AudioClip", "cc.AudioClip", "audio");
          var i = e;
          return s._loadByUrl(t, (function(t, e) {
           e && (i.clip = e)
          })), {
           v: void 0
          }
         })();
         if ("object" == typeof i) return i.v
        }
        t !== this._clip && (this._clip = t, this.audio.stop(), this.preload && (this.audio.src = this._clip))
       },
       type: s,
       tooltip: !1,
       animatable: !1
      },
      volume: {
       get: function() {
        return this._volume
       },
       set: function(t) {
        return t = n.clamp01(t), this._volume = t, this._mute || this.audio.setVolume(t), t
       },
       tooltip: !1
      },
      mute: {
       get: function() {
        return this._mute
       },
       set: function(t) {
        return this._mute = t, this.audio.setVolume(t ? 0 : this._volume), t
       },
       animatable: !1,
       tooltip: !1
      },
      loop: {
       get: function() {
        return this._loop
       },
       set: function(t) {
        return this._loop = t, this.audio.setLoop(t), t
       },
       animatable: !1,
       tooltip: !1
      },
      playOnLoad: {
       default: !1,
       tooltip: !1,
       animatable: !1
      },
      preload: {
       default: !1,
       animatable: !1
      }
     },
     _ensureDataLoaded: function() {
      this.audio.src !== this._clip && (this.audio.src = this._clip)
     },
     _pausedCallback: function() {
      if(!window.LetoAudio) {
        this.audio.getState() === cc.Audio.State.PLAYING && (this.audio.pause(), this._pausedFlag = !0)
      }
     },
     _restoreCallback: function() {
      if(!window.LetoAudio) {
        this._pausedFlag && this.audio.resume(), this._pausedFlag = !1
      }
     },
     onLoad: function() {
      this.audio.setVolume(this._mute ? 0 : this._volume), this.audio.setLoop(this._loop)
     },
     onEnable: function() {
      this.preload && (this.audio.src = this._clip), this.playOnLoad && this.play(), cc.game.on(cc.game.EVENT_HIDE, this._pausedCallback, this), cc.game.on(cc.game.EVENT_SHOW, this._restoreCallback, this)
     },
     onDisable: function() {
      this.stop(), cc.game.off(cc.game.EVENT_HIDE, this._pausedCallback, this), cc.game.off(cc.game.EVENT_SHOW, this._restoreCallback, this)
     },
     onDestroy: function() {
      this.stop(), this.audio.destroy(), cc.audioEngine.uncache(this._clip)
     },
     play: function() {
      if (this._clip) {
       var t = this.audio;
       this._clip.loaded && t.stop(), this._ensureDataLoaded(), t.setCurrentTime(0), t.play()
      }
     },
     stop: function() {
      this.audio.stop()
     },
     pause: function() {
      this.audio.pause()
     },
     resume: function() {
      this._ensureDataLoaded(), this.audio.resume()
     },
     rewind: function() {
      this.audio.setCurrentTime(0)
     },
     getCurrentTime: function() {
      return this.audio.getCurrentTime()
     },
     setCurrentTime: function(t) {
      return this.audio.setCurrentTime(t), t
     },
     getDuration: function() {
      return this.audio.getDuration()
     }
    });
   cc.AudioSource = e.exports = a
  }), {
   "../assets/CCAudioClip": 29,
   "../utils/misc": 186,
   "./CCComponent": 54
  }],
  51: [(function(t, e, i) {
   var n = ["touchstart", "touchmove", "touchend", "mousedown", "mousemove", "mouseup", "mouseenter", "mouseleave", "mousewheel"];

   function r(t) {
    t.stopPropagation()
   }
   var s = cc.Class({
    name: "cc.BlockInputEvents",
    extends: t("./CCComponent"),
    editor: {
     menu: "i18n:MAIN_MENU.component.ui/Block Input Events",
     inspector: "packages://inspector/inspectors/comps/block-input-events.js",
     help: "i18n:COMPONENT.help_url.block_input_events"
    },
    onEnable: function() {
     for (var t = 0; t < n.length; t++) this.node.on(n[t], r, this)
    },
    onDisable: function() {
     for (var t = 0; t < n.length; t++) this.node.off(n[t], r, this)
    }
   });
   cc.BlockInputEvents = e.exports = s
  }), {
   "./CCComponent": 54
  }],
  52: [(function(t, e, i) {
   var n = t("./CCComponent"),
    r = cc.Enum({
     NONE: 0,
     COLOR: 1,
     SPRITE: 2,
     SCALE: 3
    }),
    s = cc.Enum({
     NORMAL: 0,
     HOVER: 1,
     PRESSED: 2,
     DISABLED: 3
    }),
    a = cc.Class({
     name: "cc.Button",
     extends: n,
     ctor: function() {
      this._pressed = !1, this._hovered = !1, this._fromColor = null, this._toColor = null, this._time = 0, this._transitionFinished = !0, this._fromScale = cc.Vec2.ZERO, this._toScale = cc.Vec2.ZERO, this._originalScale = cc.Vec2.ZERO, this._sprite = null
     },
     editor: !1,
     properties: {
      interactable: {
       default: !0,
       tooltip: !1,
       notify: function() {
        this._updateState(), this.interactable || this._resetState()
       },
       animatable: !1
      },
      _resizeToTarget: {
       animatable: !1,
       set: function(t) {
        t && this._resizeNodeToTargetNode()
       }
      },
      enableAutoGrayEffect: {
       default: !1,
       tooltip: !1,
       notify: function() {
        this._updateDisabledState()
       }
      },
      transition: {
       default: r.NONE,
       tooltip: !1,
       type: r,
       animatable: !1,
       notify: function(t) {
        this._updateTransition(t)
       },
       formerlySerializedAs: "transition"
      },
      normalColor: {
       default: cc.color(214, 214, 214),
       displayName: "Normal",
       tooltip: !1,
       notify: function() {
        this.transition === r.Color && this._getButtonState() === s.NORMAL && (this._getTarget().opacity = this.normalColor.a), this._updateState()
       }
      },
      pressedColor: {
       default: cc.color(211, 211, 211),
       displayName: "Pressed",
       tooltip: !1,
       notify: function() {
        this.transition === r.Color && this._getButtonState() === s.PRESSED && (this._getTarget().opacity = this.pressedColor.a), this._updateState()
       },
       formerlySerializedAs: "pressedColor"
      },
      hoverColor: {
       default: cc.Color.WHITE,
       displayName: "Hover",
       tooltip: !1,
       notify: function() {
        this.transition === r.Color && this._getButtonState() === s.HOVER && (this._getTarget().opacity = this.hoverColor.a), this._updateState()
       },
       formerlySerializedAs: "hoverColor"
      },
      disabledColor: {
       default: cc.color(124, 124, 124),
       displayName: "Disabled",
       tooltip: !1,
       notify: function() {
        this.transition === r.Color && this._getButtonState() === s.DISABLED && (this._getTarget().opacity = this.disabledColor.a), this._updateState()
       }
      },
      duration: {
       default: .1,
       range: [0, 10],
       tooltip: !1
      },
      zoomScale: {
       default: 1.2,
       tooltip: !1
      },
      normalSprite: {
       default: null,
       type: cc.SpriteFrame,
       displayName: "Normal",
       tooltip: !1,
       notify: function() {
        this._updateState()
       }
      },
      pressedSprite: {
       default: null,
       type: cc.SpriteFrame,
       displayName: "Pressed",
       tooltip: !1,
       formerlySerializedAs: "pressedSprite",
       notify: function() {
        this._updateState()
       }
      },
      hoverSprite: {
       default: null,
       type: cc.SpriteFrame,
       displayName: "Hover",
       tooltip: !1,
       formerlySerializedAs: "hoverSprite",
       notify: function() {
        this._updateState()
       }
      },
      disabledSprite: {
       default: null,
       type: cc.SpriteFrame,
       displayName: "Disabled",
       tooltip: !1,
       notify: function() {
        this._updateState()
       }
      },
      target: {
       default: null,
       type: cc.Node,
       tooltip: !1,
       notify: function() {
        this._applyTarget()
       }
      },
      clickEvents: {
       default: [],
       type: cc.Component.EventHandler,
       tooltip: !1
      }
     },
     statics: {
      Transition: r
     },
     __preload: function() {
      this._applyTarget(), this._updateState()
     },
     _resetState: function() {
      this._pressed = !1, this._hovered = !1;
      var t = this._getTarget(),
       e = this.transition;
      e === r.COLOR && this.interactable ? this._setTargetColor(this.normalColor) : e === r.SCALE && (t.scaleX = this._originalScale.x, t.scaleY = this._originalScale.y), this._transitionFinished = !0
     },
     onEnable: function() {
      this.normalSprite && this.normalSprite.ensureLoadTexture(), this.hoverSprite && this.hoverSprite.ensureLoadTexture(), this.pressedSprite && this.pressedSprite.ensureLoadTexture(), this.disabledSprite && this.disabledSprite.ensureLoadTexture(), this._registerEvent()
     },
     _getTarget: function() {
      return this.target ? this.target : this.node
     },
     _setTargetColor: function(t) {
      var e = this._getTarget();
      e.color = t, e.opacity = t.a
     },
     _getStateColor: function(t) {
      switch (t) {
       case s.NORMAL:
        return this.normalColor;
       case s.HOVER:
        return this.hoverColor;
       case s.PRESSED:
        return this.pressedColor;
       case s.DISABLED:
        return this.disabledColor
      }
     },
     _getStateSprite: function(t) {
      switch (t) {
       case s.NORMAL:
        return this.normalSprite;
       case s.HOVER:
        return this.hoverSprite;
       case s.PRESSED:
        return this.pressedSprite;
       case s.DISABLED:
        return this.disabledSprite
      }
     },
     _setCurrentStateColor: function(t) {
      switch (this._getButtonState()) {
       case s.NORMAL:
        this.normalColor = t;
        break;
       case s.HOVER:
        this.hoverColor = t;
        break;
       case s.PRESSED:
        this.pressedColor = t;
        break;
       case s.DISABLED:
        this.disabledColor = t
      }
     },
     _setCurrentStateSprite: function(t) {
      switch (this._getButtonState()) {
       case s.NORMAL:
        this.normalSprite = t;
        break;
       case s.HOVER:
        this.hoverSprite = t;
        break;
       case s.PRESSED:
        this.pressedSprite = t;
        break;
       case s.DISABLED:
        this.disabledSprite = t
      }
     },
     onDisable: function() {
      this._resetState(), this.node.off(cc.Node.EventType.TOUCH_START, this._onTouchBegan, this), this.node.off(cc.Node.EventType.TOUCH_MOVE, this._onTouchMove, this), this.node.off(cc.Node.EventType.TOUCH_END, this._onTouchEnded, this), this.node.off(cc.Node.EventType.TOUCH_CANCEL, this._onTouchCancel, this), this.node.off(cc.Node.EventType.MOUSE_ENTER, this._onMouseMoveIn, this), this.node.off(cc.Node.EventType.MOUSE_LEAVE, this._onMouseMoveOut, this)
     },
     update: function(t) {
      var e = this._getTarget();
      if (!this._transitionFinished && (this.transition === r.COLOR || this.transition === r.SCALE)) {
       this.time += t;
       var i = 1;
       if (this.duration > 0 && (i = this.time / this.duration), i >= 1 && (i = 1, this._transitionFinished = !0), this.transition === r.COLOR) {
        var n = this._fromColor.lerp(this._toColor, i);
        this._setTargetColor(n)
       } else this.transition === r.SCALE && (e.scale = this._fromScale.lerp(this._toScale, i))
      }
     },
     _registerEvent: function() {
      this.node.on(cc.Node.EventType.TOUCH_START, this._onTouchBegan, this), this.node.on(cc.Node.EventType.TOUCH_MOVE, this._onTouchMove, this), this.node.on(cc.Node.EventType.TOUCH_END, this._onTouchEnded, this), this.node.on(cc.Node.EventType.TOUCH_CANCEL, this._onTouchCancel, this), this.node.on(cc.Node.EventType.MOUSE_ENTER, this._onMouseMoveIn, this), this.node.on(cc.Node.EventType.MOUSE_LEAVE, this._onMouseMoveOut, this)
     },
     _getTargetSprite: function(t) {
      var e = null;
      return t && (e = t.getComponent(cc.Sprite)), e
     },
     _applyTarget: function() {
      var t = this._getTarget();
      this._sprite = this._getTargetSprite(t), this._originalScale.x = t.scaleX, this._originalScale.y = t.scaleY
     },
     _onTouchBegan: function(t) {
      this.interactable && this.enabledInHierarchy && (this._pressed = !0, this._updateState(), t.stopPropagation())
     },
     _onTouchMove: function(t) {
      if (this.interactable && this.enabledInHierarchy && this._pressed) {
       var e = t.touch,
        i = this.node._hitTest(e.getLocation()),
        n = this._getTarget();
       if (this.transition === r.SCALE) i ? (this._fromScale.x = this._originalScale.x, this._fromScale.y = this._originalScale.y, this._toScale.x = this._originalScale.x * this.zoomScale, this._toScale.y = this._originalScale.y * this.zoomScale, this._transitionFinished = !1) : (this.time = 0, this._transitionFinished = !0, n.scaleX = this._originalScale.x, n.scaleY = this._originalScale.y);
       else {
        var a = void 0;
        a = i ? s.PRESSED : s.NORMAL, this._applyTransition(a)
       }
       t.stopPropagation()
      }
     },
     _onTouchEnded: function(t) {
      this.interactable && this.enabledInHierarchy && (this._pressed && (cc.Component.EventHandler.emitEvents(this.clickEvents, t), this.node.emit("click", this)), this._pressed = !1, this._updateState(), t.stopPropagation())
     },
     _onTouchCancel: function() {
      this.interactable && this.enabledInHierarchy && (this._pressed = !1, this._updateState())
     },
     _onMouseMoveIn: function() {
      !this._pressed && this.interactable && this.enabledInHierarchy && (this.transition !== r.SPRITE || this.hoverSprite) && (this._hovered || (this._hovered = !0, this._updateState()))
     },
     _onMouseMoveOut: function() {
      this._hovered && (this._hovered = !1, this._updateState())
     },
     _updateState: function() {
      var t = this._getButtonState();
      this._applyTransition(t), this._updateDisabledState()
     },
     _getButtonState: function() {
      return this.interactable ? this._pressed ? s.PRESSED : this._hovered ? s.HOVER : s.NORMAL : s.DISABLED
     },
     _updateColorTransitionImmediately: function(t) {
      var e = this._getStateColor(t);
      this._setTargetColor(e)
     },
     _updateColorTransition: function(t) {
      if (t === s.DISABLED) this._updateColorTransitionImmediately(t);
      else {
       var e = this._getTarget(),
        i = this._getStateColor(t);
       this._fromColor = e.color.clone(), this._toColor = i, this.time = 0, this._transitionFinished = !1
      }
     },
     _updateSpriteTransition: function(t) {
      var e = this._getStateSprite(t);
      this._sprite && e && (this._sprite.spriteFrame = e)
     },
     _updateScaleTransition: function(t) {
      t === s.PRESSED ? this._zoomUp() : this._zoomBack()
     },
     _zoomUp: function() {
      this._fromScale.x = this._originalScale.x, this._fromScale.y = this._originalScale.y, this._toScale.x = this._originalScale.x * this.zoomScale, this._toScale.y = this._originalScale.y * this.zoomScale, this.time = 0, this._transitionFinished = !1
     },
     _zoomBack: function() {
      var t = this._getTarget();
      this._fromScale.x = t.scaleX, this._fromScale.y = t.scaleY, this._toScale.x = this._originalScale.x, this._toScale.y = this._originalScale.y, this.time = 0, this._transitionFinished = !1
     },
     _updateTransition: function(t) {
      t === r.COLOR ? this._updateColorTransitionImmediately(s.NORMAL) : t === r.SPRITE && this._updateSpriteTransition(s.NORMAL), this._updateState()
     },
     _applyTransition: function(t) {
      var e = this.transition;
      e === r.COLOR ? this._updateColorTransition(t) : e === r.SPRITE ? this._updateSpriteTransition(t) : e === r.SCALE && this._updateScaleTransition(t)
     },
     _resizeNodeToTargetNode: !1,
     _updateDisabledState: function() {
      if (this._sprite) {
       if (this.enableAutoGrayEffect && !(this.transition === r.SPRITE && this.disabledSprite || this.interactable)) return void this._sprite.setState(cc.Sprite.State.GRAY);
       this._sprite.setState(cc.Sprite.State.NORMAL)
      }
     }
    });
   cc.Button = e.exports = a
  }), {
   "./CCComponent": 54
  }],
  53: [(function(t, e, i) {
   var n = t("../camera/CCCamera"),
    r = t("./CCComponent"),
    s = cc.Class({
     name: "cc.Canvas",
     extends: r,
     editor: !1,
     resetInEditor: !1,
     statics: {
      instance: null
     },
     properties: {
      _designResolution: cc.size(960, 640),
      designResolution: {
       get: function() {
        return cc.size(this._designResolution)
       },
       set: function(t) {
        this._designResolution.width = t.width, this._designResolution.height = t.height, this.applySettings(), this.alignWithScreen()
       },
       tooltip: !1
      },
      _fitWidth: !1,
      _fitHeight: !0,
      fitHeight: {
       get: function() {
        return this._fitHeight
       },
       set: function(t) {
        this._fitHeight !== t && (this._fitHeight = t, this.applySettings(), this.alignWithScreen())
       },
       tooltip: !1
      },
      fitWidth: {
       get: function() {
        return this._fitWidth
       },
       set: function(t) {
        this._fitWidth !== t && (this._fitWidth = t, this.applySettings(), this.alignWithScreen())
       },
       tooltip: !1
      }
     },
     ctor: function() {
      this._thisOnResized = this.alignWithScreen.bind(this)
     },
     __preload: function() {
      if (s.instance) return cc.errorID(6700, this.node.name, s.instance.node.name);
      s.instance = this, cc.sys.isMobile ? window.addEventListener("resize", this._thisOnResized) : cc.view.on("canvas-resize", this._thisOnResized), this.applySettings(), this.alignWithScreen();
      var t = cc.find("Main Camera", this.node);
      t || ((t = new cc.Node("Main Camera")).parent = this.node, t.setSiblingIndex(0));
      var e = t.getComponent(n);
      if (!e) {
       e = t.addComponent(n);
       var i = n.ClearFlags;
       e.clearFlags = i.COLOR | i.DEPTH | i.STENCIL, e.depth = -1
      }
      n.main = e
     },
     onDestroy: function() {
      cc.sys.isMobile ? window.removeEventListener("resize", this._thisOnResized) : cc.view.off("canvas-resize", this._thisOnResized), s.instance === this && (s.instance = null)
     },
     alignWithScreen: function() {
      var t, e, i = e = cc.visibleRect;
      t = cc.view.getDesignResolutionSize();
      var n = 0,
       r = 0;
      !this.fitHeight && !this.fitWidth && (n = .5 * (t.width - i.width), r = .5 * (t.height - i.height)), this.node.setPosition(.5 * i.width + n, .5 * i.height + r), this.node.width = e.width, this.node.height = e.height
     },
     applySettings: function() {
      var t, e = cc.ResolutionPolicy;
      t = this.fitHeight && this.fitWidth ? e.SHOW_ALL : this.fitHeight || this.fitWidth ? this.fitWidth ? e.FIXED_WIDTH : e.FIXED_HEIGHT : e.NO_BORDER;
      var i = this._designResolution;
      cc.view.setDesignResolutionSize(i.width, i.height, t)
     }
    });
   cc.Canvas = e.exports = s
  }), {
   "../camera/CCCamera": 46,
   "./CCComponent": 54
  }],
  54: [(function(t, e, i) {
   var n = t("../platform/CCObject"),
    r = t("../platform/js"),
    s = new(t("../platform/id-generater"))("Comp"),
    a = (n.Flags.IsOnEnableCalled, n.Flags.IsOnLoadCalled),
    o = cc.Class({
     name: "cc.Component",
     extends: n,
     ctor: function() {
      this._id = s.getNewId(), this.__eventTargets = []
     },
     properties: {
      node: {
       default: null,
       visible: !1
      },
      name: {
       get: function() {
        if (this._name) return this._name;
        var t = cc.js.getClassName(this),
         e = t.lastIndexOf(".");
        return e >= 0 && (t = t.slice(e + 1)), this.node.name + "<" + t + ">"
       },
       set: function(t) {
        this._name = t
       },
       visible: !1
      },
      uuid: {
       get: function() {
        return this._id
       },
       visible: !1
      },
      __scriptAsset: !1,
      _enabled: !0,
      enabled: {
       get: function() {
        return this._enabled
       },
       set: function(t) {
        if (this._enabled !== t && (this._enabled = t, this.node._activeInHierarchy)) {
         var e = cc.director._compScheduler;
         t ? e.enableComp(this) : e.disableComp(this)
        }
       },
       visible: !1
      },
      enabledInHierarchy: {
       get: function() {
        return this._enabled && this.node._activeInHierarchy
       },
       visible: !1
      },
      _isOnLoadCalled: {
       get: function() {
        return this._objFlags & a
       }
      }
     },
     update: null,
     lateUpdate: null,
     __preload: null,
     onLoad: null,
     start: null,
     onEnable: null,
     onDisable: null,
     onDestroy: null,
     onFocusInEditor: null,
     onLostFocusInEditor: null,
     resetInEditor: null,
     addComponent: function(t) {
      return this.node.addComponent(t)
     },
     getComponent: function(t) {
      return this.node.getComponent(t)
     },
     getComponents: function(t) {
      return this.node.getComponents(t)
     },
     getComponentInChildren: function(t) {
      return this.node.getComponentInChildren(t)
     },
     getComponentsInChildren: function(t) {
      return this.node.getComponentsInChildren(t)
     },
     _getLocalBounds: null,
     onRestore: null,
     destroy: function() {
      this._super() && this._enabled && this.node._activeInHierarchy && cc.director._compScheduler.disableComp(this)
     },
     _onPreDestroy: function() {
      this.unscheduleAllCallbacks();
      for (var t = this.__eventTargets, e = 0, i = t.length; e < i; ++e) {
       var n = t[e];
       n && n.targetOff(this)
      }
      t.length = 0, cc.director._nodeActivator.destroyComp(this), this.node._removeComponent(this)
     },
     _instantiate: function(t) {
      return t || (t = cc.instantiate._clone(this, this)), t.node = null, t
     },
     schedule: function(t, e, i, n) {
      cc.assertID(t, 1619), cc.assertID(e >= 0, 1620), e = e || 0, i = isNaN(i) ? cc.macro.REPEAT_FOREVER : i, n = n || 0;
      var r = cc.director.getScheduler(),
       s = r.isTargetPaused(this);
      r.schedule(t, this, e, i, n, s)
     },
     scheduleOnce: function(t, e) {
      this.schedule(t, 0, 0, e)
     },
     unschedule: function(t) {
      t && cc.director.getScheduler().unschedule(t, this)
     },
     unscheduleAllCallbacks: function() {
      cc.director.getScheduler().unscheduleAllForTarget(this)
     }
    });
   o._requireComponent = null, o._executionOrder = 0, r.value(o, "_registerEditorProps", (function(t, e) {
    var i = e.requireComponent;
    i && (t._requireComponent = i);
    var n = e.executionOrder;
    n && "number" == typeof n && (t._executionOrder = n)
   })), o.prototype.__scriptUuid = "", cc.Component = e.exports = o
  }), {
   "../platform/CCObject": 114,
   "../platform/id-generater": 124,
   "../platform/js": 128
  }],
  55: [(function(t, e, i) {
   cc.Component.EventHandler = cc.Class({
    name: "cc.ClickEvent",
    properties: {
     target: {
      default: null,
      type: cc.Node
     },
     component: "",
     _componentId: "",
     _componentName: {
      get: function() {
       return this._genCompIdIfNeeded(), this._compId2Name(this._componentId)
      },
      set: function(t) {
       this._componentId = this._compName2Id(t)
      }
     },
     handler: {
      default: ""
     },
     customEventData: {
      default: ""
     }
    },
    statics: {
     emitEvents: function(t) {
      "use strict";
      var e = void 0;
      if (arguments.length > 0)
       for (var i = 0, n = (e = new Array(arguments.length - 1)).length; i < n; i++) e[i] = arguments[i + 1];
      for (var r = 0, s = t.length; r < s; r++) {
       var a = t[r];
       a instanceof cc.Component.EventHandler && a.emit(e)
      }
     }
    },
    emit: function(t) {
     var e = this.target;
     if (cc.isValid(e)) {
      this._genCompIdIfNeeded();
      var i = cc.js._getClassById(this._componentId),
       n = e.getComponent(i);
      if (cc.isValid(n)) {
       var r = n[this.handler];
       "function" == typeof r && (null != this.customEventData && "" !== this.customEventData && (t = t.slice()).push(this.customEventData), r.apply(n, t))
      }
     }
    },
    _compName2Id: function(t) {
     var e = cc.js.getClassByName(t);
     return cc.js._getClassId(e)
    },
    _compId2Name: function(t) {
     var e = cc.js._getClassById(t);
     return cc.js.getClassName(e)
    },
    _genCompIdIfNeeded: function() {
     this._componentId || (this._componentName = this.component, this.component = "")
    }
   })
  }), {}],
  56: [(function(t, e, i) {
   var n = t("../platform/CCMacro"),
    r = t("./CCRenderComponent"),
    s = t("../renderer/render-engine"),
    a = t("../renderer/render-flow"),
    o = s.SpriteMaterial,
    c = t("../renderer/utils/dynamic-atlas/manager"),
    h = t("../renderer/utils/label/label-frame"),
    u = n.TextAlignment,
    l = n.VerticalTextAlignment,
    _ = cc.Enum({
     NONE: 0,
     CLAMP: 1,
     SHRINK: 2,
     RESIZE_HEIGHT: 3
    }),
    d = cc.Class({
     name: "cc.Label",
     extends: r,
     ctor: function() {
      this._actualFontSize = 0, this._assemblerData = null, this._frame = null, this._ttfTexture = null
     },
     editor: !1,
     properties: {
      _useOriginalSize: !0,
      _string: {
       default: "",
       formerlySerializedAs: "_N$string"
      },
      string: {
       get: function() {
        return this._string
       },
       set: function(t) {
        var e = this._string;
        this._string = t.toString(), this.string !== e && this._updateRenderData(), this._checkStringEmpty()
       },
       multiline: !0,
       tooltip: !1
      },
      horizontalAlign: {
       default: u.LEFT,
       type: u,
       tooltip: !1,
       notify: function(t) {
        this.horizontalAlign !== t && this._updateRenderData()
       },
       animatable: !1
      },
      verticalAlign: {
       default: l.TOP,
       type: l,
       tooltip: !1,
       notify: function(t) {
        this.verticalAlign !== t && this._updateRenderData()
       },
       animatable: !1
      },
      actualFontSize: {
       displayName: "Actual Font Size",
       animatable: !1,
       readonly: !0,
       get: function() {
        return this._actualFontSize
       }
      },
      _fontSize: 40,
      fontSize: {
       get: function() {
        return this._fontSize
       },
       set: function(t) {
        this._fontSize !== t && (this._fontSize = t, this._updateRenderData())
       },
       tooltip: !1
      },
      fontFamily: {
       default: "Arial",
       tooltip: !1,
       notify: function(t) {
        this.fontFamily !== t && this._updateRenderData()
       },
       animatable: !1
      },
      _lineHeight: 40,
      lineHeight: {
       get: function() {
        return this._lineHeight
       },
       set: function(t) {
        this._lineHeight !== t && (this._lineHeight = t, this._updateRenderData())
       },
       tooltip: !1
      },
      overflow: {
       default: _.NONE,
       type: _,
       tooltip: !1,
       notify: function(t) {
        this.overflow !== t && this._updateRenderData()
       },
       animatable: !1
      },
      _enableWrapText: !0,
      enableWrapText: {
       get: function() {
        return this._enableWrapText
       },
       set: function(t) {
        this._enableWrapText !== t && (this._enableWrapText = t, this._updateRenderData())
       },
       animatable: !1,
       tooltip: !1
      },
      _N$file: null,
      font: {
       get: function() {
        return this._N$file
       },
       set: function(t) {
        this.font !== t && (t || (this._isSystemFontUsed = !0), this._N$file = t, t && this._isSystemFontUsed && (this._isSystemFontUsed = !1), "string" == typeof t && cc.warnID(4e3), this._renderData && (this.destroyRenderData(this._renderData), this._renderData = null), this._fontAtlas = null, this._updateAssembler(), this._applyFontTexture(!0), this._updateRenderData())
       },
       type: cc.Font,
       tooltip: !1,
       animatable: !1
      },
      _isSystemFontUsed: !0,
      useSystemFont: {
       get: function() {
        return this._isSystemFontUsed
       },
       set: function(t) {
        this._isSystemFontUsed !== t && (this.destroyRenderData(this._renderData), this._renderData = null, this._isSystemFontUsed = !!t, t ? (this.font = null, this._updateAssembler(), this._updateRenderData(), this._checkStringEmpty()) : this._userDefinedFont || this.disableRender())
       },
       animatable: !1,
       tooltip: !1
      },
      _bmFontOriginalSize: {
       displayName: "BMFont Original Size",
       get: function() {
        return this._N$file instanceof cc.BitmapFont ? this._N$file.fontSize : -1
       },
       visible: !0,
       animatable: !1
      },
      _spacingX: 0,
      spacingX: {
       get: function() {
        return this._spacingX
       },
       set: function(t) {
        this._spacingX = t, this._updateRenderData()
       }
      },
      _batchAsBitmap: !1,
      batchAsBitmap: {
       get: function() {
        return this._batchAsBitmap
       },
       set: function(t) {
        this._batchAsBitmap !== t && (this._batchAsBitmap = t, this._batchAsBitmap || this.font instanceof cc.BitmapFont || this._frame._resetDynamicAtlasFrame(), this._activateMaterial(!0), this._updateRenderData())
       },
       animatable: !1,
       tooltip: !1
      },
      _isBold: {
       default: !1,
       serializable: !1
      },
      _isItalic: {
       default: !1,
       serializable: !1
      },
      _isUnderline: {
       default: !1,
       serializable: !1
      }
     },
     statics: {
      HorizontalAlign: u,
      VerticalAlign: l,
      Overflow: _
     },
     onEnable: function() {
      this._super(), this.font || this._isSystemFontUsed || (this.useSystemFont = !0), this.useSystemFont && !this.fontFamily && (this.fontFamily = "Arial"), this.node.on(cc.Node.EventType.SIZE_CHANGED, this._updateRenderData, this), this.node.on(cc.Node.EventType.ANCHOR_CHANGED, this._updateRenderData, this), this._checkStringEmpty(), this._updateRenderData(!0)
     },
     onDisable: function() {
      this._super(), this.node.off(cc.Node.EventType.SIZE_CHANGED, this._updateRenderData, this), this.node.off(cc.Node.EventType.ANCHOR_CHANGED, this._updateRenderData, this)
     },
     onDestroy: function() {
      this._assembler._resetAssemblerData && this._assembler._resetAssemblerData(this._assemblerData), this._assemblerData = null, this._ttfTexture && (this._ttfTexture.destroy(), this._ttfTexture = null), this._super()
     },
     _canRender: function() {
      var t = this._super(),
       e = this.font;
      if (e instanceof cc.BitmapFont) {
       var i = e.spriteFrame;
       i && i.textureLoaded() || (t = !1)
      }
      return t
     },
     _checkStringEmpty: function() {
      this.markForRender(!!this.string)
     },
     _updateAssembler: function() {
      var t = d._assembler.getAssembler(this);
      this._assembler !== t && (this._assembler = t, this._renderData = null, this._frame = null), this._renderData || (this._renderData = this._assembler.createData(this))
     },
     _applyFontTexture: function(t) {
      var e = this,
       i = this.font;
      i instanceof cc.BitmapFont ? (function() {
       var n = i.spriteFrame;
       e._frame = n;
       var r = e,
        s = function() {
         r._frame._texture = n._texture, r._activateMaterial(t), t && r._assembler && r._assembler.updateRenderData(r)
        };
       n && n.textureLoaded() ? s() : (e.disableRender(), n && (n.once("load", s, e), n.ensureLoadTexture()))
      })() : (this._ttfTexture || (this._ttfTexture = new cc.Texture2D, this._ttfTexture.setPremultiplyAlpha(!0), this._assemblerData = this._assembler._getAssemblerData(), this._ttfTexture.initWithElement(this._assemblerData.canvas)), this._frame || (this._frame = new h), this._frame._refreshTexture(this._ttfTexture), this._activateMaterial(t), t && this._assembler && this._assembler.updateRenderData(this))
     },
     _activateMaterial: function(t) {
      var e = this._material;
      e && !t || (cc.game.renderType === cc.game.RENDER_TYPE_CANVAS ? this._frame._texture.url = this.uuid + "_texture" : (e || (e = new o), this._frame._texture === this._ttfTexture ? this._srcBlendFactor = cc.macro.BlendFactor.ONE : this._srcBlendFactor = cc.macro.BlendFactor.SRC_ALPHA, e.texture = this._frame._texture, e.useColor = !1, this._updateMaterial(e)), this.markForUpdateRenderData(!0), this.markForRender(!0))
     },
     _updateColor: function() {
      this.font instanceof cc.BitmapFont ? this._super() : (this._updateRenderData(), this.node._renderFlag &= ~a.FLAG_COLOR)
     },
     _updateRenderData: function(t) {
      var e = this._renderData;
      e && (e.vertDirty = !0, e.uvDirty = !0, this.markForUpdateRenderData(!0)), t && (this._updateAssembler(), this._applyFontTexture(t))
     },
     _calDynamicAtlas: function() {
      if (c) {
       if (!this._frame._original) {
        var t = c.insertSpriteFrame(this._frame);
        t && this._frame._setDynamicAtlasFrame(t)
       }
       this._material._texture !== this._frame._texture && this._activateMaterial(!0)
      }
     },
     _enableBold: function(t) {
      this._isBold = !!t
     },
     _enableItalics: function(t) {
      this._isItalic = !!t
     },
     _enableUnderline: function(t) {
      this._isUnderline = !!t
     }
    });
   cc.Label = e.exports = d
  }), {
   "../platform/CCMacro": 113,
   "../renderer/render-engine": 150,
   "../renderer/render-flow": 151,
   "../renderer/utils/dynamic-atlas/manager": 153,
   "../renderer/utils/label/label-frame": 155,
   "./CCRenderComponent": 62
  }],
  57: [(function(t, e, i) {
   var n = cc.Class({
    name: "cc.LabelOutline",
    extends: t("./CCComponent"),
    editor: !1,
    properties: {
     _color: cc.color(255, 255, 255, 255),
     _width: 1,
     color: {
      get: function() {
       return this._color
      },
      set: function(t) {
       this._color = cc.color(t), this._updateRenderData()
      }
     },
     width: {
      get: function() {
       return this._width
      },
      set: function(t) {
       this._width = t, this._updateRenderData()
      }
     }
    },
    onEnable: function() {
     this._updateRenderData()
    },
    onDisable: function() {
     this._updateRenderData()
    },
    _updateRenderData: function() {
     var t = this.node.getComponent(cc.Label);
     t && t._updateRenderData()
    }
   });
   cc.LabelOutline = e.exports = n
  }), {
   "./CCComponent": 54
  }],
  58: [(function(t, e, i) {
   var n = t("../CCNode").EventType,
    r = cc.Enum({
     NONE: 0,
     HORIZONTAL: 1,
     VERTICAL: 2,
     GRID: 3
    }),
    s = cc.Enum({
     NONE: 0,
     CONTAINER: 1,
     CHILDREN: 2
    }),
    a = cc.Enum({
     HORIZONTAL: 0,
     VERTICAL: 1
    }),
    o = cc.Enum({
     BOTTOM_TO_TOP: 0,
     TOP_TO_BOTTOM: 1
    }),
    c = cc.Enum({
     LEFT_TO_RIGHT: 0,
     RIGHT_TO_LEFT: 1
    }),
    h = cc.Class({
     name: "cc.Layout",
     extends: t("./CCComponent"),
     editor: !1,
     properties: {
      _layoutSize: cc.size(300, 200),
      _layoutDirty: {
       default: !0,
       serializable: !1
      },
      _resize: s.NONE,
      _N$layoutType: r.NONE,
      type: {
       type: r,
       get: function() {
        return this._N$layoutType
       },
       set: function(t) {
        this._N$layoutType = t, this._doLayoutDirty()
       },
       tooltip: !1,
       animatable: !1
      },
      resizeMode: {
       type: s,
       tooltip: !1,
       animatable: !1,
       get: function() {
        return this._resize
       },
       set: function(t) {
        this.type === r.NONE && t === s.CHILDREN || (this._resize = t, this._doLayoutDirty())
       }
      },
      cellSize: {
       default: cc.size(40, 40),
       tooltip: !1,
       type: cc.Size,
       notify: function() {
        this._doLayoutDirty()
       }
      },
      startAxis: {
       default: a.HORIZONTAL,
       tooltip: !1,
       type: a,
       notify: function() {
        this._doLayoutDirty()
       },
       animatable: !1
      },
      _N$padding: {
       default: 0
      },
      paddingLeft: {
       default: 0,
       tooltip: !1,
       notify: function() {
        this._doLayoutDirty()
       }
      },
      paddingRight: {
       default: 0,
       tooltip: !1,
       notify: function() {
        this._doLayoutDirty()
       }
      },
      paddingTop: {
       default: 0,
       tooltip: !1,
       notify: function() {
        this._doLayoutDirty()
       }
      },
      paddingBottom: {
       default: 0,
       tooltip: !1,
       notify: function() {
        this._doLayoutDirty()
       }
      },
      spacingX: {
       default: 0,
       notify: function() {
        this._doLayoutDirty()
       },
       tooltip: !1
      },
      spacingY: {
       default: 0,
       notify: function() {
        this._doLayoutDirty()
       },
       tooltip: !1
      },
      verticalDirection: {
       default: o.TOP_TO_BOTTOM,
       type: o,
       notify: function() {
        this._doLayoutDirty()
       },
       tooltip: !1,
       animatable: !1
      },
      horizontalDirection: {
       default: c.LEFT_TO_RIGHT,
       type: c,
       notify: function() {
        this._doLayoutDirty()
       },
       tooltip: !1,
       animatable: !1
      },
      affectedByScale: {
       default: !1,
       notify: function() {
        this._doLayoutDirty()
       },
       animatable: !1,
       tooltip: !1
      }
     },
     statics: {
      Type: r,
      VerticalDirection: o,
      HorizontalDirection: c,
      ResizeMode: s,
      AxisDirection: a
     },
     _migratePaddingData: function() {
      this.paddingLeft = this._N$padding, this.paddingRight = this._N$padding, this.paddingTop = this._N$padding, this.paddingBottom = this._N$padding, this._N$padding = 0
     },
     onEnable: function() {
      this._addEventListeners(), this.node.getContentSize().equals(cc.size(0, 0)) && this.node.setContentSize(this._layoutSize), 0 !== this._N$padding && this._migratePaddingData(), this._doLayoutDirty()
     },
     onDisable: function() {
      this._removeEventListeners()
     },
     _doLayoutDirty: function() {
      this._layoutDirty = !0
     },
     _doScaleDirty: function() {
      this._layoutDirty = this._layoutDirty || this.affectedByScale
     },
     _addEventListeners: function() {
      cc.director.on(cc.Director.EVENT_AFTER_UPDATE, this.updateLayout, this), this.node.on(n.SIZE_CHANGED, this._resized, this), this.node.on(n.ANCHOR_CHANGED, this._doLayoutDirty, this), this.node.on(n.CHILD_ADDED, this._childAdded, this), this.node.on(n.CHILD_REMOVED, this._childRemoved, this), this.node.on(n.CHILD_REORDER, this._doLayoutDirty, this), this._addChildrenEventListeners()
     },
     _removeEventListeners: function() {
      cc.director.off(cc.Director.EVENT_AFTER_UPDATE, this.updateLayout, this), this.node.off(n.SIZE_CHANGED, this._resized, this), this.node.off(n.ANCHOR_CHANGED, this._doLayoutDirty, this), this.node.off(n.CHILD_ADDED, this._childAdded, this), this.node.off(n.CHILD_REMOVED, this._childRemoved, this), this.node.off(n.CHILD_REORDER, this._doLayoutDirty, this), this._removeChildrenEventListeners()
     },
     _addChildrenEventListeners: function() {
      for (var t = this.node.children, e = 0; e < t.length; ++e) {
       var i = t[e];
       i.on(n.SCALE_CHANGED, this._doScaleDirty, this), i.on(n.SIZE_CHANGED, this._doLayoutDirty, this), i.on(n.POSITION_CHANGED, this._doLayoutDirty, this), i.on(n.ANCHOR_CHANGED, this._doLayoutDirty, this), i.on("active-in-hierarchy-changed", this._doLayoutDirty, this)
      }
     },
     _removeChildrenEventListeners: function() {
      for (var t = this.node.children, e = 0; e < t.length; ++e) {
       var i = t[e];
       i.off(n.SCALE_CHANGED, this._doScaleDirty, this), i.off(n.SIZE_CHANGED, this._doLayoutDirty, this), i.off(n.POSITION_CHANGED, this._doLayoutDirty, this), i.off(n.ANCHOR_CHANGED, this._doLayoutDirty, this), i.off("active-in-hierarchy-changed", this._doLayoutDirty, this)
      }
     },
     _childAdded: function(t) {
      t.on(n.SCALE_CHANGED, this._doScaleDirty, this), t.on(n.SIZE_CHANGED, this._doLayoutDirty, this), t.on(n.POSITION_CHANGED, this._doLayoutDirty, this), t.on(n.ANCHOR_CHANGED, this._doLayoutDirty, this), t.on("active-in-hierarchy-changed", this._doLayoutDirty, this), this._doLayoutDirty()
     },
     _childRemoved: function(t) {
      t.off(n.SCALE_CHANGED, this._doScaleDirty, this), t.off(n.SIZE_CHANGED, this._doLayoutDirty, this), t.off(n.POSITION_CHANGED, this._doLayoutDirty, this), t.off(n.ANCHOR_CHANGED, this._doLayoutDirty, this), t.off("active-in-hierarchy-changed", this._doLayoutDirty, this), this._doLayoutDirty()
     },
     _resized: function() {
      this._layoutSize = this.node.getContentSize(), this._doLayoutDirty()
     },
     _doLayoutHorizontally: function(t, e, i, n) {
      var a = this.node.getAnchorPoint(),
       h = this.node.children,
       u = 1,
       l = this.paddingLeft,
       _ = -a.x * t;
      this.horizontalDirection === c.RIGHT_TO_LEFT && (u = -1, _ = (1 - a.x) * t, l = this.paddingRight);
      for (var d = _ + u * l - u * this.spacingX, f = 0, p = 0, m = 0, v = 0, g = 0, y = 0, T = 0, E = 0; E < h.length; ++E) {
       (C = h[E]).activeInHierarchy && T++
      }
      var x = this.cellSize.width;
      this.type !== r.GRID && this.resizeMode === s.CHILDREN && (x = (t - (this.paddingLeft + this.paddingRight) - (T - 1) * this.spacingX) / T);
      for (E = 0; E < h.length; ++E) {
       var C = h[E],
        A = this._getUsedScaleValue(C.scaleX),
        b = this._getUsedScaleValue(C.scaleY);
       if (C.activeInHierarchy) {
        this._resize === s.CHILDREN && (C.width = x / A, this.type === r.GRID && (C.height = this.cellSize.height / b));
        var S = C.anchorX,
         w = C.width * A,
         D = C.height * b;
        m > p && (p = m), D >= p && (m = p, p = D, y = C.getAnchorPoint().y), this.horizontalDirection === c.RIGHT_TO_LEFT && (S = 1 - C.anchorX), d = d + u * S * w + u * this.spacingX;
        var R = u * (1 - S) * w;
        if (e) {
         var M = d + R + u * (u > 0 ? this.paddingRight : this.paddingLeft),
          I = this.horizontalDirection === c.LEFT_TO_RIGHT && M > (1 - a.x) * t,
          L = this.horizontalDirection === c.RIGHT_TO_LEFT && M < -a.x * t;
         (I || L) && (D >= p ? (0 === m && (m = p), f += m, m = p) : (f += p, m = D, p = 0), d = _ + u * (l + S * w), v++)
        }
        var O = i(C, f, v);
        t >= w + this.paddingLeft + this.paddingRight && n && C.setPosition(cc.v2(d, O));
        var P, F = 1,
         N = 0 === p ? D : p;
        this.verticalDirection === o.TOP_TO_BOTTOM ? (g = g || this.node._contentSize.height, (P = O + (F = -1) * (N * y + this.paddingBottom)) < g && (g = P)) : (g = g || -this.node._contentSize.height, (P = O + F * (N * y + this.paddingTop)) > g && (g = P)), d += R
       }
      }
      return g
     },
     _getVerticalBaseHeight: function(t) {
      var e = 0,
       i = 0;
      if (this.resizeMode === s.CONTAINER) {
       for (var n = 0; n < t.length; ++n) {
        var r = t[n];
        r.activeInHierarchy && (i++, e += r.height * this._getUsedScaleValue(r.scaleY))
       }
       e += (i - 1) * this.spacingY + this.paddingBottom + this.paddingTop
      } else e = this.node.getContentSize().height;
      return e
     },
     _doLayoutVertically: function(t, e, i, n) {
      var a = this.node.getAnchorPoint(),
       h = this.node.children,
       u = 1,
       l = this.paddingBottom,
       _ = -a.y * t;
      this.verticalDirection === o.TOP_TO_BOTTOM && (u = -1, _ = (1 - a.y) * t, l = this.paddingTop);
      for (var d = _ + u * l - u * this.spacingY, f = 0, p = 0, m = 0, v = 0, g = 0, y = 0, T = 0, E = 0; E < h.length; ++E) {
       (C = h[E]).activeInHierarchy && T++
      }
      var x = this.cellSize.height;
      this.type !== r.GRID && this.resizeMode === s.CHILDREN && (x = (t - (this.paddingTop + this.paddingBottom) - (T - 1) * this.spacingY) / T);
      for (E = 0; E < h.length; ++E) {
       var C = h[E],
        A = this._getUsedScaleValue(C.scaleX),
        b = this._getUsedScaleValue(C.scaleY);
       if (C.activeInHierarchy) {
        this.resizeMode === s.CHILDREN && (C.height = x / b, this.type === r.GRID && (C.width = this.cellSize.width / A));
        var S = C.anchorY,
         w = C.width * A,
         D = C.height * b;
        m > p && (p = m), w >= p && (m = p, p = w, y = C.getAnchorPoint().x), this.verticalDirection === o.TOP_TO_BOTTOM && (S = 1 - C.anchorY), d = d + u * S * D + u * this.spacingY;
        var R = u * (1 - S) * D;
        if (e) {
         var M = d + R + u * (u > 0 ? this.paddingTop : this.paddingBottom),
          I = this.verticalDirection === o.BOTTOM_TO_TOP && M > (1 - a.y) * t,
          L = this.verticalDirection === o.TOP_TO_BOTTOM && M < -a.y * t;
         (I || L) && (w >= p ? (0 === m && (m = p), f += m, m = p) : (f += p, m = w, p = 0), d = _ + u * (l + S * D), v++)
        }
        var O = i(C, f, v);
        t >= D + (this.paddingTop + this.paddingBottom) && n && C.setPosition(cc.v2(O, d));
        var P, F = 1,
         N = 0 === p ? w : p;
        this.horizontalDirection === c.RIGHT_TO_LEFT ? (F = -1, g = g || this.node._contentSize.width, (P = O + F * (N * y + this.paddingLeft)) < g && (g = P)) : (g = g || -this.node._contentSize.width, (P = O + F * (N * y + this.paddingRight)) > g && (g = P)), d += R
       }
      }
      return g
     },
     _doLayoutBasic: function() {
      for (var t = this.node.children, e = null, i = 0; i < t.length; ++i) {
       var n = t[i];
       n.activeInHierarchy && (e ? e.union(e, n.getBoundingBoxToWorld()) : e = n.getBoundingBoxToWorld())
      }
      if (e) {
       var r = this.node.parent.convertToNodeSpaceAR(cc.v2(e.x, e.y));
       r = cc.v2(r.x - this.paddingLeft, r.y - this.paddingBottom);
       var s = this.node.parent.convertToNodeSpaceAR(cc.v2(e.x + e.width, e.y + e.height));
       s = cc.v2(s.x + this.paddingRight, s.y + this.paddingTop);
       var a = cc.size(parseFloat((s.x - r.x).toFixed(2)), parseFloat((s.y - r.y).toFixed(2))),
        o = this.node.getPosition();
       if (0 !== a.width) {
        var c = (o.x - r.x) / a.width;
        this.node.anchorX = parseFloat(c.toFixed(2))
       }
       if (0 !== a.height) {
        var h = (o.y - r.y) / a.height;
        this.node.anchorY = parseFloat(h.toFixed(2))
       }
       this.node.setContentSize(a)
      }
     },
     _doLayoutGridAxisHorizontal: function(t, e) {
      var i = e.width,
       n = 1,
       r = -t.y * e.height,
       a = this.paddingBottom;
      this.verticalDirection === o.TOP_TO_BOTTOM && (n = -1, r = (1 - t.y) * e.height, a = this.paddingTop);
      var c = function(t, e, i) {
        return r + n * (e + t.anchorY * t.height * this._getUsedScaleValue(t.scaleY) + a + i * this.spacingY)
       }.bind(this),
       h = 0;
      if (this.resizeMode === s.CONTAINER) {
       var u = this._doLayoutHorizontally(i, !0, c, !1);
       (h = r - u) < 0 && (h *= -1), r = -t.y * h, this.verticalDirection === o.TOP_TO_BOTTOM && (n = -1, r = (1 - t.y) * h)
      }
      this._doLayoutHorizontally(i, !0, c, !0), this.resizeMode === s.CONTAINER && this.node.setContentSize(i, h)
     },
     _doLayoutGridAxisVertical: function(t, e) {
      var i = e.height,
       n = 1,
       r = -t.x * e.width,
       a = this.paddingLeft;
      this.horizontalDirection === c.RIGHT_TO_LEFT && (n = -1, r = (1 - t.x) * e.width, a = this.paddingRight);
      var o = function(t, e, i) {
        return r + n * (e + t.anchorX * t.width * this._getUsedScaleValue(t.scaleX) + a + i * this.spacingX)
       }.bind(this),
       h = 0;
      if (this.resizeMode === s.CONTAINER) {
       var u = this._doLayoutVertically(i, !0, o, !1);
       (h = r - u) < 0 && (h *= -1), r = -t.x * h, this.horizontalDirection === c.RIGHT_TO_LEFT && (n = -1, r = (1 - t.x) * h)
      }
      this._doLayoutVertically(i, !0, o, !0), this.resizeMode === s.CONTAINER && this.node.setContentSize(h, i)
     },
     _doLayoutGrid: function() {
      var t = this.node.getAnchorPoint(),
       e = this.node.getContentSize();
      this.startAxis === a.HORIZONTAL ? this._doLayoutGridAxisHorizontal(t, e) : this.startAxis === a.VERTICAL && this._doLayoutGridAxisVertical(t, e)
     },
     _getHorizontalBaseWidth: function(t) {
      var e = 0,
       i = 0;
      if (this.resizeMode === s.CONTAINER) {
       for (var n = 0; n < t.length; ++n) {
        var r = t[n];
        r.activeInHierarchy && (i++, e += r.width * this._getUsedScaleValue(r.scaleX))
       }
       e += (i - 1) * this.spacingX + this.paddingLeft + this.paddingRight
      } else e = this.node.getContentSize().width;
      return e
     },
     _doLayout: function() {
      if (this.type === r.HORIZONTAL) {
       var t = this._getHorizontalBaseWidth(this.node.children);
       this._doLayoutHorizontally(t, !1, (function(t) {
        return t.y
       }), !0), this.node.width = t
      } else if (this.type === r.VERTICAL) {
       var e = this._getVerticalBaseHeight(this.node.children);
       this._doLayoutVertically(e, !1, (function(t) {
        return t.x
       }), !0), this.node.height = e
      } else this.type === r.NONE ? this.resizeMode === s.CONTAINER && this._doLayoutBasic() : this.type === r.GRID && this._doLayoutGrid()
     },
     _getUsedScaleValue: function(t) {
      return this.affectedByScale ? Math.abs(t) : 1
     },
     updateLayout: function() {
      this._layoutDirty && this.node.children.length > 0 && (this._doLayout(), this._layoutDirty = !1)
     }
    });
   Object.defineProperty(h.prototype, "padding", {
    get: function() {
     return cc.warnID(4100), this.paddingLeft
    },
    set: function(t) {
     this._N$padding = t, this._migratePaddingData(), this._doLayoutDirty()
    }
   }), cc.Layout = e.exports = h
  }), {
   "../CCNode": 24,
   "./CCComponent": 54
  }],
  59: [(function(t, e, i) {
   var n = t("../utils/misc"),
    r = t("../renderer/render-engine"),
    s = r.math,
    a = r.StencilMaterial,
    o = t("./CCRenderComponent"),
    c = t("../renderer/render-flow"),
    h = t("../graphics/graphics"),
    u = t("../CCNode"),
    l = t("../renderer/utils/dynamic-atlas/manager"),
    _ = cc.v2(),
    d = s.mat4.create(),
    f = [];
   var p = cc.Enum({
     RECT: 0,
     ELLIPSE: 1,
     IMAGE_STENCIL: 2
    }),
    m = cc.Class({
     name: "cc.Mask",
     extends: o,
     editor: !1,
     ctor: function() {
      this._graphics = null, this._clearGraphics = null
     },
     properties: {
      _spriteFrame: {
       default: null,
       type: cc.SpriteFrame
      },
      _type: p.RECT,
      type: {
       get: function() {
        return this._type
       },
       set: function(t) {
        this._type = t, this._type !== p.IMAGE_STENCIL && (this.spriteFrame = null, this.alphaThreshold = 0, this._updateGraphics()), this._renderData && (this.destroyRenderData(this._renderData), this._renderData = null), this._activateMaterial()
       },
       type: p,
       tooltip: !1
      },
      spriteFrame: {
       type: cc.SpriteFrame,
       tooltip: !1,
       get: function() {
        return this._spriteFrame
       },
       set: function(t) {
        var e = this._spriteFrame;
        e !== t && (this._spriteFrame = t, this._applySpriteFrame(e))
       }
      },
      alphaThreshold: {
       default: 0,
       type: cc.Float,
       range: [0, 1, .1],
       slide: !0,
       tooltip: !1,
       notify: function() {
        cc.game.renderType !== cc.game.RENDER_TYPE_CANVAS ? this._material && (this._material.alphaThreshold = this.alphaThreshold, this._material.updateHash()) : cc.warnID(4201)
       }
      },
      inverted: {
       default: !1,
       type: cc.Boolean,
       tooltip: !1,
       notify: function() {
        cc.game.renderType !== cc.game.RENDER_TYPE_CANVAS || cc.warnID(4202)
       }
      },
      _segments: 64,
      segements: {
       get: function() {
        return this._segments
       },
       set: function(t) {
        this._segments = n.clampf(t, 3, 1e4), this._updateGraphics()
       },
       type: cc.Integer,
       tooltip: !1
      },
      _resizeToTarget: {
       animatable: !1,
       set: function(t) {
        t && this._resizeNodeToTargetNode()
       }
      }
     },
     statics: {
      Type: p
     },
     onLoad: function() {
      this._createGraphics()
     },
     onRestore: function() {
      this._createGraphics(), this._type !== p.IMAGE_STENCIL ? this._updateGraphics() : this._applySpriteFrame()
     },
     onEnable: function() {
      this._super(), this._type === p.IMAGE_STENCIL ? this._spriteFrame && this._spriteFrame.textureLoaded() || (this.markForRender(!1), this._spriteFrame && (this.markForUpdateRenderData(!1), this._spriteFrame.once("load", this._onTextureLoaded, this), this._spriteFrame.ensureLoadTexture())) : this._updateGraphics(), this.node.on(cc.Node.EventType.POSITION_CHANGED, this._updateGraphics, this), this.node.on(cc.Node.EventType.ROTATION_CHANGED, this._updateGraphics, this), this.node.on(cc.Node.EventType.SCALE_CHANGED, this._updateGraphics, this), this.node.on(cc.Node.EventType.SIZE_CHANGED, this._updateGraphics, this), this.node.on(cc.Node.EventType.ANCHOR_CHANGED, this._updateGraphics, this), this.node._renderFlag |= c.FLAG_POST_RENDER, this._activateMaterial()
     },
     onDisable: function() {
      this._super(), this.node.off(cc.Node.EventType.POSITION_CHANGED, this._updateGraphics, this), this.node.off(cc.Node.EventType.ROTATION_CHANGED, this._updateGraphics, this), this.node.off(cc.Node.EventType.SCALE_CHANGED, this._updateGraphics, this), this.node.off(cc.Node.EventType.SIZE_CHANGED, this._updateGraphics, this), this.node.off(cc.Node.EventType.ANCHOR_CHANGED, this._updateGraphics, this), this.node._renderFlag &= ~c.FLAG_POST_RENDER
     },
     onDestroy: function() {
      this._super(), this._removeGraphics()
     },
     _resizeNodeToTargetNode: !1,
     _onTextureLoaded: function() {
      this._renderData && (this._renderData.uvDirty = !0, this._renderData.vertDirty = !0, this.markForUpdateRenderData(!0)), this.enabledInHierarchy && this._activateMaterial()
     },
     _applySpriteFrame: function(t) {
      t && t.off && t.off("load", this._onTextureLoaded, this);
      var e = this._spriteFrame;
      e ? e.textureLoaded() ? this._onTextureLoaded(null) : (e.once("load", this._onTextureLoaded, this), e.ensureLoadTexture()) : this.disableRender()
     },
     _activateMaterial: function() {
      if (this._type !== p.IMAGE_STENCIL || this.spriteFrame && this.spriteFrame.textureLoaded()) {
       if (cc.game.renderType !== cc.game.RENDER_TYPE_CANVAS)
        if (this._material || (this._material = new a), this._type === p.IMAGE_STENCIL) {
         var t = this.spriteFrame.getTexture();
         this._material.useModel = !1, this._material.useTexture = !0, this._material.useColor = !0, this._material.texture = t, this._material.alphaThreshold = this.alphaThreshold
        } else this._material.useModel = !0, this._material.useTexture = !1, this._material.useColor = !1;
       this.markForRender(!0)
      } else this.markForRender(!1)
     },
     _createGraphics: function() {
      this._graphics || (this._graphics = new h, this._graphics.node = this.node, this._graphics.lineWidth = 0, this._graphics.strokeColor = cc.color(0, 0, 0, 0)), this._clearGraphics || (this._clearGraphics = new h, this._clearGraphics.node = new u, this._clearGraphics._activateMaterial(), this._clearGraphics.lineWidth = 0, this._clearGraphics.rect(0, 0, cc.visibleRect.width, cc.visibleRect.height), this._clearGraphics.fill())
     },
     _updateGraphics: function() {
      var t = this.node,
       e = this._graphics;
      e.clear(!1);
      var i = t._contentSize.width,
       n = t._contentSize.height,
       r = -i * t._anchorPoint.x,
       s = -n * t._anchorPoint.y;
      if (this._type === p.RECT) e.rect(r, s, i, n);
      else if (this._type === p.ELLIPSE) {
       for (var a = (function(t, e, i) {
         f.length = 0;
         for (var n = 2 * Math.PI / i, r = 0; r < i; ++r) f.push(cc.v2(e.x * Math.cos(n * r) + t.x, e.y * Math.sin(n * r) + t.y));
         return f
        })(cc.v2(r + i / 2, s + n / 2), {
         x: i / 2,
         y: n / 2
        }, this._segments), o = 0; o < a.length; ++o) {
        var c = a[o];
        0 === o ? e.moveTo(c.x, c.y) : e.lineTo(c.x, c.y)
       }
       e.close()
      }
      cc.game.renderType === cc.game.RENDER_TYPE_CANVAS ? e.stroke() : e.fill()
     },
     _removeGraphics: function() {
      this._graphics && this._graphics.destroy(), this._clearGraphics && this._clearGraphics.destroy()
     },
     _hitTest: function(t) {
      var e = this.node,
       i = e.getContentSize(),
       n = i.width,
       r = i.height,
       a = _;
      if (e._updateWorldMatrix(), s.mat4.invert(d, e._worldMatrix), s.vec2.transformMat4(a, t, d), a.x += e._anchorPoint.x * n, a.y += e._anchorPoint.y * r, this.type === p.RECT || this.type === p.IMAGE_STENCIL) return a.x >= 0 && a.y >= 0 && a.x <= n && a.y <= r;
      if (this.type === p.ELLIPSE) {
       var o = n / 2,
        c = r / 2,
        h = a.x - .5 * n,
        u = a.y - .5 * r;
       return h * h / (o * o) + u * u / (c * c) < 1
      }
     },
     markForUpdateRenderData: function(t) {
      t && this.enabledInHierarchy ? this.node._renderFlag |= c.FLAG_UPDATE_RENDER_DATA : t || (this.node._renderFlag &= ~c.FLAG_UPDATE_RENDER_DATA)
     },
     markForRender: function(t) {
      t && this.enabledInHierarchy ? this.node._renderFlag |= c.FLAG_RENDER | c.FLAG_UPDATE_RENDER_DATA | c.FLAG_POST_RENDER : t || (this.node._renderFlag &= ~(c.FLAG_RENDER | c.FLAG_POST_RENDER))
     },
     disableRender: function() {
      this.node._renderFlag &= ~(c.FLAG_RENDER | c.FLAG_UPDATE_RENDER_DATA | c.FLAG_POST_RENDER)
     },
     _calDynamicAtlas: function() {
      if (this._spriteFrame) {
       if (!this._spriteFrame._original && l) {
        var t = l.insertSpriteFrame(this._spriteFrame);
        t && this._spriteFrame._setDynamicAtlasFrame(t)
       }
       this._material._texture !== this._spriteFrame._texture && this._activateMaterial()
      }
     }
    });
   cc.Mask = e.exports = m
  }), {
   "../CCNode": 24,
   "../graphics/graphics": 81,
   "../renderer/render-engine": 150,
   "../renderer/render-flow": 151,
   "../renderer/utils/dynamic-atlas/manager": 153,
   "../utils/misc": 186,
   "./CCRenderComponent": 62
  }],
  60: [(function(t, e, i) {
   var n = t("../components/CCRenderComponent"),
    r = t("../renderer/render-engine").SpriteMaterial,
    s = t("../utils/texture-util"),
    a = cc.Class({
     name: "cc.MotionStreak",
     extends: n,
     editor: !1,
     ctor: function() {
      this._points = []
     },
     properties: {
      preview: {
       default: !1,
       editorOnly: !0,
       notify: !1,
       animatable: !1
      },
      _fadeTime: 1,
      fadeTime: {
       get: function() {
        return this._fadeTime
       },
       set: function(t) {
        this._fadeTime = t, this.reset()
       },
       animatable: !1,
       tooltip: !1
      },
      _minSeg: 1,
      minSeg: {
       get: function() {
        return this._minSeg
       },
       set: function(t) {
        this._minSeg = t
       },
       animatable: !1,
       tooltip: !1
      },
      _stroke: 64,
      stroke: {
       get: function() {
        return this._stroke
       },
       set: function(t) {
        this._stroke = t
       },
       animatable: !1,
       tooltip: !1
      },
      _texture: {
       default: null,
       type: cc.Texture2D
      },
      texture: {
       get: function() {
        return this._texture
       },
       set: function(t) {
        this._texture !== t && (this._texture = t, t && t.loaded ? this._activateMaterial() : (this.disableRender(), this._ensureLoadTexture()))
       },
       type: cc.Texture2D,
       animatable: !1,
       tooltip: !1
      },
      _color: cc.Color.WHITE,
      color: {
       get: function() {
        return this._color
       },
       set: function(t) {
        this._color = t
       },
       type: cc.Color,
       tooltip: !1
      },
      _fastMode: !1,
      fastMode: {
       get: function() {
        return this._fastMode
       },
       set: function(t) {
        this._fastMode = t
       },
       animatable: !1,
       tooltip: !1
      }
     },
     onEnable: function() {
      this._super(), this._texture && this._texture.loaded ? this._activateMaterial() : (this.disableRender(), this._ensureLoadTexture()), this.reset()
     },
     _ensureLoadTexture: function() {
      var t = this;
      this._texture && !this._texture.loaded && (function() {
       var e = t;
       s.postLoadTexture(t._texture, (function() {
        e._activateMaterial()
       }))
      })()
     },
     _activateMaterial: function() {
      var t = this._material;
      t || ((t = this._material = new r).useColor = !1), this._texture && this._texture.loaded && (t.texture = this._texture, this._updateMaterial(t), this.markForRender(!0), this.markForUpdateRenderData(!0))
     },
     onFocusInEditor: !1,
     onLostFocusInEditor: !1,
     reset: function() {
      this._points.length = 0;
      var t = this._renderData;
      t && (t.dataLength = 0, t.vertexCount = 0, t.indiceCount = 0)
     }
    });
   cc.MotionStreak = e.exports = a
  }), {
   "../components/CCRenderComponent": 62,
   "../renderer/render-engine": 150,
   "../utils/texture-util": 194
  }],
  61: [(function(t, e, i) {
   var n = t("../utils/misc"),
    r = t("./CCComponent"),
    s = cc.Enum({
     HORIZONTAL: 0,
     VERTICAL: 1,
     FILLED: 2
    }),
    a = cc.Class({
     name: "cc.ProgressBar",
     extends: r,
     editor: !1,
     _initBarSprite: function() {
      if (this.barSprite) {
       var t = this.barSprite.node;
       if (!t) return;
       var e = this.node.getContentSize(),
        i = this.node.getAnchorPoint(),
        n = t.getContentSize();
       t.parent === this.node && this.node.setContentSize(n), this.barSprite.fillType === cc.Sprite.FillType.RADIAL && (this.mode = s.FILLED);
       var r = t.getContentSize();
       if (this.mode === s.HORIZONTAL ? this.totalLength = r.width : this.mode === s.VERTICAL ? this.totalLength = r.height : this.totalLength = this.barSprite.fillRange, t.parent === this.node) {
        var a = -e.width * i.x;
        t.setPosition(cc.v2(a, 0))
       }
      }
     },
     _updateBarStatus: function() {
      if (this.barSprite) {
       var t = this.barSprite.node;
       if (!t) return;
       var e, i, r, a = t.getAnchorPoint(),
        o = t.getContentSize(),
        c = t.getPosition(),
        h = cc.v2(0, .5),
        u = n.clamp01(this.progress),
        l = this.totalLength * u;
       switch (this.mode) {
        case s.HORIZONTAL:
         this.reverse && (h = cc.v2(1, .5)), e = cc.size(l, o.height), i = this.totalLength, r = o.height;
         break;
        case s.VERTICAL:
         h = this.reverse ? cc.v2(.5, 1) : cc.v2(.5, 0), e = cc.size(o.width, l), i = o.width, r = this.totalLength
       }
       if (this.mode === s.FILLED) this.barSprite.type !== cc.Sprite.Type.FILLED ? cc.warn("ProgressBar FILLED mode only works when barSprite's Type is FILLED!") : (this.reverse && (l *= -1), this.barSprite.fillRange = l);
       else if (this.barSprite.type !== cc.Sprite.Type.FILLED) {
        var _ = h.x - a.x,
         d = h.y - a.y,
         f = cc.v2(i * _, r * d);
        t.setPosition(c.x + f.x, c.y + f.y), t.setAnchorPoint(h), t.setContentSize(e)
       } else cc.warn("ProgressBar non-FILLED mode only works when barSprite's Type is non-FILLED!")
      }
     },
     properties: {
      barSprite: {
       default: null,
       type: cc.Sprite,
       tooltip: !1,
       notify: function() {
        this._initBarSprite()
       },
       animatable: !1
      },
      mode: {
       default: s.HORIZONTAL,
       type: s,
       tooltip: !1,
       notify: function() {
        if (this.barSprite) {
         var t = this.barSprite.node;
         if (!t) return;
         var e = t.getContentSize();
         this.mode === s.HORIZONTAL ? this.totalLength = e.width : this.mode === s.VERTICAL ? this.totalLength = e.height : this.mode === s.FILLED && (this.totalLength = this.barSprite.fillRange)
        }
       },
       animatable: !1
      },
      _N$totalLength: 1,
      totalLength: {
       range: [0, Number.MAX_VALUE],
       tooltip: !1,
       get: function() {
        return this._N$totalLength
       },
       set: function(t) {
        this.mode === s.FILLED && (t = n.clamp01(t)), this._N$totalLength = t, this._updateBarStatus()
       }
      },
      progress: {
       default: 1,
       type: "Float",
       range: [0, 1, .1],
       slide: !0,
       tooltip: !1,
       notify: function() {
        this._updateBarStatus()
       }
      },
      reverse: {
       default: !1,
       tooltip: !1,
       notify: function() {
        this.barSprite && (this.barSprite.fillStart = 1 - this.barSprite.fillStart), this._updateBarStatus()
       },
       animatable: !1
      }
     },
     statics: {
      Mode: s
     }
    });
   cc.ProgressBar = e.exports = a
  }), {
   "../utils/misc": 186,
   "./CCComponent": 54
  }],
  62: [(function(t, e, i) {
   var n = t("./CCComponent"),
    r = t("../renderer/render-engine"),
    s = t("../renderer/render-flow"),
    a = t("../platform/CCMacro").BlendFactor,
    o = r.RenderData,
    c = r.gfx,
    h = cc.Class({
     name: "RenderComponent",
     extends: n,
     editor: !1,
     properties: {
      _srcBlendFactor: a.SRC_ALPHA,
      _dstBlendFactor: a.ONE_MINUS_SRC_ALPHA,
      srcBlendFactor: {
       get: function() {
        return this._srcBlendFactor
       },
       set: function(t) {
        this._srcBlendFactor !== t && (this._srcBlendFactor = t, this._updateBlendFunc(!0))
       },
       animatable: !1,
       type: a,
       tooltip: !1
      },
      dstBlendFactor: {
       get: function() {
        return this._dstBlendFactor
       },
       set: function(t) {
        this._dstBlendFactor !== t && (this._dstBlendFactor = t, this._updateBlendFunc(!0))
       },
       animatable: !1,
       type: a,
       tooltip: !1
      }
     },
     ctor: function() {
      this._material = null, this._renderData = null, this.__allocedDatas = [], this._vertexFormat = null, this._toPostHandle = !1, this._assembler = this.constructor._assembler, this._postAssembler = this.constructor._postAssembler
     },
     onEnable: function() {
      this.node._renderComponent && (this.node._renderComponent.enabled = !1), this.node._renderComponent = this, this.node._renderFlag |= s.FLAG_RENDER | s.FLAG_UPDATE_RENDER_DATA | s.FLAG_COLOR
     },
     onDisable: function() {
      this.node._renderComponent = null, this.disableRender()
     },
     onDestroy: function() {
      for (var t = 0, e = this.__allocedDatas.length; t < e; t++) o.free(this.__allocedDatas[t]);
      this.__allocedDatas.length = 0, this._material = null, this._renderData = null
     },
     _canRender: function() {
      return this._enabled && this.node._activeInHierarchy
     },
     markForUpdateRenderData: function(t) {
      t && this._canRender() ? this.node._renderFlag |= s.FLAG_UPDATE_RENDER_DATA : t || (this.node._renderFlag &= ~s.FLAG_UPDATE_RENDER_DATA)
     },
     markForRender: function(t) {
      t && this._canRender() ? this.node._renderFlag |= s.FLAG_RENDER : t || (this.node._renderFlag &= ~s.FLAG_RENDER)
     },
     markForCustomIARender: function(t) {
      t && this._canRender() ? this.node._renderFlag |= s.FLAG_CUSTOM_IA_RENDER : t || (this.node._renderFlag &= ~s.FLAG_CUSTOM_IA_RENDER)
     },
     disableRender: function() {
      this.node._renderFlag &= ~(s.FLAG_RENDER | s.FLAG_CUSTOM_IA_RENDER | s.FLAG_UPDATE_RENDER_DATA | s.FLAG_COLOR)
     },
     requestRenderData: function() {
      var t = o.alloc();
      return this.__allocedDatas.push(t), t
     },
     destroyRenderData: function(t) {
      var e = this.__allocedDatas.indexOf(t); - 1 !== e && (this.__allocedDatas.splice(e, 1), o.free(t))
     },
     _updateColor: function() {
      var t = this._material;
      t && (t.useColor && (t.color = this.node.color, t.updateHash()), this.node._renderFlag &= ~s.FLAG_COLOR)
     },
     getMaterial: function() {
      return this._material
     },
     _updateMaterial: function(t) {
      this._material = t, this._updateBlendFunc(), t.updateHash()
     },
     _updateBlendFunc: function(t) {
      this._material && (this._material._mainTech.passes[0].setBlend(c.BLEND_FUNC_ADD, this._srcBlendFactor, this._dstBlendFactor, c.BLEND_FUNC_ADD, this._srcBlendFactor, this._dstBlendFactor), t && this._material.updateHash())
     }
    });
   h._assembler = null, h._postAssembler = null, cc.RenderComponent = e.exports = h
  }), {
   "../platform/CCMacro": 113,
   "../renderer/render-engine": 150,
   "../renderer/render-flow": 151,
   "./CCComponent": 54
  }],
  63: [(function(t, e, i) {
   var n = t("../CCNode").EventType,
    r = function() {
     return (new Date).getMilliseconds()
    },
    s = cc.Enum({
     SCROLL_TO_TOP: 0,
     SCROLL_TO_BOTTOM: 1,
     SCROLL_TO_LEFT: 2,
     SCROLL_TO_RIGHT: 3,
     SCROLLING: 4,
     BOUNCE_TOP: 5,
     BOUNCE_BOTTOM: 6,
     BOUNCE_LEFT: 7,
     BOUNCE_RIGHT: 8,
     SCROLL_ENDED: 9,
     TOUCH_UP: 10,
     AUTOSCROLL_ENDED_WITH_THRESHOLD: 11,
     SCROLL_BEGAN: 12
    }),
    a = {
     "scroll-to-top": s.SCROLL_TO_TOP,
     "scroll-to-bottom": s.SCROLL_TO_BOTTOM,
     "scroll-to-left": s.SCROLL_TO_LEFT,
     "scroll-to-right": s.SCROLL_TO_RIGHT,
     scrolling: s.SCROLLING,
     "bounce-bottom": s.BOUNCE_BOTTOM,
     "bounce-left": s.BOUNCE_LEFT,
     "bounce-right": s.BOUNCE_RIGHT,
     "bounce-top": s.BOUNCE_TOP,
     "scroll-ended": s.SCROLL_ENDED,
     "touch-up": s.TOUCH_UP,
     "scroll-ended-with-threshold": s.AUTOSCROLL_ENDED_WITH_THRESHOLD,
     "scroll-began": s.SCROLL_BEGAN
    },
    o = cc.Class({
     name: "cc.ScrollView",
     extends: t("./CCViewGroup"),
     editor: !1,
     ctor: function() {
      this._topBoundary = 0, this._bottomBoundary = 0, this._leftBoundary = 0, this._rightBoundary = 0, this._touchMoveDisplacements = [], this._touchMoveTimeDeltas = [], this._touchMovePreviousTimestamp = 0, this._touchMoved = !1, this._autoScrolling = !1, this._autoScrollAttenuate = !1, this._autoScrollStartPosition = cc.v2(0, 0), this._autoScrollTargetDelta = cc.v2(0, 0), this._autoScrollTotalTime = 0, this._autoScrollAccumulatedTime = 0, this._autoScrollCurrentlyOutOfBoundary = !1, this._autoScrollBraking = !1, this._autoScrollBrakingStartPosition = cc.v2(0, 0), this._outOfBoundaryAmount = cc.v2(0, 0), this._outOfBoundaryAmountDirty = !0, this._stopMouseWheel = !1, this._mouseWheelEventElapsedTime = 0, this._isScrollEndedWithThresholdEventFired = !1, this._scrollEventEmitMask = 0, this._isBouncing = !1, this._scrolling = !1
     },
     properties: {
      content: {
       default: void 0,
       type: cc.Node,
       tooltip: !1,
       formerlySerializedAs: "content",
       notify: function(t) {
        this._calculateBoundary()
       }
      },
      horizontal: {
       default: !0,
       animatable: !1,
       tooltip: !1
      },
      vertical: {
       default: !0,
       animatable: !1,
       tooltip: !1
      },
      inertia: {
       default: !0,
       tooltip: !1
      },
      brake: {
       default: .5,
       type: "Float",
       range: [0, 1, .1],
       tooltip: !1
      },
      elastic: {
       default: !0,
       animatable: !1,
       tooltip: !1
      },
      bounceDuration: {
       default: 1,
       range: [0, 10],
       tooltip: !1
      },
      horizontalScrollBar: {
       default: void 0,
       type: cc.Scrollbar,
       tooltip: !1,
       notify: function() {
        this.horizontalScrollBar && (this.horizontalScrollBar.setTargetScrollView(this), this._updateScrollBar(0))
       },
       animatable: !1
      },
      verticalScrollBar: {
       default: void 0,
       type: cc.Scrollbar,
       tooltip: !1,
       notify: function() {
        this.verticalScrollBar && (this.verticalScrollBar.setTargetScrollView(this), this._updateScrollBar(0))
       },
       animatable: !1
      },
      scrollEvents: {
       default: [],
       type: cc.Component.EventHandler,
       tooltip: !1
      },
      cancelInnerEvents: {
       default: !0,
       animatable: !1,
       tooltip: !1
      },
      _view: {
       get: function() {
        if (this.content) return this.content.parent
       }
      }
     },
     statics: {
      EventType: s
     },
     scrollToBottom: function(t, e) {
      var i = this._calculateMovePercentDelta({
       anchor: cc.v2(0, 0),
       applyToHorizontal: !1,
       applyToVertical: !0
      });
      t ? this._startAutoScroll(i, t, !1 !== e) : this._moveContent(i, !0)
     },
     scrollToTop: function(t, e) {
      var i = this._calculateMovePercentDelta({
       anchor: cc.v2(0, 1),
       applyToHorizontal: !1,
       applyToVertical: !0
      });
      t ? this._startAutoScroll(i, t, !1 !== e) : this._moveContent(i)
     },
     scrollToLeft: function(t, e) {
      var i = this._calculateMovePercentDelta({
       anchor: cc.v2(0, 0),
       applyToHorizontal: !0,
       applyToVertical: !1
      });
      t ? this._startAutoScroll(i, t, !1 !== e) : this._moveContent(i)
     },
     scrollToRight: function(t, e) {
      var i = this._calculateMovePercentDelta({
       anchor: cc.v2(1, 0),
       applyToHorizontal: !0,
       applyToVertical: !1
      });
      t ? this._startAutoScroll(i, t, !1 !== e) : this._moveContent(i)
     },
     scrollToTopLeft: function(t, e) {
      var i = this._calculateMovePercentDelta({
       anchor: cc.v2(0, 1),
       applyToHorizontal: !0,
       applyToVertical: !0
      });
      t ? this._startAutoScroll(i, t, !1 !== e) : this._moveContent(i)
     },
     scrollToTopRight: function(t, e) {
      var i = this._calculateMovePercentDelta({
       anchor: cc.v2(1, 1),
       applyToHorizontal: !0,
       applyToVertical: !0
      });
      t ? this._startAutoScroll(i, t, !1 !== e) : this._moveContent(i)
     },
     scrollToBottomLeft: function(t, e) {
      var i = this._calculateMovePercentDelta({
       anchor: cc.v2(0, 0),
       applyToHorizontal: !0,
       applyToVertical: !0
      });
      t ? this._startAutoScroll(i, t, !1 !== e) : this._moveContent(i)
     },
     scrollToBottomRight: function(t, e) {
      var i = this._calculateMovePercentDelta({
       anchor: cc.v2(1, 0),
       applyToHorizontal: !0,
       applyToVertical: !0
      });
      t ? this._startAutoScroll(i, t, !1 !== e) : this._moveContent(i)
     },
     scrollToOffset: function(t, e, i) {
      var n = this.getMaxScrollOffset(),
       r = cc.v2(0, 0);
      0 === n.x ? r.x = 0 : r.x = t.x / n.x, 0 === n.y ? r.y = 1 : r.y = (n.y - t.y) / n.y, this.scrollTo(r, e, i)
     },
     getScrollOffset: function() {
      var t = this._getContentTopBoundary() - this._topBoundary,
       e = this._getContentLeftBoundary() - this._leftBoundary;
      return cc.v2(e, t)
     },
     getMaxScrollOffset: function() {
      var t = this._view.getContentSize(),
       e = this.content.getContentSize(),
       i = e.width - t.width,
       n = e.height - t.height;
      return i = i >= 0 ? i : 0, n = n >= 0 ? n : 0, cc.v2(i, n)
     },
     scrollToPercentHorizontal: function(t, e, i) {
      var n = this._calculateMovePercentDelta({
       anchor: cc.v2(t, 0),
       applyToHorizontal: !0,
       applyToVertical: !1
      });
      e ? this._startAutoScroll(n, e, !1 !== i) : this._moveContent(n)
     },
     scrollTo: function(t, e, i) {
      var n = this._calculateMovePercentDelta({
       anchor: cc.v2(t),
       applyToHorizontal: !0,
       applyToVertical: !0
      });
      e ? this._startAutoScroll(n, e, !1 !== i) : this._moveContent(n)
     },
     scrollToPercentVertical: function(t, e, i) {
      var n = this._calculateMovePercentDelta({
       anchor: cc.v2(0, t),
       applyToHorizontal: !1,
       applyToVertical: !0
      });
      e ? this._startAutoScroll(n, e, !1 !== i) : this._moveContent(n)
     },
     stopAutoScroll: function() {
      this._autoScrolling = !1, this._autoScrollAccumulatedTime = this._autoScrollTotalTime
     },
     setContentPosition: function(t) {
      t.fuzzyEquals(this.getContentPosition(), 1e-4) || (this.content.setPosition(t), this._outOfBoundaryAmountDirty = !0)
     },
     getContentPosition: function() {
      return this.content.getPosition()
     },
     isScrolling: function() {
      return this._scrolling
     },
     isAutoScrolling: function() {
      return this._autoScrolling
     },
     _registerEvent: function() {
      this.node.on(cc.Node.EventType.TOUCH_START, this._onTouchBegan, this, !0), this.node.on(cc.Node.EventType.TOUCH_MOVE, this._onTouchMoved, this, !0), this.node.on(cc.Node.EventType.TOUCH_END, this._onTouchEnded, this, !0), this.node.on(cc.Node.EventType.TOUCH_CANCEL, this._onTouchCancelled, this, !0), this.node.on(cc.Node.EventType.MOUSE_WHEEL, this._onMouseWheel, this, !0)
     },
     _unregisterEvent: function() {
      this.node.off(cc.Node.EventType.TOUCH_START, this._onTouchBegan, this, !0), this.node.off(cc.Node.EventType.TOUCH_MOVE, this._onTouchMoved, this, !0), this.node.off(cc.Node.EventType.TOUCH_END, this._onTouchEnded, this, !0), this.node.off(cc.Node.EventType.TOUCH_CANCEL, this._onTouchCancelled, this, !0), this.node.off(cc.Node.EventType.MOUSE_WHEEL, this._onMouseWheel, this, !0)
     },
     _onMouseWheel: function(t, e) {
      if (this.enabledInHierarchy && !this._hasNestedViewGroup(t, e)) {
       var i = cc.v2(0, 0),
        n = -.1;
       0, this.vertical ? i = cc.v2(0, t.getScrollY() * n) : this.horizontal && (i = cc.v2(t.getScrollY() * n, 0)), this._mouseWheelEventElapsedTime = 0, this._processDeltaMove(i), this._stopMouseWheel || (this._handlePressLogic(), this.schedule(this._checkMouseWheel, 1 / 60), this._stopMouseWheel = !0), this._stopPropagationIfTargetIsMe(t)
      }
     },
     _checkMouseWheel: function(t) {
      if (!this._getHowMuchOutOfBoundary().fuzzyEquals(cc.v2(0, 0), 1e-4)) return this._processInertiaScroll(), this.unschedule(this._checkMouseWheel), void(this._stopMouseWheel = !1);
      this._mouseWheelEventElapsedTime += t, this._mouseWheelEventElapsedTime > .1 && (this._onScrollBarTouchEnded(), this.unschedule(this._checkMouseWheel), this._stopMouseWheel = !1)
     },
     _calculateMovePercentDelta: function(t) {
      var e = t.anchor,
       i = t.applyToHorizontal,
       n = t.applyToVertical;
      this._calculateBoundary(), e = e.clampf(cc.v2(0, 0), cc.v2(1, 1));
      var r = this._view.getContentSize(),
       s = this.content.getContentSize(),
       a = this._getContentBottomBoundary() - this._bottomBoundary;
      a = -a;
      var o = this._getContentLeftBoundary() - this._leftBoundary;
      o = -o;
      var c = cc.v2(0, 0),
       h = 0;
      return i && (h = s.width - r.width, c.x = o - h * e.x), n && (h = s.height - r.height, c.y = a - h * e.y), c
     },
     _moveContentToTopLeft: function(t) {
      var e = this.content.getContentSize(),
       i = this._getContentBottomBoundary() - this._bottomBoundary;
      i = -i;
      var n = cc.v2(0, 0),
       r = 0,
       s = this._getContentLeftBoundary() - this._leftBoundary;
      s = -s, e.height < t.height ? (r = e.height - t.height, n.y = i - r, this.verticalScrollBar && this.verticalScrollBar.hide()) : this.verticalScrollBar && this.verticalScrollBar.show(), e.width < t.width ? (r = e.width - t.width, n.x = s, this.horizontalScrollBar && this.horizontalScrollBar.hide()) : this.horizontalScrollBar && this.horizontalScrollBar.show(), this._moveContent(n), this._adjustContentOutOfBoundary()
     },
     _calculateBoundary: function() {
      if (this.content) {
       var t = this.content.getComponent(cc.Layout);
       t && t.enabledInHierarchy && t.updateLayout();
       var e = this._view.getContentSize(),
        i = this._convertToContentParentSpace(cc.v2(0, 0));
       this._leftBoundary = i.x, this._bottomBoundary = i.y;
       var n = this._convertToContentParentSpace(cc.v2(e.width, e.height));
       this._rightBoundary = n.x, this._topBoundary = n.y, this._moveContentToTopLeft(e)
      }
     },
     _convertToContentParentSpace: function(t) {
      var e = this._view,
       i = e.convertToWorldSpace(t);
      return e.convertToNodeSpaceAR(i)
     },
     _hasNestedViewGroup: function(t, e) {
      if (t.eventPhase === cc.Event.CAPTURING_PHASE) {
       if (e)
        for (var i = 0; i < e.length; ++i) {
         var n = e[i];
         if (this.node === n) return !!t.target.getComponent(cc.ViewGroup);
         if (n.getComponent(cc.ViewGroup)) return !0
        }
       return !1
      }
     },
     _stopPropagationIfTargetIsMe: function(t) {
      t.eventPhase === cc.Event.AT_TARGET && t.target === this.node && t.stopPropagation()
     },
     _onTouchBegan: function(t, e) {
      if (this.enabledInHierarchy && !this._hasNestedViewGroup(t, e)) {
       var i = t.touch;
       this.content && this._handlePressLogic(i), this._touchMoved = !1, this._stopPropagationIfTargetIsMe(t)
      }
     },
     _onTouchMoved: function(t, e) {
      if (this.enabledInHierarchy && !this._hasNestedViewGroup(t, e)) {
       var i = t.touch;
       if (this.content && this._handleMoveLogic(i), this.cancelInnerEvents) {
        if (i.getLocation().sub(i.getStartLocation()).mag() > 7 && !this._touchMoved && t.target !== this.node) {
         var n = new cc.Event.EventTouch(t.getTouches(), t.bubbles);
         n.type = cc.Node.EventType.TOUCH_CANCEL, n.touch = t.touch, n.simulate = !0, t.target.dispatchEvent(n), this._touchMoved = !0
        }
        this._stopPropagationIfTargetIsMe(t)
       }
      }
     },
     _onTouchEnded: function(t, e) {
      if (this.enabledInHierarchy && !this._hasNestedViewGroup(t, e)) {
       this._dispatchEvent("touch-up");
       var i = t.touch;
       this.content && this._handleReleaseLogic(i), this._touchMoved ? t.stopPropagation() : this._stopPropagationIfTargetIsMe(t)
      }
     },
     _onTouchCancelled: function(t, e) {
      if (this.enabledInHierarchy && !this._hasNestedViewGroup(t, e)) {
       if (!t.simulate) {
        var i = t.touch;
        this.content && this._handleReleaseLogic(i)
       }
       this._stopPropagationIfTargetIsMe(t)
      }
     },
     _processDeltaMove: function(t) {
      this._scrollChildren(t), this._gatherTouchMove(t)
     },
     _handleMoveLogic: function(t) {
      var e = t.getDelta();
      this._processDeltaMove(e)
     },
     _scrollChildren: function(t) {
      var e = t = this._clampDelta(t),
       i = void 0;
      this.elastic && (i = this._getHowMuchOutOfBoundary(), e.x *= 0 === i.x ? 1 : .5, e.y *= 0 === i.y ? 1 : .5), this.elastic || (i = this._getHowMuchOutOfBoundary(e), e = e.add(i));
      var n = -1;
      if (e.y > 0) this.content.y - this.content.anchorY * this.content.height + e.y > this._bottomBoundary && (n = "scroll-to-bottom");
      else if (e.y < 0) {
       this.content.y - this.content.anchorY * this.content.height + this.content.height + e.y <= this._topBoundary && (n = "scroll-to-top")
      } else if (e.x < 0) {
       this.content.x - this.content.anchorX * this.content.width + this.content.width + e.x <= this._rightBoundary && (n = "scroll-to-right")
      } else if (e.x > 0) {
       this.content.x - this.content.anchorX * this.content.width + e.x >= this._leftBoundary && (n = "scroll-to-left")
      }
      this._moveContent(e, !1), 0 === e.x && 0 === e.y || (this._scrolling || (this._scrolling = !0, this._dispatchEvent("scroll-began")), this._dispatchEvent("scrolling")), -1 !== n && this._dispatchEvent(n)
     },
     _handlePressLogic: function() {
      this._autoScrolling && this._dispatchEvent("scroll-ended"), this._autoScrolling = !1, this._isBouncing = !1, this._touchMovePreviousTimestamp = r(), this._touchMoveDisplacements.length = 0, this._touchMoveTimeDeltas.length = 0, this._onScrollBarTouchBegan()
     },
     _clampDelta: function(t) {
      var e = this.content.getContentSize(),
       i = this.node.getContentSize();
      return e.width < i.width && (t.x = 0), e.height < i.height && (t.y = 0), t
     },
     _gatherTouchMove: function(t) {
      for (t = this._clampDelta(t); this._touchMoveDisplacements.length >= 5;) this._touchMoveDisplacements.shift(), this._touchMoveTimeDeltas.shift();
      this._touchMoveDisplacements.push(t);
      var e = r();
      this._touchMoveTimeDeltas.push((e - this._touchMovePreviousTimestamp) / 1e3), this._touchMovePreviousTimestamp = e
     },
     _startBounceBackIfNeeded: function() {
      if (!this.elastic) return !1;
      var t = this._getHowMuchOutOfBoundary();
      if ((t = this._clampDelta(t)).fuzzyEquals(cc.v2(0, 0), 1e-4)) return !1;
      var e = Math.max(this.bounceDuration, 0);
      return this._startAutoScroll(t, e, !0), this._isBouncing || (t.y > 0 && this._dispatchEvent("bounce-top"), t.y < 0 && this._dispatchEvent("bounce-bottom"), t.x > 0 && this._dispatchEvent("bounce-right"), t.x < 0 && this._dispatchEvent("bounce-left"), this._isBouncing = !0), !0
     },
     _processInertiaScroll: function() {
      if (!this._startBounceBackIfNeeded() && this.inertia) {
       var t = this._calculateTouchMoveVelocity();
       !t.fuzzyEquals(cc.v2(0, 0), 1e-4) && this.brake < 1 && this._startInertiaScroll(t)
      }
      this._onScrollBarTouchEnded()
     },
     _handleReleaseLogic: function(t) {
      var e = t.getDelta();
      this._gatherTouchMove(e), this._processInertiaScroll(), this._scrolling && (this._scrolling = !1, this._autoScrolling || this._dispatchEvent("scroll-ended"))
     },
     _isOutOfBoundary: function() {
      return !this._getHowMuchOutOfBoundary().fuzzyEquals(cc.v2(0, 0), 1e-4)
     },
     _isNecessaryAutoScrollBrake: function() {
      if (this._autoScrollBraking) return !0;
      if (this._isOutOfBoundary()) {
       if (!this._autoScrollCurrentlyOutOfBoundary) return this._autoScrollCurrentlyOutOfBoundary = !0, this._autoScrollBraking = !0, this._autoScrollBrakingStartPosition = this.getContentPosition(), !0
      } else this._autoScrollCurrentlyOutOfBoundary = !1;
      return !1
     },
     getScrollEndedEventTiming: function() {
      return 1e-4
     },
     _processAutoScrolling: function(t) {
      var e = this._isNecessaryAutoScrollBrake(),
       i = e ? .05 : 1;
      this._autoScrollAccumulatedTime += t * (1 / i);
      var n = Math.min(1, this._autoScrollAccumulatedTime / this._autoScrollTotalTime);
      this._autoScrollAttenuate && (n = (function(t) {
       return (t -= 1) * t * t * t * t + 1
      })(n));
      var r = this._autoScrollStartPosition.add(this._autoScrollTargetDelta.mul(n)),
       s = Math.abs(n - 1) <= 1e-4;
      if (Math.abs(n - 1) <= this.getScrollEndedEventTiming() && !this._isScrollEndedWithThresholdEventFired && (this._dispatchEvent("scroll-ended-with-threshold"), this._isScrollEndedWithThresholdEventFired = !0), this.elastic) {
       var a = r.sub(this._autoScrollBrakingStartPosition);
       e && (a = a.mul(i)), r = this._autoScrollBrakingStartPosition.add(a)
      } else {
       var o = r.sub(this.getContentPosition()),
        c = this._getHowMuchOutOfBoundary(o);
       c.fuzzyEquals(cc.v2(0, 0), 1e-4) || (r = r.add(c), s = !0)
      }
      s && (this._autoScrolling = !1);
      var h = r.sub(this.getContentPosition());
      this._moveContent(this._clampDelta(h), s), this._dispatchEvent("scrolling"), this._autoScrolling || (this._isBouncing = !1, this._scrolling = !1, this._dispatchEvent("scroll-ended"))
     },
     _startInertiaScroll: function(t) {
      var e = t.mul(.7);
      this._startAttenuatingAutoScroll(e, t)
     },
     _calculateAttenuatedFactor: function(t) {
      return this.brake <= 0 ? 1 - this.brake : (1 - this.brake) * (1 / (1 + 14e-6 * t + t * t * 8e-9))
     },
     _startAttenuatingAutoScroll: function(t, e) {
      var i = this._calculateAutoScrollTimeByInitalSpeed(e.mag()),
       n = t.normalize(),
       r = this.content.getContentSize(),
       s = this.node.getContentSize(),
       a = r.width - s.width,
       o = r.height - s.height,
       c = this._calculateAttenuatedFactor(a),
       h = this._calculateAttenuatedFactor(o);
      n = cc.v2(n.x * a * (1 - this.brake) * c, n.y * o * h * (1 - this.brake));
      var u = t.mag(),
       l = n.mag() / u;
      n = n.add(t), this.brake > 0 && l > 7 && (l = Math.sqrt(l), n = t.mul(l).add(t)), this.brake > 0 && l > 3 && (i *= l = 3), 0 === this.brake && l > 1 && (i *= l), this._startAutoScroll(n, i, !0)
     },
     _calculateAutoScrollTimeByInitalSpeed: function(t) {
      return Math.sqrt(Math.sqrt(t / 5))
     },
     _startAutoScroll: function(t, e, i) {
      var n = this._flattenVectorByDirection(t);
      this._autoScrolling = !0, this._autoScrollTargetDelta = n, this._autoScrollAttenuate = i, this._autoScrollStartPosition = this.getContentPosition(), this._autoScrollTotalTime = e, this._autoScrollAccumulatedTime = 0, this._autoScrollBraking = !1, this._isScrollEndedWithThresholdEventFired = !1, this._autoScrollBrakingStartPosition = cc.v2(0, 0), this._getHowMuchOutOfBoundary().fuzzyEquals(cc.v2(0, 0), 1e-4) || (this._autoScrollCurrentlyOutOfBoundary = !0)
     },
     _calculateTouchMoveVelocity: function() {
      var t = 0;
      if ((t = this._touchMoveTimeDeltas.reduce((function(t, e) {
        return t + e
       }), t)) <= 0 || t >= .5) return cc.v2(0, 0);
      var e = cc.v2(0, 0);
      return e = this._touchMoveDisplacements.reduce((function(t, e) {
       return t.add(e)
      }), e), cc.v2(e.x * (1 - this.brake) / t, e.y * (1 - this.brake) / t)
     },
     _flattenVectorByDirection: function(t) {
      var e = t;
      return e.x = this.horizontal ? e.x : 0, e.y = this.vertical ? e.y : 0, e
     },
     _moveContent: function(t, e) {
      var i = this._flattenVectorByDirection(t),
       n = this.getContentPosition().add(i);
      this.setContentPosition(n);
      var r = this._getHowMuchOutOfBoundary();
      this._updateScrollBar(r), this.elastic && e && this._startBounceBackIfNeeded()
     },
     _getContentLeftBoundary: function() {
      return this.getContentPosition().x - this.content.getAnchorPoint().x * this.content.getContentSize().width
     },
     _getContentRightBoundary: function() {
      var t = this.content.getContentSize();
      return this._getContentLeftBoundary() + t.width
     },
     _getContentTopBoundary: function() {
      var t = this.content.getContentSize();
      return this._getContentBottomBoundary() + t.height
     },
     _getContentBottomBoundary: function() {
      return this.getContentPosition().y - this.content.getAnchorPoint().y * this.content.getContentSize().height
     },
     _getHowMuchOutOfBoundary: function(t) {
      if ((t = t || cc.v2(0, 0)).fuzzyEquals(cc.v2(0, 0), 1e-4) && !this._outOfBoundaryAmountDirty) return this._outOfBoundaryAmount;
      var e = cc.v2(0, 0);
      return this._getContentLeftBoundary() + t.x > this._leftBoundary ? e.x = this._leftBoundary - (this._getContentLeftBoundary() + t.x) : this._getContentRightBoundary() + t.x < this._rightBoundary && (e.x = this._rightBoundary - (this._getContentRightBoundary() + t.x)), this._getContentTopBoundary() + t.y < this._topBoundary ? e.y = this._topBoundary - (this._getContentTopBoundary() + t.y) : this._getContentBottomBoundary() + t.y > this._bottomBoundary && (e.y = this._bottomBoundary - (this._getContentBottomBoundary() + t.y)), t.fuzzyEquals(cc.v2(0, 0), 1e-4) && (this._outOfBoundaryAmount = e, this._outOfBoundaryAmountDirty = !1), e = this._clampDelta(e)
     },
     _updateScrollBar: function(t) {
      this.horizontalScrollBar && this.horizontalScrollBar._onScroll(t), this.verticalScrollBar && this.verticalScrollBar._onScroll(t)
     },
     _onScrollBarTouchBegan: function() {
      this.horizontalScrollBar && this.horizontalScrollBar._onTouchBegan(), this.verticalScrollBar && this.verticalScrollBar._onTouchBegan()
     },
     _onScrollBarTouchEnded: function() {
      this.horizontalScrollBar && this.horizontalScrollBar._onTouchEnded(), this.verticalScrollBar && this.verticalScrollBar._onTouchEnded()
     },
     _dispatchEvent: function(t) {
      if ("scroll-ended" === t) this._scrollEventEmitMask = 0;
      else if ("scroll-to-top" === t || "scroll-to-bottom" === t || "scroll-to-left" === t || "scroll-to-right" === t) {
       var e = 1 << a[t];
       if (this._scrollEventEmitMask & e) return;
       this._scrollEventEmitMask |= e
      }
      cc.Component.EventHandler.emitEvents(this.scrollEvents, this, a[t]), this.node.emit(t, this)
     },
     _adjustContentOutOfBoundary: function() {
      if (this._outOfBoundaryAmountDirty = !0, this._isOutOfBoundary()) {
       var t = this._getHowMuchOutOfBoundary(cc.v2(0, 0)),
        e = this.getContentPosition().add(t);
       this.content && (this.content.setPosition(e), this._updateScrollBar(0))
      }
     },
     start: function() {
      this._calculateBoundary(), this.content && cc.director.once(cc.Director.EVENT_BEFORE_DRAW, this._adjustContentOutOfBoundary, this)
     },
     _hideScrollbar: function() {
      this.horizontalScrollBar && this.horizontalScrollBar.hide(), this.verticalScrollBar && this.verticalScrollBar.hide()
     },
     _showScrollbar: function() {
      this.horizontalScrollBar && this.horizontalScrollBar.show(), this.verticalScrollBar && this.verticalScrollBar.show()
     },
     onDisable: function() {
      this._unregisterEvent(), this.node.off(n.SIZE_CHANGED, this._calculateBoundary, this), this.node.off(n.SCALE_CHANGED, this._calculateBoundary, this), this.content && (this.content.off(n.SIZE_CHANGED, this._calculateBoundary, this), this.content.off(n.SCALE_CHANGED, this._calculateBoundary, this), this._view && (this._view.off(n.POSITION_CHANGED, this._calculateBoundary, this), this._view.off(n.SCALE_CHANGED, this._calculateBoundary, this), this._view.off(n.SIZE_CHANGED, this._calculateBoundary, this))), this._hideScrollbar(), this.stopAutoScroll()
     },
     onEnable: function() {
      this._registerEvent(), this.node.on(n.SIZE_CHANGED, this._calculateBoundary, this), this.node.on(n.SCALE_CHANGED, this._calculateBoundary, this), this.content && (this.content.on(n.SIZE_CHANGED, this._calculateBoundary, this), this.content.on(n.SCALE_CHANGED, this._calculateBoundary, this), this._view && (this._view.on(n.POSITION_CHANGED, this._calculateBoundary, this), this._view.on(n.SCALE_CHANGED, this._calculateBoundary, this), this._view.on(n.SIZE_CHANGED, this._calculateBoundary, this))), this._showScrollbar()
     },
     update: function(t) {
      this._autoScrolling && this._processAutoScrolling(t)
     }
    });
   cc.ScrollView = e.exports = o
  }), {
   "../CCNode": 24,
   "./CCViewGroup": 66
  }],
  64: [(function(t, e, i) {
   var n = t("../utils/misc"),
    r = t("../CCNode").EventType,
    s = t("./CCRenderComponent"),
    a = t("../renderer/render-flow"),
    o = t("../renderer/render-engine"),
    c = o.SpriteMaterial,
    h = o.GraySpriteMaterial,
    u = t("../renderer/utils/dynamic-atlas/manager"),
    l = cc.Enum({
     SIMPLE: 0,
     SLICED: 1,
     TILED: 2,
     FILLED: 3,
     MESH: 4
    }),
    _ = cc.Enum({
     HORIZONTAL: 0,
     VERTICAL: 1,
     RADIAL: 2
    }),
    d = cc.Enum({
     CUSTOM: 0,
     TRIMMED: 1,
     RAW: 2
    }),
    f = cc.Enum({
     NORMAL: 0,
     GRAY: 1
    }),
    p = cc.Class({
     name: "cc.Sprite",
     extends: s,
     ctor: function() {
      this._assembler = null, this._graySpriteMaterial = null, this._spriteMaterial = null
     },
     editor: !1,
     properties: {
      _spriteFrame: {
       default: null,
       type: cc.SpriteFrame
      },
      _type: l.SIMPLE,
      _sizeMode: d.TRIMMED,
      _fillType: 0,
      _fillCenter: cc.v2(0, 0),
      _fillStart: 0,
      _fillRange: 0,
      _isTrimmedMode: !0,
      _state: 0,
      _atlas: {
       default: null,
       type: cc.SpriteAtlas,
       tooltip: !1,
       editorOnly: !0,
       visible: !0,
       animatable: !1
      },
      spriteFrame: {
       get: function() {
        return this._spriteFrame
       },
       set: function(t, e) {
        var i = this._spriteFrame;
        i !== t && (this._spriteFrame = t, this.markForUpdateRenderData(!1), this._applySpriteFrame(i))
       },
       type: cc.SpriteFrame
      },
      type: {
       get: function() {
        return this._type
       },
       set: function(t) {
        this._type !== t && (this.destroyRenderData(this._renderData), this._renderData = null, this._type = t, this._updateAssembler())
       },
       type: l,
       animatable: !1,
       tooltip: !1
      },
      fillType: {
       get: function() {
        return this._fillType
       },
       set: function(t) {
        t !== this._fillType && (t === _.RADIAL || this._fillType === _.RADIAL ? (this.destroyRenderData(this._renderData), this._renderData = null) : this._renderData && this.markForUpdateRenderData(!0), this._fillType = t, this._updateAssembler())
       },
       type: _,
       tooltip: !1
      },
      fillCenter: {
       get: function() {
        return this._fillCenter
       },
       set: function(t) {
        this._fillCenter.x = t.x, this._fillCenter.y = t.y, this._type === l.FILLED && this._renderData && this.markForUpdateRenderData(!0)
       },
       tooltip: !1
      },
      fillStart: {
       get: function() {
        return this._fillStart
       },
       set: function(t) {
        this._fillStart = n.clampf(t, -1, 1), this._type === l.FILLED && this._renderData && this.markForUpdateRenderData(!0)
       },
       tooltip: !1
      },
      fillRange: {
       get: function() {
        return this._fillRange
       },
       set: function(t) {
        this._fillRange = n.clampf(t, -1, 1), this._type === l.FILLED && this._renderData && this.markForUpdateRenderData(!0)
       },
       tooltip: !1
      },
      trim: {
       get: function() {
        return this._isTrimmedMode
       },
       set: function(t) {
        this._isTrimmedMode !== t && (this._isTrimmedMode = t, this._type !== l.SIMPLE && this._type !== l.MESH || !this._renderData || this.markForUpdateRenderData(!0))
       },
       animatable: !1,
       tooltip: !1
      },
      sizeMode: {
       get: function() {
        return this._sizeMode
       },
       set: function(t) {
        this._sizeMode = t, t !== d.CUSTOM && this._applySpriteSize()
       },
       animatable: !1,
       type: d,
       tooltip: !1
      }
     },
     statics: {
      FillType: _,
      Type: l,
      SizeMode: d,
      State: f
     },
     setVisible: function(t) {
      this.enabled = t
     },
     setState: function(t) {
      this._state !== t && (this._state = t, this._activateMaterial())
     },
     getState: function() {
      return this._state
     },
     onEnable: function() {
      this._super(), this._spriteFrame && this._spriteFrame.textureLoaded() || (this.disableRender(), this._spriteFrame && (this._spriteFrame.once("load", this._onTextureLoaded, this), this._spriteFrame.ensureLoadTexture())), this._updateAssembler(), this._activateMaterial(), this.node.on(r.SIZE_CHANGED, this._onNodeSizeDirty, this), this.node.on(r.ANCHOR_CHANGED, this._onNodeSizeDirty, this)
     },
     onDisable: function() {
      this._super(), this.node.off(r.SIZE_CHANGED, this._onNodeSizeDirty, this), this.node.off(r.ANCHOR_CHANGED, this._onNodeSizeDirty, this)
     },
     _onNodeSizeDirty: function() {
      this._renderData && this.markForUpdateRenderData(!0)
     },
     _updateAssembler: function() {
      var t = p._assembler.getAssembler(this);
      this._assembler !== t && (this._assembler = t, this._renderData = null), this._renderData || (this._renderData = this._assembler.createData(this), this._renderData.material = this._material, this.markForUpdateRenderData(!0))
     },
     _activateMaterial: function() {
      var t = this._spriteFrame;
      if (cc.game.renderType !== cc.game.RENDER_TYPE_CANVAS) {
       var e = void 0;
       if (this._state === f.GRAY ? (this._graySpriteMaterial || (this._graySpriteMaterial = new h), e = this._graySpriteMaterial) : (this._spriteMaterial || (this._spriteMaterial = new c), e = this._spriteMaterial), e.useColor = !1, t && t.textureLoaded()) {
        var i = t.getTexture();
        e.texture !== i ? (e.texture = i, this._updateMaterial(e)) : e !== this._material && this._updateMaterial(e), this._renderData && (this._renderData.material = e), this.node._renderFlag |= a.FLAG_COLOR, this.markForUpdateRenderData(!0), this.markForRender(!0)
       } else this.disableRender()
      } else this.markForUpdateRenderData(!0), this.markForRender(!0)
     },
     _applyAtlas: !1,
     _canRender: function() {
      if (cc.game.renderType === cc.game.RENDER_TYPE_CANVAS) {
       if (!this._enabled) return !1
      } else if (!this._enabled || !this._material || !this.node._activeInHierarchy) return !1;
      var t = this._spriteFrame;
      return !(!t || !t.textureLoaded())
     },
     markForUpdateRenderData: function(t) {
      if (t && this._canRender()) {
       this.node._renderFlag |= a.FLAG_UPDATE_RENDER_DATA;
       var e = this._renderData;
       e && (e.uvDirty = !0, e.vertDirty = !0)
      } else t || (this.node._renderFlag &= ~a.FLAG_UPDATE_RENDER_DATA)
     },
     _applySpriteSize: function() {
      if (this._spriteFrame) {
       if (d.RAW === this._sizeMode) {
        var t = this._spriteFrame.getOriginalSize();
        this.node.setContentSize(t)
       } else if (d.TRIMMED === this._sizeMode) {
        var e = this._spriteFrame.getRect();
        this.node.setContentSize(e.width, e.height)
       }
       this._activateMaterial()
      }
     },
     _onTextureLoaded: function() {
      this.isValid && this._applySpriteSize()
     },
     _applySpriteFrame: function(t) {
      t && t.off && t.off("load", this._onTextureLoaded, this);
      var e = this._spriteFrame;
      e && (this._material && this._material._texture) === (e && e._texture) || this.markForRender(!1), e && (t && e._texture === t._texture ? this._applySpriteSize() : e.textureLoaded() ? this._onTextureLoaded(null) : (e.once("load", this._onTextureLoaded, this), e.ensureLoadTexture()))
     },
     _resized: !1,
     _calDynamicAtlas: function() {
      if (this._spriteFrame) {
       if (!this._spriteFrame._original && u) {
        var t = u.insertSpriteFrame(this._spriteFrame);
        t && this._spriteFrame._setDynamicAtlasFrame(t)
       }
       this._material._texture !== this._spriteFrame._texture && this._activateMaterial()
      }
     }
    });
   cc.Sprite = e.exports = p
  }), {
   "../CCNode": 24,
   "../renderer/render-engine": 150,
   "../renderer/render-flow": 151,
   "../renderer/utils/dynamic-atlas/manager": 153,
   "../utils/misc": 186,
   "./CCRenderComponent": 62
  }],
  65: [(function(t, e, i) {
   var n = cc.Enum({
     NONE: 0,
     CHECKBOX: 1,
     TEXT_ATLAS: 2,
     SLIDER_BAR: 3,
     LIST_VIEW: 4,
     PAGE_VIEW: 5
    }),
    r = cc.Enum({
     VERTICAL: 0,
     HORIZONTAL: 1
    }),
    s = cc.Enum({
     TOP: 0,
     CENTER: 1,
     BOTTOM: 2
    }),
    a = cc.Enum({
     LEFT: 0,
     CENTER: 1,
     RIGHT: 2
    }),
    o = cc.Class({
     name: "cc.StudioComponent",
     extends: cc.Component,
     editor: !1,
     properties: !1,
     statics: {
      ComponentType: n,
      ListDirection: r,
      VerticalAlign: s,
      HorizontalAlign: a
     }
    }),
    c = t("../utils/prefab-helper");
   o.PlaceHolder = cc.Class({
    name: "cc.StudioComponent.PlaceHolder",
    extends: cc.Component,
    properties: {
     _baseUrl: "",
     nestedPrefab: cc.Prefab
    },
    onLoad: function() {
     this.nestedPrefab && this._replaceWithNestedPrefab()
    },
    _replaceWithNestedPrefab: function() {
     var t = this.node,
      e = t._prefab;
     e.root = t, e.asset = this.nestedPrefab, c.syncWithPrefab(t)
    }
   }), cc.StudioComponent = e.exports = o;
   var h = cc.Class({
    name: "cc.StudioWidget",
    extends: cc.Widget,
    editor: !1,
    _validateTargetInDEV: function() {}
   });
   cc.StudioWidget = e.exports = h
  }), {
   "../utils/prefab-helper": 189
  }],
  66: [(function(t, e, i) {
   var n = cc.Class({
    name: "cc.ViewGroup",
    extends: t("./CCComponent")
   });
   cc.ViewGroup = e.exports = n
  }), {
   "./CCComponent": 54
  }],
  67: [(function(t, e, i) {
   var n = t("./CCComponent"),
    r = void 0;
   r = cc.sys.platform === cc.sys.BAIDU_GAME ? cc.Class({
    name: "cc.SwanSubContextView",
    extends: n,
    editor: !1,
    ctor: function() {
     this._sprite = null, this._tex = new cc.Texture2D, this._context = null
    },
    onLoad: function() {
     if (swan.getOpenDataContext) {
      this._context = swan.getOpenDataContext();
      var t = this._context.canvas;
      t && (t.width = this.node.width, t.height = this.node.height), this._tex.setPremultiplyAlpha(!0), this._tex.initWithElement(t), this._sprite = this.node.getComponent(cc.Sprite), this._sprite || (this._sprite = this.node.addComponent(cc.Sprite), this._sprite.srcBlendFactor = cc.macro.BlendFactor.ONE), this._sprite.spriteFrame = new cc.SpriteFrame(this._tex)
     } else this.enabled = !1
    },
    onEnable: function() {
     this.updateSubContextViewport()
    },
    update: function() {
     this._tex && this._context && (this._tex.initWithElement(this._context.canvas), this._sprite._activateMaterial())
    },
    updateSubContextViewport: function() {
     if (this._context) {
      var t = this.node.getBoundingBoxToWorld(),
       e = cc.view._scaleX,
       i = cc.view._scaleY;
      this._context.postMessage({
       fromEngine: !0,
       event: "viewport",
       x: t.x * e + cc.view._viewportRect.x,
       y: t.y * i + cc.view._viewportRect.y,
       width: t.width * e,
       height: t.height * i
      })
     }
    }
   }) : cc.Class({
    name: "cc.SwanSubContextView",
    extends: n
   }), cc.SwanSubContextView = e.exports = r
  }), {
   "./CCComponent": 54
  }],
  68: [(function(t, e, i) {
   var n = t("./CCComponent"),
    r = void 0;
   r = cc.Class({
    name: "cc.WXSubContextView",
    extends: n,
    editor: !1,
    ctor: function() {
     this._sprite = null, this._tex = new cc.Texture2D, this._context = null
    },
    onLoad: function() {
     if (wx.getOpenDataContext) {
      this._context = wx.getOpenDataContext();
      var t = this._context.canvas;
      t && (t.width = this.node.width, t.height = this.node.height), this._tex.setPremultiplyAlpha(!0), this._tex.initWithElement(t), this._sprite = this.node.getComponent(cc.Sprite), this._sprite || (this._sprite = this.node.addComponent(cc.Sprite), this._sprite.srcBlendFactor = cc.macro.BlendFactor.ONE), this._sprite.spriteFrame = new cc.SpriteFrame(this._tex)
     } else this.enabled = !1
    },
    onEnable: function() {
     this.updateSubContextViewport()
    },
    update: function() {
     this._tex && this._context && (this._tex.initWithElement(this._context.canvas), this._sprite._activateMaterial())
    },
    updateSubContextViewport: function() {
     if (this._context) {
      var t = this.node.getBoundingBoxToWorld(),
       e = cc.view._scaleX,
       i = cc.view._scaleY;
      this._context.postMessage({
       fromEngine: !0,
       event: "viewport",
       x: t.x * e + cc.view._viewportRect.x,
       y: t.y * i + cc.view._viewportRect.y,
       width: t.width * e,
       height: t.height * i
      })
     }
    }
   }), cc.WXSubContextView = e.exports = r
  }), {
   "./CCComponent": 54
  }],
  69: [(function(t, e, i) {
   t("./CCComponent"), t("./CCComponentEventHandler"), t("./missing-script");
   var n = [t("./CCSprite"), t("./CCWidget"), t("./CCCanvas"), t("./CCAudioSource"), t("./CCAnimation"), t("./CCButton"), t("./CCLabel"), t("./CCProgressBar"), t("./CCMask"), t("./CCScrollBar"), t("./CCScrollView"), t("./CCPageViewIndicator"), t("./CCPageView"), t("./CCSlider"), t("./CCLayout"), t("./editbox/CCEditBox"), t("./CCLabelOutline"), t("./CCRichText"), t("./CCToggleContainer"), t("./CCToggleGroup"), t("./CCToggle"), t("./CCBlockInputEvents"), t("./CCMotionStreak"), t("./WXSubContextView"), t("./SwanSubContextView")];
   e.exports = n
  }), {
   "./CCAnimation": 49,
   "./CCAudioSource": 50,
   "./CCBlockInputEvents": 51,
   "./CCButton": 52,
   "./CCCanvas": 53,
   "./CCComponent": 54,
   "./CCComponentEventHandler": 55,
   "./CCLabel": 56,
   "./CCLabelOutline": 57,
   "./CCLayout": 58,
   "./CCMask": 59,
   "./CCMotionStreak": 60,
   "./CCPageView": void 0,
   "./CCPageViewIndicator": void 0,
   "./CCProgressBar": 61,
   "./CCRichText": void 0,
   "./CCScrollBar": void 0,
   "./CCScrollView": 63,
   "./CCSlider": void 0,
   "./CCSprite": 64,
   "./CCToggle": void 0,
   "./CCToggleContainer": void 0,
   "./CCToggleGroup": void 0,
   "./CCWidget": void 0,
   "./SwanSubContextView": 67,
   "./WXSubContextView": 68,
   "./editbox/CCEditBox": void 0,
   "./missing-script": 70
  }],
  70: [(function(t, e, i) {
   var n = cc.js,
    r = t("../utils/misc").BUILTIN_CLASSID_RE,
    s = cc.Class({
     name: "cc.MissingClass",
     properties: {
      _$erialized: {
       default: null,
       visible: !1,
       editorOnly: !0
      }
     }
    }),
    a = cc.Class({
     name: "cc.MissingScript",
     extends: cc.Component,
     editor: {
      inspector: "packages://inspector/inspectors/comps/missing-script.js"
     },
     properties: {
      compiled: {
       default: !1,
       serializable: !1
      },
      _$erialized: {
       default: null,
       visible: !1,
       editorOnly: !0
      }
     },
     ctor: !1,
     statics: {
      safeFindClass: function(t, e) {
       var i = n._getClassById(t);
       return i || (t ? (cc.deserialize.reportMissingClass(t), a.getMissingWrapper(t, e)) : null)
      },
      getMissingWrapper: function(t, e) {
       return e.node && (/^[0-9a-zA-Z+/]{23}$/.test(t) || r.test(t)) ? a : s
      }
     },
     onLoad: function() {
      cc.warnID(4600, this.node.name)
     }
    });
   cc._MissingScript = e.exports = a
  }), {
   "../utils/misc": 186
  }],
  71: [(function(t, e, i) {
   var n = cc.js;
   t("../event/event");
   var r = function(t, e) {
    cc.Event.call(this, cc.Event.MOUSE, e), this._eventType = t, this._button = 0, this._x = 0, this._y = 0, this._prevX = 0, this._prevY = 0, this._scrollX = 0, this._scrollY = 0
   };
   n.extend(r, cc.Event);
   var s = r.prototype;
   s.setScrollData = function(t, e) {
    this._scrollX = t, this._scrollY = e
   }, s.getScrollX = function() {
    return this._scrollX
   }, s.getScrollY = function() {
    return this._scrollY
   }, s.setLocation = function(t, e) {
    this._x = t, this._y = e
   }, s.getLocation = function() {
    return cc.v2(this._x, this._y)
   }, s.getLocationInView = function() {
    return cc.v2(this._x, cc.view._designResolutionSize.height - this._y)
   }, s._setPrevCursor = function(t, e) {
    this._prevX = t, this._prevY = e
   }, s.getPreviousLocation = function() {
    return cc.v2(this._prevX, this._prevY)
   }, s.getDelta = function() {
    return cc.v2(this._x - this._prevX, this._y - this._prevY)
   }, s.getDeltaX = function() {
    return this._x - this._prevX
   }, s.getDeltaY = function() {
    return this._y - this._prevY
   }, s.setButton = function(t) {
    this._button = t
   }, s.getButton = function() {
    return this._button
   }, s.getLocationX = function() {
    return this._x
   }, s.getLocationY = function() {
    return this._y
   }, r.NONE = 0, r.DOWN = 1, r.UP = 2, r.MOVE = 3, r.SCROLL = 4, r.BUTTON_LEFT = 0, r.BUTTON_RIGHT = 2, r.BUTTON_MIDDLE = 1, r.BUTTON_4 = 3, r.BUTTON_5 = 4, r.BUTTON_6 = 5, r.BUTTON_7 = 6, r.BUTTON_8 = 7;
   var a = function(t, e) {
    cc.Event.call(this, cc.Event.TOUCH, e), this._eventCode = 0, this._touches = t || [], this.touch = null, this.currentTouch = null
   };
   n.extend(a, cc.Event), (s = a.prototype).getEventCode = function() {
    return this._eventCode
   }, s.getTouches = function() {
    return this._touches
   }, s._setEventCode = function(t) {
    this._eventCode = t
   }, s._setTouches = function(t) {
    this._touches = t
   }, s.setLocation = function(t, e) {
    this.touch && this.touch.setTouchInfo(this.touch.getID(), t, e)
   }, s.getLocation = function() {
    return this.touch ? this.touch.getLocation() : cc.v2()
   }, s.getLocationInView = function() {
    return this.touch ? this.touch.getLocationInView() : cc.v2()
   }, s.getPreviousLocation = function() {
    return this.touch ? this.touch.getPreviousLocation() : cc.v2()
   }, s.getStartLocation = function() {
    return this.touch ? this.touch.getStartLocation() : cc.v2()
   }, s.getID = function() {
    return this.touch ? this.touch.getID() : null
   }, s.getDelta = function() {
    return this.touch ? this.touch.getDelta() : cc.v2()
   }, s.getDeltaX = function() {
    return this.touch ? this.touch.getDelta().x : 0
   }, s.getDeltaY = function() {
    return this.touch ? this.touch.getDelta().y : 0
   }, s.getLocationX = function() {
    return this.touch ? this.touch.getLocationX() : 0
   }, s.getLocationY = function() {
    return this.touch ? this.touch.getLocationY() : 0
   }, a.MAX_TOUCHES = 5, a.BEGAN = 0, a.MOVED = 1, a.ENDED = 2, a.CANCELED = 3;
   var o = function(t, e) {
    cc.Event.call(this, cc.Event.ACCELERATION, e), this.acc = t
   };
   n.extend(o, cc.Event);
   var c = function(t, e, i) {
    cc.Event.call(this, cc.Event.KEYBOARD, i), this.keyCode = t, this.isPressed = e
   };
   n.extend(c, cc.Event), cc.Event.EventMouse = r, cc.Event.EventTouch = a, cc.Event.EventAcceleration = o, cc.Event.EventKeyboard = c, e.exports = cc.Event
  }), {
   "../event/event": 78
  }],
  72: [(function(t, e, i) {
   var n = t("../platform/js");
   cc.EventListener = function(t, e, i) {
    this._onEvent = i, this._type = t || 0, this._listenerID = e || "", this._registered = !1, this._fixedPriority = 0, this._node = null, this._target = null, this._paused = !0, this._isEnabled = !0
   }, cc.EventListener.prototype = {
    constructor: cc.EventListener,
    _setPaused: function(t) {
     this._paused = t
    },
    _isPaused: function() {
     return this._paused
    },
    _setRegistered: function(t) {
     this._registered = t
    },
    _isRegistered: function() {
     return this._registered
    },
    _getType: function() {
     return this._type
    },
    _getListenerID: function() {
     return this._listenerID
    },
    _setFixedPriority: function(t) {
     this._fixedPriority = t
    },
    _getFixedPriority: function() {
     return this._fixedPriority
    },
    _setSceneGraphPriority: function(t) {
     this._target = t, this._node = t
    },
    _getSceneGraphPriority: function() {
     return this._node
    },
    checkAvailable: function() {
     return null !== this._onEvent
    },
    clone: function() {
     return null
    },
    setEnabled: function(t) {
     this._isEnabled = t
    },
    isEnabled: function() {
     return this._isEnabled
    },
    retain: function() {},
    release: function() {}
   }, cc.EventListener.UNKNOWN = 0, cc.EventListener.TOUCH_ONE_BY_ONE = 1, cc.EventListener.TOUCH_ALL_AT_ONCE = 2, cc.EventListener.KEYBOARD = 3, cc.EventListener.MOUSE = 4, cc.EventListener.ACCELERATION = 6, cc.EventListener.CUSTOM = 8;
   var r = cc.EventListener.ListenerID = {
     MOUSE: "__cc_mouse",
     TOUCH_ONE_BY_ONE: "__cc_touch_one_by_one",
     TOUCH_ALL_AT_ONCE: "__cc_touch_all_at_once",
     KEYBOARD: "__cc_keyboard",
     ACCELERATION: "__cc_acceleration"
    },
    s = function(t, e) {
     this._onCustomEvent = e, cc.EventListener.call(this, cc.EventListener.CUSTOM, t, this._callback)
    };
   n.extend(s, cc.EventListener), n.mixin(s.prototype, {
    _onCustomEvent: null,
    _callback: function(t) {
     null !== this._onCustomEvent && this._onCustomEvent(t)
    },
    checkAvailable: function() {
     return cc.EventListener.prototype.checkAvailable.call(this) && null !== this._onCustomEvent
    },
    clone: function() {
     return new s(this._listenerID, this._onCustomEvent)
    }
   });
   var a = function() {
    cc.EventListener.call(this, cc.EventListener.MOUSE, r.MOUSE, this._callback)
   };
   n.extend(a, cc.EventListener), n.mixin(a.prototype, {
    onMouseDown: null,
    onMouseUp: null,
    onMouseMove: null,
    onMouseScroll: null,
    _callback: function(t) {
     var e = cc.Event.EventMouse;
     switch (t._eventType) {
      case e.DOWN:
       this.onMouseDown && this.onMouseDown(t);
       break;
      case e.UP:
       this.onMouseUp && this.onMouseUp(t);
       break;
      case e.MOVE:
       this.onMouseMove && this.onMouseMove(t);
       break;
      case e.SCROLL:
       this.onMouseScroll && this.onMouseScroll(t)
     }
    },
    clone: function() {
     var t = new a;
     return t.onMouseDown = this.onMouseDown, t.onMouseUp = this.onMouseUp, t.onMouseMove = this.onMouseMove, t.onMouseScroll = this.onMouseScroll, t
    },
    checkAvailable: function() {
     return !0
    }
   });
   var o = function() {
    cc.EventListener.call(this, cc.EventListener.TOUCH_ONE_BY_ONE, r.TOUCH_ONE_BY_ONE, null), this._claimedTouches = []
   };
   n.extend(o, cc.EventListener), n.mixin(o.prototype, {
    constructor: o,
    _claimedTouches: null,
    swallowTouches: !1,
    onTouchBegan: null,
    onTouchMoved: null,
    onTouchEnded: null,
    onTouchCancelled: null,
    setSwallowTouches: function(t) {
     this.swallowTouches = t
    },
    isSwallowTouches: function() {
     return this.swallowTouches
    },
    clone: function() {
     var t = new o;
     return t.onTouchBegan = this.onTouchBegan, t.onTouchMoved = this.onTouchMoved, t.onTouchEnded = this.onTouchEnded, t.onTouchCancelled = this.onTouchCancelled, t.swallowTouches = this.swallowTouches, t
    },
    checkAvailable: function() {
     return !!this.onTouchBegan || (cc.logID(1801), !1)
    }
   });
   var c = function() {
    cc.EventListener.call(this, cc.EventListener.TOUCH_ALL_AT_ONCE, r.TOUCH_ALL_AT_ONCE, null)
   };
   n.extend(c, cc.EventListener), n.mixin(c.prototype, {
    constructor: c,
    onTouchesBegan: null,
    onTouchesMoved: null,
    onTouchesEnded: null,
    onTouchesCancelled: null,
    clone: function() {
     var t = new c;
     return t.onTouchesBegan = this.onTouchesBegan, t.onTouchesMoved = this.onTouchesMoved, t.onTouchesEnded = this.onTouchesEnded, t.onTouchesCancelled = this.onTouchesCancelled, t
    },
    checkAvailable: function() {
     return null !== this.onTouchesBegan || null !== this.onTouchesMoved || null !== this.onTouchesEnded || null !== this.onTouchesCancelled || (cc.logID(1802), !1)
    }
   });
   var h = function(t) {
    this._onAccelerationEvent = t, cc.EventListener.call(this, cc.EventListener.ACCELERATION, r.ACCELERATION, this._callback)
   };
   n.extend(h, cc.EventListener), n.mixin(h.prototype, {
    constructor: h,
    _onAccelerationEvent: null,
    _callback: function(t) {
     this._onAccelerationEvent(t.acc, t)
    },
    checkAvailable: function() {
     return cc.assertID(this._onAccelerationEvent, 1803), !0
    },
    clone: function() {
     return new h(this._onAccelerationEvent)
    }
   });
   var u = function() {
    cc.EventListener.call(this, cc.EventListener.KEYBOARD, r.KEYBOARD, this._callback)
   };
   n.extend(u, cc.EventListener), n.mixin(u.prototype, {
    constructor: u,
    onKeyPressed: null,
    onKeyReleased: null,
    _callback: function(t) {
     t.isPressed ? this.onKeyPressed && this.onKeyPressed(t.keyCode, t) : this.onKeyReleased && this.onKeyReleased(t.keyCode, t)
    },
    clone: function() {
     var t = new u;
     return t.onKeyPressed = this.onKeyPressed, t.onKeyReleased = this.onKeyReleased, t
    },
    checkAvailable: function() {
     return null !== this.onKeyPressed || null !== this.onKeyReleased || (cc.logID(1800), !1)
    }
   }), cc.EventListener.create = function(t) {
    cc.assertID(t && t.event, 1900);
    var e = t.event;
    delete t.event;
    var i = null;
    for (var n in e === cc.EventListener.TOUCH_ONE_BY_ONE ? i = new o : e === cc.EventListener.TOUCH_ALL_AT_ONCE ? i = new c : e === cc.EventListener.MOUSE ? i = new a : e === cc.EventListener.CUSTOM ? (i = new s(t.eventName, t.callback), delete t.eventName, delete t.callback) : e === cc.EventListener.KEYBOARD ? i = new u : e === cc.EventListener.ACCELERATION && (i = new h(t.callback), delete t.callback), t) i[n] = t[n];
    return i
   }, e.exports = cc.EventListener
  }), {
   "../platform/js": 128
  }],
  73: [(function(t, e, i) {
   var n = t("../platform/js");
   t("./CCEventListener");
   var r = cc.EventListener.ListenerID,
    s = function() {
     this._fixedListeners = [], this._sceneGraphListeners = [], this.gt0Index = 0
    };
   s.prototype = {
    constructor: s,
    size: function() {
     return this._fixedListeners.length + this._sceneGraphListeners.length
    },
    empty: function() {
     return 0 === this._fixedListeners.length && 0 === this._sceneGraphListeners.length
    },
    push: function(t) {
     0 === t._getFixedPriority() ? this._sceneGraphListeners.push(t) : this._fixedListeners.push(t)
    },
    clearSceneGraphListeners: function() {
     this._sceneGraphListeners.length = 0
    },
    clearFixedListeners: function() {
     this._fixedListeners.length = 0
    },
    clear: function() {
     this._sceneGraphListeners.length = 0, this._fixedListeners.length = 0
    },
    getFixedPriorityListeners: function() {
     return this._fixedListeners
    },
    getSceneGraphPriorityListeners: function() {
     return this._sceneGraphListeners
    }
   };
   var a = {
    DIRTY_NONE: 0,
    DIRTY_FIXED_PRIORITY: 1,
    DIRTY_SCENE_GRAPH_PRIORITY: 2,
    DIRTY_ALL: 3,
    _listenersMap: {},
    _priorityDirtyFlagMap: {},
    _nodeListenersMap: {},
    _nodePriorityMap: n.createMap(!0),
    _globalZOrderNodeMap: [],
    _toAddedListeners: [],
    _toRemovedListeners: [],
    _dirtyNodes: [],
    _inDispatch: 0,
    _isEnabled: !1,
    _nodePriorityIndex: 0,
    _internalCustomListenerIDs: [],
    _setDirtyForNode: function(t) {
     if (void 0 !== this._nodeListenersMap[t._id] && this._dirtyNodes.push(t), t.getChildren)
      for (var e = t.getChildren(), i = 0, n = e ? e.length : 0; i < n; i++) this._setDirtyForNode(e[i])
    },
    pauseTarget: function(t, e) {
     if (t instanceof cc._BaseNode) {
      var i, n, r = this._nodeListenersMap[t._id];
      if (r)
       for (i = 0, n = r.length; i < n; i++) r[i]._setPaused(!0);
      if (!0 === e) {
       var s = t.getChildren();
       for (i = 0, n = s ? s.length : 0; i < n; i++) this.pauseTarget(s[i], !0)
      }
     } else cc.warnID(3506)
    },
    resumeTarget: function(t, e) {
     if (t instanceof cc._BaseNode) {
      var i, n, r = this._nodeListenersMap[t._id];
      if (r)
       for (i = 0, n = r.length; i < n; i++) r[i]._setPaused(!1);
      if (this._setDirtyForNode(t), !0 === e && t.getChildren) {
       var s = t.getChildren();
       for (i = 0, n = s ? s.length : 0; i < n; i++) this.resumeTarget(s[i], !0)
      }
     } else cc.warnID(3506)
    },
    _addListener: function(t) {
     0 === this._inDispatch ? this._forceAddEventListener(t) : this._toAddedListeners.push(t)
    },
    _forceAddEventListener: function(t) {
     var e = t._getListenerID(),
      i = this._listenersMap[e];
     if (i || (i = new s, this._listenersMap[e] = i), i.push(t), 0 === t._getFixedPriority()) {
      this._setDirty(e, this.DIRTY_SCENE_GRAPH_PRIORITY);
      var n = t._getSceneGraphPriority();
      null === n && cc.logID(3507), this._associateNodeAndEventListener(n, t), n.activeInHierarchy && this.resumeTarget(n)
     } else this._setDirty(e, this.DIRTY_FIXED_PRIORITY)
    },
    _getListeners: function(t) {
     return this._listenersMap[t]
    },
    _updateDirtyFlagForSceneGraph: function() {
     if (0 !== this._dirtyNodes.length) {
      for (var t, e, i = this._dirtyNodes, n = this._nodeListenersMap, r = 0, s = i.length; r < s; r++)
       if (t = n[i[r]._id])
        for (var a = 0, o = t.length; a < o; a++)(e = t[a]) && this._setDirty(e._getListenerID(), this.DIRTY_SCENE_GRAPH_PRIORITY);
      this._dirtyNodes.length = 0
     }
    },
    _removeAllListenersInVector: function(t) {
     if (t)
      for (var e, i = 0; i < t.length;)(e = t[i])._setRegistered(!1), null != e._getSceneGraphPriority() && (this._dissociateNodeAndEventListener(e._getSceneGraphPriority(), e), e._setSceneGraphPriority(null)), 0 === this._inDispatch ? cc.js.array.remove(t, e) : ++i
    },
    _removeListenersForListenerID: function(t) {
     var e, i = this._listenersMap[t];
     if (i) {
      var n = i.getFixedPriorityListeners(),
       r = i.getSceneGraphPriorityListeners();
      this._removeAllListenersInVector(r), this._removeAllListenersInVector(n), delete this._priorityDirtyFlagMap[t], this._inDispatch || (i.clear(), delete this._listenersMap[t])
     }
     var s, a = this._toAddedListeners;
     for (e = 0; e < a.length;)(s = a[e]) && s._getListenerID() === t ? cc.js.array.remove(a, s) : ++e
    },
    _sortEventListeners: function(t) {
     var e = this.DIRTY_NONE,
      i = this._priorityDirtyFlagMap;
     if (i[t] && (e = i[t]), e !== this.DIRTY_NONE && (i[t] = this.DIRTY_NONE, e & this.DIRTY_FIXED_PRIORITY && this._sortListenersOfFixedPriority(t), e & this.DIRTY_SCENE_GRAPH_PRIORITY)) {
      var n = cc.director.getScene();
      n && this._sortListenersOfSceneGraphPriority(t, n)
     }
    },
    _sortListenersOfSceneGraphPriority: function(t, e) {
     var i = this._getListeners(t);
     if (i) {
      var r = i.getSceneGraphPriorityListeners();
      r && 0 !== r.length && (this._nodePriorityIndex = 0, this._nodePriorityMap = n.createMap(!0), this._visitTarget(e, !0), i.getSceneGraphPriorityListeners().sort(this._sortEventListenersOfSceneGraphPriorityDes))
     }
    },
    _sortEventListenersOfSceneGraphPriorityDes: function(t, e) {
     var i = a._nodePriorityMap,
      n = t._getSceneGraphPriority(),
      r = e._getSceneGraphPriority();
     return e && r && i[r._id] ? t && n && i[n._id] ? i[r._id] - i[n._id] : 1 : -1
    },
    _sortListenersOfFixedPriority: function(t) {
     var e = this._listenersMap[t];
     if (e) {
      var i = e.getFixedPriorityListeners();
      if (i && 0 !== i.length) {
       i.sort(this._sortListenersOfFixedPriorityAsc);
       for (var n = 0, r = i.length; n < r && !(i[n]._getFixedPriority() >= 0);) ++n;
       e.gt0Index = n
      }
     }
    },
    _sortListenersOfFixedPriorityAsc: function(t, e) {
     return t._getFixedPriority() - e._getFixedPriority()
    },
    _onUpdateListeners: function(t) {
     var e, i, n, r = t.getFixedPriorityListeners(),
      s = t.getSceneGraphPriorityListeners(),
      a = this._toRemovedListeners;
     if (s)
      for (e = 0; e < s.length;)(i = s[e])._isRegistered() ? ++e : (cc.js.array.remove(s, i), -1 !== (n = a.indexOf(i)) && a.splice(n, 1));
     if (r)
      for (e = 0; e < r.length;)(i = r[e])._isRegistered() ? ++e : (cc.js.array.remove(r, i), -1 !== (n = a.indexOf(i)) && a.splice(n, 1));
     s && 0 === s.length && t.clearSceneGraphListeners(), r && 0 === r.length && t.clearFixedListeners()
    },
    frameUpdateListeners: function() {
     var t = this._listenersMap,
      e = this._priorityDirtyFlagMap;
     for (var i in t) t[i].empty() && (delete e[i], delete t[i]);
     var n = this._toAddedListeners;
     if (0 !== n.length) {
      for (var r = 0, s = n.length; r < s; r++) this._forceAddEventListener(n[r]);
      n.length = 0
     }
     0 !== this._toRemovedListeners.length && this._cleanToRemovedListeners()
    },
    _updateTouchListeners: function(t) {
     var e = this._inDispatch;
     if (cc.assertID(e > 0, 3508), !(e > 1)) {
      var i;
      (i = this._listenersMap[r.TOUCH_ONE_BY_ONE]) && this._onUpdateListeners(i), (i = this._listenersMap[r.TOUCH_ALL_AT_ONCE]) && this._onUpdateListeners(i), cc.assertID(1 === e, 3509);
      var n = this._toAddedListeners;
      if (0 !== n.length) {
       for (var s = 0, a = n.length; s < a; s++) this._forceAddEventListener(n[s]);
       this._toAddedListeners.length = 0
      }
      0 !== this._toRemovedListeners.length && this._cleanToRemovedListeners()
     }
    },
    _cleanToRemovedListeners: function() {
     for (var t = this._toRemovedListeners, e = 0; e < t.length; e++) {
      var i = t[e],
       n = this._listenersMap[i._getListenerID()];
      if (n) {
       var r, s = n.getFixedPriorityListeners(),
        a = n.getSceneGraphPriorityListeners();
       a && -1 !== (r = a.indexOf(i)) && a.splice(r, 1), s && -1 !== (r = s.indexOf(i)) && s.splice(r, 1)
      }
     }
     t.length = 0
    },
    _onTouchEventCallback: function(t, e) {
     if (!t._isRegistered()) return !1;
     var i = e.event,
      n = i.currentTouch;
     i.currentTarget = t._node;
     var r, s = !1,
      o = i.getEventCode(),
      c = cc.Event.EventTouch;
     return o === c.BEGAN ? t.onTouchBegan && (s = t.onTouchBegan(n, i)) && t._registered && t._claimedTouches.push(n) : t._claimedTouches.length > 0 && -1 !== (r = t._claimedTouches.indexOf(n)) && (s = !0, o === c.MOVED && t.onTouchMoved ? t.onTouchMoved(n, i) : o === c.ENDED ? (t.onTouchEnded && t.onTouchEnded(n, i), t._registered && t._claimedTouches.splice(r, 1)) : o === c.CANCELLED && (t.onTouchCancelled && t.onTouchCancelled(n, i), t._registered && t._claimedTouches.splice(r, 1))), i.isStopped() ? (a._updateTouchListeners(i), !0) : !(!s || !t.swallowTouches) && (e.needsMutableSet && e.touches.splice(n, 1), !0)
    },
    _dispatchTouchEvent: function(t) {
     this._sortEventListeners(r.TOUCH_ONE_BY_ONE), this._sortEventListeners(r.TOUCH_ALL_AT_ONCE);
     var e = this._getListeners(r.TOUCH_ONE_BY_ONE),
      i = this._getListeners(r.TOUCH_ALL_AT_ONCE);
     if (null !== e || null !== i) {
      var n = t.getTouches(),
       s = cc.js.array.copy(n),
       a = {
        event: t,
        needsMutableSet: e && i,
        touches: s,
        selTouch: null
       };
      if (e)
       for (var o = 0; o < n.length; o++) t.currentTouch = n[o], t._propagationStopped = t._propagationImmediateStopped = !1, this._dispatchEventToListeners(e, this._onTouchEventCallback, a);
      i && s.length > 0 && (this._dispatchEventToListeners(i, this._onTouchesEventCallback, {
       event: t,
       touches: s
      }), t.isStopped()) || this._updateTouchListeners(t)
     }
    },
    _onTouchesEventCallback: function(t, e) {
     if (!t._registered) return !1;
     var i = cc.Event.EventTouch,
      n = e.event,
      r = e.touches,
      s = n.getEventCode();
     return n.currentTarget = t._node, s === i.BEGAN && t.onTouchesBegan ? t.onTouchesBegan(r, n) : s === i.MOVED && t.onTouchesMoved ? t.onTouchesMoved(r, n) : s === i.ENDED && t.onTouchesEnded ? t.onTouchesEnded(r, n) : s === i.CANCELLED && t.onTouchesCancelled && t.onTouchesCancelled(r, n), !!n.isStopped() && (a._updateTouchListeners(n), !0)
    },
    _associateNodeAndEventListener: function(t, e) {
     var i = this._nodeListenersMap[t._id];
     i || (i = [], this._nodeListenersMap[t._id] = i), i.push(e)
    },
    _dissociateNodeAndEventListener: function(t, e) {
     var i = this._nodeListenersMap[t._id];
     i && (cc.js.array.remove(i, e), 0 === i.length && delete this._nodeListenersMap[t._id])
    },
    _dispatchEventToListeners: function(t, e, i) {
     var n, r, s = !1,
      a = t.getFixedPriorityListeners(),
      o = t.getSceneGraphPriorityListeners(),
      c = 0;
     if (a && 0 !== a.length)
      for (; c < t.gt0Index; ++c)
       if ((r = a[c]).isEnabled() && !r._isPaused() && r._isRegistered() && e(r, i)) {
        s = !0;
        break
       } if (o && !s)
      for (n = 0; n < o.length; n++)
       if ((r = o[n]).isEnabled() && !r._isPaused() && r._isRegistered() && e(r, i)) {
        s = !0;
        break
       } if (a && !s)
      for (; c < a.length; ++c)
       if ((r = a[c]).isEnabled() && !r._isPaused() && r._isRegistered() && e(r, i)) {
        s = !0;
        break
       }
    },
    _setDirty: function(t, e) {
     var i = this._priorityDirtyFlagMap;
     null == i[t] ? i[t] = e : i[t] = e | i[t]
    },
    _visitTarget: function(t, e) {
     t._reorderChildDirty && t.sortAllChildren();
     var i, n = t.getChildren(),
      r = 0,
      s = n.length,
      a = this._globalZOrderNodeMap,
      o = this._nodeListenersMap;
     if (s > 0)
      for (void 0 !== o[t._id] && (a || (a = []), a.push(t._id)); r < s; r++)(i = n[r]) && this._visitTarget(i, !1);
     else void 0 !== o[t._id] && (a || (a = []), a.push(t._id));
     if (e) {
      for (var c = this._nodePriorityMap, h = 0; h < a.length; h++) c[a[h]] = ++this._nodePriorityIndex;
      this._globalZOrderNodeMap.length = 0
     }
    },
    _sortNumberAsc: function(t, e) {
     return t - e
    },
    hasEventListener: function(t) {
     return !!this._getListeners(t)
    },
    addListener: function(t, e) {
     if (cc.assertID(t && e, 3503), cc.js.isNumber(e) || e instanceof cc._BaseNode) {
      if (t instanceof cc.EventListener) {
       if (t._isRegistered()) return void cc.logID(3505)
      } else cc.assertID(!cc.js.isNumber(e), 3504), t = cc.EventListener.create(t);
      if (t.checkAvailable()) {
       if (cc.js.isNumber(e)) {
        if (0 === e) return void cc.logID(3500);
        t._setSceneGraphPriority(null), t._setFixedPriority(e), t._setRegistered(!0), t._setPaused(!1), this._addListener(t)
       } else t._setSceneGraphPriority(e), t._setFixedPriority(0), t._setRegistered(!0), this._addListener(t);
       return t
      }
     } else cc.warnID(3506)
    },
    addCustomListener: function(t, e) {
     var i = new cc.EventListener.create({
      event: cc.EventListener.CUSTOM,
      eventName: t,
      callback: e
     });
     return this.addListener(i, 1), i
    },
    removeListener: function(t) {
     if (null != t) {
      var e, i = this._listenersMap;
      for (var n in i) {
       var r = i[n],
        s = r.getFixedPriorityListeners(),
        a = r.getSceneGraphPriorityListeners();
       if ((e = this._removeListenerInVector(a, t)) ? this._setDirty(t._getListenerID(), this.DIRTY_SCENE_GRAPH_PRIORITY) : (e = this._removeListenerInVector(s, t)) && this._setDirty(t._getListenerID(), this.DIRTY_FIXED_PRIORITY), r.empty() && (delete this._priorityDirtyFlagMap[t._getListenerID()], delete i[n]), e) break
      }
      if (!e)
       for (var o = this._toAddedListeners, c = 0, h = o.length; c < h; c++) {
        var u = o[c];
        if (u === t) {
         cc.js.array.remove(o, u), u._setRegistered(!1);
         break
        }
       }
     }
    },
    _removeListenerInCallback: function(t, e) {
     if (null == t) return !1;
     for (var i = 0, n = t.length; i < n; i++) {
      var r = t[i];
      if (r._onCustomEvent === e || r._onEvent === e) return r._setRegistered(!1), null != r._getSceneGraphPriority() && (this._dissociateNodeAndEventListener(r._getSceneGraphPriority(), r), r._setSceneGraphPriority(null)), 0 === this._inDispatch ? cc.js.array.remove(t, r) : this._toRemovedListeners.push(r), !0
     }
     return !1
    },
    _removeListenerInVector: function(t, e) {
     if (null == t) return !1;
     for (var i = 0, n = t.length; i < n; i++) {
      var r = t[i];
      if (r === e) return r._setRegistered(!1), null != r._getSceneGraphPriority() && (this._dissociateNodeAndEventListener(r._getSceneGraphPriority(), r), r._setSceneGraphPriority(null)), 0 === this._inDispatch ? cc.js.array.remove(t, r) : this._toRemovedListeners.push(r), !0
     }
     return !1
    },
    removeListeners: function(t, e) {
     var i = this;
     if (cc.js.isNumber(t) || t instanceof cc._BaseNode)
      if (void 0 !== t._id) {
       delete i._nodePriorityMap[t._id], cc.js.array.remove(i._dirtyNodes, t);
       var n, s = i._nodeListenersMap[t._id];
       if (s) {
        var a = cc.js.array.copy(s);
        for (n = 0; n < a.length; n++) i.removeListener(a[n]);
        delete i._nodeListenersMap[t._id]
       }
       var o = i._toAddedListeners;
       for (n = 0; n < o.length;) {
        var c = o[n];
        c._getSceneGraphPriority() === t ? (c._setSceneGraphPriority(null), c._setRegistered(!1), o.splice(n, 1)) : ++n
       }
       if (!0 === e) {
        var h, u = t.getChildren();
        for (n = 0, h = u.length; n < h; n++) i.removeListeners(u[n], !0)
       }
      } else t === cc.EventListener.TOUCH_ONE_BY_ONE ? i._removeListenersForListenerID(r.TOUCH_ONE_BY_ONE) : t === cc.EventListener.TOUCH_ALL_AT_ONCE ? i._removeListenersForListenerID(r.TOUCH_ALL_AT_ONCE) : t === cc.EventListener.MOUSE ? i._removeListenersForListenerID(r.MOUSE) : t === cc.EventListener.ACCELERATION ? i._removeListenersForListenerID(r.ACCELERATION) : t === cc.EventListener.KEYBOARD ? i._removeListenersForListenerID(r.KEYBOARD) : cc.logID(3501);
     else cc.warnID(3506)
    },
    removeCustomListeners: function(t) {
     this._removeListenersForListenerID(t)
    },
    removeAllListeners: function() {
     var t = this._listenersMap,
      e = this._internalCustomListenerIDs;
     for (var i in t) - 1 === e.indexOf(i) && this._removeListenersForListenerID(i)
    },
    setPriority: function(t, e) {
     if (null != t) {
      var i = this._listenersMap;
      for (var n in i) {
       var r = i[n].getFixedPriorityListeners();
       if (r)
        if (-1 !== r.indexOf(t)) return null != t._getSceneGraphPriority() && cc.logID(3502), void(t._getFixedPriority() !== e && (t._setFixedPriority(e), this._setDirty(t._getListenerID(), this.DIRTY_FIXED_PRIORITY)))
      }
     }
    },
    setEnabled: function(t) {
     this._isEnabled = t
    },
    isEnabled: function() {
     return this._isEnabled
    },
    dispatchEvent: function(t) {
     if (this._isEnabled)
      if (this._updateDirtyFlagForSceneGraph(), this._inDispatch++, t && t.getType) {
       if (t.getType().startsWith(cc.Event.TOUCH)) return this._dispatchTouchEvent(t), void this._inDispatch--;
       var e = (function(t) {
        var e = cc.Event,
         i = t.type;
        return i === e.ACCELERATION ? r.ACCELERATION : i === e.KEYBOARD ? r.KEYBOARD : i.startsWith(e.MOUSE) ? r.MOUSE : (i.startsWith(e.TOUCH) && cc.logID(2e3), "")
       })(t);
       this._sortEventListeners(e);
       var i = this._listenersMap[e];
       null != i && (this._dispatchEventToListeners(i, this._onListenerCallback, t), this._onUpdateListeners(i)), this._inDispatch--
      } else cc.errorID(3511)
    },
    _onListenerCallback: function(t, e) {
     return e.currentTarget = t._target, t._onEvent(e), e.isStopped()
    },
    dispatchCustomEvent: function(t, e) {
     var i = new cc.Event.EventCustom(t);
     i.setUserData(e), this.dispatchEvent(i)
    }
   };
   n.get(cc, "eventManager", (function() {
    return cc.warnID(1405, "cc.eventManager", "cc.EventTarget or cc.systemEvent"), a
   })), e.exports = a
  }), {
   "../platform/js": 128,
   "./CCEventListener": 72
  }],
  74: [(function(t, e, i) {
   cc.Touch = function(t, e, i) {
    this._lastModified = 0, this.setTouchInfo(i, t, e)
   }, cc.Touch.prototype = {
    constructor: cc.Touch,
    getLocation: function() {
     return cc.v2(this._point.x, this._point.y)
    },
    getLocationX: function() {
     return this._point.x
    },
    getLocationY: function() {
     return this._point.y
    },
    getPreviousLocation: function() {
     return cc.v2(this._prevPoint.x, this._prevPoint.y)
    },
    getStartLocation: function() {
     return cc.v2(this._startPoint.x, this._startPoint.y)
    },
    getDelta: function() {
     return this._point.sub(this._prevPoint)
    },
    getLocationInView: function() {
     return cc.v2(this._point.x, cc.view._designResolutionSize.height - this._point.y)
    },
    getPreviousLocationInView: function() {
     return cc.v2(this._prevPoint.x, cc.view._designResolutionSize.height - this._prevPoint.y)
    },
    getStartLocationInView: function() {
     return cc.v2(this._startPoint.x, cc.view._designResolutionSize.height - this._startPoint.y)
    },
    getID: function() {
     return this._id
    },
    setTouchInfo: function(t, e, i) {
     this._prevPoint = this._point, this._point = cc.v2(e || 0, i || 0), this._id = t, this._startPointCaptured || (this._startPoint = cc.v2(this._point), cc.view._convertPointWithScale(this._startPoint), this._startPointCaptured = !0)
    },
    _setPoint: function(t, e) {
     void 0 === e ? (this._point.x = t.x, this._point.y = t.y) : (this._point.x = t, this._point.y = e)
    },
    _setPrevPoint: function(t, e) {
     this._prevPoint = void 0 === e ? cc.v2(t.x, t.y) : cc.v2(t || 0, e || 0)
    }
   }
  }), {}],
  75: [(function(t, e, i) {
   t("./CCEvent"), t("./CCTouch"), t("./CCEventListener");
   var n = t("./CCEventManager");
   e.exports = n
  }), {
   "./CCEvent": 71,
   "./CCEventListener": 72,
   "./CCEventManager": 73,
   "./CCTouch": 74
  }],
  76: [(function(t, e, i) {
   var n = cc.js,
    r = t("../platform/callbacks-invoker").CallbacksHandler;

   function s() {
    r.call(this)
   }
   n.extend(s, r), s.prototype.invoke = function(t, e) {
    var i = t.type,
     n = this._callbackTable[i];
    if (n) {
     var r = !n.isInvoking;
     n.isInvoking = !0;
     for (var s = n.callbacks, a = n.targets, o = 0, c = s.length; o < c; ++o) {
      var h = s[o];
      if (h) {
       var u = a[o] || t.currentTarget;
       if (h.call(u, t, e), t._propagationImmediateStopped) break
      }
     }
     r && (n.isInvoking = !1, n.containCanceled && n.purgeCanceled())
    }
   }, e.exports = s
  }), {
   "../platform/callbacks-invoker": 121
  }],
  77: [(function(t, e, i) {
   var n = t("../platform/js"),
    r = t("../platform/callbacks-invoker"),
    s = n.array.fastRemove;

   function a() {
    r.call(this)
   }
   n.extend(a, r);
   var o = a.prototype;
   o.on = function(t, e, i) {
    if (e) return this.hasEventListener(t, e, i) || (this.add(t, e, i), i && i.__eventTargets && i.__eventTargets.push(this)), e;
    cc.errorID(6800)
   }, o.off = function(t, e, i) {
    e ? (this.remove(t, e, i), i && i.__eventTargets && s(i.__eventTargets, this)) : this.removeAll(t)
   }, o.targetOff = o.removeAll, o.once = function(t, e, i) {
    var n = "__ONCE_FLAG:" + t;
    if (!this.hasEventListener(n, e, i)) {
     var r = this,
      s = function(a, o, c, h, u) {
       r.off(t, s, i), r.remove(n, e, i), e.call(this, a, o, c, h, u)
      };
     this.on(t, s, i), this.add(n, e, i)
    }
   }, o.emit = r.prototype.invoke, o.dispatchEvent = function(t) {
    this.invoke(t.type, t)
   }, cc.EventTarget = e.exports = a
  }), {
   "../platform/callbacks-invoker": 121,
   "../platform/js": 128
  }],
  78: [(function(t, e, i) {
   var n = t("../platform/js");
   cc.Event = function(t, e) {
    this.type = t, this.bubbles = !!e, this.target = null, this.currentTarget = null, this.eventPhase = 0, this._propagationStopped = !1, this._propagationImmediateStopped = !1
   }, cc.Event.prototype = {
    constructor: cc.Event,
    unuse: function() {
     this.type = cc.Event.NO_TYPE, this.target = null, this.currentTarget = null, this.eventPhase = cc.Event.NONE, this._propagationStopped = !1, this._propagationImmediateStopped = !1
    },
    reuse: function(t, e) {
     this.type = t, this.bubbles = e || !1
    },
    stopPropagation: function() {
     this._propagationStopped = !0
    },
    stopPropagationImmediate: function() {
     this._propagationImmediateStopped = !0
    },
    isStopped: function() {
     return this._propagationStopped || this._propagationImmediateStopped
    },
    getCurrentTarget: function() {
     return this.currentTarget
    },
    getType: function() {
     return this.type
    }
   }, cc.Event.NO_TYPE = "no_type", cc.Event.TOUCH = "touch", cc.Event.MOUSE = "mouse", cc.Event.KEYBOARD = "keyboard", cc.Event.ACCELERATION = "acceleration", cc.Event.NONE = 0, cc.Event.CAPTURING_PHASE = 1, cc.Event.AT_TARGET = 2, cc.Event.BUBBLING_PHASE = 3;
   var r = function(t, e) {
    cc.Event.call(this, t, e), this.detail = null
   };
   n.extend(r, cc.Event), r.prototype.reset = r, r.prototype.setUserData = function(t) {
    this.detail = t
   }, r.prototype.getUserData = function() {
    return this.detail
   }, r.prototype.getEventName = cc.Event.prototype.getType;
   var s = new n.Pool(10);
   r.put = function(t) {
    s.put(t)
   }, r.get = function(t, e) {
    var i = s._get();
    return i ? i.reset(t, e) : i = new r(t, e), i
   }, cc.Event.EventCustom = r, e.exports = cc.Event
  }), {
   "../platform/js": 128
  }],
  79: [(function(t, e, i) {
   t("./event"), t("./event-listeners"), t("./event-target"), t("./system-event")
  }), {
   "./event": 78,
   "./event-listeners": 76,
   "./event-target": 77,
   "./system-event": 80
  }],
  80: [(function(t, e, i) {
   var n = t("../event/event-target"),
    r = t("../event-manager"),
    s = t("../platform/CCInputManager"),
    a = cc.Enum({
     KEY_DOWN: "keydown",
     KEY_UP: "keyup",
     DEVICEMOTION: "devicemotion"
    }),
    o = null,
    c = null,
    h = cc.Class({
     name: "SystemEvent",
     extends: n,
     statics: {
      EventType: a
     },
     setAccelerometerEnabled: function(t) {
      s.setAccelerometerEnabled(t)
     },
     setAccelerometerInterval: function(t) {
      s.setAccelerometerInterval(t)
     },
     on: function(t, e, i) {
      this._super(t, e, i), t !== a.KEY_DOWN && t !== a.KEY_UP || (o || (o = cc.EventListener.create({
       event: cc.EventListener.KEYBOARD,
       onKeyPressed: function(t, e) {
        e.type = a.KEY_DOWN, cc.systemEvent.dispatchEvent(e)
       },
       onKeyReleased: function(t, e) {
        e.type = a.KEY_UP, cc.systemEvent.dispatchEvent(e)
       }
      })), r.hasEventListener(cc.EventListener.ListenerID.KEYBOARD) || r.addListener(o, 1)), t === a.DEVICEMOTION && (c || (c = cc.EventListener.create({
       event: cc.EventListener.ACCELERATION,
       callback: function(t, e) {
        e.type = a.DEVICEMOTION, cc.systemEvent.dispatchEvent(e)
       }
      })), r.hasEventListener(cc.EventListener.ListenerID.ACCELERATION) || r.addListener(c, 1))
     },
     off: function(t, e, i) {
      if (this._super(t, e, i), o && (t === a.KEY_DOWN || t === a.KEY_UP)) {
       var n = this.hasEventListener(a.KEY_DOWN),
        s = this.hasEventListener(a.KEY_UP);
       n || s || r.removeListener(o)
      }
      c && t === a.DEVICEMOTION && r.removeListener(c)
     }
    });
   cc.SystemEvent = e.exports = h, cc.systemEvent = new cc.SystemEvent
  }), {
   "../event-manager": 75,
   "../event/event-target": 77,
   "../platform/CCInputManager": 112
  }],
  81: [(function(t, e, i) {
   var n = t("../components/CCRenderComponent"),
    r = t("../renderer/render-engine").SpriteMaterial,
    s = t("./types"),
    a = s.LineCap,
    o = s.LineJoin,
    c = cc.Class({
     name: "cc.Graphics",
     extends: n,
     editor: !1,
     ctor: function() {
      this._impl = c._assembler.createImpl(this)
     },
     properties: {
      _lineWidth: 1,
      _strokeColor: cc.Color.BLACK,
      _lineJoin: o.MITER,
      _lineCap: a.BUTT,
      _fillColor: cc.Color.WHITE,
      _miterLimit: 10,
      lineWidth: {
       get: function() {
        return this._lineWidth
       },
       set: function(t) {
        this._lineWidth = t, this._impl.lineWidth = t
       }
      },
      lineJoin: {
       get: function() {
        return this._lineJoin
       },
       set: function(t) {
        this._lineJoin = t, this._impl.lineJoin = t
       },
       type: o
      },
      lineCap: {
       get: function() {
        return this._lineCap
       },
       set: function(t) {
        this._lineCap = t, this._impl.lineCap = t
       },
       type: a
      },
      strokeColor: {
       get: function() {
        return this._strokeColor
       },
       set: function(t) {
        this._impl.strokeColor = this._strokeColor = cc.color(t)
       }
      },
      fillColor: {
       get: function() {
        return this._fillColor
       },
       set: function(t) {
        this._impl.fillColor = this._fillColor = cc.color(t)
       }
      },
      miterLimit: {
       get: function() {
        return this._miterLimit
       },
       set: function(t) {
        this._miterLimit = t, this._impl.miterLimit = t
       }
      }
     },
     statics: {
      LineJoin: o,
      LineCap: a
     },
     onRestore: function() {
      this._impl || (this._impl = c._assembler.createImpl())
     },
     onEnable: function() {
      this._super(), this._activateMaterial()
     },
     onDestroy: function() {
      this._super(), this._impl.clear(this, !0), this._impl = null
     },
     _activateMaterial: function() {
      if (cc.game.renderType !== cc.game.RENDER_TYPE_CANVAS && (this.node._renderFlag &= ~cc.RenderFlow.FLAG_RENDER, this.node._renderFlag |= cc.RenderFlow.FLAG_CUSTOM_IA_RENDER, !this._material)) {
       var t = new r;
       t.useColor = !1, t.useTexture = !1, t.useModel = !0, this._updateMaterial(t)
      }
     },
     moveTo: function(t, e) {
      this._impl.moveTo(t, e)
     },
     lineTo: function(t, e) {
      this._impl.lineTo(t, e)
     },
     bezierCurveTo: function(t, e, i, n, r, s) {
      this._impl.bezierCurveTo(t, e, i, n, r, s)
     },
     quadraticCurveTo: function(t, e, i, n) {
      this._impl.quadraticCurveTo(t, e, i, n)
     },
     arc: function(t, e, i, n, r, s) {
      this._impl.arc(t, e, i, n, r, s)
     },
     ellipse: function(t, e, i, n) {
      this._impl.ellipse(t, e, i, n)
     },
     circle: function(t, e, i) {
      this._impl.circle(t, e, i)
     },
     rect: function(t, e, i, n) {
      this._impl.rect(t, e, i, n)
     },
     roundRect: function(t, e, i, n, r) {
      this._impl.roundRect(t, e, i, n, r)
     },
     fillRect: function(t, e, i, n) {
      this.rect(t, e, i, n), this.fill()
     },
     clear: function(t) {
      this._impl.clear(this, t)
     },
     close: function() {
      this._impl.close()
     },
     stroke: function() {
      c._assembler.stroke(this)
     },
     fill: function() {
      c._assembler.fill(this)
     }
    });
   cc.Graphics = e.exports = c
  }), {
   "../components/CCRenderComponent": 62,
   "../renderer/render-engine": 150,
   "./types": 84
  }],
  82: [(function(t, e, i) {
   var n = t("./types").PointFlags,
    r = Math.PI,
    s = Math.min,
    a = Math.max,
    o = Math.cos,
    c = Math.sin,
    h = Math.abs,
    u = Math.sign,
    l = .5522847493;
   e.exports = {
    arc: function(t, e, i, n, u, l, _) {
     var d, f, p, m = 0,
      v = 0,
      g = 0,
      y = 0,
      T = 0,
      E = 0,
      x = 0,
      C = 0,
      A = 0,
      b = 0,
      S = 0,
      w = 0,
      D = 0;
     if (v = l - u, _ = _ || !1)
      if (h(v) >= 2 * r) v = 2 * r;
      else
       for (; v < 0;) v += 2 * r;
     else if (h(v) >= 2 * r) v = 2 * -r;
     else
      for (; v > 0;) v -= 2 * r;
     for (p = 0 | a(1, s(h(v) / (.5 * r) + .5, 5)), g = h(4 / 3 * (1 - o(d = v / p / 2)) / c(d)), _ || (g = -g), f = 0; f <= p; f++) E = e + (y = o(m = u + v * (f / p))) * n, x = i + (T = c(m)) * n, C = -T * n * g, A = y * n * g, 0 === f ? t.moveTo(E, x) : t.bezierCurveTo(b + w, S + D, E - C, x - A, E, x), b = E, S = x, w = C, D = A
    },
    ellipse: function(t, e, i, n, r) {
     t.moveTo(e - n, i), t.bezierCurveTo(e - n, i + r * l, e - n * l, i + r, e, i + r), t.bezierCurveTo(e + n * l, i + r, e + n, i + r * l, e + n, i), t.bezierCurveTo(e + n, i - r * l, e + n * l, i - r, e, i - r), t.bezierCurveTo(e - n * l, i - r, e - n, i - r * l, e - n, i), t.close()
    },
    roundRect: function(t, e, i, n, r, a) {
     if (a < .1) t.rect(e, i, n, r);
     else {
      var o = s(a, .5 * h(n)) * u(n),
       c = s(a, .5 * h(r)) * u(r);
      t.moveTo(e, i + c), t.lineTo(e, i + r - c), t.bezierCurveTo(e, i + r - c * (1 - l), e + o * (1 - l), i + r, e + o, i + r), t.lineTo(e + n - o, i + r), t.bezierCurveTo(e + n - o * (1 - l), i + r, e + n, i + r - c * (1 - l), e + n, i + r - c), t.lineTo(e + n, i + c), t.bezierCurveTo(e + n, i + c * (1 - l), e + n - o * (1 - l), i, e + n - o, i), t.lineTo(e + o, i), t.bezierCurveTo(e + o * (1 - l), i, e, i + c * (1 - l), e, i + c), t.close()
     }
    },
    tesselateBezier: function t(e, i, r, s, a, o, c, u, l, _, d) {
     var f, p, m, v, g, y, T, E, x, C, A, b, S, w, D, R;
     _ > 10 || (g = .5 * (o + u), y = .5 * (c + l), T = .5 * ((f = .5 * (i + s)) + (m = .5 * (s + o))), E = .5 * ((p = .5 * (r + a)) + (v = .5 * (a + c))), ((D = h((s - u) * (w = l - r) - (a - l) * (S = u - i))) + (R = h((o - u) * w - (c - l) * S))) * (D + R) < e._tessTol * (S * S + w * w) ? e._addPoint(u, l, 0 === d ? d | n.PT_BEVEL : d) : (t(e, i, r, f, p, T, E, A = .5 * (T + (x = .5 * (m + g))), b = .5 * (E + (C = .5 * (v + y))), _ + 1, 0), t(e, A, b, x, C, g, y, u, l, _ + 1, d)))
    }
   }
  }), {
   "./types": 84
  }],
  83: [(function(t, e, i) {
   "use strict";
   t("./graphics")
  }), {
   "./graphics": 81
  }],
  84: [(function(t, e, i) {
   "use strict";
   var n = cc.Enum({
     BUTT: 0,
     ROUND: 1,
     SQUARE: 2
    }),
    r = cc.Enum({
     BEVEL: 0,
     ROUND: 1,
     MITER: 2
    }),
    s = cc.Enum({
     PT_CORNER: 1,
     PT_LEFT: 2,
     PT_BEVEL: 4,
     PT_INNERBEVEL: 8
    });
   e.exports = {
    LineCap: n,
    LineJoin: r,
    PointFlags: s
   }
  }), {}],
  85: [(function(t, e, i) {
   t("./platform"), t("./assets"), t("./CCNode"), t("./CCPrivateNode"), t("./CCScene"), t("./components"), t("./graphics"), t("./collider"), t("./collider/CCIntersection"), t("./physics"), t("./camera/CCCamera"), t("./utils/polyfill-3d"), t("./base-ui/CCWidgetManager")
  }), {
   "./CCNode": 24,
   "./CCPrivateNode": 25,
   "./CCScene": 26,
   "./assets": 45,
   "./base-ui/CCWidgetManager": void 0,
   "./camera/CCCamera": 46,
   "./collider": void 0,
   "./collider/CCIntersection": 47,
   "./components": 69,
   "./graphics": 83,
   "./physics": void 0,
   "./platform": 125,
   "./utils/polyfill-3d": 188
  }],
  86: [(function(t, e, i) {
   var n = t("../platform/js"),
    r = t("./pipeline"),
    s = t("./loading-items"),
    a = t("./asset-loader"),
    o = t("./downloader"),
    c = t("./loader"),
    h = t("./asset-table"),
    u = t("../platform/utils").callInNextTick,
    l = t("./auto-release-utils"),
    _ = new h;
   var d = {
    url: null,
    raw: !1
   };

   function f(t) {
    var e, i, n;
    if ("object" == typeof t) {
     if (i = t, t.url) return i;
     e = t.uuid
    } else i = {}, e = t;
    return n = i.type ? "uuid" === i.type : cc.AssetLibrary._uuidInSettings(e), cc.AssetLibrary._getAssetInfoInRuntime(e, d), i.url = n ? d.url : e, d.url && "uuid" === i.type && d.raw ? (i.type = null, i.isRawAsset = !0) : n || (i.isRawAsset = !0), i
   }
   var p = [],
    m = [];

   function v() {
    var t = new a,
     e = new o,
     i = new c;
    r.call(this, [t, e, i]), this.assetLoader = t, this.md5Pipe = null, this.downloader = e, this.loader = i, this.onProgress = null, this._autoReleaseSetting = n.createMap(!0)
   }
   n.extend(v, r);
   var g = v.prototype;
   g.init = function(t) {}, g.getXMLHttpRequest = function() {
    return window.XMLHttpRequest ? new window.XMLHttpRequest : new ActiveXObject("MSXML2.XMLHTTP")
   }, g.addDownloadHandlers = function(t) {
    this.downloader.addHandlers(t)
   }, g.addLoadHandlers = function(t) {
    this.loader.addHandlers(t)
   }, g.load = function(t, e, i) {
    void 0 === i && (i = e, e = this.onProgress || null);
    var n, r = this,
     a = !1;
    t instanceof Array || (t ? (a = !0, t = [t]) : t = []), p.length = 0;
    for (var o = 0; o < t.length; ++o) {
     var c = t[o];
     if (c && c.id && (cc.warnID(4920, c.id), c.uuid || c.url || (c.url = c.id)), (n = f(c)).url || n.uuid) {
      var h = this._cache[n.url];
      p.push(h || n)
     }
    }
    var l = s.create(this, e, (function(t, e) {
     u((function() {
      if (i) {
       if (a) {
        var s = n.url;
        i.call(r, t, e.getContent(s))
       } else i.call(r, t, e);
       i = null
      }
      e.destroy()
     }))
    }));
    s.initQueueDeps(l), l.append(p), p.length = 0
   }, g.flowInDeps = function(t, e, i) {
    m.length = 0;
    for (var n = 0; n < e.length; ++n) {
     var r = f(e[n]);
     if (r.url || r.uuid) {
      var a = this._cache[r.url];
      a ? m.push(a) : m.push(r)
     }
    }
    var o = s.create(this, t ? function(t, e, i) {
     this._ownerQueue && this._ownerQueue.onProgress && this._ownerQueue._childOnProgress(i)
    } : null, (function(e, n) {
     i(e, n), t && t.deps && (t.deps.length = 0), n.destroy()
    }));
    if (t) {
     var c = s.getQueue(t);
     o._ownerQueue = c._ownerQueue || c
    }
    var h = o.append(m, t);
    return m.length = 0, h
   }, g._resources = _, g._getResUuid = function(t, e, i) {
    if (!t) return null;
    var n = t.indexOf("?"); - 1 !== n && (t = t.substr(0, n));
    var r = _.getUuid(t, e);
    if (!r) {
     var s = cc.path.extname(t);
     s && (t = t.slice(0, -s.length), (r = _.getUuid(t, e)) && !i && cc.warnID(4901, t, s))
    }
    return r
   }, g._getReferenceKey = function(t) {
    var e;
    return "object" == typeof t ? e = t._uuid || null : "string" == typeof t && (e = this._getResUuid(t, null, !0) || t), e ? (cc.AssetLibrary._getAssetInfoInRuntime(e, d), this._cache[d.url] ? d.url : e) : (cc.warnID(4800, t), e)
   }, g._urlNotFound = function(t, e, i) {
    u((function() {
     t = cc.url.normalize(t);
     var r = (e ? n.getClassName(e) : "Asset") + ' in "resources/' + t + '" does not exist.';
     i && i(new Error(r), [])
    }))
   }, g._parseLoadResArgs = function(t, e, i) {
    if (void 0 === i) {
     var r = t instanceof Array || n.isChildClassOf(t, cc.RawAsset);
     e ? (i = e, r && (e = this.onProgress || null)) : void 0 !== e || r || (i = t, e = this.onProgress || null, t = null), void 0 === e || r || (e = t, t = null)
    }
    return {
     type: t,
     onProgress: e,
     onComplete: i
    }
   }, g.loadRes = function(t, e, i, n) {
    var r = this._parseLoadResArgs(e, i, n);
    e = r.type, i = r.onProgress, n = r.onComplete;
    var s = this,
     a = s._getResUuid(t, e);
    a ? this.load({
     type: "uuid",
     uuid: a
    }, i, (function(t, e) {
     e && s.setAutoReleaseRecursively(a, !1), n && n(t, e)
    })) : s._urlNotFound(t, e, n)
   }, g._loadResUuids = function(t, e, i, n) {
    if (t.length > 0) {
     var r = this,
      s = t.map((function(t) {
       return {
        type: "uuid",
        uuid: t
       }
      }));
     this.load(s, e, (function(t, e) {
      if (i) {
       for (var a = [], o = n && [], c = 0; c < s.length; ++c) {
        var h = s[c].uuid,
         u = this._getReferenceKey(h),
         l = e.getContent(u);
        l && (r.setAutoReleaseRecursively(h, !1), a.push(l), o && o.push(n[c]))
       }
       n ? i(t, a, o) : i(t, a)
      }
     }))
    } else i && u((function() {
     n ? i(null, [], []) : i(null, [])
    }))
   }, g.loadResArray = function(t, e, i, n) {
    var r = this._parseLoadResArgs(e, i, n);
    e = r.type, i = r.onProgress, n = r.onComplete;
    for (var s = [], a = e instanceof Array, o = 0; o < t.length; o++) {
     var c = t[o],
      h = a ? e[o] : e,
      u = this._getResUuid(c, h);
     if (!u) return void this._urlNotFound(c, h, n);
     s.push(u)
    }
    this._loadResUuids(s, i, n)
   }, g.loadResDir = function(t, e, i, n) {
    var r = this._parseLoadResArgs(e, i, n);
    e = r.type, i = r.onProgress, n = r.onComplete;
    var s = [],
     a = _.getUuidArray(t, e, s);
    this._loadResUuids(a, i, (function(t, e, i) {
     for (var r = e.length, s = 0; s < r; ++s)
      if (e[s] instanceof cc.SpriteAtlas) {
       var a = e[s].getSpriteFrames();
       for (var o in a) {
        var c = a[o];
        e.push(c), i && i.push(i[s] + "/" + c.name)
       }
      } n && n(t, e, i)
    }), s)
   }, g.getRes = function(t, e) {
    var i = this._cache[t];
    if (!i) {
     var n = this._getResUuid(t, e, !0);
     if (!n) return null;
     var r = this._getReferenceKey(n);
     i = this._cache[r]
    }
    return i && i.alias && (i = i.alias), i && i.complete ? i.content : null
   }, g.getResCount = function() {
    return Object.keys(this._cache).length
   }, g.getDependsRecursively = function(t) {
    if (t) {
     var e = this._getReferenceKey(t),
      i = l.getDependsRecursively(e);
     return i.push(e), i
    }
    return []
   }, g.release = function(t) {
    if (Array.isArray(t))
     for (var e = 0; e < t.length; e++) {
      var i = t[e];
      this.release(i)
     } else if (t) {
      var n = this._getReferenceKey(t),
       r = this.getItem(n);
      if (r) {
       this.removeItem(n);
       if ((t = r.content) instanceof cc.Asset) {
        var s = t.nativeUrl;
        s && this.release(s), t.destroy()
       }
       0
      }
     }
   }, g.releaseAsset = function(t) {
    var e = t._uuid;
    e && this.release(e)
   }, g.releaseRes = function(t, e) {
    var i = this._getResUuid(t, e);
    i ? this.release(i) : cc.errorID(4914, t)
   }, g.releaseResDir = function(t, e) {
    for (var i = _.getUuidArray(t, e), n = 0; n < i.length; n++) {
     var r = i[n];
     this.release(r)
    }
   }, g.releaseAll = function() {
    for (var t in this._cache) this.release(t)
   }, g.removeItem = function(t) {
    var e = r.prototype.removeItem.call(this, t);
    return delete this._autoReleaseSetting[t], e
   }, g.setAutoRelease = function(t, e) {
    var i = this._getReferenceKey(t);
    i && (this._autoReleaseSetting[i] = !!e)
   }, g.setAutoReleaseRecursively = function(t, e) {
    e = !!e;
    var i = this._getReferenceKey(t);
    if (i) {
     this._autoReleaseSetting[i] = e;
     for (var n = l.getDependsRecursively(i), r = 0; r < n.length; r++) {
      var s = n[r];
      this._autoReleaseSetting[s] = e
     }
    } else 0
   }, g.isAutoRelease = function(t) {
    var e = this._getReferenceKey(t);
    return !!e && !!this._autoReleaseSetting[e]
   }, cc.loader = new v, e.exports = cc.loader
  }), {
   "../platform/js": 128,
   "../platform/utils": 132,
   "./asset-loader": 87,
   "./asset-table": 88,
   "./auto-release-utils": 90,
   "./downloader": 92,
   "./loader": 95,
   "./loading-items": 96,
   "./pipeline": 99,
   "./released-asset-checker": 100
  }],
  87: [(function(t, e, i) {
   t("../utils/CCPath");
   var n = t("../CCDebug"),
    r = t("./pipeline"),
    s = t("./loading-items"),
    a = "AssetLoader",
    o = function(t) {
     this.id = a, this.async = !0, this.pipeline = null
    };
   o.ID = a;
   var c = [];
   o.prototype.handle = function(t, e) {
    var i = t.uuid;
    if (!i) return t.content || null;
    cc.AssetLibrary.queryAssetInfo(i, (function(r, a, o) {
     if (r) e(r);
     else if (t.url = t.rawUrl = a, t.isRawAsset = o, o) {
      var h = cc.path.extname(a).toLowerCase();
      if (!h) return void e(new Error(n.getError(4931, i, a)));
      h = h.substr(1);
      var u = s.getQueue(t);
      c[0] = {
       queueId: t.queueId,
       id: a,
       url: a,
       type: h,
       error: null,
       alias: t,
       complete: !0
      }, u.append(c), t.type = h, e(null, t.content)
     } else t.type = "uuid", e(null, t.content)
    }))
   }, r.AssetLoader = e.exports = o
  }), {
   "../CCDebug": 21,
   "../utils/CCPath": 178,
   "./loading-items": 96,
   "./pipeline": 99
  }],
  88: [(function(t, e, i) {
   var n = t("../utils/misc").pushToMap,
    r = t("../platform/js");

   function s() {
    this._pathToUuid = r.createMap(!0)
   }

   function a(t, e) {
    if (t.length > e.length) {
     var i = t.charCodeAt(e.length);
     return 46 === i || 47 === i
    }
    return !0
   }
   var o = s.prototype;
   o.getUuid = function(t, e) {
    t = cc.url.normalize(t);
    var i = this._pathToUuid[t];
    if (i)
     if (Array.isArray(i)) {
      if (!e) return i[0].uuid;
      for (var n = 0; n < i.length; n++) {
       var s = i[n];
       if (r.isChildClassOf(s.type, e)) return s.uuid
      }
     } else {
      if (!e || r.isChildClassOf(i.type, e)) return i.uuid
     } return ""
   }, o.getUuidArray = function(t, e, i) {
    "/" === (t = cc.url.normalize(t))[t.length - 1] && (t = t.slice(0, -1));
    var n = this._pathToUuid,
     s = [],
     o = r.isChildClassOf;
    for (var c in n)
     if (c.startsWith(t) && a(c, t) || !t) {
      var h = n[c];
      if (Array.isArray(h))
       for (var u = 0; u < h.length; u++) {
        var l = h[u];
        (!e || o(l.type, e)) && (s.push(l.uuid), i && i.push(c))
       } else(!e || o(h.type, e)) && (s.push(h.uuid), i && i.push(c))
     } return s
   }, o.add = function(t, e, i, r) {
    t = t.substring(0, t.length - cc.path.extname(t).length);
    var s = new function(t, e) {
     this.uuid = t, this.type = e
    }(e, i);
    n(this._pathToUuid, t, s, r)
   }, o._getInfo_DEBUG = !1, o.reset = function() {
    this._pathToUuid = r.createMap(!0)
   }, e.exports = s
  }), {
   "../platform/js": 128,
   "../utils/misc": 186
  }],
  89: [(function(t, e, i) {
   var n = t("../platform/CCSys"),
    r = t("../CCDebug"),
    s = n.__audioSupport,
    a = s.format,
    o = s.context;

   function c(t, e) {
    var i = document.createElement("audio");
    i.src = t.url;
    cc.sys.platform, cc.sys.BAIDU_GAME;
    e(null, i)
   }

   function h(t, e) {
    o || e(new Error(r.getError(4926)));
    var i = cc.loader.getXMLHttpRequest();
    i.open("GET", t.url, !0), i.responseType = "arraybuffer", i.onload = function() {
     o.decodeAudioData(i.response, (function(t) {
      e(null, t)
     }), (function() {
      e("decode error - " + t.id, null)
     }))
    }, i.onerror = function() {
     e("request error - " + t.id, null)
    }, i.send()
   }
   e.exports = function(t, e) {
    if (0 === a.length) return new Error(r.getError(4927));
    var i;
    i = s.WEB_AUDIO ? t._owner instanceof cc.AudioClip ? t._owner.loadMode === cc.AudioClip.LoadMode.WEB_AUDIO ? h : c : t.urlParam && t.urlParam.useDom ? c : h : c, i(t, e)
   }
  }), {
   "../CCDebug": 21,
   "../platform/CCSys": 117
  }],
  90: [(function(t, e, i) {
   var n = t("../platform/js");

   function r(t, e) {
    var i = cc.loader.getItem(t);
    if (i) {
     var n = i.dependKeys;
     if (n)
      for (var s = 0; s < n.length; s++) {
       var a = n[s];
       e[a] || (e[a] = !0, r(a, e))
      }
    }
   }

   function s(t, e) {
    if (t._uuid) {
     var i = cc.loader._getReferenceKey(t);
     e[i] || (e[i] = !0, r(i, e))
    }
   }

   function a(t, e) {
    for (var i = Object.getOwnPropertyNames(t), n = 0; n < i.length; n++) {
     var r = t[i[n]];
     if ("object" == typeof r && r)
      if (Array.isArray(r))
       for (var a = 0; a < r.length; a++) {
        var o = r[a];
        o instanceof cc.RawAsset && s(o, e)
       } else if (r.constructor && r.constructor !== Object) r instanceof cc.RawAsset && s(r, e);
       else
        for (var c = Object.getOwnPropertyNames(r), h = 0; h < c.length; h++) {
         var u = r[c[h]];
         u instanceof cc.RawAsset && s(u, e)
        }
    }
   }

   function o(t, e) {
    for (var i = 0; i < t._components.length; i++) a(t._components[i], e);
    for (var n = 0; n < t._children.length; n++) o(t._children[n], e)
   }
   e.exports = {
    autoRelease: function(t, e, i) {
     var r = cc.loader._autoReleaseSetting,
      s = n.createMap();
     if (e)
      for (var a = 0; a < e.length; a++) s[e[a]] = !0;
     for (var c = 0; c < i.length; c++) o(i[c], s);
     if (t)
      for (var h = 0; h < t.length; h++) {
       var u = t[h];
       !1 === r[u] || s[u] || cc.loader.release(u)
      }
     for (var l = Object.keys(r), _ = 0; _ < l.length; _++) {
      var d = l[_];
      !0 !== r[d] || s[d] || cc.loader.release(d)
     }
    },
    getDependsRecursively: function(t) {
     var e = {};
     return r(t, e), Object.keys(e)
    }
   }
  }), {
   "../platform/js": 128
  }],
  91: [(function(t, e, i) {
   e.exports = function(t, e) {
    var i = t.url,
     n = cc.loader.getXMLHttpRequest(),
     r = "Load binary data failed: " + i;
    n.open("GET", i, !0), n.responseType = "arraybuffer", n.onload = function() {
     var t = n.response;
     if (t) {
      var i = new Uint8Array(t);
      e(null, i)
     } else e({
      status: n.status,
      errorMessage: r + "(no response)"
     })
    }, n.onerror = function() {
     e({
      status: n.status,
      errorMessage: r + "(error)"
     })
    }, n.ontimeout = function() {
     e({
      status: n.status,
      errorMessage: r + "(time out)"
     })
    }, n.send(null)
   }
  }), {}],
  92: [(function(t, e, i) {
   var n = t("../platform/js"),
    r = t("../CCDebug");
   t("../utils/CCPath");
   var s, a = t("./pipeline"),
    o = t("./pack-downloader"),
    c = t("./binary-downloader"),
    h = t("./text-downloader"),
    u = t("./utils").urlAppendTimestamp;

   function l() {
    return null
   }

   function _(t, e, i) {
    var n = t.url,
     s = document,
     a = document.createElement("script");

    function o() {
     a.parentNode.removeChild(a), a.removeEventListener("load", o, !1), a.removeEventListener("error", c, !1), e(null, n)
    }

    function c() {
     a.parentNode.removeChild(a), a.removeEventListener("load", o, !1), a.removeEventListener("error", c, !1), e(new Error(r.getError(4928, n)))
    }
    "file:" !== window.location.protocol && (a.crossOrigin = "anonymous"), a.async = i, a.src = u(n), a.addEventListener("load", o, !1), a.addEventListener("error", c, !1), s.body.appendChild(a)
   }

   function d(t, e, i, n) {
    void 0 === i && (i = !0);
    var s = u(t.url);
    if (n = n || new Image, i && "file:" !== window.location.protocol ? n.crossOrigin = "anonymous" : n.crossOrigin = null, n.complete && n.naturalWidth > 0 && n.src === s) return n;
    (function() {
     function i() {
      n.removeEventListener("load", i), n.removeEventListener("error", a), n.id = t.id, e(null, n)
     }

     function a() {
      n.removeEventListener("load", i), n.removeEventListener("error", a), "https:" !== window.location.protocol && n.crossOrigin && "anonymous" === n.crossOrigin.toLowerCase() ? d(t, e, !1, n) : e(new Error(r.getError(4930, s)))
     }
     n.addEventListener("load", i), n.addEventListener("error", a), n.src = s
    })()
   }
   var f = {
     js: _,
     png: d,
     jpg: d,
     bmp: d,
     jpeg: d,
     gif: d,
     ico: d,
     tiff: d,
     webp: function(t, e, i, n) {
      return cc.sys.capabilities.webp ? d(t, e, i, n) : new Error(r.getError(4929, t.url))
     },
     image: d,
     mp3: s = t("./audio-downloader"),
     ogg: s,
     wav: s,
     m4a: s,
     txt: h,
     xml: h,
     vsh: h,
     fsh: h,
     atlas: h,
     tmx: h,
     tsx: h,
     json: h,
     ExportJson: h,
     plist: h,
     fnt: h,
     font: l,
     eot: l,
     ttf: l,
     woff: l,
     svg: l,
     ttc: l,
     uuid: function(t, e) {
      var i = o.load(t, e);
      return void 0 === i ? this.extMap.json(t, e) : i || void 0
     },
     binary: c,
     dbbin: c,
     default: h
    },
    p = "Downloader",
    m = function(t) {
     this.id = p, this.async = !0, this.pipeline = null, this._curConcurrent = 0, this._loadQueue = [], this._subpackages = {}, this.extMap = n.mixin(t, f)
    };
   m.ID = p, m.PackDownloader = o, m.prototype.addHandlers = function(t) {
    n.mixin(this.extMap, t)
   }, m.prototype._handleLoadQueue = function() {
    for (; this._curConcurrent < cc.macro.DOWNLOAD_MAX_CONCURRENT;) {
     var t = this._loadQueue.shift();
     if (!t) break;
     var e = this.handle(t.item, t.callback);
     void 0 !== e && (e instanceof Error ? t.callback(e) : t.callback(null, e))
    }
   }, m.prototype.handle = function(t, e) {
    var i = this,
     n = this.extMap[t.type] || this.extMap.default,
     r = void 0;
    if (this._curConcurrent < cc.macro.DOWNLOAD_MAX_CONCURRENT) {
     if (this._curConcurrent++, void 0 !== (r = n.call(this, t, (function(t, n) {
       i._curConcurrent = Math.max(0, i._curConcurrent - 1), i._handleLoadQueue(), e && e(t, n)
      })))) return this._curConcurrent = Math.max(0, this._curConcurrent - 1), this._handleLoadQueue(), r
    } else if (t.ignoreMaxConcurrency) {
     if (void 0 !== (r = n.call(this, t, e))) return r
    } else this._loadQueue.push({
     item: t,
     callback: e
    })
   }, m.prototype.loadSubpackage = function(t, e) {
    var i = this._subpackages[t];
    i ? i.loaded ? e && e() : _({
     url: i.path
    }, (function(t) {
     t || (i.loaded = !0), e && e(t)
    })) : e && e(new Error("Can't find subpackage " + t))
   }, a.Downloader = e.exports = m
  }), {
   "../CCDebug": 21,
   "../platform/js": 128,
   "../utils/CCPath": 178,
   "./audio-downloader": 89,
   "./binary-downloader": 91,
   "./pack-downloader": 98,
   "./pipeline": 99,
   "./text-downloader": 101,
   "./utils": 103
  }],
  93: [(function(t, e, i) {
   var n = t("../utils/text-utils"),
    r = null,
    s = "BES bswy:->@123\u4e01\u3041\u1101",
    a = {},
    o = -1,
    c = [],
    h = 6e4,
    u = (function() {
     var t = void 0;
     return function() {
      if (void 0 === t)
       if (window.FontFace) {
        var e = /Gecko.*Firefox\/(\d+)/.exec(window.navigator.userAgent),
         i = /OS X.*Version\/10\..*Safari/.exec(window.navigator.userAgent) && /Apple/.exec(window.navigator.vendor);
        t = e ? parseInt(e[1], 10) > 42 : !i
       } else t = !1;
      return t
     }
    })();

   function l() {
    for (var t = !0, e = Date.now(), i = c.length - 1; i >= 0; i--) {
     var a = c[i],
      u = a.fontFamilyName;
     if (e - a.startTime > h) cc.warnID(4933, u), a.callback(null, u), c.splice(i, 1);
     else {
      var l = a.refWidth;
      r.font = "40px " + u, l !== n.safeMeasureText(r, s) ? (c.splice(i, 1), a.callback(null, u)) : t = !1
     }
    }
    t && (clearInterval(o), o = -1)
   }
   var _ = {
    loadFont: function(t, e) {
     var i = t.url,
      d = _._getFontFamily(i);
     if (a[d]) return d;
     if (!r) {
      var f = document.createElement("canvas");
      f.width = 100, f.height = 100, r = f.getContext("2d")
     }
     var p = "40px " + d;
     r.font = p;
     var m = n.safeMeasureText(r, s),
      v = document.createElement("style");
     v.type = "text/css";
     var g = "";
     isNaN(d - 0) ? g += "@font-face { font-family:" + d + "; src:" : g += "@font-face { font-family:'" + d + "'; src:", g += "url('" + i + "');", v.textContent = g + "}", document.body.appendChild(v);
     var y = document.createElement("div"),
      T = y.style;
     if (T.fontFamily = d, y.innerHTML = ".", T.position = "absolute", T.left = "-100px", T.top = "-100px", document.body.appendChild(y), u())(function(t, e, i) {
      var n = new Promise(function(i, n) {
        var r = function() {
         Date.now() - t >= h ? n() : document.fonts.load("40px " + e).then((function(t) {
          t.length >= 1 ? i() : setTimeout(r, 100)
         }), (function() {
          n()
         }))
        };
        r()
       }),
       r = null,
       s = new Promise(function(t, e) {
        r = setTimeout(e, h)
       });
      Promise.race([s, n]).then((function() {
       r && (clearTimeout(r), r = null), i(null, e)
      }), (function() {
       cc.warnID(4933, e), i(null, e)
      }))
     })(Date.now(), d, e);
     else {
      var E = {
       fontFamilyName: d,
       refWidth: m,
       callback: e,
       startTime: Date.now()
      };
      c.push(E), -1 === o && (o = setInterval(l, 100))
     }
     a[d] = v
    },
    _getFontFamily: function(t) {
     var e = t.lastIndexOf(".ttf");
     if (-1 === e) return t;
     var i, n = t.lastIndexOf("/");
     return -1 !== (i = -1 === n ? t.substring(0, e) + "_LABEL" : t.substring(n + 1, e) + "_LABEL").indexOf(" ") && (i = '"' + i + '"'), i
    }
   };
   e.exports = _
  }), {
   "../utils/text-utils": 193
  }],
  94: [(function(t, e, i) {
   t("./downloader"), t("./loader"), t("./loading-items"), t("./pipeline"), t("./CCLoader")
  }), {
   "./CCLoader": 86,
   "./downloader": 92,
   "./loader": 95,
   "./loading-items": 96,
   "./pipeline": 99
  }],
  95: [(function(t, e, i) {
   var n = t("../platform/js"),
    r = t("../platform/CCSAXParser").plistParser,
    s = t("./pipeline"),
    a = t("../assets/CCTexture2D"),
    o = t("./uuid-loader"),
    c = t("./font-loader");

   function h(t) {
    if ("string" != typeof t.content) return new Error("JSON Loader: Input item doesn't contain string content");
    try {
     return JSON.parse(t.content)
    } catch (e) {
     return new Error("JSON Loader: Parse json [" + t.id + "] failed : " + e)
    }
   }

   function u(t) {
    if (t._owner instanceof cc.Asset) return null;
    var e = t.content;
    var i = t.rawUrl,
     n = t.texture || new a;
    return n._uuid = t.uuid, n.url = i, n._setRawAsset(i, !1), n._nativeAsset = e, n
   }

   function l(t, e) {
    if (t._owner instanceof cc.Asset) return null;
    var i = new cc.AudioClip;
    return i._setRawAsset(t.rawUrl, !1), i._nativeAsset = t.content, i
   }

   function _(t) {
    return t.load ? t.load(t.content) : null
   }
   var d = {
     png: u,
     jpg: u,
     bmp: u,
     jpeg: u,
     gif: u,
     ico: u,
     tiff: u,
     webp: u,
     image: u,
     mp3: l,
     ogg: l,
     wav: l,
     m4a: l,
     json: h,
     ExportJson: h,
     plist: function(t) {
      if ("string" != typeof t.content) return new Error("Plist Loader: Input item doesn't contain string content");
      var e = r.parse(t.content);
      return e || new Error("Plist Loader: Parse [" + t.id + "] failed")
     },
     uuid: o,
     prefab: o,
     fire: o,
     scene: o,
     binary: _,
     dbbin: _,
     font: c.loadFont,
     eot: c.loadFont,
     ttf: c.loadFont,
     woff: c.loadFont,
     svg: c.loadFont,
     ttc: c.loadFont,
     default: function() {
      return null
     }
    },
    f = function(t) {
     this.id = "Loader", this.async = !0, this.pipeline = null, this.extMap = n.mixin(t, d)
    };
   f.ID = "Loader", f.prototype.addHandlers = function(t) {
    this.extMap = n.mixin(this.extMap, t)
   }, f.prototype.handle = function(t, e) {
    return (this.extMap[t.type] || this.extMap.default).call(this, t, e)
   }, s.Loader = e.exports = f
  }), {
   "../assets/CCTexture2D": 44,
   "../platform/CCSAXParser": 115,
   "../platform/js": 128,
   "./font-loader": 93,
   "./pipeline": 99,
   "./uuid-loader": 104
  }],
  96: [(function(t, e, i) {
   var n = t("../platform/callbacks-invoker");
   t("../utils/CCPath");
   var r = t("../platform/js"),
    s = 0 | 998 * Math.random(),
    a = r.createMap(!0),
    o = [],
    c = {
     WORKING: 1,
     COMPLETE: 2,
     ERROR: 3
    },
    h = r.createMap(!0);

   function u(t) {
    return "string" == typeof(t.url || t)
   }

   function l(t, e) {
    var i = "object" == typeof t ? t.url : t,
     n = {
      queueId: e,
      id: i,
      url: i,
      rawUrl: void 0,
      urlParam: (function(t) {
       if (t) {
        var e = t.split("?");
        if (e && e[0] && e[1]) {
         var i = {};
         return e[1].split("&").forEach((function(t) {
          var e = t.split("=");
          i[e[0]] = e[1]
         })), i
        }
       }
      })(i),
      type: "",
      error: null,
      content: null,
      complete: !1,
      states: {},
      deps: null
     };
    if ("object" == typeof t && (r.mixin(n, t), t.skips))
     for (var s = 0; s < t.skips.length; s++) {
      var a = t.skips[s];
      n.states[a] = c.COMPLETE
     }
    return n.rawUrl = n.url, i && !n.type && (n.type = cc.path.extname(i).toLowerCase().substr(1)), n
   }
   var _ = [];

   function d(t, e, i) {
    if (!t || !e) return !1;
    var n = !1;
    if (_.push(e.id), e.deps) {
     var r, s, a = e.deps;
     for (r = 0; r < a.length; r++) {
      if ((s = a[r]).id === t.id) {
       n = !0;
       break
      }
      if (!(_.indexOf(s.id) >= 0) && (s.deps && d(t, s, !0))) {
       n = !0;
       break
      }
     }
    }
    return i || (_.length = 0), n
   }
   var f = function(t, e, i, o) {
    n.call(this), this._id = ++s, a[this._id] = this, this._pipeline = t, this._errorUrls = r.createMap(!0), this._appending = !1, this._ownerQueue = null, this.onProgress = i, this.onComplete = o, this.map = r.createMap(!0), this.completed = {}, this.totalCount = 0, this.completedCount = 0, this._pipeline ? this.active = !0 : this.active = !1, e && (e.length > 0 ? this.append(e) : this.allComplete())
   };
   f.ItemState = new cc.Enum(c), f.create = function(t, e, i, n) {
    void 0 === i ? "function" == typeof e && (n = e, e = i = null) : void 0 === n && ("function" == typeof e ? (n = i, i = e, e = null) : (n = i, i = null));
    var r = o.pop();
    return r ? (r._pipeline = t, r.onProgress = i, r.onComplete = n, a[r._id] = r, r._pipeline && (r.active = !0), e && r.append(e)) : r = new f(t, e, i, n), r
   }, f.getQueue = function(t) {
    return t.queueId ? a[t.queueId] : null
   }, f.itemComplete = function(t) {
    var e = a[t.queueId];
    e && e.itemComplete(t.id)
   }, f.initQueueDeps = function(t) {
    var e = h[t._id];
    e ? (e.completed.length = 0, e.deps.length = 0) : e = h[t._id] = {
     completed: [],
     deps: []
    }
   }, f.registerQueueDep = function(t, e) {
    var i = t.queueId || t;
    if (!i) return !1;
    var n = h[i];
    if (n) - 1 === n.deps.indexOf(e) && n.deps.push(e);
    else if (t.id)
     for (var r in h) {
      var s = h[r]; - 1 !== s.deps.indexOf(t.id) && -1 === s.deps.indexOf(e) && s.deps.push(e)
     }
   }, f.finishDep = function(t) {
    for (var e in h) {
     var i = h[e]; - 1 !== i.deps.indexOf(t) && -1 === i.completed.indexOf(t) && i.completed.push(t)
    }
   };
   var p = f.prototype;
   r.mixin(p, n.prototype), p.append = function(t, e) {
    if (!this.active) return [];
    e && !e.deps && (e.deps = []), this._appending = !0;
    var i, n, r, s = [];
    for (i = 0; i < t.length; ++i)
     if (!(n = t[i]).queueId || this.map[n.id]) {
      if (u(n)) {
       var o = (r = l(n, this._id)).id;
       this.map[o] || (this.map[o] = r, this.totalCount++, e && e.deps.push(r), f.registerQueueDep(e || this._id, o), s.push(r))
      }
     } else {
      if (this.map[n.id] = n, e && e.deps.push(n), n.complete || d(e, n)) {
       this.totalCount++, this.itemComplete(n.id);
       continue
      }
      var c = this,
       h = a[n.queueId];
      h && (this.totalCount++, f.registerQueueDep(e || this._id, n.id), h.addListener(n.id, (function(t) {
       c.itemComplete(t.id)
      })))
     } return this._appending = !1, this.completedCount === this.totalCount ? this.allComplete() : this._pipeline.flowIn(s), s
   }, p._childOnProgress = function(t) {
    if (this.onProgress) {
     var e = h[this._id];
     this.onProgress(e ? e.completed.length : this.completedCount, e ? e.deps.length : this.totalCount, t)
    }
   }, p.allComplete = function() {
    var t = r.isEmptyObject(this._errorUrls) ? null : this._errorUrls;
    this.onComplete && this.onComplete(t, this)
   }, p.isCompleted = function() {
    return this.completedCount >= this.totalCount
   }, p.isItemCompleted = function(t) {
    return !!this.completed[t]
   }, p.exists = function(t) {
    return !!this.map[t]
   }, p.getContent = function(t) {
    var e = this.map[t],
     i = null;
    return e && (e.content ? i = e.content : e.alias && (i = e.alias.content)), i
   }, p.getError = function(t) {
    var e = this.map[t],
     i = null;
    return e && (e.error ? i = e.error : e.alias && (i = e.alias.error)), i
   }, p.addListener = n.prototype.add, p.hasListener = n.prototype.has, p.removeListener = n.prototype.remove, p.removeAllListeners = n.prototype.removeAll, p.removeItem = function(t) {
    var e = this.map[t];
    e && this.completed[e.alias || t] && (delete this.completed[t], delete this.map[t], e.alias && (delete this.completed[e.alias.id], delete this.map[e.alias.id]), this.completedCount--, this.totalCount--)
   }, p.itemComplete = function(t) {
    var e = this.map[t];
    if (e) {
     var i = t in this._errorUrls;
     if (e.error instanceof Error || r.isString(e.error) ? this._errorUrls[t] = e.error : e.error ? r.mixin(this._errorUrls, e.error) : !e.error && i && delete this._errorUrls[t], this.completed[t] = e, this.completedCount++, f.finishDep(e.id), this.onProgress) {
      var n = h[this._id];
      this.onProgress(n ? n.completed.length : this.completedCount, n ? n.deps.length : this.totalCount, e)
     }
     this.invoke(t, e), this.removeAll(t), !this._appending && this.completedCount >= this.totalCount && this.allComplete()
    }
   }, p.destroy = function() {
    this.active = !1, this._appending = !1, this._pipeline = null, this._ownerQueue = null, r.clear(this._errorUrls), this.onProgress = null, this.onComplete = null, this.map = r.createMap(!0), this.completed = {}, this.totalCount = 0, this.completedCount = 0, n.call(this), h[this._id] && (h[this._id].completed.length = 0, h[this._id].deps.length = 0), delete a[this._id], delete h[this._id], -1 === o.indexOf(this) && o.length < 10 && o.push(this)
   }, cc.LoadingItems = e.exports = f
  }), {
   "../platform/callbacks-invoker": 121,
   "../platform/js": 128,
   "../utils/CCPath": 178
  }],
  97: [(function(t, e, i) {
   var n = t("./pipeline"),
    r = "MD5Pipe",
    s = /(\.[^.\n\\/]*)$/,
    a = /.*[/\\][0-9a-fA-F]{2}[/\\]([0-9a-fA-F-]{8,})/;
   var o = function(t, e, i) {
    this.id = r, this.async = !1, this.pipeline = null, this.md5AssetsMap = t, this.md5NativeAssetsMap = e, this.libraryBase = i
   };
   o.ID = r, o.prototype.handle = function(t) {
    var e = !1;
    return "ttf" === t.type && (e = !0), t.url = this.transformURL(t.url, e), null
   }, o.prototype.transformURL = function(t, e) {
    var i, n, r, o, c = this,
     h = (function(t) {
      var e = t.match(a);
      return e ? e[1] : ""
     })(t);
    h && (function() {
     i = !t.startsWith(c.libraryBase);
     var a = (i ? c.md5NativeAssetsMap : c.md5AssetsMap)[h];
     a && (e ? (n = cc.path.dirname(t), r = cc.path.basename(t), t = n + "." + a + "/" + r) : (o = !1, t = t.replace(s, (function(t, e) {
      return o = !0, "." + a + e
     })), o || (t = t + "." + a)))
    })();
    return t
   }, n.MD5Pipe = e.exports = o
  }), {
   "./pipeline": 99
  }],
  98: [(function(t, e, i) {
   var n = t("./unpackers"),
    r = t("../utils/misc").pushToMap,
    s = {
     Invalid: 0,
     Removed: 1,
     Downloading: 2,
     Loaded: 3
    };

   function a() {
    this.unpacker = null, this.state = s.Invalid
   }
   var o = {},
    c = {},
    h = {};

   function u(t, e) {
    return new Error("Can not retrieve " + t + " from packer " + e)
   }
   e.exports = {
    initPacks: function(t) {
     for (var e in c = t, t)
      for (var i = t[e], n = 0; n < i.length; n++) {
       var s = i[n],
        a = 1 === i.length;
       r(o, s, e, a)
      }
    },
    _loadNewPack: function(t, e, i) {
     var n = this,
      r = cc.AssetLibrary.getLibUrlNoExt(e) + ".json";
     cc.loader.load({
      url: r,
      ignoreMaxConcurrency: !0
     }, (function(r, s) {
      if (r) return cc.errorID(4916, t), i(r);
      var a = n._doLoadNewPack(t, e, s);
      a ? i(null, a) : i(u(t, e))
     }))
    },
    _doPreload: function(t, e) {
     var i = h[t];
     i || ((i = h[t] = new a).state = s.Downloading), i.state !== s.Loaded && (i.unpacker = new n.JsonUnpacker, i.unpacker.load(c[t], e), i.state = s.Loaded)
    },
    _doLoadNewPack: function(t, e, i) {
     var r = h[e];
     return r.state !== s.Loaded && ("string" == typeof i && (i = JSON.parse(i)), Array.isArray(i) ? r.unpacker = new n.JsonUnpacker : i.type === n.TextureUnpacker.ID && (r.unpacker = new n.TextureUnpacker), r.unpacker.load(c[e], i), r.state = s.Loaded), r.unpacker.retrieve(t)
    },
    _selectLoadedPack: function(t) {
     for (var e = s.Invalid, i = "", n = 0; n < t.length; n++) {
      var r = t[n],
       a = h[r];
      if (a) {
       var o = a.state;
       if (o === s.Loaded) return r;
       o > e && (e = o, i = r)
      }
     }
     return e !== s.Invalid ? i : t[0]
    },
    load: function(t, e) {
     var i = t.uuid,
      n = o[i];
     if (n) {
      Array.isArray(n) && (n = this._selectLoadedPack(n));
      var r = h[n];
      if (r && r.state === s.Loaded) {
       var c = r.unpacker.retrieve(i);
       return c || u(i, n)
      }
      return r || (console.log("Create unpacker %s for %s", n, i), (r = h[n] = new a).state = s.Downloading), this._loadNewPack(i, n, e), null
     }
    }
   }
  }), {
   "../utils/misc": 186,
   "./unpackers": 102
  }],
  99: [(function(t, e, i) {
   var n = t("../platform/js"),
    r = t("./loading-items"),
    s = r.ItemState;

   function a(t, e) {
    var i = t.id,
     n = e.states[i],
     r = t.next,
     o = t.pipeline;
    if (!e.error && n !== s.WORKING && n !== s.ERROR)
     if (n === s.COMPLETE) r ? a(r, e) : o.flowOut(e);
     else {
      e.states[i] = s.WORKING;
      var c = t.handle(e, (function(t, n) {
       t ? (e.error = t, e.states[i] = s.ERROR, o.flowOut(e)) : (n && (e.content = n), e.states[i] = s.COMPLETE, r ? a(r, e) : o.flowOut(e))
      }));
      c instanceof Error ? (e.error = c, e.states[i] = s.ERROR, o.flowOut(e)) : void 0 !== c && (null !== c && (e.content = c), e.states[i] = s.COMPLETE, r ? a(r, e) : o.flowOut(e))
     }
   }
   var o = function(t) {
    this._pipes = t, this._cache = n.createMap(!0);
    for (var e = 0; e < t.length; ++e) {
     var i = t[e];
     i.handle && i.id && (i.pipeline = this, i.next = e < t.length - 1 ? t[e + 1] : null)
    }
   };
   o.ItemState = s;
   var c = o.prototype;
   c.insertPipe = function(t, e) {
    if (!t.handle || !t.id || e > this._pipes.length) cc.warnID(4921);
    else if (this._pipes.indexOf(t) > 0) cc.warnID(4922);
    else {
     t.pipeline = this;
     var i = null;
     e < this._pipes.length && (i = this._pipes[e]);
     var n = null;
     e > 0 && (n = this._pipes[e - 1]), n && (n.next = t), t.next = i, this._pipes.splice(e, 0, t)
    }
   }, c.insertPipeAfter = function(t, e) {
    var i = this._pipes.indexOf(t);
    i < 0 || this.insertPipe(e, i + 1)
   }, c.appendPipe = function(t) {
    t.handle && t.id && (t.pipeline = this, t.next = null, this._pipes.length > 0 && (this._pipes[this._pipes.length - 1].next = t), this._pipes.push(t))
   }, c.flowIn = function(t) {
    var e, i, n = this._pipes[0];
    if (n) {
     for (e = 0; e < t.length; e++) i = t[e], this._cache[i.id] = i;
     for (e = 0; e < t.length; e++) a(n, i = t[e])
    } else
     for (e = 0; e < t.length; e++) this.flowOut(t[e])
   }, c.flowInDeps = function(t, e, i) {
    return r.create(this, (function(t, e) {
     i(t, e), e.destroy()
    })).append(e, t)
   }, c.flowOut = function(t) {
    t.error ? delete this._cache[t.id] : this._cache[t.id] || (this._cache[t.id] = t), t.complete = !0, r.itemComplete(t)
   }, c.copyItemStates = function(t, e) {
    if (e instanceof Array)
     for (var i = 0; i < e.length; ++i) e[i].states = t.states;
    else e.states = t.states
   }, c.getItem = function(t) {
    var e = this._cache[t];
    return e ? (e.alias && (e = e.alias), e) : e
   }, c.removeItem = function(t) {
    var e = this._cache[t];
    return e && e.complete && delete this._cache[t], e
   }, c.clear = function() {
    for (var t in this._cache) {
     var e = this._cache[t];
     delete this._cache[t], e.complete || (e.error = new Error("Canceled manually"), this.flowOut(e))
    }
   }, cc.Pipeline = e.exports = o
  }), {
   "../platform/js": 128,
   "./loading-items": 96
  }],
  100: [(function(t, e, i) {}), {
   "../platform/js": 128
  }],
  101: [(function(t, e, i) {
   var n = t("./utils").urlAppendTimestamp;
   e.exports = function(t, e) {
    var i = t.url;
    i = n(i);
    var r = cc.loader.getXMLHttpRequest(),
     s = "Load text file failed: " + i;
    r.open("GET", i, !0), r.overrideMimeType && r.overrideMimeType("text/plain; charset=utf-8"), r.onload = function() {
     4 === r.readyState ? 200 === r.status || 0 === r.status ? e(null, r.responseText) : e({
      status: r.status,
      errorMessage: s + "(wrong status)"
     }) : e({
      status: r.status,
      errorMessage: s + "(wrong readyState)"
     })
    }, r.onerror = function() {
     e({
      status: r.status,
      errorMessage: s + "(error)"
     })
    }, r.ontimeout = function() {
     e({
      status: r.status,
      errorMessage: s + "(time out)"
     })
    }, r.send(null)
   }
  }), {
   "./utils": 103
  }],
  102: [(function(t, e, i) {
   var n = t("../assets/CCTexture2D"),
    r = t("../platform/js");

   function s() {
    this.jsons = {}
   }

   function a() {
    this.contents = {}
   }
   s.prototype.load = function(t, e) {
    e.length !== t.length && cc.errorID(4915);
    for (var i = 0; i < t.length; i++) {
     var n = t[i],
      r = e[i];
     this.jsons[n] = r
    }
   }, s.prototype.retrieve = function(t) {
    return this.jsons[t] || null
   }, a.ID = r._getClassId(n), a.prototype.load = function(t, e) {
    var i = e.data.split("|");
    i.length !== t.length && cc.errorID(4915);
    for (var n = 0; n < t.length; n++) this.contents[t[n]] = i[n]
   }, a.prototype.retrieve = function(t) {
    var e = this.contents[t];
    return e ? {
     __type__: a.ID,
     content: e
    } : null
   }, e.exports = {
    JsonUnpacker: s,
    TextureUnpacker: a
   }
  }), {
   "../assets/CCTexture2D": 44,
   "../platform/js": 128
  }],
  103: [(function(t, e, i) {
   var n = /\?/;
   e.exports = {
    urlAppendTimestamp: function(t) {
     return cc.game.config.noCache && "string" == typeof t && (n.test(t) ? t += "&_t=" + (new Date - 0) : t += "?_t=" + (new Date - 0)), t
    }
   }
  }), {}],
  104: [(function(t, e, i) {
   var n = t("../platform/js"),
    r = t("../CCDebug");
   t("../platform/deserialize");
   var s = t("./loading-items");

   function a(t) {
    return t && (t[0] && "cc.Scene" === t[0].__type__ || t[1] && "cc.Scene" === t[1].__type__ || t[0] && "cc.Prefab" === t[0].__type__)
   }

   function o(t, e) {
    var i, o;
    if ("string" == typeof t.content) try {
     i = JSON.parse(t.content)
    } catch (e) {
     return new Error(r.getError(4923, t.id, e.stack))
    } else {
     if ("object" != typeof t.content) return new Error(r.getError(4924));
     i = t.content
    }
    var c = a(i);
    o = c ? cc._MissingScript.safeFindClass : function(t) {
     var e = n._getClassById(t);
     return e || (cc.warnID(4903, t), Object)
    };
    var h, u = cc.deserialize.Details.pool.get();
    try {
     h = cc.deserialize(i, u, {
      classFinder: o,
      target: t.existingAsset,
      customEnv: t
     })
    } catch (e) {
     cc.deserialize.Details.pool.put(u);
     var l = e.stack;
     return new Error(r.getError(4925, t.id, l))
    }
    h._uuid = t.uuid;
    var _ = (function(t, e, i, n) {
     var r, s, a, o = i.uuidList,
      c = i.uuidObjList,
      h = i.uuidPropList,
      u = i._stillUseUrl,
      l = t.dependKeys = [];
     if (n)
      for (r = [], s = 0; s < o.length; s++) {
       a = o[s];
       var _ = c[s],
        d = h[s],
        f = cc.AssetLibrary._getAssetInfoInRuntime(a);
       if (f.raw) {
        var p = f.url;
        _[d] = p, l.push(p)
       } else r.push({
        type: "uuid",
        uuid: a,
        deferredLoadRaw: !0,
        _owner: _,
        _ownerProp: d,
        _stillUseUrl: u[s]
       })
      } else {
       for (r = new Array(o.length), s = 0; s < o.length; s++) a = o[s], r[s] = {
        type: "uuid",
        uuid: a,
        _owner: c[s],
        _ownerProp: h[s],
        _stillUseUrl: u[s]
       };
       e._native && !e.constructor.preventPreloadNativeObject && r.push({
        url: e.nativeUrl,
        _owner: e,
        _ownerProp: "_nativeAsset"
       })
      }
     return r
    })(t, h, u, (function(t, e, i) {
     var n = e.deferredLoadRaw;
     return n ? t instanceof cc.Asset && t.constructor.preventDeferredLoadDependents && (n = !1) : i && (t instanceof cc.SceneAsset || t instanceof cc.Prefab) && (n = t.asyncLoadAssets), n
    })(h, t, c));
    if (cc.deserialize.Details.pool.put(u), 0 === _.length) return e(null, h);
    (function(t, e, i, n, r) {
     e.content = i;
     var a = e.dependKeys;
     t.flowInDeps(e, n, (function(t, e) {
      var o, c = e.map;
      for (var h in c)(o = c[h]).uuid && o.content && (o.content._uuid = o.uuid);
      for (var u = 0; u < n.length; u++) {
       var l = n[u],
        _ = l.uuid,
        d = l.url;
       if (l._owner, l._ownerProp, o = c[d]) {
        var f = l;
        if (o.complete || o.content) o.error ? cc._throw(o.error) : v.call(f, o);
        else {
         var p = s.getQueue(o),
          m = p._callbackTable[_];
         m ? m.unshift(v, f) : p.addListener(_, v, f)
        }
       }

       function v(t) {
        var e = t.content;
        this._stillUseUrl && (e = e && cc.RawAsset.wasRawAssetType(e.constructor) ? e.nativeUrl : t.rawUrl), this._owner[this._ownerProp] = e, t.uuid !== i._uuid && a.indexOf(t.id) < 0 && a.push(t.id)
       }
      }
      r(t, i)
     }))
    })(this.pipeline, t, h, _, e)
   }
   e.exports = o, o.isSceneObj = a
  }), {
   "../CCDebug": 21,
   "../platform/deserialize": 123,
   "../platform/js": 128,
   "./loading-items": 96
  }],
  105: [(function(t, e, i) {
   var n = t("./component-scheduler"),
    r = t("./platform/CCObject").Flags,
    s = t("./platform/js"),
    a = r.IsPreloadStarted,
    o = r.IsOnLoadStarted,
    c = r.IsOnLoadCalled,
    h = r.Deactivating,
    u = function(t) {
     t.__preload()
    },
    l = function(t) {
     t.onLoad(), t._objFlags |= c
    },
    _ = cc.Class({
     extends: n.LifeCycleInvoker,
     add: function(t) {
      this._zero.array.push(t)
     },
     remove: function(t) {
      this._zero.fastRemove(t)
     },
     cancelInactive: function(t) {
      n.LifeCycleInvoker.stableRemoveInactive(this._zero, t)
     },
     invoke: function() {
      this._invoke(this._zero), this._zero.array.length = 0
     }
    }),
    d = n.createInvokeImpl(u),
    f = n.createInvokeImpl(l),
    p = new s.Pool(4);

   function m(t, e, i) {
    e ? t._removeComponent(e) : s.array.removeAt(t._components, i)
   }

   function v() {
    this._activatingStack = []
   }
   p.get = function() {
    var t = this._get() || {
     preload: new _(d),
     onLoad: new n.OneOffInvoker(f),
     onEnable: new n.OneOffInvoker(n.invokeOnEnable)
    };
    t.preload._zero.i = -1;
    var e = t.onLoad;
    return e._zero.i = -1, e._neg.i = -1, e._pos.i = -1, (e = t.onEnable)._zero.i = -1, e._neg.i = -1, e._pos.i = -1, t
   };
   var g = cc.Class({
    ctor: v,
    reset: v,
    _activateNodeRecursively: function(t, e, i, n) {
     if (t._objFlags & h) cc.errorID(3816, t.name);
     else {
      t._activeInHierarchy = !0;
      for (var r = t._components.length, s = 0; s < r; ++s) {
       var a = t._components[s];
       a instanceof cc.Component ? this.activateComp(a, e, i, n) : (m(t, a, s), --s, --r)
      }
      for (var o = 0, c = t._children.length; o < c; ++o) {
       var u = t._children[o];
       u._active && this._activateNodeRecursively(u, e, i, n)
      }
      t._onPostActivated(!0)
     }
    },
    _deactivateNodeRecursively: function(t) {
     t._objFlags |= h, t._activeInHierarchy = !1;
     for (var e = t._components.length, i = 0; i < e; ++i) {
      var n = t._components[i];
      if (n._enabled && (cc.director._compScheduler.disableComp(n), t._activeInHierarchy)) return void(t._objFlags &= ~h)
     }
     for (var r = 0, s = t._children.length; r < s; ++r) {
      var a = t._children[r];
      if (a._activeInHierarchy && (this._deactivateNodeRecursively(a), t._activeInHierarchy)) return void(t._objFlags &= ~h)
     }
     t._onPostActivated(!1), t._objFlags &= ~h
    },
    activateNode: function(t, e) {
     if (e) {
      var i = p.get();
      this._activatingStack.push(i), this._activateNodeRecursively(t, i.preload, i.onLoad, i.onEnable), i.preload.invoke(), i.onLoad.invoke(), i.onEnable.invoke(), this._activatingStack.pop(), p.put(i)
     } else {
      this._deactivateNodeRecursively(t);
      for (var n = this._activatingStack, r = 0; r < n.length; r++) {
       var s = n[r];
       s.preload.cancelInactive(a), s.onLoad.cancelInactive(o), s.onEnable.cancelInactive()
      }
     }
     t.emit("active-in-hierarchy-changed", t)
    },
    activateComp: function(t, e, i, n) {
     if (cc.isValid(t, !0) && (t._objFlags & a || (t._objFlags |= a, t.__preload && (e ? e.add(t) : t.__preload())), t._objFlags & o || (t._objFlags |= o, t.onLoad ? i ? i.add(t) : (t.onLoad(), t._objFlags |= c) : t._objFlags |= c), t._enabled)) {
      if (!t.node._activeInHierarchy) return;
      cc.director._compScheduler.enableComp(t, n)
     }
    },
    destroyComp: function(t) {
     cc.director._compScheduler.disableComp(t), t.onDestroy && t._objFlags & c && t.onDestroy()
    },
    resetComp: !1
   });
   e.exports = g
  }), {
   "./component-scheduler": 48,
   "./platform/CCObject": 114,
   "./platform/js": 128,
   "./utils/misc": 186
  }],
  106: [(function(t, e, i) {}), {
   "../event-manager": 75,
   "../platform/js": 128,
   "./CCMacro": 113,
   "./CCSys": 117
  }],
  107: [(function(t, e, i) {
   t("../assets/CCAsset");
   var n = t("./utils").callInNextTick,
    r = t("../load-pipeline/CCLoader"),
    s = t("../load-pipeline/pack-downloader"),
    a = t("../load-pipeline/auto-release-utils"),
    o = t("../utils/decode-uuid"),
    c = t("../load-pipeline/md5-pipe"),
    h = t("./js"),
    u = "",
    l = "",
    _ = h.createMap(!0);

   function d(t) {
    return t && (t.constructor === cc.SceneAsset || t instanceof cc.Scene)
   }

   function f(t, e) {
    this.url = t, this.type = e
   }
   var p = {
    loadAsset: function(t, e, i) {
     if ("string" != typeof t) return n(e, new Error("[AssetLibrary] uuid must be string"), null);
     var s = {
      uuid: t,
      type: "uuid"
     };
     i && i.existingAsset && (s.existingAsset = i.existingAsset), r.load(s, (function(i, n) {
      if (i || !n) i = new Error("[AssetLibrary] loading JSON or dependencies failed: " + (i ? i.message : "Unknown error"));
      else {
       if (n.constructor === cc.SceneAsset) {
        var s = cc.loader._getReferenceKey(t);
        n.scene.dependAssets = a.getDependsRecursively(s)
       }
       if (d(n)) {
        var o = cc.loader._getReferenceKey(t);
        r.removeItem(o)
       }
      }
      e && e(i, n)
     }))
    },
    getLibUrlNoExt: function(t, e) {
     return t = o(t), (e ? l + "assets/" : u) + t.slice(0, 2) + "/" + t
    },
    _queryAssetInfoInEditor: function(t, e) {
     0
    },
    _getAssetInfoInRuntime: function(t, e) {
     e = e || {
      url: null,
      raw: !1
     };
     var i = _[t];
     return i && !h.isChildClassOf(i.type, cc.Asset) ? (e.url = l + i.url, e.raw = !0) : (e.url = this.getLibUrlNoExt(t) + ".json", e.raw = !1), e
    },
    _uuidInSettings: function(t) {
     return t in _
    },
    queryAssetInfo: function(t, e) {
     var i = this._getAssetInfoInRuntime(t);
     e(null, i.url, i.raw)
    },
    parseUuidInEditor: function(t) {},
    loadJson: function(t, e) {
     var i = "" + ((new Date).getTime() + Math.random()),
      n = {
       uuid: i,
       type: "uuid",
       content: t,
       skips: [r.assetLoader.id, r.downloader.id]
      };
     r.load(n, (function(t, n) {
      if (t) t = new Error("[AssetLibrary] loading JSON or dependencies failed: " + t.message);
      else {
       if (n.constructor === cc.SceneAsset) {
        var s = cc.loader._getReferenceKey(i);
        n.scene.dependAssets = a.getDependsRecursively(s)
       }
       if (d(n)) {
        var o = cc.loader._getReferenceKey(i);
        r.removeItem(o)
       }
      }
      n._uuid = "", e && e(t, n)
     }))
    },
    getAssetByUuid: function(t) {
     return p._uuidToAsset[t] || null
    },
    init: function(t) {
     var e = t.libraryPath;
     e = e.replace(/\\/g, "/"), u = cc.path.stripSep(e) + "/", l = t.rawAssetsBase;
     var i = t.md5AssetsMap;
     if (i && i.import) {
      var n = 0,
       a = 0,
       d = h.createMap(!0),
       p = i.import;
      for (n = 0; n < p.length; n += 2) d[a = o(p[n])] = p[n + 1];
      var m = h.createMap(!0);
      for (p = i["raw-assets"], n = 0; n < p.length; n += 2) m[a = o(p[n])] = p[n + 1];
      var v = new c(d, m, u);
      window.__MD5Pipe = v;
      cc.loader.insertPipeAfter(cc.loader.assetLoader, v), cc.loader.md5Pipe = v
     }
     var g = r._resources;
     g.reset();
     var y = t.rawAssets;
     if (y)
      for (var T in y) {
       var E = y[T];
       for (var a in E) {
        var x = E[a],
         C = x[0],
         A = x[1],
         b = cc.js._getClassById(A);
        if (b) {
         if (_[a] = new f(T + "/" + C, b), "assets" === T) {
          var S = cc.path.extname(C);
          S && (C = C.slice(0, -S.length));
          var w = 1 === x[2];
          g.add(C, a, b, !w)
         }
        } else cc.error("Cannot get", A)
       }
      }
     t.packedAssets && s.initPacks(t.packedAssets), cc.url._init(t.mountPaths && t.mountPaths.assets || l + "assets")
    },
    _uuidToAsset: {}
   };
   e.exports = cc.AssetLibrary = p
  }), {
   "../assets/CCAsset": 28,
   "../load-pipeline/CCLoader": 86,
   "../load-pipeline/auto-release-utils": 90,
   "../load-pipeline/md5-pipe": 97,
   "../load-pipeline/pack-downloader": 98,
   "../utils/decode-uuid": 182,
   "./js": 128,
   "./utils": 132
  }],
  108: [(function(t, e, i) {
   var n = t("./js"),
    r = t("./CCEnum"),
    s = t("./utils"),
    a = (s.isPlainEmptyObj_DEV, s.cloneable_DEV, t("./attribute")),
    o = a.DELIMETER,
    c = a.getTypeChecker,
    h = t("./preprocess-class");
   t("./requiring-frame");
   var u = ["name", "extends", "mixins", "ctor", "__ctor__", "properties", "statics", "editor", "__ES6__"];

   function l(t, e) {
    t.indexOf(e) < 0 && t.push(e)
   }
   var _ = {
    datas: null,
    push: function(t) {
     if (this.datas) this.datas.push(t);
     else {
      this.datas = [t];
      var e = this;
      setTimeout((function() {
       e.init()
      }), 0)
     }
    },
    init: function() {
     var t = this.datas;
     if (t) {
      for (var e = 0; e < t.length; ++e) {
       var i = t[e],
        r = i.cls,
        s = i.props;
       "function" == typeof s && (s = s());
       var a = n.getClassName(r);
       s ? S(r, a, s, r.$super, i.mixins) : cc.errorID(3633, a)
      }
      this.datas = null
     }
    }
   };

   function d(t, e) {
    l(t.__props__, e)
   }
   var f = [];

   function p(t, e, i, n, r) {
    var s = n.default;
    a.setClassAttr(t, i, "default", s), d(t, i);
    var o = M(t, n, e, i, !1);
    if (o) {
     for (var c = f, h = 0; h < o.length; h++) {
      var u = o[h];
      a.attr(t, i, u), u._onAfterProp && c.push(u._onAfterProp)
     }
     for (var l = 0; l < c.length; l++) c[l](t, i);
     f.length = 0, o.length = 0
    }
   }

   function m(t, e, i, r, s) {
    var o = r.get,
     c = r.set,
     h = t.prototype,
     u = Object.getOwnPropertyDescriptor(h, i),
     l = !u;
    if (o) {
     0;
     for (var _ = M(t, r, e, i, !0), d = 0; d < _.length; d++) a.attr(t, i, _[d]);
     _.length = 0, a.setClassAttr(t, i, "serializable", !1), s || n.get(h, i, o, l, l)
    }
    c && (s || n.set(h, i, c, l, l))
   }

   function v(t) {
    return "function" == typeof t ? t() : t
   }

   function g(t, e, i) {
    for (var r in e) t.hasOwnProperty(r) || i && !i(r) || Object.defineProperty(t, r, n.getPropertyDescriptor(e, r))
   }

   function y(t, e, i, r) {
    var s, o, c = r.__ctor__,
     h = r.ctor,
     u = r.__ES6__;
    u ? (s = [h], o = h) : (s = c ? [c] : (function(t, e, i) {
     function n(t) {
      return w._isCCClass(t) ? t.__ctors__ || [] : [t]
     }
     for (var r = [], s = [t].concat(e), a = 0; a < s.length; a++) {
      var o = s[a];
      if (o)
       for (var c = n(o), h = 0; h < c.length; h++) l(r, c[h])
     }
     var u = i.ctor;
     u && r.push(u);
     return r
    })(e, i, r), o = C(s, e, t, r), n.value(o, "extend", (function(t) {
     return t.extends = this, w(t)
    }), !0)), n.value(o, "__ctors__", s.length > 0 ? s : null, !0);
    var _ = o.prototype;
    if (e && (u || (n.extend(o, e), _ = o.prototype), o.$super = e), i) {
     for (var d = i.length - 1; d >= 0; d--) {
      var f = i[d];
      g(_, f.prototype), g(o, f, (function(t) {
       return f.hasOwnProperty(t) && !0
      })), w._isCCClass(f) && g(a.getClassAttrs(o).constructor.prototype, a.getClassAttrs(f).constructor.prototype)
     }
     _.constructor = o
    }
    return u || (_.__initProps__ = x), n.setClassName(t, o), o
   }

   function T(t) {
    return JSON.stringify(t).replace(/\u2028/g, "\\u2028").replace(/\u2029/g, "\\u2029")
   }
   var E = /^[A-Za-z_$][0-9A-Za-z_$]*$/;

   function x(t) {
    var e = a.getClassAttrs(t),
     i = t.__props__;
    null === i && (_.init(), i = t.__props__);
    var n = (function(t, e) {
     for (var i = [], n = [], r = [], s = [], a = 0; a < e.length; ++a) {
      var c = e[a],
       h = c + o + "default";
      if (h in t) {
       var u = t[h];
       "object" == typeof u && u || "function" == typeof u ? (i.push(c), n.push(u)) : (r.push(c), s.push(u))
      }
     }
     return function() {
      for (var t = 0; t < r.length; ++t) this[r[t]] = s[t];
      for (var e = 0; e < i.length; e++) {
       var a, o = i[e],
        c = n[e];
       a = "object" == typeof c ? c instanceof cc.ValueType ? c.clone() : Array.isArray(c) ? [] : {} : c(), this[o] = a
      }
     }
    })(e, i);
    t.prototype.__initProps__ = n, n.call(this)
   }
   var C = function(t, e, i, n) {
    var r, s = e && b(e, n, i),
     a = t.length;
    return r = a > 0 ? s ? 2 === a ? function() {
     this._super = null, this.__initProps__(r), t[0].apply(this, arguments), t[1].apply(this, arguments)
    } : function() {
     this._super = null, this.__initProps__(r);
     for (var e = 0; e < t.length; ++e) t[e].apply(this, arguments)
    } : 3 === a ? function() {
     this.__initProps__(r), t[0].apply(this, arguments), t[1].apply(this, arguments), t[2].apply(this, arguments)
    } : function() {
     this.__initProps__(r);
     for (var t = r.__ctors__, e = 0; e < t.length; ++e) t[e].apply(this, arguments)
    } : function() {
     s && (this._super = null), this.__initProps__(r)
    }
   };
   var A = /xyz/.test((function() {
    xyz
   })) ? /\b\._super\b/ : /.*/;
   /xyz/.test((function() {
    xyz
   }));

   function b(t, e, i) {
    var r = !1;
    for (var s in e)
     if (!(u.indexOf(s) >= 0)) {
      var a = e[s];
      if ("function" == typeof a) {
       var o = n.getPropertyDescriptor(t.prototype, s);
       if (o) {
        var c = o.value;
        if ("function" == typeof c) {
         A.test(a) && (r = !0, e[s] = (function(t, e) {
          return function() {
           var i = this._super;
           this._super = t;
           var n = e.apply(this, arguments);
           return this._super = i, n
          }
         })(c, a));
         continue
        }
       }
       0
      }
     } return r
   }

   function S(t, e, i, n, r, s) {
    if (t.__props__ = [], n && n.__props__ && (t.__props__ = n.__props__.slice()), r)
     for (var c = 0; c < r.length; ++c) {
      var u = r[c];
      u.__props__ && (t.__props__ = t.__props__.concat(u.__props__.filter((function(e) {
       return t.__props__.indexOf(e) < 0
      }))))
     }
    if (i)
     for (var l in h.preprocessAttrs(i, e, t, s), i) {
      var _ = i[l];
      "default" in _ ? p(t, e, l, _) : m(t, e, l, _, s)
     }
    var d = a.getClassAttrs(t);
    t.__values__ = t.__props__.filter((function(t) {
     return !1 !== d[t + o + "serializable"]
    }))
   }

   function w(t) {
    var e = (t = t || {}).name,
     i = t.extends,
     r = t.mixins,
     s = (function(t, e, i, r) {
      var s = cc.Component,
       a = cc._RF.peek();
      if (a && n.isChildClassOf(e, s)) {
       if (n.isChildClassOf(a.cls, s)) return cc.errorID(3615), null;
       t = t || a.script
      }
      var o = y(t, e, i, r);
      if (a)
       if (n.isChildClassOf(e, s)) {
        var c = a.uuid;
        c && n._setClassId(c, o), a.cls = o
       } else n.isChildClassOf(a.cls, s) || (a.cls = o);
      return o
     })(e, i, r, t);
    e || (e = cc.js.getClassName(s)), s._sealed = !0, i && (i._sealed = !1);
    var a = t.properties;
    "function" == typeof a || i && null === i.__props__ || r && r.some((function(t) {
     return null === t.__props__
    })) ? (_.push({
     cls: s,
     props: a,
     mixins: r
    }), s.__props__ = s.__values__ = null) : S(s, e, a, i, t.mixins, t.__ES6__);
    var o, c = t.statics;
    if (c)
     for (o in c) s[o] = c[o];
    for (var l in t)
     if (!(u.indexOf(l) >= 0)) {
      var d = t[l];
      h.validateMethodWithProps(d, l, e, s, i) && n.value(s.prototype, l, d, !0, !0)
     } var f = t.editor;
    return f && n.isChildClassOf(i, cc.Component) && cc.Component._registerEditorProps(s, f), s
   }
   w._isCCClass = function(t) {
    return t && t.hasOwnProperty("__ctors__")
   }, w._fastDefine = function(t, e, i) {
    n.setClassName(t, e);
    for (var r = e.__props__ = e.__values__ = Object.keys(i), s = a.getClassAttrsProto(e), c = 0; c < r.length; c++) {
     var h = r[c];
     s[h + o + "visible"] = !1, s[h + o + "default"] = i[h]
    }
   }, w.Attr = a, w.attr = a.attr, w.getInheritanceChain = function(t) {
    for (var e = []; t = n.getSuper(t);) t !== Object && e.push(t);
    return e
   };
   var D = {
     Integer: "Number",
     Float: "Number",
     Boolean: "Boolean",
     String: "String"
    },
    R = [];

   function M(t, e, i, n, s) {
    var h = null,
     u = "";

    function l() {
     return u = n + o, h = a.getClassAttrsProto(t)
    }
    R.length = 0;
    var _ = R,
     d = e.type;
    if (d) {
     var f = D[d];
     if (f) _.push({
      type: d,
      _onAfterProp: c(f, "cc." + d)
     });
     else if ("Object" === d) 0;
     else if (d === a.ScriptUuid) {
      var p = a.ObjectType(cc.ScriptAsset);
      p.type = "Script", _.push(p)
     } else "object" == typeof d ? r.isEnum(d) && _.push({
      type: "Enum",
      enumList: r.getList(d)
     }) : "function" == typeof d && (e.url ? _.push({
      type: "Object",
      ctor: d,
      _onAfterProp: c("String", "cc.String")
     }) : _.push(e._short ? {
      type: "Object",
      ctor: d
     } : a.ObjectType(d)))
    }

    function m(t, i) {
     if (t in e) {
      var n = e[t];
      typeof n === i && ((h || l())[u + t] = n)
     }
    }
    e.editorOnly && ((h || l())[u + "editorOnly"] = !0), e.url && ((h || l())[u + "saveUrlAsAsset"] = !0), !1 === e.serializable && ((h || l())[u + "serializable"] = !1), m("formerlySerializedAs", "string");
    var v = e.range;
    return v && Array.isArray(v) && v.length >= 2 && ((h || l())[u + "min"] = v[0], h[u + "max"] = v[1], v.length > 2 && (h[u + "step"] = v[2])), m("min", "number"), m("max", "number"), m("step", "number"), _
   }
   cc.Class = w, e.exports = {
    isArray: function(t) {
     return t = v(t), Array.isArray(t)
    },
    fastDefine: w._fastDefine,
    getNewValueTypeCode: !1,
    IDENTIFIER_RE: E,
    escapeForJS: T,
    getDefault: v
   }
  }), {
   "./CCEnum": 110,
   "./attribute": 120,
   "./js": 128,
   "./preprocess-class": 129,
   "./requiring-frame": 130,
   "./utils": 132
  }],
  109: [(function(t, e, i) {
   t("./CCClass");
   var n = t("./preprocess-class"),
    r = t("./js"),
    s = "__ccclassCache__";

   function a(t) {
    return t
   }

   function o(t, e) {
    return t[e] || (t[e] = {})
   }

   function c(t) {
    return function(e) {
     return "function" == typeof e ? t(e) : function(i) {
      return t(i, e)
     }
    }
   }

   function h(t, e, i) {
    return function(t) {
     return function(i) {
      return e(i, t)
     }
    }
   }
   var u = h.bind(null, !1);

   function l(t) {
    return h.bind(null, !1)
   }
   var _ = l(),
    d = l();

   function f(t, e) {
    return o(t, s)
   }
   var p = c((function(t, e) {
    var i = r.getSuper(t);
    i === Object && (i = null);
    var n = {
      name: e,
      extends: i,
      ctor: t,
      __ES6__: !0
     },
     a = t[s];
    if (a) {
     var o = a.proto;
     o && r.mixin(n, o), t[s] = void 0
    }
    return cc.Class(n)
   }));

   function m(t, e, i) {
    return t((function(t, n) {
     var r = f(t);
     if (r) {
      var s = void 0 !== i ? i : n;
      o(o(r, "proto"), "editor")[e] = s
     }
    }), e)
   }

   function v(t) {
    return t(a)
   }
   var g = v(c),
    y = m(u, "requireComponent"),
    T = v(_),
    E = m(d, "executionOrder"),
    x = v(c),
    C = v(c),
    A = v(_),
    b = v(_),
    S = v(_);
   cc._decorator = e.exports = {
    ccclass: p,
    property: function(t, e, i) {
     var s = null;

     function a(t, e, i) {
      var a = f(t.constructor);
      if (a) {
       var c = o(o(a, "proto"), "properties");
       (function(t, e, i, s, a, o) {
        var c;
        s && (c = (c = n.getFullFormOfProperty(s)) || s);
        var h = e[i],
         u = r.mixin(h || {}, c || {});
        if (a && (a.get || a.set)) a.get && (u.get = a.get), a.set && (u.set = a.set);
        else {
         var l = void 0;
         if (a) a.initializer && (l = (function(t) {
          var e;
          try {
           e = t()
          } catch (e) {
           return t
          }
          return "object" != typeof e || null === e ? e : t
         })(a.initializer));
         else {
          var _ = o.default || (o.default = (function(t) {
           var e;
           try {
            e = new t
           } catch (t) {
            return {}
           }
           return e
          })(t));
          _.hasOwnProperty(i) && (l = _[i])
         }
         u.default = l
        }
        e[i] = u
       })(t.constructor, c, e, s, i, a)
      }
     }
     if (void 0 === e) return s = t, a;
     a(t, e, i)
    },
    executeInEditMode: g,
    requireComponent: y,
    menu: T,
    executionOrder: E,
    disallowMultiple: x,
    playOnFocus: C,
    inspector: A,
    icon: b,
    help: S,
    mixins: function() {
     for (var t = [], e = 0; e < arguments.length; e++) t[e] = arguments[e];
     return function(e) {
      var i = f(e);
      i && (o(i, "proto").mixins = t)
     }
    }
   }
  }), {
   "./CCClass": 108,
   "./js": 128,
   "./preprocess-class": 129,
   "./utils": 132
  }],
  110: [(function(t, e, i) {
   var n = t("./js");

   function r(t) {
    if ("__enums__" in t) return t;
    n.value(t, "__enums__", null, !0);
    for (var e = -1, i = Object.keys(t), r = 0; r < i.length; r++) {
     var s = i[r],
      a = t[s];
     if (-1 === a) a = ++e, t[s] = a;
     else if ("number" == typeof a) e = a;
     else if ("string" == typeof a && Number.isInteger(parseFloat(s))) continue;
     var o = "" + a;
     s !== o && n.value(t, o, s)
    }
    return t
   }
   r.isEnum = function(t) {
    return t && t.hasOwnProperty("__enums__")
   }, r.getList = function(t) {
    if (t.__enums__) return t.__enums__;
    var e = t.__enums__ = [];
    for (var i in t) {
     var n = t[i];
     Number.isInteger(n) && e.push({
      name: i,
      value: n
     })
    }
    return e.sort((function(t, e) {
     return t.value - e.value
    })), e
   }, e.exports = cc.Enum = r
  }), {
   "./js": 128
  }],
  111: [(function(t, e, i) {
   var n = t("../event-manager"),
    r = t("./CCInputManager"),
    s = void 0;
   cc.Acceleration = function(t, e, i, n) {
    this.x = t || 0, this.y = e || 0, this.z = i || 0, this.timestamp = n || 0
   }, r.setAccelerometerEnabled = function(t) {
    var e = this;
    if (e._accelEnabled !== t) {
     e._accelEnabled = t;
     var i = cc.director.getScheduler();
     i.enableForTarget(e), e._accelEnabled ? (e._registerAccelerometerEvent(), e._accelCurTime = 0, i.scheduleUpdate(e)) : (e._unregisterAccelerometerEvent(), e._accelCurTime = 0, i.unscheduleUpdate(e))
    }
   }, r.setAccelerometerInterval = function(t) {
    this._accelInterval !== t && (this._accelInterval = t)
   }, r._registerKeyboardEvent = function() {
    cc.game.canvas.addEventListener("keydown", (function(t) {
     n.dispatchEvent(new cc.Event.EventKeyboard(t.keyCode, !0)), t.stopPropagation(), t.preventDefault()
    }), !1), cc.game.canvas.addEventListener("keyup", (function(t) {
     n.dispatchEvent(new cc.Event.EventKeyboard(t.keyCode, !1)), t.stopPropagation(), t.preventDefault()
    }), !1)
   }, r._registerAccelerometerEvent = function() {
    var t = window,
     e = this;
    e._acceleration = new cc.Acceleration, e._accelDeviceEvent = t.DeviceMotionEvent || t.DeviceOrientationEvent, cc.sys.browserType === cc.sys.BROWSER_TYPE_MOBILE_QQ && (e._accelDeviceEvent = window.DeviceOrientationEvent);
    var i = e._accelDeviceEvent === t.DeviceMotionEvent ? "devicemotion" : "deviceorientation",
     n = navigator.userAgent;
    (/Android/.test(n) || /Adr/.test(n) && cc.sys.browserType === cc.BROWSER_TYPE_UC) && (e._minus = -1), s = e.didAccelerate.bind(e), t.addEventListener(i, s, !1)
   }, r._unregisterAccelerometerEvent = function() {
    var t = window,
     e = this._accelDeviceEvent === t.DeviceMotionEvent ? "devicemotion" : "deviceorientation";
    s && t.removeEventListener(e, s, !1)
   }, r.didAccelerate = function(t) {
    var e = this,
     i = window;
    if (e._accelEnabled) {
     var n = e._acceleration,
      r = void 0,
      s = void 0,
      a = void 0;
     if (e._accelDeviceEvent === window.DeviceMotionEvent) {
      var o = t.accelerationIncludingGravity;
      r = e._accelMinus * o.x * .1, s = e._accelMinus * o.y * .1, a = .1 * o.z
     } else r = t.gamma / 90 * .981, s = -t.beta / 90 * .981, a = t.alpha / 90 * .981;
     if (cc.view._isRotated) {
      var c = r;
      r = -s, s = c
     }
     n.x = r, n.y = s, n.z = a, n.timestamp = t.timeStamp || Date.now();
     var h = n.x;
     90 === i.orientation ? (n.x = -n.y, n.y = h) : -90 === i.orientation ? (n.x = n.y, n.y = -h) : 180 === i.orientation && (n.x = -n.x, n.y = -n.y), cc.sys.os === cc.sys.OS_ANDROID && cc.sys.browserType !== cc.sys.BROWSER_TYPE_MOBILE_QQ && (n.x = -n.x, n.y = -n.y)
    }
   }
  }), {
   "../event-manager": 75,
   "./CCInputManager": 112
  }],
  112: [(function(t, e, i) {
   var n = t("./CCMacro"),
    r = t("./CCSys"),
    s = t("../event-manager"),
    a = n.TOUCH_TIMEOUT,
    o = cc.v2(),
    c = {
     _mousePressed: !1,
     _isRegisterEvent: !1,
     _preTouchPoint: cc.v2(0, 0),
     _prevMousePoint: cc.v2(0, 0),
     _preTouchPool: [],
     _preTouchPoolPointer: 0,
     _touches: [],
     _touchesIntegerDict: {},
     _indexBitsUsed: 0,
     _maxTouches: 8,
     _accelEnabled: !1,
     _accelInterval: .2,
     _accelMinus: 1,
     _accelCurTime: 0,
     _acceleration: null,
     _accelDeviceEvent: null,
     _getUnUsedIndex: function() {
      for (var t = this._indexBitsUsed, e = cc.sys.now(), i = 0; i < this._maxTouches; i++) {
       if (!(1 & t)) return this._indexBitsUsed |= 1 << i, i;
       var n = this._touches[i];
       if (e - n._lastModified > a) return this._removeUsedIndexBit(i), delete this._touchesIntegerDict[n.getID()], i;
       t >>= 1
      }
      return -1
     },
     _removeUsedIndexBit: function(t) {
      if (!(t < 0 || t >= this._maxTouches)) {
       var e = 1 << t;
       e = ~e, this._indexBitsUsed &= e
      }
     },
     _glView: null,
     handleTouchesBegin: function(t) {
      for (var e = void 0, i = void 0, n = void 0, a = [], o = this._touchesIntegerDict, c = r.now(), h = 0, u = t.length; h < u; h++)
       if (null == o[n = (e = t[h]).getID()]) {
        var l = this._getUnUsedIndex();
        if (-1 === l) {
         cc.logID(2300, l);
         continue
        }(i = this._touches[l] = new cc.Touch(e._point.x, e._point.y, e.getID()))._lastModified = c, i._setPrevPoint(e._prevPoint), o[n] = l, a.push(i)
       } if (a.length > 0) {
       this._glView._convertTouchesWithScale(a);
       var _ = new cc.Event.EventTouch(a);
       _._eventCode = cc.Event.EventTouch.BEGAN, s.dispatchEvent(_)
      }
     },
     handleTouchesMove: function(t) {
      for (var e = void 0, i = void 0, n = void 0, a = [], o = this._touches, c = r.now(), h = 0, u = t.length; h < u; h++) n = (e = t[h]).getID(), null != (i = this._touchesIntegerDict[n]) && o[i] && (o[i]._setPoint(e._point), o[i]._setPrevPoint(e._prevPoint), o[i]._lastModified = c, a.push(o[i]));
      if (a.length > 0) {
       this._glView._convertTouchesWithScale(a);
       var l = new cc.Event.EventTouch(a);
       l._eventCode = cc.Event.EventTouch.MOVED, s.dispatchEvent(l)
      }
     },
     handleTouchesEnd: function(t) {
      var e = this.getSetOfTouchesEndOrCancel(t);
      if (e.length > 0) {
       this._glView._convertTouchesWithScale(e);
       var i = new cc.Event.EventTouch(e);
       i._eventCode = cc.Event.EventTouch.ENDED, s.dispatchEvent(i)
      }
      this._preTouchPool.length = 0
     },
     handleTouchesCancel: function(t) {
      var e = this.getSetOfTouchesEndOrCancel(t);
      if (e.length > 0) {
       this._glView._convertTouchesWithScale(e);
       var i = new cc.Event.EventTouch(e);
       i._eventCode = cc.Event.EventTouch.CANCELLED, s.dispatchEvent(i)
      }
      this._preTouchPool.length = 0
     },
     getSetOfTouchesEndOrCancel: function(t) {
      for (var e = void 0, i = void 0, n = void 0, r = [], s = this._touches, a = this._touchesIntegerDict, o = 0, c = t.length; o < c; o++) null != (i = a[n = (e = t[o]).getID()]) && s[i] && (s[i]._setPoint(e._point), s[i]._setPrevPoint(e._prevPoint), r.push(s[i]), this._removeUsedIndexBit(i), delete a[n]);
      return r
     },
     getHTMLElementPosition: function(t) {
      return {
       left: 0,
       top: 0,
       width: window.innerWidth,
       height: window.innerHeight
      }
     },
     getPreTouch: function(t) {
      for (var e = null, i = this._preTouchPool, n = t.getID(), r = i.length - 1; r >= 0; r--)
       if (i[r].getID() === n) {
        e = i[r];
        break
       } return e || (e = t), e
     },
     setPreTouch: function(t) {
      for (var e = !1, i = this._preTouchPool, n = t.getID(), r = i.length - 1; r >= 0; r--)
       if (i[r].getID() === n) {
        i[r] = t, e = !0;
        break
       } e || (i.length <= 50 ? i.push(t) : (i[this._preTouchPoolPointer] = t, this._preTouchPoolPointer = (this._preTouchPoolPointer + 1) % 50))
     },
     getTouchByXY: function(t, e, i) {
      var n = this._preTouchPoint,
       r = this._glView.convertToLocationInView(t, e, i),
       s = new cc.Touch(r.x, r.y, 0);
      return s._setPrevPoint(n.x, n.y), n.x = r.x, n.y = r.y, s
     },
     getMouseEvent: function(t, e, i) {
      var n = this._prevMousePoint,
       r = new cc.Event.EventMouse(i);
      return r._setPrevCursor(n.x, n.y), n.x = t.x, n.y = t.y, this._glView._convertMouseToLocationInView(n, e), r.setLocation(n.x, n.y), r
     },
     getPointByEvent: function(t, e) {
      return null != t.pageX ? {
       x: t.pageX,
       y: t.pageY
      } : (e.left = 0, e.top = 0, {
       x: t.clientX,
       y: t.clientY
      })
     },
     getTouchesByEvent: function(t, e) {
      for (var i = [], n = this._glView, s = void 0, a = void 0, c = void 0, h = this._preTouchPoint, u = t.changedTouches.length, l = 0; l < u; l++)
       if (s = t.changedTouches[l]) {
        var _ = void 0;
        _ = r.BROWSER_TYPE_FIREFOX === r.browserType ? n.convertToLocationInView(s.pageX, s.pageY, e, o) : n.convertToLocationInView(s.clientX, s.clientY, e, o), null != s.identifier ? (a = new cc.Touch(_.x, _.y, s.identifier), c = this.getPreTouch(a).getLocation(), a._setPrevPoint(c.x, c.y), this.setPreTouch(a)) : (a = new cc.Touch(_.x, _.y))._setPrevPoint(h.x, h.y), h.x = _.x, h.y = _.y, i.push(a)
       } return i
     },
     registerSystemEvent: function(t) {
      if (!this._isRegisterEvent) {
       this._glView = cc.view;
       var e = this,
        i = r.isMobile,
        n = (r.capabilities, "touches" in r.capabilities);
       if (i = !1, n = !0, !1) {
        i || (window.addEventListener("mousedown", (function() {
         e._mousePressed = !0
        }), !1), window.addEventListener("mouseup", (function(i) {
         if (e._mousePressed) {
          e._mousePressed = !1;
          var n = e.getHTMLElementPosition(t),
           r = e.getPointByEvent(i, n);
          if (!cc.rect(n.left, n.top, n.width, n.height).contains(r)) {
           e.handleTouchesEnd([e.getTouchByXY(r.x, r.y, n)]);
           var a = e.getMouseEvent(r, n, cc.Event.EventMouse.UP);
           a.setButton(i.button), s.dispatchEvent(a)
          }
         }
        }), !1));
        for (var a = cc.Event.EventMouse, o = [!i && ["mousedown", a.DOWN, function(i, n, r, s) {
           e._mousePressed = !0, e.handleTouchesBegin([e.getTouchByXY(r.x, r.y, s)]), t.focus()
          }], !i && ["mouseup", a.UP, function(t, i, n, r) {
           e._mousePressed = !1, e.handleTouchesEnd([e.getTouchByXY(n.x, n.y, r)])
          }], !i && ["mousemove", a.MOVE, function(t, i, n, r) {
           e.handleTouchesMove([e.getTouchByXY(n.x, n.y, r)]), e._mousePressed || i.setButton(null)
          }],
          ["mousewheel", a.SCROLL, function(t, e) {
           e.setScrollData(0, t.wheelDelta)
          }],
          ["DOMMouseScroll", a.SCROLL, function(t, e) {
           e.setScrollData(0, -120 * t.detail)
          }]
         ], c = 0; c < o.length; ++c) {
         var h = o[c];
         h && (function() {
          var i = h[0],
           n = h[1],
           r = h[2];
          t.addEventListener(i, (function(i) {
           var a = e.getHTMLElementPosition(t),
            o = e.getPointByEvent(i, a),
            c = e.getMouseEvent(o, a, n);
           c.setButton(i.button), r(i, c, o, a), s.dispatchEvent(c), i.stopPropagation(), i.preventDefault()
          }), !1)
         })()
        }
       }
       if (window.navigator.msPointerEnabled) {
        var u = {
          MSPointerDown: e.handleTouchesBegin,
          MSPointerMove: e.handleTouchesMove,
          MSPointerUp: e.handleTouchesEnd,
          MSPointerCancel: e.handleTouchesCancel
         },
         l = function(i) {
          var n = u[i];
          t.addEventListener(i, (function(i) {
           var r = e.getHTMLElementPosition(t);
           r.left -= document.documentElement.scrollLeft, r.top -= document.documentElement.scrollTop, n.call(e, [e.getTouchByXY(i.clientX, i.clientY, r)]), i.stopPropagation()
          }), !1)
         };
        for (var _ in u) l(_)
       }
       n && (function() {
        var i = {
          touchstart: function(t) {
           e.handleTouchesBegin(t)
          },
          touchmove: function(t) {
           e.handleTouchesMove(t)
          },
          touchend: function(t) {
           e.handleTouchesEnd(t)
          },
          touchcancel: function(t) {
           e.handleTouchesCancel(t)
          }
         },
         n = void 0;
        for (var r in cc.sys.browserType === cc.sys.BROWSER_TYPE_WECHAT_GAME_SUB ? (i = {
          onTouchStart: i.touchstart,
          onTouchMove: i.touchmove,
          onTouchEnd: i.touchend,
          onTouchCancel: i.touchcancel
         }, n = function(n) {
          var r = i[n];
          wx[n]((function(i) {
           if (i.changedTouches) {
            var n = e.getHTMLElementPosition(t),
             s = document.body;
            n.left -= s.scrollLeft || 0, n.top -= s.scrollTop || 0, r(e.getTouchesByEvent(i, n))
           }
          }))
         }) : n = function(n) {
          var r = i[n];
          t.addEventListener(n, (function(i) {
           if (i.changedTouches) {
            var n = e.getHTMLElementPosition(t),
             s = document.body;
            n.left -= s.scrollLeft || 0, n.top -= s.scrollTop || 0, r(e.getTouchesByEvent(i, n)), i.stopPropagation(), i.preventDefault()
           }
          }), !1)
         }, i) n(r)
       })(), cc.sys.browserType !== cc.sys.BROWSER_TYPE_WECHAT_GAME_SUB && this._registerKeyboardEvent(), this._isRegisterEvent = !0
      }
     },
     _registerKeyboardEvent: function() {},
     _registerAccelerometerEvent: function() {},
     update: function(t) {
      this._accelCurTime > this._accelInterval && (this._accelCurTime -= this._accelInterval, s.dispatchEvent(new cc.Event.EventAcceleration(this._acceleration))), this._accelCurTime += t
     }
    };
   e.exports = _cc.inputManager = c
  }), {
   "../event-manager": 75,
   "./CCMacro": 113,
   "./CCSys": 117
  }],
  113: [(function(t, e, i) {
   var n = t("./js");
   cc.macro = {
    RAD: Math.PI / 180,
    DEG: 180 / Math.PI,
    REPEAT_FOREVER: Number.MAX_VALUE - 1,
    FLT_EPSILON: 1.192092896e-7,
    MIN_ZINDEX: -Math.pow(2, 15),
    MAX_ZINDEX: Math.pow(2, 15) - 1,
    ONE: 1,
    ZERO: 0,
    SRC_ALPHA: 770,
    SRC_ALPHA_SATURATE: 776,
    SRC_COLOR: 768,
    DST_ALPHA: 772,
    DST_COLOR: 774,
    ONE_MINUS_SRC_ALPHA: 771,
    ONE_MINUS_SRC_COLOR: 769,
    ONE_MINUS_DST_ALPHA: 773,
    ONE_MINUS_DST_COLOR: 775,
    ONE_MINUS_CONSTANT_ALPHA: 32772,
    ONE_MINUS_CONSTANT_COLOR: 32770,
    ORIENTATION_PORTRAIT: 1,
    ORIENTATION_LANDSCAPE: 2,
    ORIENTATION_AUTO: 3,
    DENSITYDPI_DEVICE: "device-dpi",
    DENSITYDPI_HIGH: "high-dpi",
    DENSITYDPI_MEDIUM: "medium-dpi",
    DENSITYDPI_LOW: "low-dpi",
    FIX_ARTIFACTS_BY_STRECHING_TEXEL_TMX: !0,
    DIRECTOR_STATS_POSITION: cc.v2(0, 0),
    ENABLE_STACKABLE_ACTIONS: !0,
    TOUCH_TIMEOUT: 5e3,
    BATCH_VERTEX_COUNT: 2e4,
    ENABLE_TILEDMAP_CULLING: !0,
    DOWNLOAD_MAX_CONCURRENT: 64,
    ENABLE_TRANSPARENT_CANVAS: !1,
    ENABLE_WEBGL_ANTIALIAS: !1,
    ENABLE_CULLING: !1,
    CLEANUP_IMAGE_CACHE: !1
   }, n.getset(cc.macro, "ENABLE_3D", (function() {
    return cc._polyfill3D.enabled
   }), (function(t) {
    t ? cc._polyfill3D.enable() : cc._polyfill3D.disable()
   })), cc.macro.KEY = {
    none: 0,
    back: 6,
    menu: 18,
    backspace: 8,
    tab: 9,
    enter: 13,
    shift: 16,
    ctrl: 17,
    alt: 18,
    pause: 19,
    capslock: 20,
    escape: 27,
    space: 32,
    pageup: 33,
    pagedown: 34,
    end: 35,
    home: 36,
    left: 37,
    up: 38,
    right: 39,
    down: 40,
    select: 41,
    insert: 45,
    Delete: 46,
    0: 48,
    1: 49,
    2: 50,
    3: 51,
    4: 52,
    5: 53,
    6: 54,
    7: 55,
    8: 56,
    9: 57,
    a: 65,
    b: 66,
    c: 67,
    d: 68,
    e: 69,
    f: 70,
    g: 71,
    h: 72,
    i: 73,
    j: 74,
    k: 75,
    l: 76,
    m: 77,
    n: 78,
    o: 79,
    p: 80,
    q: 81,
    r: 82,
    s: 83,
    t: 84,
    u: 85,
    v: 86,
    w: 87,
    x: 88,
    y: 89,
    z: 90,
    num0: 96,
    num1: 97,
    num2: 98,
    num3: 99,
    num4: 100,
    num5: 101,
    num6: 102,
    num7: 103,
    num8: 104,
    num9: 105,
    "*": 106,
    "+": 107,
    "-": 109,
    numdel: 110,
    "/": 111,
    f1: 112,
    f2: 113,
    f3: 114,
    f4: 115,
    f5: 116,
    f6: 117,
    f7: 118,
    f8: 119,
    f9: 120,
    f10: 121,
    f11: 122,
    f12: 123,
    numlock: 144,
    scrolllock: 145,
    ";": 186,
    semicolon: 186,
    equal: 187,
    "=": 187,
    ",": 188,
    comma: 188,
    dash: 189,
    ".": 190,
    period: 190,
    forwardslash: 191,
    grave: 192,
    "[": 219,
    openbracket: 219,
    backslash: 220,
    "]": 221,
    closebracket: 221,
    quote: 222,
    dpadLeft: 1e3,
    dpadRight: 1001,
    dpadUp: 1003,
    dpadDown: 1004,
    dpadCenter: 1005
   }, cc.macro.ImageFormat = cc.Enum({
    JPG: 0,
    PNG: 1,
    TIFF: 2,
    WEBP: 3,
    PVR: 4,
    ETC: 5,
    S3TC: 6,
    ATITC: 7,
    TGA: 8,
    RAWDATA: 9,
    UNKNOWN: 10
   }), cc.macro.BlendFactor = cc.Enum({
    ONE: 1,
    ZERO: 0,
    SRC_ALPHA: 770,
    SRC_COLOR: 768,
    DST_ALPHA: 772,
    DST_COLOR: 774,
    ONE_MINUS_SRC_ALPHA: 771,
    ONE_MINUS_SRC_COLOR: 769,
    ONE_MINUS_DST_ALPHA: 773,
    ONE_MINUS_DST_COLOR: 775
   }), cc.macro.TextAlignment = cc.Enum({
    LEFT: 0,
    CENTER: 1,
    RIGHT: 2
   }), cc.macro.VerticalTextAlignment = cc.Enum({
    TOP: 0,
    CENTER: 1,
    BOTTOM: 2
   }), e.exports = cc.macro
  }), {
   "./js": 128
  }],
  114: [(function(t, e, i) {
   var n = t("./js"),
    r = t("./CCClass"),
    s = 1;

   function a() {
    this._name = "", this._objFlags = 0
   }
   r.fastDefine("cc.Object", a, {
    _name: "",
    _objFlags: 0
   }), n.value(a, "Flags", {
    Destroyed: s,
    DontSave: 8,
    EditorOnly: 16,
    Dirty: 32,
    DontDestroy: 64,
    PersistentMask: -4192741,
    Destroying: 128,
    Deactivating: 256,
    LockedInEditor: 512,
    IsPreloadStarted: 8192,
    IsOnLoadStarted: 32768,
    IsOnLoadCalled: 16384,
    IsOnEnableCalled: 2048,
    IsStartCalled: 65536,
    IsEditorOnEnableCalled: 4096,
    IsPositionLocked: 1 << 21,
    IsRotationLocked: 1 << 17,
    IsScaleLocked: 1 << 18,
    IsAnchorLocked: 1 << 19,
    IsSizeLocked: 1 << 20
   });
   var o = [];

   function c() {
    for (var t = o.length, e = 0; e < t; ++e) {
     var i = o[e];
     i._objFlags & s || i._destroyImmediate()
    }
    t === o.length ? o.length = 0 : o.splice(0, t)
   }
   n.value(a, "_deferredDestroy", c);
   var h = a.prototype;
   n.getset(h, "name", (function() {
    return this._name
   }), (function(t) {
    this._name = t
   }), !0), n.get(h, "isValid", (function() {
    return !(this._objFlags & s)
   }), !0);
   h.destroy = function() {
    return this._objFlags & s ? (cc.warnID(5e3), !1) : !(4 & this._objFlags) && (this._objFlags |= 4, o.push(this), !0)
   }, h._destruct = function() {
    var t = this.constructor,
     e = t.__destruct__;
    e || (e = (function(t, e) {
     var i, n = t instanceof cc._BaseNode || t instanceof cc.Component,
      r = n ? "_id" : null,
      s = {};
     for (i in t)
      if (t.hasOwnProperty(i)) {
       if (i === r) continue;
       switch (typeof t[i]) {
        case "string":
         s[i] = "";
         break;
        case "object":
        case "function":
         s[i] = null
       }
      } if (cc.Class._isCCClass(e))
      for (var a = cc.Class.Attr.getClassAttrs(e), o = e.__props__, c = 0; c < o.length; c++) {
       var h = (i = o[c]) + cc.Class.Attr.DELIMETER + "default";
       if (h in a) {
        if (n && "_id" === i) continue;
        switch (typeof a[h]) {
         case "string":
          s[i] = "";
          break;
         case "object":
         case "function":
          s[i] = null;
          break;
         case "undefined":
          s[i] = void 0
        }
       }
      }
     return function(t) {
      for (var e in s) t[e] = s[e]
     }
    })(this, t), n.value(t, "__destruct__", e, !0)), e(this)
   }, h._onPreDestroy = null, h._destroyImmediate = function() {
    this._objFlags & s ? cc.errorID(5e3) : (this._onPreDestroy && this._onPreDestroy(), this._destruct(), this._objFlags |= s)
   }, h._deserialize = null, cc.isValid = function(t, e) {
    return "object" == typeof t ? !(!t || t._objFlags & (e ? 4 | s : s)) : void 0 !== t
   }, cc.Object = e.exports = a
  }), {
   "./CCClass": 108,
   "./js": 128
  }],
  115: [(function(t, e, i) {
   var n = t("../platform/js");
   cc.SAXParser = function() {
    window.DOMParser ? (this._isSupportDOMParser = !0, this._parser = new DOMParser) : (this._isSupportDOMParser = !1, this._parser = null)
   }, cc.SAXParser.prototype = {
    constructor: cc.SAXParser,
    parse: function(t) {
     return this._parseXML(t)
    },
    _parseXML: function(t) {
     var e;
     return this._isSupportDOMParser ? e = this._parser.parseFromString(t, "text/xml") : ((e = new ActiveXObject("Microsoft.XMLDOM")).async = "false", e.loadXML(t)), e
    }
   }, cc.PlistParser = function() {
    cc.SAXParser.call(this)
   }, n.extend(cc.PlistParser, cc.SAXParser), n.mixin(cc.PlistParser.prototype, {
    parse: function(t) {
     var e = this._parseXML(t),
      i = e.documentElement;
     if ("plist" !== i.tagName) return cc.warnID(5100), {};
     for (var n = null, r = 0, s = i.childNodes.length; r < s && 1 !== (n = i.childNodes[r]).nodeType; r++);
     return e = null, this._parseNode(n)
    },
    _parseNode: function(t) {
     var e = null,
      i = t.tagName;
     if ("dict" === i) e = this._parseDict(t);
     else if ("array" === i) e = this._parseArray(t);
     else if ("string" === i)
      if (1 === t.childNodes.length) e = t.firstChild.nodeValue;
      else {
       e = "";
       for (var n = 0; n < t.childNodes.length; n++) e += t.childNodes[n].nodeValue
      }
     else "false" === i ? e = !1 : "true" === i ? e = !0 : "real" === i ? e = parseFloat(t.firstChild.nodeValue) : "integer" === i && (e = parseInt(t.firstChild.nodeValue, 10));
     return e
    },
    _parseArray: function(t) {
     for (var e = [], i = 0, n = t.childNodes.length; i < n; i++) {
      var r = t.childNodes[i];
      1 === r.nodeType && e.push(this._parseNode(r))
     }
     return e
    },
    _parseDict: function(t) {
     for (var e = {}, i = null, n = 0, r = t.childNodes.length; n < r; n++) {
      var s = t.childNodes[n];
      1 === s.nodeType && ("key" === s.tagName ? i = s.firstChild.nodeValue : e[i] = this._parseNode(s))
     }
     return e
    }
   }), cc.saxParser = new cc.SAXParser, cc.plistParser = new cc.PlistParser, e.exports = {
    saxParser: cc.saxParser,
    plistParser: cc.plistParser
   }
  }), {
   "../platform/js": 128
  }],
  116: [(function(t, e, i) {
   cc.screen = {
    _supportsFullScreen: !1,
    _preOnFullScreenChange: null,
    _touchEvent: "",
    _fn: null,
    _fnMap: [
     ["requestFullscreen", "exitFullscreen", "fullscreenchange", "fullscreenEnabled", "fullscreenElement"],
     ["requestFullScreen", "exitFullScreen", "fullScreenchange", "fullScreenEnabled", "fullScreenElement"],
     ["webkitRequestFullScreen", "webkitCancelFullScreen", "webkitfullscreenchange", "webkitIsFullScreen", "webkitCurrentFullScreenElement"],
     ["mozRequestFullScreen", "mozCancelFullScreen", "mozfullscreenchange", "mozFullScreen", "mozFullScreenElement"],
     ["msRequestFullscreen", "msExitFullscreen", "MSFullscreenChange", "msFullscreenEnabled", "msFullscreenElement"]
    ],
    init: function() {
     this._fn = {};
     var t, e, i, n, r = this._fnMap;
     for (t = 0, e = r.length; t < e; t++)
      if ((i = r[t]) && void 0 !== document[i[1]]) {
       for (t = 0, n = i.length; t < n; t++) this._fn[r[0][t]] = i[t];
       break
      } this._supportsFullScreen = void 0 !== this._fn.requestFullscreen, this._touchEvent = "ontouchstart" in window ? "touchstart" : "mousedown"
    },
    fullScreen: function() {
     return !!this._supportsFullScreen && !!(document[this._fn.fullscreenElement] || document[this._fn.webkitFullscreenElement] || document[this._fn.mozFullScreenElement])
    },
    requestFullScreen: function(t, e) {
     if (t && "video" === t.tagName.toLowerCase()) {
      if (cc.sys.os === cc.sys.OS_IOS && cc.sys.isBrowser && t.readyState > 0) return void(t.webkitEnterFullscreen && t.webkitEnterFullscreen());
      t.setAttribute("x5-video-player-fullscreen", "true")
     }
     if (this._supportsFullScreen) {
      if (t = t || document.documentElement, e) {
       var i = this._fn.fullscreenchange;
       this._preOnFullScreenChange && document.removeEventListener(i, this._preOnFullScreenChange), this._preOnFullScreenChange = e, document.addEventListener(i, e, !1)
      }
      return t[this._fn.requestFullscreen]()
     }
    },
    exitFullScreen: function(t) {
     if (t && "video" === t.tagName.toLowerCase()) {
      if (cc.sys.os === cc.sys.OS_IOS && cc.sys.isBrowser) return void(t.webkitExitFullscreen && t.webkitExitFullscreen());
      t.setAttribute("x5-video-player-fullscreen", "false")
     }
     return !this._supportsFullScreen || document[this._fn.exitFullscreen]()
    },
    autoFullScreen: function(t, e) {
     t = t || document.body;
     var i = cc.game.canvas || t,
      n = this;
     this.requestFullScreen(t, e), i.addEventListener(this._touchEvent, (function r() {
      i.removeEventListener(n._touchEvent, r), n.requestFullScreen(t, e)
     }))
    }
   }, cc.screen.init()
  }), {}],
  117: [(function(t, e, i) {
   var n = void 0;
   n = window._CCSettings ? _CCSettings.platform : void 0;
   var r = cc && cc.sys ? cc.sys : (function() {
    cc.sys = {};
    var t = cc.sys;
    t.LANGUAGE_ENGLISH = "en", t.LANGUAGE_CHINESE = "zh", t.LANGUAGE_FRENCH = "fr", t.LANGUAGE_ITALIAN = "it", t.LANGUAGE_GERMAN = "de", t.LANGUAGE_SPANISH = "es", t.LANGUAGE_DUTCH = "du", t.LANGUAGE_RUSSIAN = "ru", t.LANGUAGE_KOREAN = "ko", t.LANGUAGE_JAPANESE = "ja", t.LANGUAGE_HUNGARIAN = "hu", t.LANGUAGE_PORTUGUESE = "pt", t.LANGUAGE_ARABIC = "ar", t.LANGUAGE_NORWEGIAN = "no", t.LANGUAGE_POLISH = "pl", t.LANGUAGE_TURKISH = "tr", t.LANGUAGE_UKRAINIAN = "uk", t.LANGUAGE_ROMANIAN = "ro", t.LANGUAGE_BULGARIAN = "bg", t.LANGUAGE_UNKNOWN = "unknown", t.OS_IOS = "iOS", t.OS_ANDROID = "Android", t.OS_WINDOWS = "Windows", t.OS_MARMALADE = "Marmalade", t.OS_LINUX = "Linux", t.OS_BADA = "Bada", t.OS_BLACKBERRY = "Blackberry", t.OS_OSX = "OS X", t.OS_WP8 = "WP8", t.OS_WINRT = "WINRT", t.OS_UNKNOWN = "Unknown", t.UNKNOWN = -1, t.WIN32 = 0, t.LINUX = 1, t.MACOS = 2, t.ANDROID = 3, t.IPHONE = 4, t.IPAD = 5, t.BLACKBERRY = 6, t.NACL = 7, t.EMSCRIPTEN = 8, t.TIZEN = 9, t.WINRT = 10, t.WP8 = 11, t.MOBILE_BROWSER = 100, t.DESKTOP_BROWSER = 101, t.EDITOR_PAGE = 102, t.EDITOR_CORE = 103, t.WECHAT_GAME = 104, t.QQ_PLAY = 105, t.FB_PLAYABLE_ADS = 106, t.BAIDU_GAME = 107, t.VIVO_GAME = 108, t.OPPO_GAME = 109, t.BROWSER_TYPE_WECHAT = "wechat", t.BROWSER_TYPE_WECHAT_GAME = "wechatgame", t.BROWSER_TYPE_WECHAT_GAME_SUB = "wechatgamesub", t.BROWSER_TYPE_BAIDU_GAME = "baidugame", t.BROWSER_TYPE_BAIDU_GAME_SUB = "baidugamesub", t.BROWSER_TYPE_QQ_PLAY = "qqplay", t.BROWSER_TYPE_ANDROID = "androidbrowser", t.BROWSER_TYPE_IE = "ie", t.BROWSER_TYPE_QQ = "qqbrowser", t.BROWSER_TYPE_MOBILE_QQ = "mqqbrowser", t.BROWSER_TYPE_UC = "ucbrowser", t.BROWSER_TYPE_UCBS = "ucbs", t.BROWSER_TYPE_360 = "360browser", t.BROWSER_TYPE_BAIDU_APP = "baiduboxapp", t.BROWSER_TYPE_BAIDU = "baidubrowser", t.BROWSER_TYPE_MAXTHON = "maxthon", t.BROWSER_TYPE_OPERA = "opera", t.BROWSER_TYPE_OUPENG = "oupeng", t.BROWSER_TYPE_MIUI = "miuibrowser", t.BROWSER_TYPE_FIREFOX = "firefox", t.BROWSER_TYPE_SAFARI = "safari", t.BROWSER_TYPE_CHROME = "chrome", t.BROWSER_TYPE_LIEBAO = "liebao", t.BROWSER_TYPE_QZONE = "qzone", t.BROWSER_TYPE_SOUGOU = "sogou", t.BROWSER_TYPE_UNKNOWN = "unknown", t.isNative = !1, t.isBrowser = "object" == typeof window && "object" == typeof document && !1;
    var e = wx.getSystemInfoSync();
    t.isMobile = !0, t.platform = t.WECHAT_GAME, t.language = e.language.substr(0, 2), t.languageCode = e.language.toLowerCase();
    var i = e.system.toLowerCase();
    "android" === e.platform ? t.os = t.OS_ANDROID : "ios" === e.platform ? t.os = t.OS_IOS : "devtools" === e.platform && (t.isMobile = !1, i.indexOf("android") > -1 ? t.os = t.OS_ANDROID : i.indexOf("ios") > -1 && (t.os = t.OS_IOS)), "android p" === i && (i = "android p 9.0");
    var n = /[\d\.]+/.exec(i);
    t.osVersion = n ? n[0] : i, t.osMainVersion = parseInt(t.osVersion), t.browserType = t.BROWSER_TYPE_WECHAT_GAME, t.browserVersion = e.version;
    var r = e.windowWidth,
     s = e.windowHeight,
     a = e.pixelRatio || 1;
    return t.windowPixelResolution = {
     width: a * r,
     height: a * s
    }, t.localStorage = window.localStorage, t.capabilities = {
     canvas: !0,
     opengl: t.browserType !== t.BROWSER_TYPE_WECHAT_GAME_SUB,
     webp: !1
    }, t.__audioSupport = {
     ONLY_ONE: !1,
     WEB_AUDIO: !1,
     DELAY_CREATE_CTX: !1,
     format: [".mp3"]
    }, t.NetworkType = {
     NONE: 0,
     LAN: 1,
     WWAN: 2
    }, t.getNetworkType = function() {
     return t.NetworkType.LAN
    }, t.getBatteryLevel = function() {
     return 1
    }, t.garbageCollect = function() {}, t.restartVM = function() {}, t.getSafeAreaRect = function() {
     var t = cc.view.getVisibleSize();
     return cc.rect(0, 0, t.width, t.height)
    }, t.isObjectValid = function(t) {
     return !!t
    }, t.dump = function() {
     var t = "";
     t += "isMobile : " + this.isMobile + "\r\n", t += "language : " + this.language + "\r\n", t += "browserType : " + this.browserType + "\r\n", t += "browserVersion : " + this.browserVersion + "\r\n", t += "capabilities : " + JSON.stringify(this.capabilities) + "\r\n", t += "os : " + this.os + "\r\n", t += "osVersion : " + this.osVersion + "\r\n", t += "platform : " + this.platform + "\r\n", t += "Using " + (cc.game.renderType === cc.game.RENDER_TYPE_WEBGL ? "WEBGL" : "CANVAS") + " renderer.\r\n", cc.log(t)
    }, t.openURL = function(t) {
     window.open(t)
    }, t.now = function() {
     return Date.now ? Date.now() : +new Date
    }, t
   })();
   e.exports = r
  }), {}],
  118: [(function(t, e, i) {
   var n = t("../event/event-target"),
    r = t("../platform/js"),
    s = t("../renderer");
   t("../platform/CCClass");
   var a = cc.sys.platform === cc.sys.BAIDU_GAME,
    o = {
     init: function() {
      0
     },
     availWidth: function(t) {
      return t && t !== this.html ? t.clientWidth : window.innerWidth
     },
     availHeight: function(t) {
      return t && t !== this.html ? t.clientHeight : window.innerHeight
     },
     meta: {
      width: "device-width"
     },
     adaptationType: cc.sys.browserType
    };
   switch (cc.sys.os === cc.sys.OS_IOS && (o.adaptationType = cc.sys.BROWSER_TYPE_SAFARI), a && (cc.sys.browserType === cc.sys.BROWSER_TYPE_BAIDU_GAME_SUB ? o.adaptationType = cc.sys.BROWSER_TYPE_BAIDU_GAME_SUB : o.adaptationType = cc.sys.BROWSER_TYPE_BAIDU_GAME), cc.sys.browserType === cc.sys.BROWSER_TYPE_WECHAT_GAME_SUB ? o.adaptationType = cc.sys.BROWSER_TYPE_WECHAT_GAME_SUB : o.adaptationType = cc.sys.BROWSER_TYPE_WECHAT_GAME, o.adaptationType) {
    case cc.sys.BROWSER_TYPE_SAFARI:
     o.meta["minimal-ui"] = "true";
    case cc.sys.BROWSER_TYPE_SOUGOU:
    case cc.sys.BROWSER_TYPE_UC:
     o.availWidth = function(t) {
      return t.clientWidth
     }, o.availHeight = function(t) {
      return t.clientHeight
     };
     break;
    case cc.sys.BROWSER_TYPE_WECHAT_GAME:
     o.availWidth = function() {
      return window.innerWidth
     }, o.availHeight = function() {
      return window.innerHeight
     };
     break;
    case cc.sys.BROWSER_TYPE_WECHAT_GAME_SUB:
     var c = window.sharedCanvas || wx.getSharedCanvas();
     o.availWidth = function() {
      return c.width
     }, o.availHeight = function() {
      return c.height
     }
   }
   var h = null,
    u = function() {
     n.call(this);
     var t = this,
      e = cc.ContainerStrategy,
      i = cc.ContentStrategy;
     o.init(this), t._frameSize = cc.size(0, 0), t._designResolutionSize = cc.size(0, 0), t._originalDesignResolutionSize = cc.size(0, 0), t._scaleX = 1, t._scaleY = 1, t._viewportRect = cc.rect(0, 0, 0, 0), t._visibleRect = cc.rect(0, 0, 0, 0), t._autoFullScreen = !1, t._devicePixelRatio = 1, t._retinaEnabled = !1, t._resizeCallback = null, t._resizing = !1, t._resizeWithBrowserSize = !1, t._orientationChanging = !0, t._isRotated = !1, t._orientation = cc.macro.ORIENTATION_AUTO, t._isAdjustViewport = !0, t._antiAliasEnabled = !1, t._resolutionPolicy = null, t._rpExactFit = new cc.ResolutionPolicy(e.EQUAL_TO_FRAME, i.EXACT_FIT), t._rpShowAll = new cc.ResolutionPolicy(e.EQUAL_TO_FRAME, i.SHOW_ALL), t._rpNoBorder = new cc.ResolutionPolicy(e.EQUAL_TO_FRAME, i.NO_BORDER), t._rpFixedHeight = new cc.ResolutionPolicy(e.EQUAL_TO_FRAME, i.FIXED_HEIGHT), t._rpFixedWidth = new cc.ResolutionPolicy(e.EQUAL_TO_FRAME, i.FIXED_WIDTH), cc.game.once(cc.game.EVENT_ENGINE_INITED, this.init, this)
    };
   cc.js.extend(u, n), cc.js.mixin(u.prototype, {
    init: function() {
     this._initFrameSize(), this.enableAntiAlias(!0);
     var t = cc.game.canvas.width,
      e = cc.game.canvas.height;
     this._designResolutionSize.width = t, this._designResolutionSize.height = e, this._originalDesignResolutionSize.width = t, this._originalDesignResolutionSize.height = e, this._viewportRect.width = t, this._viewportRect.height = e, this._visibleRect.width = t, this._visibleRect.height = e, cc.winSize.width = this._visibleRect.width, cc.winSize.height = this._visibleRect.height, cc.visibleRect && cc.visibleRect.init(this._visibleRect)
    },
    _resizeEvent: function(t) {
     var e, i = (e = this.setDesignResolutionSize ? this : cc.view)._frameSize.width,
      n = e._frameSize.height,
      r = e._isRotated;
     if (cc.sys.isMobile) {
      var s = cc.game.container.style,
       a = s.margin;
      s.margin = "0", s.display = "none", e._initFrameSize(), s.margin = a, s.display = "block"
     } else e._initFrameSize();
     if (!0 === t || e._isRotated !== r || e._frameSize.width !== i || e._frameSize.height !== n) {
      var o = e._originalDesignResolutionSize.width,
       c = e._originalDesignResolutionSize.height;
      e._resizing = !0, o > 0 && e.setDesignResolutionSize(o, c, e._resolutionPolicy), e._resizing = !1, e.emit("canvas-resize"), e._resizeCallback && e._resizeCallback.call()
     }
    },
    _orientationChange: function() {
     cc.view._orientationChanging = !0, cc.view._resizeEvent()
    },
    resizeWithBrowserSize: function(t) {
     t ? this._resizeWithBrowserSize || (this._resizeWithBrowserSize = !0, window.addEventListener("resize", this._resizeEvent), window.addEventListener("orientationchange", this._orientationChange)) : this._resizeWithBrowserSize && (this._resizeWithBrowserSize = !1, window.removeEventListener("resize", this._resizeEvent), window.removeEventListener("orientationchange", this._orientationChange))
    },
    setResizeCallback: function(t) {
     "function" != typeof t && null != t || (this._resizeCallback = t)
    },
    setOrientation: function(t) {
     if ((t &= cc.macro.ORIENTATION_AUTO) && this._orientation !== t) {
      this._orientation = t;
      var e = this._originalDesignResolutionSize.width,
       i = this._originalDesignResolutionSize.height;
      this.setDesignResolutionSize(e, i, this._resolutionPolicy)
     }
    },
    _initFrameSize: function() {
     var t = this._frameSize,
      e = o.availWidth(cc.game.frame),
      i = o.availHeight(cc.game.frame),
      n = e >= i;
     !cc.sys.isMobile || n && this._orientation & cc.macro.ORIENTATION_LANDSCAPE || !n && this._orientation & cc.macro.ORIENTATION_PORTRAIT ? (t.width = e, t.height = i, cc.game.container.style["-webkit-transform"] = "rotate(0deg)", cc.game.container.style.transform = "rotate(0deg)", this._isRotated = !1) : (t.width = i, t.height = e, cc.game.container.style["-webkit-transform"] = "rotate(90deg)", cc.game.container.style.transform = "rotate(90deg)", cc.game.container.style["-webkit-transform-origin"] = "0px 0px 0px", cc.game.container.style.transformOrigin = "0px 0px 0px", this._isRotated = !0), this._orientationChanging && setTimeout((function() {
      cc.view._orientationChanging = !1
     }), 1e3)
    },
    _adjustSizeKeepCanvasSize: function() {
     var t = this._originalDesignResolutionSize.width,
      e = this._originalDesignResolutionSize.height;
     t > 0 && this.setDesignResolutionSize(t, e, this._resolutionPolicy)
    },
    _setViewportMeta: function(t, e) {
     var i = document.getElementById("cocosMetaElement");
     i && e && document.head.removeChild(i);
     var n, r, s, a = document.getElementsByName("viewport"),
      o = a ? a[0] : null;
     for (r in n = o ? o.content : "", (i = i || document.createElement("meta")).id = "cocosMetaElement", i.name = "viewport", i.content = "", t) - 1 == n.indexOf(r) ? n += "," + r + "=" + t[r] : e && (s = new RegExp(r + "s*=s*[^,]+"), n.replace(s, r + "=" + t[r]));
     /^,/.test(n) && (n = n.substr(1)), i.content = n, o && (o.content = n), document.head.appendChild(i)
    },
    _adjustViewportMeta: function() {
     this._isAdjustViewport, 0
    },
    adjustViewportMeta: function(t) {
     this._isAdjustViewport = t
    },
    enableRetina: function(t) {
     this._retinaEnabled = !!t
    },
    isRetinaEnabled: function() {
     return this._retinaEnabled
    },
    enableAntiAlias: function(t) {
     if (this._antiAliasEnabled !== t)
      if (this._antiAliasEnabled = t, cc.game.renderType === cc.game.RENDER_TYPE_WEBGL) {
       var e = cc.loader._cache;
       for (var i in e) {
        var n = e[i],
         r = n && n.content instanceof cc.Texture2D ? n.content : null;
        if (r) {
         var s = cc.Texture2D.Filter;
         t ? r.setFilters(s.LINEAR, s.LINEAR) : r.setFilters(s.NEAREST, s.NEAREST)
        }
       }
      } else if (cc.game.renderType === cc.game.RENDER_TYPE_CANVAS) {
      var a = cc.game.canvas.getContext("2d");
      a.imageSmoothingEnabled = t, a.mozImageSmoothingEnabled = t
     }
    },
    isAntiAliasEnabled: function() {
     return this._antiAliasEnabled
    },
    enableAutoFullScreen: function(t) {
     t && t !== this._autoFullScreen && cc.sys.isMobile && cc.sys.browserType !== cc.sys.BROWSER_TYPE_WECHAT ? (this._autoFullScreen = !0, cc.screen.autoFullScreen(cc.game.frame)) : this._autoFullScreen = !1
    },
    isAutoFullScreenEnabled: function() {
     return this._autoFullScreen
    },
    setCanvasSize: function(t, e) {
     var i = cc.game.canvas,
      n = cc.game.container;
     i.width = t * this._devicePixelRatio, i.height = e * this._devicePixelRatio, i.style.width = t + "px", i.style.height = e + "px", n.style.width = t + "px", n.style.height = e + "px", this._resizeEvent()
    },
    getCanvasSize: function() {
     return cc.size(cc.game.canvas.width, cc.game.canvas.height)
    },
    getFrameSize: function() {
     return cc.size(this._frameSize.width, this._frameSize.height)
    },
    setFrameSize: function(t, e) {
     this._frameSize.width = t, this._frameSize.height = e, cc.game.frame.style.width = t + "px", cc.game.frame.style.height = e + "px", this._resizeEvent(!0)
    },
    getVisibleSize: function() {
     return cc.size(this._visibleRect.width, this._visibleRect.height)
    },
    getVisibleSizeInPixel: function() {
     return cc.size(this._visibleRect.width * this._scaleX, this._visibleRect.height * this._scaleY)
    },
    getVisibleOrigin: function() {
     return cc.v2(this._visibleRect.x, this._visibleRect.y)
    },
    getVisibleOriginInPixel: function() {
     return cc.v2(this._visibleRect.x * this._scaleX, this._visibleRect.y * this._scaleY)
    },
    getResolutionPolicy: function() {
     return this._resolutionPolicy
    },
    setResolutionPolicy: function(t) {
     var e = this;
     if (t instanceof cc.ResolutionPolicy) e._resolutionPolicy = t;
     else {
      var i = cc.ResolutionPolicy;
      t === i.EXACT_FIT && (e._resolutionPolicy = e._rpExactFit), t === i.SHOW_ALL && (e._resolutionPolicy = e._rpShowAll), t === i.NO_BORDER && (e._resolutionPolicy = e._rpNoBorder), t === i.FIXED_HEIGHT && (e._resolutionPolicy = e._rpFixedHeight), t === i.FIXED_WIDTH && (e._resolutionPolicy = e._rpFixedWidth)
     }
    },
    setDesignResolutionSize: function(t, e, i) {
     if (t > 0 || e > 0) {
      this.setResolutionPolicy(i);
      var n = this._resolutionPolicy;
      if (n && n.preApply(this), cc.sys.isMobile && this._adjustViewportMeta(), this._orientationChanging = !0, this._resizing || this._initFrameSize(), n) {
       this._originalDesignResolutionSize.width = this._designResolutionSize.width = t, this._originalDesignResolutionSize.height = this._designResolutionSize.height = e;
       var r = n.apply(this, this._designResolutionSize);
       if (r.scale && 2 === r.scale.length && (this._scaleX = r.scale[0], this._scaleY = r.scale[1]), r.viewport) {
        var a = this._viewportRect,
         o = this._visibleRect,
         c = r.viewport;
        a.x = c.x, a.y = c.y, a.width = c.width, a.height = c.height, o.x = 0, o.y = 0, o.width = c.width / this._scaleX, o.height = c.height / this._scaleY
       }
       n.postApply(this), cc.winSize.width = this._visibleRect.width, cc.winSize.height = this._visibleRect.height, cc.visibleRect && cc.visibleRect.init(this._visibleRect), s.updateCameraViewport(), this.emit("design-resolution-changed")
      } else cc.logID(2201)
     } else cc.logID(2200)
    },
    getDesignResolutionSize: function() {
     return cc.size(this._designResolutionSize.width, this._designResolutionSize.height)
    },
    setRealPixelResolution: function(t, e, i) {
     this.setDesignResolutionSize(t, e, i)
    },
    setViewportInPoints: function(t, e, i, n) {
     var r = this._scaleX,
      s = this._scaleY;
     cc.game._renderContext.viewport(t * r + this._viewportRect.x, e * s + this._viewportRect.y, i * r, n * s)
    },
    setScissorInPoints: function(t, e, i, n) {
     var r = this._scaleX,
      s = this._scaleY,
      a = Math.ceil(t * r + this._viewportRect.x),
      o = Math.ceil(e * s + this._viewportRect.y),
      c = Math.ceil(i * r),
      u = Math.ceil(n * s),
      l = cc.game._renderContext;
     if (!h) {
      var _ = l.getParameter(l.SCISSOR_BOX);
      h = cc.rect(_[0], _[1], _[2], _[3])
     }
     h.x === a && h.y === o && h.width === c && h.height === u || (h.x = a, h.y = o, h.width = c, h.height = u, l.scissor(a, o, c, u))
    },
    isScissorEnabled: function() {
     return cc.game._renderContext.isEnabled(gl.SCISSOR_TEST)
    },
    getScissorRect: function() {
     if (!h) {
      var t = gl.getParameter(gl.SCISSOR_BOX);
      h = cc.rect(t[0], t[1], t[2], t[3])
     }
     var e = 1 / this._scaleX,
      i = 1 / this._scaleY;
     return cc.rect((h.x - this._viewportRect.x) * e, (h.y - this._viewportRect.y) * i, h.width * e, h.height * i)
    },
    getViewportRect: function() {
     return this._viewportRect
    },
    getScaleX: function() {
     return this._scaleX
    },
    getScaleY: function() {
     return this._scaleY
    },
    getDevicePixelRatio: function() {
     return this._devicePixelRatio
    },
    convertToLocationInView: function(t, e, i, n) {
     var r = n || cc.v2(),
      s = this._devicePixelRatio * (t - i.left),
      a = this._devicePixelRatio * (i.top + i.height - e);
     return this._isRotated ? (r.x = cc.game.canvas.width - a, r.y = s) : (r.x = s, r.y = a), r
    },
    _convertMouseToLocationInView: function(t, e) {
     var i = this._viewportRect;
     t.x = (this._devicePixelRatio * (t.x - e.left) - i.x) / this._scaleX, t.y = (this._devicePixelRatio * (e.top + e.height - t.y) - i.y) / this._scaleY
    },
    _convertPointWithScale: function(t) {
     var e = this._viewportRect;
     t.x = (t.x - e.x) / this._scaleX, t.y = (t.y - e.y) / this._scaleY
    },
    _convertTouchesWithScale: function(t) {
     for (var e, i, n, r = this._viewportRect, s = this._scaleX, a = this._scaleY, o = 0; o < t.length; o++) i = (e = t[o])._point, n = e._prevPoint, i.x = (i.x - r.x) / s, i.y = (i.y - r.y) / a, n.x = (n.x - r.x) / s, n.y = (n.y - r.y) / a
    }
   }), cc.ContainerStrategy = cc.Class({
    name: "ContainerStrategy",
    preApply: function(t) {},
    apply: function(t, e) {},
    postApply: function(t) {},
    _setupContainer: function(t, e, i) {
     var n = cc.game.canvas;
     cc.game.container;
     var r = t._devicePixelRatio = 1;
     t.isRetinaEnabled() && (r = t._devicePixelRatio = Math.min(2, window.devicePixelRatio || 1)), n.width = e * r, n.height = i * r
    },
    _fixContainer: function() {
     document.body.insertBefore(cc.game.container, document.body.firstChild);
     var t = document.body.style;
     t.width = window.innerWidth + "px", t.height = window.innerHeight + "px", t.overflow = "hidden";
     var e = cc.game.container.style;
     e.position = "fixed", e.left = e.top = "0px", document.body.scrollTop = 0
    }
   }), cc.ContentStrategy = cc.Class({
    name: "ContentStrategy",
    ctor: function() {
     this._result = {
      scale: [1, 1],
      viewport: null
     }
    },
    _buildResult: function(t, e, i, n, r, s) {
     Math.abs(t - i) < 2 && (i = t), Math.abs(e - n) < 2 && (n = e);
     var a = cc.rect((t - i) / 2, (e - n) / 2, i, n);
     return cc.game.renderType, cc.game.RENDER_TYPE_CANVAS, this._result.scale = [r, s], this._result.viewport = a, this._result
    },
    preApply: function(t) {},
    apply: function(t, e) {
     return {
      scale: [1, 1]
     }
    },
    postApply: function(t) {}
   }), (function() {
    var t = cc.Class({
      name: "EqualToFrame",
      extends: cc.ContainerStrategy,
      apply: function(t) {
       var e = t._frameSize.height,
        i = cc.game.container.style;
       this._setupContainer(t, t._frameSize.width, t._frameSize.height), t._isRotated ? i.margin = "0 0 0 " + e + "px" : i.margin = "0px", i.padding = "0px"
      }
     }),
     e = cc.Class({
      name: "ProportionalToFrame",
      extends: cc.ContainerStrategy,
      apply: function(t, e) {
       var i, n, r = t._frameSize.width,
        s = t._frameSize.height,
        a = cc.game.container.style,
        o = e.width,
        c = e.height,
        h = r / o,
        u = s / c;
       h < u ? (i = r, n = c * h) : (i = o * u, n = s);
       var l = Math.round((r - i) / 2),
        _ = Math.round((s - n) / 2);
       i = r - 2 * l, n = s - 2 * _, this._setupContainer(t, i, n), t._isRotated ? a.margin = "0 0 0 " + s + "px" : a.margin = "0px", a.paddingLeft = l + "px", a.paddingRight = l + "px", a.paddingTop = _ + "px", a.paddingBottom = _ + "px"
      }
     }),
     i = (cc.Class({
      name: "EqualToWindow",
      extends: t,
      preApply: function(t) {
       this._super(t), cc.game.frame = document.documentElement
      },
      apply: function(t) {
       this._super(t), this._fixContainer()
      }
     }), cc.Class({
      name: "ProportionalToWindow",
      extends: e,
      preApply: function(t) {
       this._super(t), cc.game.frame = document.documentElement
      },
      apply: function(t, e) {
       this._super(t, e), this._fixContainer()
      }
     }), cc.Class({
      name: "OriginalContainer",
      extends: cc.ContainerStrategy,
      apply: function(t) {
       this._setupContainer(t, cc.game.canvas.width, cc.game.canvas.height)
      }
     }));
    cc.ContainerStrategy.EQUAL_TO_FRAME = new t, cc.ContainerStrategy.PROPORTION_TO_FRAME = new e, cc.ContainerStrategy.ORIGINAL_CONTAINER = new i;
    var n = cc.Class({
      name: "ExactFit",
      extends: cc.ContentStrategy,
      apply: function(t, e) {
       var i = cc.game.canvas.width,
        n = cc.game.canvas.height,
        r = i / e.width,
        s = n / e.height;
       return this._buildResult(i, n, i, n, r, s)
      }
     }),
     r = cc.Class({
      name: "ShowAll",
      extends: cc.ContentStrategy,
      apply: function(t, e) {
       var i, n, r = cc.game.canvas.width,
        s = cc.game.canvas.height,
        a = e.width,
        o = e.height,
        c = r / a,
        h = s / o,
        u = 0;
       return c < h ? (i = r, n = o * (u = c)) : (i = a * (u = h), n = s), this._buildResult(r, s, i, n, u, u)
      }
     }),
     s = cc.Class({
      name: "NoBorder",
      extends: cc.ContentStrategy,
      apply: function(t, e) {
       var i, n, r, s = cc.game.canvas.width,
        a = cc.game.canvas.height,
        o = e.width,
        c = e.height,
        h = s / o,
        u = a / c;
       return h < u ? (n = o * (i = u), r = a) : (n = s, r = c * (i = h)), this._buildResult(s, a, n, r, i, i)
      }
     }),
     a = cc.Class({
      name: "FixedHeight",
      extends: cc.ContentStrategy,
      apply: function(t, e) {
       var i = cc.game.canvas.width,
        n = cc.game.canvas.height,
        r = n / e.height,
        s = i,
        a = n;
       return this._buildResult(i, n, s, a, r, r)
      }
     }),
     o = cc.Class({
      name: "FixedWidth",
      extends: cc.ContentStrategy,
      apply: function(t, e) {
       var i = cc.game.canvas.width,
        n = cc.game.canvas.height,
        r = i / e.width,
        s = i,
        a = n;
       return this._buildResult(i, n, s, a, r, r)
      }
     });
    cc.ContentStrategy.EXACT_FIT = new n, cc.ContentStrategy.SHOW_ALL = new r, cc.ContentStrategy.NO_BORDER = new s, cc.ContentStrategy.FIXED_HEIGHT = new a, cc.ContentStrategy.FIXED_WIDTH = new o
   })(), cc.ResolutionPolicy = cc.Class({
    name: "cc.ResolutionPolicy",
    ctor: function(t, e) {
     this._containerStrategy = null, this._contentStrategy = null, this.setContainerStrategy(t), this.setContentStrategy(e)
    },
    preApply: function(t) {
     this._containerStrategy.preApply(t), this._contentStrategy.preApply(t)
    },
    apply: function(t, e) {
     return this._containerStrategy.apply(t, e), this._contentStrategy.apply(t, e)
    },
    postApply: function(t) {
     this._containerStrategy.postApply(t), this._contentStrategy.postApply(t)
    },
    setContainerStrategy: function(t) {
     t instanceof cc.ContainerStrategy && (this._containerStrategy = t)
    },
    setContentStrategy: function(t) {
     t instanceof cc.ContentStrategy && (this._contentStrategy = t)
    }
   }), r.get(cc.ResolutionPolicy.prototype, "canvasSize", (function() {
    return cc.v2(cc.game.canvas.width, cc.game.canvas.height)
   })), cc.ResolutionPolicy.EXACT_FIT = 0, cc.ResolutionPolicy.NO_BORDER = 1, cc.ResolutionPolicy.SHOW_ALL = 2, cc.ResolutionPolicy.FIXED_HEIGHT = 3, cc.ResolutionPolicy.FIXED_WIDTH = 4, cc.ResolutionPolicy.UNKNOWN = 5, cc.view = new u, cc.winSize = cc.v2(), e.exports = cc.view
  }), {
   "../event/event-target": 77,
   "../platform/CCClass": 108,
   "../platform/js": 128,
   "../renderer": 149
  }],
  119: [(function(t, e, i) {
   cc.visibleRect = {
    topLeft: cc.v2(0, 0),
    topRight: cc.v2(0, 0),
    top: cc.v2(0, 0),
    bottomLeft: cc.v2(0, 0),
    bottomRight: cc.v2(0, 0),
    bottom: cc.v2(0, 0),
    center: cc.v2(0, 0),
    left: cc.v2(0, 0),
    right: cc.v2(0, 0),
    width: 0,
    height: 0,
    init: function(t) {
     var e = this.width = t.width,
      i = this.height = t.height,
      n = t.x,
      r = t.y,
      s = r + i,
      a = n + e;
     this.topLeft.x = n, this.topLeft.y = s, this.topRight.x = a, this.topRight.y = s, this.top.x = n + e / 2, this.top.y = s, this.bottomLeft.x = n, this.bottomLeft.y = r, this.bottomRight.x = a, this.bottomRight.y = r, this.bottom.x = n + e / 2, this.bottom.y = r, this.center.x = n + e / 2, this.center.y = r + i / 2, this.left.x = n, this.left.y = r + i / 2, this.right.x = a, this.right.y = r + i / 2
    }
   }
  }), {}],
  120: [(function(t, e, i) {
   var n = t("./js"),
    r = (t("./utils").isPlainEmptyObj_DEV, "$_$");

   function s(t, e, i) {
    var r;
    r = function() {}, i && n.extend(r, i.constructor);
    var s = new r;
    return n.value(t, "__attrs__", s), s
   }

   function a(t, e, i) {
    var n, a, c;
    if ("function" == typeof t) a = (n = o(t)).constructor.prototype;
    else {
     var h = t;
     if (!(n = h.__attrs__)) n = s(h, 0, o(t = h.constructor));
     a = n
    }
    if (void 0 === i) {
     var u = e + r,
      l = {};
     for (c in n) c.startsWith(u) && (l[c.slice(u.length)] = n[c]);
     return l
    }
    if ("object" == typeof i)
     for (c in i) 95 !== c.charCodeAt(0) && (a[e + r + c] = i[c]);
    else 0
   }

   function o(t) {
    return t.hasOwnProperty("__attrs__") && t.__attrs__ || (function(t) {
     for (var e, i = cc.Class.getInheritanceChain(t), n = i.length - 1; n >= 0; n--) {
      var r = i[n];
      r.hasOwnProperty("__attrs__") && r.__attrs__ || s(r, 0, (e = i[n + 1]) && e.__attrs__)
     }
     return s(t, 0, (e = i[0]) && e.__attrs__), t.__attrs__
    })(t)
   }

   function c(t) {
    return o(t).constructor.prototype
   }

   function h(t, e) {
    0
   }
   cc.Integer = "Integer", cc.Float = "Float", cc.Boolean = "Boolean", cc.String = "String", e.exports = {
    attr: a,
    getClassAttrs: o,
    getClassAttrsProto: c,
    setClassAttr: function(t, e, i, n) {
     c(t)[e + r + i] = n
    },
    DELIMETER: r,
    getTypeChecker: h,
    ObjectType: function(t) {
     return {
      type: "Object",
      ctor: t,
      _onAfterProp: !1
     }
    },
    ScriptUuid: {}
   }
  }), {
   "./CCClass": 108,
   "./js": 128,
   "./utils": 132
  }],
  121: [(function(t, e, i) {
   var n = t("./js"),
    r = n.array.fastRemoveAt;

   function s() {
    this.callbacks = [], this.targets = [], this.isInvoking = !1, this.containCanceled = !1
   }
   var a = s.prototype;
   a.removeBy = function(t, e) {
    for (var i = this.callbacks, n = this.targets, s = 0; s < t.length; ++s) t[s] === e && (r(i, s), r(n, s), --s)
   }, a.cancel = function(t) {
    this.callbacks[t] = this.targets[t] = null, this.containCanceled = !0
   }, a.cancelAll = function() {
    for (var t = this.callbacks, e = this.targets, i = 0; i < t.length; i++) t[i] = e[i] = null;
    this.containCanceled = !0
   }, a.purgeCanceled = function() {
    this.removeBy(this.callbacks, null), this.containCanceled = !1
   };
   var o = new n.Pool(function(t) {
    t.callbacks.length = 0, t.targets.length = 0, t.isInvoking = !1, t.containCanceled = !1
   }, 16);

   function c() {
    this._callbackTable = n.createMap(!0)
   }
   o.get = function() {
    return this._get() || new s
   }, (a = c.prototype).add = function(t, e, i) {
    var n = this._callbackTable[t];
    n || (n = this._callbackTable[t] = o.get()), n.callbacks.push(e), n.targets.push(i || null)
   }, a.hasEventListener = function(t, e, i) {
    var n = this._callbackTable[t];
    if (!n) return !1;
    var r = n.callbacks;
    if (!e) {
     if (n.isInvoking) {
      for (var s = 0; s < r.length; s++)
       if (r[s]) return !0;
      return !1
     }
     return r.length > 0
    }
    i = i || null;
    for (var a = n.targets, o = 0; o < r.length; ++o)
     if (r[o] === e && a[o] === i) return !0;
    return !1
   }, a.removeAll = function(t) {
    if ("string" == typeof t) {
     var e = this._callbackTable[t];
     e && (e.isInvoking ? e.cancelAll() : (o.put(e), delete this._callbackTable[t]))
    } else if (t)
     for (var i in this._callbackTable) {
      var n = this._callbackTable[i];
      if (n.isInvoking)
       for (var r = n.targets, s = 0; s < r.length; ++s) r[s] === t && n.cancel(s);
      else n.removeBy(n.targets, t)
     }
   }, a.remove = function(t, e, i) {
    var n = this._callbackTable[t];
    if (n) {
     i = i || null;
     for (var s = n.callbacks, a = n.targets, o = 0; o < s.length; ++o)
      if (s[o] === e && a[o] === i) {
       n.isInvoking ? n.cancel(o) : (r(s, o), r(a, o));
       break
      }
    }
   };
   var h = function() {
    c.call(this)
   };
   n.extend(h, c), h.prototype.invoke = function(t, e, i, n, r, s) {
    var a = this._callbackTable[t];
    if (a) {
     var o = !a.isInvoking;
     a.isInvoking = !0;
     for (var c = a.callbacks, h = a.targets, u = 0, l = c.length; u < l; ++u) {
      var _ = c[u];
      if (_) {
       var d = h[u];
       d ? _.call(d, e, i, n, r, s) : _(e, i, n, r, s)
      }
     }
     o && (a.isInvoking = !1, a.containCanceled && a.purgeCanceled())
    }
   }, h.CallbacksHandler = c, e.exports = h
  }), {
   "./js": 128
  }],
  122: [(function(t, e, i) {
   e.exports = {
    flattenCodeArray: function(t) {
     var e = [];
     return (function t(e, i) {
      for (var n = 0; n < i.length; n++) {
       var r = i[n];
       Array.isArray(r) ? t(e, r) : e.push(r)
      }
     })(e, t), e.join("")
    }
   }
  }), {}],
  123: [(function(t, e, i) {
   var n = t("./js"),
    r = t("./attribute"),
    s = t("./CCClass"),
    a = t("../utils/misc"),
    o = function() {
     this.uuidList = [], this.uuidObjList = [], this.uuidPropList = [], this._stillUseUrl = n.createMap(!0)
    };
   o.prototype.reset = function() {
    this.uuidList.length = 0, this.uuidObjList.length = 0, this.uuidPropList.length = 0, n.clear(this._stillUseUrl)
   }, o.prototype.push = function(t, e, i, n) {
    n && (this._stillUseUrl[this.uuidList.length] = !0), this.uuidList.push(i), this.uuidObjList.push(t), this.uuidPropList.push(e)
   }, (o.pool = new n.Pool(function(t) {
    t.reset()
   }, 10)).get = function() {
    return this._get() || new o
   };
   var c = (function() {
    function t(t, e, i, n, r) {
     this.result = t, this.customEnv = n, this.deserializedList = [], this.deserializedData = null, this._classFinder = i, this._idList = [], this._idObjList = [], this._idPropList = []
    }
    var e = t.prototype;
    e.deserialize = function(t) {
     if (Array.isArray(t)) {
      var e = t,
       i = e.length;
      this.deserializedList.length = i;
      for (var n = 0; n < i; n++) {
       if (e[n]) this.deserializedList[n] = this._deserializeObject(e[n], !1)
      }
      this.deserializedData = i > 0 ? this.deserializedList[0] : []
     } else this.deserializedList.length = 1, this.deserializedData = t ? this._deserializeObject(t, !1) : null, this.deserializedList[0] = this.deserializedData;
     return (function(t) {
      var e, i, n, r = t.deserializedList,
       s = t._idPropList,
       a = t._idList,
       o = t._idObjList;
      for (t._classFinder && t._classFinder.onDereferenced, e = 0; e < a.length; e++) i = s[e], n = a[e], o[e][i] = r[n]
     })(this), this.deserializedData
    }, e._deserializeObject = function(t, e, r, s, a) {
     var o, c = null,
      h = null,
      u = t.__type__;
     if (u) {
      if (!(h = this._classFinder(u, t, s, a))) return this._classFinder === n._getClassById && cc.deserialize.reportMissingClass(u), null;
      if ((c = new h)._deserialize) return c._deserialize(t.content, this), c;
      cc.Class._isCCClass(h) ? (function(t, e, r, s, a) {
       var o;
       s.hasOwnProperty("__deserialize__") ? o = s.__deserialize__ : (o = i(t, s), n.value(s, "__deserialize__", o, !0));
       o(t, e, r, s, a), 0
      })(this, c, t, h, r) : this._deserializeTypedObject(c, t, h)
     } else if (Array.isArray(t)) {
      c = new Array(t.length);
      for (var l = 0; l < t.length; l++) "object" == typeof(o = t[l]) && o ? this._deserializeObjField(c, o, "" + l, null, e) : c[l] = o
     } else c = {}, this._deserializePrimitiveObject(c, t);
     return c
    }, e._deserializeObjField = function(t, e, i, n, r) {
     var s = e.__id__;
     if (void 0 === s) {
      var a = e.__uuid__;
      a ? this.result.push(t, i, a, r) : t[i] = this._deserializeObject(e, r)
     } else {
      var o = this.deserializedList[s];
      o ? t[i] = o : (this._idList.push(s), this._idObjList.push(t), this._idPropList.push(i))
     }
    }, e._deserializePrimitiveObject = function(t, e) {
     for (var i in e)
      if (e.hasOwnProperty(i)) {
       var n = e[i];
       "object" != typeof n ? "__type__" !== i && (t[i] = n) : n ? this._deserializeObjField(t, n, i) : t[i] = null
      }
    }, e._deserializeTypedObject = function(t, e, i) {
     if (i === cc.Vec2) return t.x = e.x || 0, void(t.y = e.y || 0);
     if (i !== cc.Color) {
      if (i === cc.Size) return t.width = e.width || 0, void(t.height = e.height || 0);
      var n = i.__props__;
      n || (n = Object.keys(t));
      for (var r = 0; r < n.length; r++) {
       var s = n[r],
        a = e[s];
       void 0 !== a && e.hasOwnProperty(s) && ("object" != typeof a ? t[s] = a : a ? this._deserializeObjField(t, a, s) : t[s] = null)
      }
     } else {
      t.r = e.r || 0, t.g = e.g || 0, t.b = e.b || 0;
      var o = e.a;
      t.a = void 0 === o ? 255 : o
     }
    };
    var i = function(t, e) {
     var i, o = a.BUILTIN_CLASSID_RE.test(n._getClassId(e)),
      c = cc.js.isChildClassOf(e, cc._BaseNode) || cc.js.isChildClassOf(e, cc.Component),
      h = [],
      u = h,
      l = [],
      _ = l,
      d = [],
      f = [];
     return (function() {
       var t = e.__values__;
       i = "_$erialized" === t[t.length - 1];
       for (var n = r.getClassAttrs(e), a = r.DELIMETER + "type", c = r.DELIMETER + "default", p = r.DELIMETER + "saveUrlAsAsset", m = r.DELIMETER + "formerlySerializedAs", v = 0; v < t.length; v++) {
        var g = t[v],
         y = g;
        n[g + m] && (y = n[g + m]);
        var T = n[g + p],
         E = s.getDefault(n[g + c]),
         x = !1;
        if (o) {
         var C = n[g + a];
         if (void 0 === E && C) x = C === cc.String || C === cc.Integer || C === cc.Float || C === cc.Boolean;
         else {
          var A = typeof E;
          x = "string" === A && !T || "number" === A || "boolean" === A
         }
        }
        o && x ? (y !== g && u === h && (u = h.slice()), h.push(g), u !== h && u.push(y)) : (y !== g && _ === l && (_ = l.slice()), l.push(g), _ !== l && _.push(y), d.push(T), f.push(E instanceof cc.ValueType && E.constructor))
       }
      })(),
      function(t, e, n, r, s) {
       for (var a = 0; a < h.length; ++a) {
        var p = n[u[a]];
        void 0 !== p && (e[h[a]] = p)
       }
       for (var m = 0; m < l.length; ++m) {
        var v = l[m],
         g = n[_[m]];
        if (void 0 !== g)
         if (o || "object" == typeof g) {
          var y = f[m];
          y ? o || g ? t._deserializeTypedObject(e[v], g, y) : e[v] = null : g ? t._deserializeObjField(e, g, v, null, d[m]) : e[v] = null
         } else e[v] = g
       }
       c && n._id && (e._id = n._id), i && (e._$erialized = JSON.parse(JSON.stringify(n)), t._deserializePrimitiveObject(e._$erialized, n))
      }
    };
    return t.pool = new n.Pool(function(t) {
     t.result = null, t.customEnv = null, t.deserializedList.length = 0, t.deserializedData = null, t._classFinder = null, t._idList.length = 0, t._idObjList.length = 0, t._idPropList.length = 0
    }, 1), t.pool.get = function(e, i, n, r, s) {
     var a = this._get();
     return a ? (a.result = e, a.customEnv = r, a._classFinder = n, a) : new t(e, i, n, r, s)
    }, t
   })();
   cc.deserialize = function(t, e, i) {
    var r = (i = i || {}).classFinder || n._getClassById,
     s = i.createAssetRefs || cc.sys.platform === cc.sys.EDITOR_CORE,
     a = i.customEnv,
     h = i.ignoreEditorOnly;
    "string" == typeof t && (t = JSON.parse(t));
    var u = !e;
    e = e || o.pool.get();
    var l = c.pool.get(e, !1, r, a, h);
    cc.game._isCloning = !0;
    var _ = l.deserialize(t);
    return cc.game._isCloning = !1, c.pool.put(l), s && e.assignAssetsBy(Editor.serialize.asAsset), u && o.pool.put(e), _
   }, cc.deserialize.Details = o, cc.deserialize.reportMissingClass = function(t) {
    cc.warnID(5302, t)
   }
  }), {
   "../utils/misc": 186,
   "./CCClass": 108,
   "./attribute": 120,
   "./js": 128
  }],
  124: [(function(t, e, i) {
   var n = ".";

   function r(t) {
    this.id = 0 | 998 * Math.random(), this.prefix = t ? t + n : ""
   }
   r.prototype.getNewId = function() {
    return this.prefix + ++this.id
   }, r.global = new r("global"), e.exports = r
  }), {}],
  125: [(function(t, e, i) {
   t("./js"), t("./CCClass"), t("./CCClassDecorator"), t("./CCEnum"), t("./CCObject"), t("./callbacks-invoker"), t("./url"), t("./deserialize"), t("./instantiate"), t("./instantiate-jit"), t("./requiring-frame"), t("./CCSys"), t("./CCMacro"), t("./CCAssetLibrary"), t("./CCVisibleRect")
  }), {
   "./CCAssetLibrary": 107,
   "./CCClass": 108,
   "./CCClassDecorator": 109,
   "./CCEnum": 110,
   "./CCMacro": 113,
   "./CCObject": 114,
   "./CCSys": 117,
   "./CCVisibleRect": 119,
   "./callbacks-invoker": 121,
   "./deserialize": 123,
   "./instantiate": 127,
   "./instantiate-jit": 126,
   "./js": 128,
   "./requiring-frame": 130,
   "./url": 131
  }],
  126: [(function(t, e, i) {
   var n = t("./CCObject"),
    r = n.Flags.Destroyed,
    s = n.Flags.PersistentMask,
    a = t("./attribute"),
    o = t("./js"),
    c = t("./CCClass"),
    h = t("./compiler"),
    u = a.DELIMETER + "default",
    l = c.IDENTIFIER_RE,
    _ = c.escapeForJS,
    d = "var ",
    f = "o",
    p = "t",
    m = {
     "cc.Node": "cc.Node",
     "cc.Sprite": "cc.Sprite",
     "cc.Label": "cc.Label",
     "cc.Button": "cc.Button",
     "cc.Widget": "cc.Widget",
     "cc.Animation": "cc.Animation",
     "cc.ClickEvent": !1,
     "cc.PrefabInfo": !1
    };

   function v(t, e) {
    this.varName = t, this.expression = e
   }

   function g(t, e) {
    return e instanceof v ? new v(e.varName, t + e.expression) : t + e
   }

   function y(t, e, i) {
    Array.isArray(i) ? (i[0] = g(e, i[0]), t.push(i)) : t.push(g(e, i) + ";")
   }

   function T(t) {
    this._exps = [], this._targetExp = t
   }

   function E(t, e) {
    if ("function" == typeof t) try {
     t = t()
    } catch (t) {
     return !1
    }
    if (t === e) return !0;
    if (t && e) {
     if (t instanceof cc.ValueType && t.equals(e)) return !0;
     if (Array.isArray(t) && Array.isArray(e) || t.constructor === Object && e.constructor === Object) try {
      return Array.isArray(t) && Array.isArray(e) && 0 === t.length && 0 === e.length
     } catch (t) {}
    }
    return !1
   }

   function x(t) {
    return l.test(t) ? "." + t : "[" + _(t) + "]"
   }

   function C(t, e) {
    var i;
    this.parent = e, this.objsToClear_iN$t = [], this.codeArray = [], this.objs = [], this.funcs = [], this.funcModuleCache = o.createMap(), o.mixin(this.funcModuleCache, m), this.globalVariables = [], this.globalVariableId = 0, this.localVariableId = 0, this.codeArray.push(d + f + "," + p + ";", "if(R){", f + "=R;", "}else{", f + "=R=new " + this.getFuncModule(t.constructor, !0) + "();", "}"), o.value(t, "_iN$t", {
     globalVar: "R"
    }, !0), this.objsToClear_iN$t.push(t), this.enumerateObject(this.codeArray, t), this.globalVariables.length > 0 && (i = d + this.globalVariables.join(",") + ";");
    var n = h.flattenCodeArray(["return (function(R){", i || [], this.codeArray, "return o;", "})"]);
    this.result = Function("O", "F", n)(this.objs, this.funcs);
    for (var r = 0, s = this.objsToClear_iN$t.length; r < s; ++r) this.objsToClear_iN$t[r]._iN$t = null;
    this.objsToClear_iN$t.length = 0
   }
   v.prototype.toString = function() {
    return d + this.varName + "=" + this.expression + ";"
   }, T.prototype.append = function(t, e) {
    this._exps.push([t, e])
   }, T.prototype.writeCode = function(t) {
    var e;
    if (this._exps.length > 1) t.push(p + "=" + this._targetExp + ";"), e = p;
    else {
     if (1 !== this._exps.length) return;
     e = this._targetExp
    }
    for (var i = 0; i < this._exps.length; i++) {
     var n = this._exps[i];
     y(t, e + x(n[0]) + "=", n[1])
    }
   }, T.pool = new o.Pool(function(t) {
    t._exps.length = 0, t._targetExp = null
   }, 1), T.pool.get = function(t) {
    var e = this._get() || new T;
    return e._targetExp = t, e
   };
   var A = C.prototype;
   A.getFuncModule = function(t, e) {
    var i = o.getClassName(t);
    if (i) {
     var n = this.funcModuleCache[i];
     if (n) return n;
     if (void 0 === n) {
      var r = -1 !== i.indexOf(".");
      if (r) try {
       if (r = t === Function("return " + i)()) return this.funcModuleCache[i] = i, i
      } catch (t) {}
     }
    }
    var s = this.funcs.indexOf(t);
    s < 0 && (s = this.funcs.length, this.funcs.push(t));
    var a = "F[" + s + "]";
    return e && (a = "(" + a + ")"), this.funcModuleCache[i] = a, a
   }, A.getObjRef = function(t) {
    var e = this.objs.indexOf(t);
    return e < 0 && (e = this.objs.length, this.objs.push(t)), "O[" + e + "]"
   }, A.setValueType = function(t, e, i, n) {
    var r = T.pool.get(n),
     s = e.constructor.__props__;
    s || (s = Object.keys(e));
    for (var a = 0; a < s.length; a++) {
     var o = s[a],
      c = i[o];
     if (e[o] !== c) {
      var h = this.enumerateField(i, o, c);
      r.append(o, h)
     }
    }
    r.writeCode(t), T.pool.put(r)
   }, A.enumerateCCClass = function(t, e, i) {
    for (var n = i.__values__, r = a.getClassAttrs(i), s = 0; s < n.length; s++) {
     var o = n[s],
      h = e[o],
      l = r[o + u];
     if (!E(l, h))
      if ("object" == typeof h && h instanceof cc.ValueType && (l = c.getDefault(l)) && l.constructor === h.constructor) {
       var _ = f + x(o);
       this.setValueType(t, l, h, _)
      } else this.setObjProp(t, e, o, h)
    }
   }, A.instantiateArray = function(t) {
    if (0 === t.length) return "[]";
    var e = "a" + ++this.localVariableId,
     i = [new v(e, "new Array(" + t.length + ")")];
    o.value(t, "_iN$t", {
     globalVar: "",
     source: i
    }, !0), this.objsToClear_iN$t.push(t);
    for (var n = 0; n < t.length; ++n) {
     y(i, e + "[" + n + "]=", this.enumerateField(t, n, t[n]))
    }
    return i
   }, A.enumerateField = function(t, e, i) {
    if ("object" == typeof i && i) {
     var r = i._iN$t;
     if (r) {
      var a = r.globalVar;
      if (!a) {
       a = r.globalVar = "v" + ++this.globalVariableId, this.globalVariables.push(a);
       var o = r.source[0];
       r.source[0] = g(a + "=", o)
      }
      return a
     }
     return Array.isArray(i) ? this.instantiateArray(i) : this.instantiateObj(i)
    }
    return "function" == typeof i ? this.getFuncModule(i) : "string" == typeof i ? _(i) : ("_objFlags" === e && t instanceof n && (i &= s), i)
   }, A.setObjProp = function(t, e, i, n) {
    y(t, f + x(i) + "=", this.enumerateField(e, i, n))
   }, A.enumerateObject = function(t, e) {
    var i = e.constructor;
    if (cc.Class._isCCClass(i)) this.enumerateCCClass(t, e, i);
    else
     for (var n in e)
      if (e.hasOwnProperty(n) && (95 !== n.charCodeAt(0) || 95 !== n.charCodeAt(1) || "__type__" === n)) {
       var r = e[n];
       "object" == typeof r && r && r === e._iN$t || this.setObjProp(t, e, n, r)
      }
   }, A.instantiateObj = function(t) {
    if (t instanceof cc.ValueType) return c.getNewValueTypeCode(t);
    if (t instanceof cc.Asset) return this.getObjRef(t);
    if (t._objFlags & r) return null;
    var e, i = t.constructor;
    if (cc.Class._isCCClass(i)) {
     if (this.parent)
      if (this.parent instanceof cc.Component) {
       if (t instanceof cc._BaseNode || t instanceof cc.Component) return this.getObjRef(t)
      } else if (this.parent instanceof cc._BaseNode)
      if (t instanceof cc._BaseNode) {
       if (!t.isChildOf(this.parent)) return this.getObjRef(t)
      } else if (t instanceof cc.Component && !t.node.isChildOf(this.parent)) return this.getObjRef(t);
     e = new v(f, "new " + this.getFuncModule(i, !0) + "()")
    } else if (i === Object) e = new v(f, "{}");
    else {
     if (i) return this.getObjRef(t);
     e = new v(f, "Object.create(null)")
    }
    var n = [e];
    return o.value(t, "_iN$t", {
     globalVar: "",
     source: n
    }, !0), this.objsToClear_iN$t.push(t), this.enumerateObject(n, t), ["(function(){", n, "return o;})();"]
   }, e.exports = {
    compile: function(t) {
     return new C(t, t instanceof cc._BaseNode && t).result
    },
    equalsToDefault: E
   }
  }), {
   "./CCClass": 108,
   "./CCObject": 114,
   "./attribute": 120,
   "./compiler": 122,
   "./js": 128
  }],
  127: [(function(t, e, i) {
   var n = t("./CCObject"),
    r = t("../value-types/value-type"),
    s = n.Flags.Destroyed,
    a = n.Flags.PersistentMask,
    o = t("./utils").isDomNode,
    c = t("./js");

   function h(t, e) {
    if (!e) {
     if ("object" != typeof t || Array.isArray(t)) return null;
     if (!t) return null;
     if (!cc.isValid(t)) return null;
     0
    }
    var i;
    if (t instanceof n) {
     if (t._instantiate) return cc.game._isCloning = !0, i = t._instantiate(), cc.game._isCloning = !1, i;
     if (t instanceof cc.Asset) return null
    }
    return cc.game._isCloning = !0, i = l(t), cc.game._isCloning = !1, i
   }
   var u = [];

   function l(t, e) {
    if (Array.isArray(t)) return null;
    if (o && o(t)) return null;
    var i;
    if (t._iN$t) i = t._iN$t;
    else if (t.constructor) {
     i = new(0, t.constructor)
    } else i = Object.create(null);
    _(t, i, e);
    for (var n = 0, r = u.length; n < r; ++n) u[n]._iN$t = null;
    return u.length = 0, i
   }

   function _(t, e, i) {
    c.value(t, "_iN$t", e, !0), u.push(t);
    var s = t.constructor;
    if (cc.Class._isCCClass(s))(function(t, e, i, n) {
     for (var s = t.__values__, a = 0; a < s.length; a++) {
      var o = s[a],
       c = e[o];
      if ("object" == typeof c && c) {
       var h = i[o];
       h instanceof r && h.constructor === c.constructor ? h.set(c) : i[o] = c._iN$t || d(c, n)
      } else i[o] = c
     }
    })(s, t, e, i);
    else
     for (var o in t)
      if (t.hasOwnProperty(o) && (95 !== o.charCodeAt(0) || 95 !== o.charCodeAt(1) || "__type__" === o)) {
       var h = t[o];
       if ("object" == typeof h && h) {
        if (h === e) continue;
        e[o] = h._iN$t || d(h, i)
       } else e[o] = h
      } t instanceof n && (e._objFlags &= a)
   }

   function d(t, e) {
    if (t instanceof r) return t.clone();
    if (t instanceof cc.Asset) return t;
    var i;
    if (Array.isArray(t)) {
     var n = t.length;
     i = new Array(n), c.value(t, "_iN$t", i, !0);
     for (var a = 0; a < n; ++a) {
      var o = t[a];
      i[a] = "object" == typeof o && o ? o._iN$t || d(o, e) : o
     }
     return u.push(t), i
    }
    if (t._objFlags & s) return null;
    var h = t.constructor;
    if (cc.Class._isCCClass(h)) {
     if (e)
      if (e instanceof cc.Component) {
       if (t instanceof cc._BaseNode || t instanceof cc.Component) return t
      } else if (e instanceof cc._BaseNode)
      if (t instanceof cc._BaseNode) {
       if (!t.isChildOf(e)) return t
      } else if (t instanceof cc.Component && !t.node.isChildOf(e)) return t;
     i = new h
    } else if (h === Object) i = {};
    else {
     if (h) return t;
     i = Object.create(null)
    }
    return _(t, i, e), i
   }
   h._clone = l, cc.instantiate = h, e.exports = h
  }), {
   "../value-types/value-type": 201,
   "./CCObject": 114,
   "./js": 128,
   "./utils": 132
  }],
  128: [(function(t, e, i) {
   var n = new(t("./id-generater"))("TmpCId.");

   function r(t, e) {
    for (; t;) {
     var i = Object.getOwnPropertyDescriptor(t, e);
     if (i) return i;
     t = Object.getPrototypeOf(t)
    }
    return null
   }

   function s(t, e, i) {
    var n = r(e, t);
    Object.defineProperty(i, t, n)
   }
   var a = {
     isNumber: function(t) {
      return "number" == typeof t || t instanceof Number
     },
     isString: function(t) {
      return "string" == typeof t || t instanceof String
     },
     addon: function(t) {
      "use strict";
      t = t || {};
      for (var e = 1, i = arguments.length; e < i; e++) {
       var n = arguments[e];
       if (n) {
        if ("object" != typeof n) {
         cc.errorID(5402, n);
         continue
        }
        for (var r in n) r in t || s(r, n, t)
       }
      }
      return t
     },
     mixin: function(t) {
      "use strict";
      t = t || {};
      for (var e = 1, i = arguments.length; e < i; e++) {
       var n = arguments[e];
       if (n) {
        if ("object" != typeof n) {
         cc.errorID(5403, n);
         continue
        }
        for (var r in n) s(r, n, t)
       }
      }
      return t
     },
     extend: function(t, e) {
      for (var i in e) e.hasOwnProperty(i) && (t[i] = e[i]);
      return t.prototype = Object.create(e.prototype, {
       constructor: {
        value: t,
        writable: !0,
        configurable: !0
       }
      }), t
     },
     getSuper: function(t) {
      var e = t.prototype,
       i = e && Object.getPrototypeOf(e);
      return i && i.constructor
     },
     isChildClassOf: function(t, e) {
      if (t && e) {
       if ("function" != typeof t) return !1;
       if ("function" != typeof e) return !1;
       if (t === e) return !0;
       for (;;) {
        if (!(t = a.getSuper(t))) return !1;
        if (t === e) return !0
       }
      }
      return !1
     },
     clear: function(t) {
      for (var e = Object.keys(t), i = 0; i < e.length; i++) delete t[e[i]]
     },
     isEmptyObject: function(t) {
      for (var e in t) return !1;
      return !0
     },
     getPropertyDescriptor: r
    },
    o = {
     value: void 0,
     enumerable: !1,
     writable: !1,
     configurable: !0
    };
   a.value = function(t, e, i, n, r) {
    o.value = i, o.writable = n, o.enumerable = r, Object.defineProperty(t, e, o), o.value = void 0
   };
   var c = {
    get: null,
    set: null,
    enumerable: !1
   };
   a.getset = function(t, e, i, n, r) {
    "function" != typeof n && (r = n, n = void 0), c.get = i, c.set = n, c.enumerable = r, Object.defineProperty(t, e, c), c.get = null, c.set = null
   };
   var h = {
    get: null,
    enumerable: !1,
    configurable: !1
   };
   a.get = function(t, e, i, n, r) {
    h.get = i, h.enumerable = n, h.configurable = r, Object.defineProperty(t, e, h), h.get = null
   };
   var u = {
    set: null,
    enumerable: !1,
    configurable: !1
   };
   a.set = function(t, e, i, n, r) {
    u.set = i, u.enumerable = n, u.configurable = r, Object.defineProperty(t, e, u), u.set = null
   }, a.getClassName = function(t) {
    if ("function" == typeof t) {
     var e = t.prototype;
     if (e && e.hasOwnProperty("__classname__") && e.__classname__) return e.__classname__;
     var i = "";
     if (t.name && (i = t.name), t.toString) {
      var n, r = t.toString();
      (n = "[" === r.charAt(0) ? r.match(/\[\w+\s*(\w+)\]/) : r.match(/function\s*(\w+)/)) && 2 === n.length && (i = n[1])
     }
     return "Object" !== i ? i : ""
    }
    return t && t.constructor ? a.getClassName(t.constructor) : ""
   }, (function() {
    var t = {},
     e = {};

    function i(t, e, i) {
     return a.getset(a, e, (function() {
       return Object.assign({}, i)
      }), (function(t) {
       a.clear(i), Object.assign(i, t)
      })),
      function(e, n) {
       if (n.prototype.hasOwnProperty(t) && delete i[n.prototype[t]], a.value(n.prototype, t, e), e) {
        var r = i[e];
        if (r && r !== n) {
         var s = "A Class already exists with the same " + t + ' : "' + e + '".';
         0, cc.error(s)
        } else i[e] = n
       }
      }
    }
    a._setClassId = i("__cid__", "_registeredClassIds", t);
    var r = i("__classname__", "_registeredClassNames", e);
    a.setClassName = function(t, e) {
     if (r(t, e), !e.prototype.hasOwnProperty("__cid__")) {
      var i = t || n.getNewId();
      i && a._setClassId(i, e)
     }
    }, a.unregisterClass = function() {
     for (var i = 0; i < arguments.length; i++) {
      var n = arguments[i].prototype,
       r = n.__cid__;
      r && delete t[r];
      var s = n.__classname__;
      s && delete e[s]
     }
    }, a._getClassById = function(e) {
     return t[e]
    }, a.getClassByName = function(t) {
     return e[t]
    }, a._getClassId = function(t, e) {
     if (e = void 0 === e || e, "function" == typeof t && t.prototype.hasOwnProperty("__cid__")) return t.prototype.__cid__;
     if (t && t.constructor) {
      var i = t.constructor.prototype;
      if (i && i.hasOwnProperty("__cid__")) return t.__cid__
     }
     return ""
    }
   })(), a.obsolete = function(t, e, i, n) {
    var r = /([^.]+)$/,
     s = r.exec(e)[0],
     o = r.exec(i)[0];

    function c() {
     return this[o]
    }
    n ? a.getset(t, s, c, (function(t) {
     this[o] = t
    })) : a.get(t, s, c)
   }, a.obsoletes = function(t, e, i, n) {
    for (var r in i) {
     var s = i[r];
     a.obsolete(t, e + "." + r, s, n)
    }
   };
   var l = /(%d)|(%s)/,
    _ = /%s/;

   function d(t, e) {
    t.splice(e, 1)
   }

   function f(t, e) {
    var i = t.indexOf(e);
    return i >= 0 && (d(t, i), !0)
   }
   a.formatStr = function() {
    var t = arguments.length;
    if (0 === t) return "";
    var e = arguments[0];
    if (1 === t) return "" + e;
    if ("string" == typeof e && l.test(e))
     for (var i = 1; i < t; ++i) {
      var n = arguments[i],
       r = "number" == typeof n ? l : _;
      r.test(e) ? e = e.replace(r, n) : e += " " + n
     } else
      for (var s = 1; s < t; ++s) e += " " + arguments[s];
    return e
   }, a.shiftArguments = function() {
    for (var t = arguments.length - 1, e = new Array(t), i = 0; i < t; ++i) e[i] = arguments[i + 1];
    return e
   }, a.createMap = function(t) {
    var e = Object.create(null);
    if (t) {
     e["."] = !0, e["/"] = !0, delete e["."], delete e["/"]
    }
    return e
   };
   var p = Array.prototype.indexOf;

   function m(t, e) {
    void 0 === e && (e = t, t = null), this.get = null, this.count = 0, this._pool = new Array(e), this._cleanup = t
   }
   a.array = {
    remove: f,
    fastRemove: function(t, e) {
     var i = t.indexOf(e);
     i >= 0 && (t[i] = t[t.length - 1], --t.length)
    },
    removeAt: d,
    fastRemoveAt: function(t, e) {
     var i = t.length;
     e < 0 || e >= i || (t[e] = t[i - 1], t.length = i - 1)
    },
    contains: function(t, e) {
     return t.indexOf(e) >= 0
    },
    verifyType: function(t, e) {
     if (t && t.length > 0)
      for (var i = 0; i < t.length; i++)
       if (!(t[i] instanceof e)) return cc.logID(1300), !1;
     return !0
    },
    removeArray: function(t, e) {
     for (var i = 0, n = e.length; i < n; i++) f(t, e[i])
    },
    appendObjectsAt: function(t, e, i) {
     return t.splice.apply(t, [i, 0].concat(e)), t
    },
    copy: function(t) {
     var e, i = t.length,
      n = new Array(i);
     for (e = 0; e < i; e += 1) n[e] = t[e];
     return n
    },
    indexOf: p,
    MutableForwardIterator: t("../utils/mutable-forward-iterator")
   }, m.prototype._get = function() {
    if (this.count > 0) {
     --this.count;
     var t = this._pool[this.count];
     return this._pool[this.count] = null, t
    }
    return null
   }, m.prototype.put = function(t) {
    var e = this._pool;
    if (this.count < e.length) {
     if (this._cleanup && !1 === this._cleanup(t)) return;
     e[this.count] = t, ++this.count
    }
   }, m.prototype.resize = function(t) {
    t >= 0 && (this._pool.length = t, this.count > t && (this.count = t))
   }, a.Pool = m, cc.js = a, e.exports = a
  }), {
   "../utils/mutable-forward-iterator": 187,
   "./id-generater": 124
  }],
  129: [(function(t, e, i) {
   var n = t("./js"),
    r = {
     url: {
      canUsedInGet: !0
     },
     default: {},
     serializable: {},
     editorOnly: {},
     formerlySerializedAs: {}
    };

   function s(t, e, i, n) {
    if (!t.get && !t.set)
     if (t.hasOwnProperty("default")) {
      var s = "_N$" + e;
      t.get = function() {
       return this[s]
      }, t.set = function(t) {
       var e = this[s];
       this[s] = t, i.call(this, e)
      };
      var a = {};
      for (var o in n[s] = a, r) {
       var c = r[o];
       t.hasOwnProperty(o) && (a[o] = t[o], c.canUsedInGet || delete t[o])
      }
     } else 0
   }

   function a(t, e, i, n) {
    Array.isArray(n) && n.length > 0 && (n = n[0]), t.type = n
   }

   function o(t, e, i, n) {
    if (Array.isArray(e)) {
     if (!(e.length > 0)) return cc.errorID(5508, i, n);
     if (cc.RawAsset.isRawAssetType(e[0])) return t.url = e[0], void delete t.type;
     t.type = e = e[0]
    }
   }
   i.getFullFormOfProperty = function(t, e, i) {
    if (!(t && t.constructor === Object)) {
     if (Array.isArray(t) && t.length > 0) {
      var r = t[0];
      return {
       default: [],
       type: t,
       _short: !0
      }
     }
     if ("function" == typeof t) {
      r = t;
      return cc.RawAsset.isRawAssetType(r) || cc.RawAsset.wasRawAssetType(r) ? {
       default: "",
       url: r,
       _short: !0
      } : {
       default: n.isChildClassOf(r, cc.ValueType) ? new r : null,
       type: r,
       _short: !0
      }
     }
     return {
      default: t,
      _short: !0
     }
    }
    return null
   }, i.preprocessAttrs = function(t, e, n, r) {
    for (var c in t) {
     var h = t[c],
      u = i.getFullFormOfProperty(h, c, e);
     if (u && (h = t[c] = u), h) {
      var l = h.notify;
      l && s(h, c, l, t), "type" in h && o(h, h.type, e, c), "url" in h && a(h, 0, 0, h.url), "type" in h && h.type
     }
    }
   }, i.validateMethodWithProps = function(t, e, i, n, r) {
    return "function" == typeof t || null === t
   }
  }), {
   "./CCClass": 108,
   "./js": 128
  }],
  130: [(function(t, e, i) {
   var n = [];
   cc._RF = {
    push: function(t, e, i) {
     void 0 === i && (i = e, e = ""), n.push({
      uuid: e,
      script: i,
      module: t,
      exports: t.exports,
      beh: null
     })
    },
    pop: function() {
     var t = n.pop(),
      e = t.module,
      i = e.exports;
     if (i === t.exports) {
      for (var r in i) return;
      e.exports = i = t.cls
     }
    },
    peek: function() {
     return n[n.length - 1]
    }
   }
  }), {}],
  131: [(function(t, e, i) {
   cc.url = {
    _rawAssets: "",
    normalize: function(t) {
     return t && (46 === t.charCodeAt(0) && 47 === t.charCodeAt(1) ? t = t.slice(2) : 47 === t.charCodeAt(0) && (t = t.slice(1))), t
    },
    raw: function(t) {
     if ((t = this.normalize(t)).startsWith("resources/")) {
      var e = cc.loader._getResUuid(t.slice(10), cc.Asset, !0);
      if (e) return cc.AssetLibrary.getLibUrlNoExt(e, !0) + cc.path.extname(t)
     } else cc.errorID(7002, t);
     return this._rawAssets + t
    },
    _init: function(t) {
     this._rawAssets = cc.path.stripSep(t) + "/"
    }
   }, e.exports = cc.url
  }), {}],
  132: [(function(t, e, i) {
   t("./js");
   e.exports = {
    contains: function(t, e) {
     if ("function" == typeof t.contains) return t.contains(e);
     if ("function" == typeof t.compareDocumentPosition) return !!(16 & t.compareDocumentPosition(e));
     var i = e.parentNode;
     if (i)
      do {
       if (i === t) return !0;
       i = i.parentNode
      } while (null !== i);
     return !1
    },
    isDomNode: "object" == typeof window && ("function" == typeof Node ? function(t) {
     return t instanceof Node
    } : function(t) {
     return t && "object" == typeof t && "number" == typeof t.nodeType && "string" == typeof t.nodeName
    }),
    callInNextTick: function(t, e, i) {
     t && setTimeout((function() {
      t(e, i)
     }), 0)
    }
   }
  }), {
   "./js": 128
  }],
  133: [(function(t, e, i) {
   t("./platform/js"), t("./value-types"), t("./utils"), t("./platform/CCInputManager"), t("./platform/CCInputExtension"), t("./event"), t("./platform/CCSys"), t("./platform/CCMacro"), t("./load-pipeline"), t("./CCDirector"), t("./renderer"), t("./platform/CCView"), t("./platform/CCScreen"), t("./CCScheduler"), t("./event-manager")
  }), {
   "./CCDirector": 22,
   "./CCScheduler": 27,
   "./event": 79,
   "./event-manager": 75,
   "./load-pipeline": 94,
   "./platform/CCInputExtension": 111,
   "./platform/CCInputManager": 112,
   "./platform/CCMacro": 113,
   "./platform/CCScreen": 116,
   "./platform/CCSys": 117,
   "./platform/CCView": 118,
   "./platform/js": 128,
   "./renderer": 149,
   "./utils": 184,
   "./value-types": 196
  }],
  134: [(function(t, e, i) {
   t("../../platform/js");
   var n = t("../render-flow");
   t("./renderers");
   var r = function(t, e) {
    this._device = t, this._camera = e, this.parentOpacity = 1, this.parentOpacityDirty = 0, this.worldMatDirty = 0, n.init(this)
   };
   r.prototype = {
    constructor: r,
    reset: function() {},
    _commitComp: function(t, e) {
     var i = this._device._ctx,
      n = this._camera;
     i.setTransform(n.a, n.b, n.c, n.d, n.tx, n.ty), i.scale(1, -1), e.draw(i, t)
    },
    visit: function(t) {
     var e = this._device._ctx,
      i = this._device._canvas,
      r = cc.Camera.main.backgroundColor,
      s = "rgba(" + r.r + ", " + r.g + ", " + r.b + ", " + r.a / 255 + ")";
     e.fillStyle = s, e.setTransform(1, 0, 0, 1, 0, 0), e.clearRect(0, 0, i.width, i.height), e.fillRect(0, 0, i.width, i.height), this._device._stats.drawcalls = 0, n.render(t)
    }
   }, e.exports = r
  }), {
   "../../platform/js": 128,
   "../render-flow": 151,
   "./renderers": 139
  }],
  135: [(function(t, e, i) {
   var n = function() {};
   n.prototype = {
    constructor: n,
    _reset: function() {},
    render: function() {}
   }, e.exports = n
  }), {}],
  136: [(function(t, e, i) {
   e.exports = {
    ForwardRenderer: t("./forward-renderer"),
    RenderComponentWalker: t("./canvas-render-walker"),
    _renderers: t("./renderers")
   }
  }), {
   "./canvas-render-walker": 134,
   "./forward-renderer": 135,
   "./renderers": 139
  }],
  137: [(function(t, e, i) {
   var n = t("../../../../graphics/helper"),
    r = t("../../../../graphics/types"),
    s = t("../../../../platform/js"),
    a = (r.PointFlags, r.LineJoin),
    o = r.LineCap;

   function c() {
    this.cmds = [], this.style = {
     strokeStyle: "black",
     fillStyle: "white",
     lineCap: "butt",
     lineJoin: "miter",
     miterLimit: 10
    }
   }
   var h = c.prototype;
   s.mixin(h, {
    moveTo: function(t, e) {
     this.cmds.push(["moveTo", [t, e]])
    },
    lineTo: function(t, e) {
     this.cmds.push(["lineTo", [t, e]])
    },
    bezierCurveTo: function(t, e, i, n, r, s) {
     this.cmds.push(["bezierCurveTo", [t, e, i, n, r, s]])
    },
    quadraticCurveTo: function(t, e, i, n) {
     this.cmds.push(["quadraticCurveTo", [t, e, i, n]])
    },
    arc: function(t, e, i, r, s, a) {
     n.arc(this, t, e, i, r, s, a)
    },
    ellipse: function(t, e, i, r) {
     n.ellipse(this, t, e, i, r)
    },
    circle: function(t, e, i) {
     n.ellipse(this, t, e, i, i)
    },
    rect: function(t, e, i, n) {
     this.moveTo(t, e), this.lineTo(t, e + n), this.lineTo(t + i, e + n), this.lineTo(t + i, e), this.close()
    },
    roundRect: function(t, e, i, r, s) {
     n.roundRect(this, t, e, i, r, s)
    },
    clear: function(t, e) {
     this.cmds.length = 0
    },
    close: function() {
     this.cmds.push(["closePath", []])
    },
    stroke: function() {
     this.cmds.push(["stroke", []])
    },
    fill: function() {
     this.cmds.push(["fill", []])
    }
   }), s.set(h, "strokeColor", (function(t) {
    var e = "rgba(" + (0 | t.r) + "," + (0 | t.g) + "," + (0 | t.b) + "," + t.a / 255 + ")";
    this.cmds.push(["strokeStyle", e]), this.style.strokeStyle = e
   })), s.set(h, "fillColor", (function(t) {
    var e = "rgba(" + (0 | t.r) + "," + (0 | t.g) + "," + (0 | t.b) + "," + t.a / 255 + ")";
    this.cmds.push(["fillStyle", e]), this.style.fillStyle = e
   })), s.set(h, "lineWidth", (function(t) {
    this.cmds.push(["lineWidth", t]), this.style.lineWidth = t
   })), s.set(h, "lineCap", (function(t) {
    var e = "butt";
    t === o.BUTT ? e = "butt" : t === o.ROUND ? e = "round" : t === o.SQUARE && (e = "square"), this.cmds.push(["lineCap", e]), this.style.lineCap = e
   })), s.set(h, "lineJoin", (function(t) {
    var e = "bevel";
    t === a.BEVEL ? e = "bevel" : t === a.ROUND ? e = "round" : t === a.MITER && (e = "miter"), this.cmds.push(["lineJoin", e]), this.style.lineJoin = e
   })), s.set(h, "miterLimit", (function(t) {
    this.cmds.push(["miterLimit", t]), this.style.miterLimit = t
   })), e.exports = c
  }), {
   "../../../../graphics/helper": 82,
   "../../../../graphics/types": 84,
   "../../../../platform/js": 128
  }],
  138: [(function(t, e, i) {
   var n = t("./impl");
   e.exports = {
    createImpl: function() {
     return new n
    },
    draw: function(t, e) {
     var i = e.node,
      n = i._worldMatrix,
      r = n.m00,
      s = n.m01,
      a = n.m04,
      o = n.m05,
      c = n.m12,
      h = n.m13;
     t.transform(r, s, a, o, c, h), t.save(), t.globalAlpha = i.opacity / 255;
     var u = e._impl.style;
     t.strokeStyle = u.strokeStyle, t.fillStyle = u.fillStyle, t.lineWidth = u.lineWidth, t.lineJoin = u.lineJoin, t.miterLimit = u.miterLimit;
     for (var l = !0, _ = e._impl.cmds, d = 0, f = _.length; d < f; d++) {
      var p = _[d],
       m = p[0],
       v = p[1];
      "moveTo" === m && l ? (t.beginPath(), l = !1) : "fill" !== m && "stroke" !== m && "fillRect" !== m || (l = !0), "function" == typeof t[m] ? t[m].apply(t, v) : t[m] = v
     }
     return t.restore(), 1
    },
    stroke: function(t) {
     t._impl.stroke()
    },
    fill: function(t) {
     t._impl.fill()
    }
   }
  }), {
   "./impl": 137
  }],
  139: [(function(t, e, i) {
   var n = t("../../../platform/js"),
    r = t("../../../components/CCSprite"),
    s = t("../../../components/CCLabel"),
    a = t("../../../components/CCMask"),
    o = t("../../../graphics/graphics"),
    c = t("./sprite"),
    h = t("./label"),
    u = t("./graphics"),
    l = t("./mask"),
    _ = {},
    d = {};

   function f(t, e, i) {
    var r = n.getClassName(t);
    _[r] = e, i && (d[r] = i), t._assembler = e, t._postAssembler = i
   }
   f(r, c), f(s, h), a && f(a, l.beforeHandler, l.afterHandler), o && f(o, u), e.exports = {
    map: _,
    postMap: d,
    addRenderer: f
   }
  }), {
   "../../../components/CCLabel": 56,
   "../../../components/CCMask": 59,
   "../../../components/CCSprite": 64,
   "../../../graphics/graphics": 81,
   "../../../platform/js": 128,
   "./graphics": 138,
   "./label": 141,
   "./mask": 143,
   "./sprite": 144
  }],
  140: [(function(t, e, i) {
   var n = t("../../../utils/label/bmfont"),
    r = t("../../../../platform/js");
   t("../utils");
   e.exports = r.addon({
    createData: function(t) {
     return t.requestRenderData()
    },
    appendQuad: function(t, e, i, n, r, s, a) {
     var o = t.dataLength;
     t.dataLength += 2;
     var c = t._data,
      h = (e.width, e.height, i.width),
      u = i.height,
      l = void 0,
      _ = void 0,
      d = void 0,
      f = void 0;
     n ? (l = i.x, d = i.x + u, _ = i.y, f = i.y + h, c[o].u = l, c[o].v = f, c[o + 1].u = l, c[o + 1].v = _) : (l = i.x, d = i.x + h, _ = i.y, f = i.y + u, c[o].u = l, c[o].v = _, c[o + 1].u = d, c[o + 1].v = f), c[o].x = r, c[o].y = s - u * a, c[o + 1].x = r + h * a, c[o + 1].y = s
    },
    draw: function(t, e) {
     var i = e.node,
      n = i._worldMatrix,
      r = n.m00,
      s = n.m01,
      a = n.m04,
      o = n.m05,
      c = n.m12,
      h = n.m13;
     t.transform(r, s, a, o, c, h), t.scale(1, -1), t.globalAlpha = i.opacity / 255;
     for (var u = e._texture, l = e._renderData._data, _ = u.getHtmlElementObj(), d = 0, f = l.length; d < f; d += 2) {
      var p = l[d].x,
       m = l[d].y,
       v = l[d + 1].x - p,
       g = l[d + 1].y - m;
      m = -m - g;
      var y = l[d].u,
       T = l[d].v,
       E = l[d + 1].u - y,
       x = l[d + 1].v - T;
      t.drawImage(_, y, T, E, x, p, m, v, g)
     }
     return 1
    }
   }, n)
  }), {
   "../../../../platform/js": 128,
   "../../../utils/label/bmfont": 154,
   "../utils": 148
  }],
  141: [(function(t, e, i) {
   var n = t("./ttf"),
    r = t("./bmfont");
   e.exports = {
    getAssembler: function(t) {
     var e = n;
     return t.font instanceof cc.BitmapFont && (e = r), e
    },
    createData: function(t) {
     return t._assembler.createData(t)
    },
    draw: function(t, e) {
     if (!e._texture) return 0;
     var i = e._assembler;
     return i ? (i.updateRenderData(e), i.draw(t, e)) : 0
    }
   }
  }), {
   "./bmfont": 140,
   "./ttf": 142
  }],
  142: [(function(t, e, i) {
   var n = t("../../../utils/label/ttf"),
    r = t("../../../../platform/js"),
    s = t("../utils");
   e.exports = r.addon({
    createData: function(t) {
     var e = t.requestRenderData();
     return e.dataLength = 2, e
    },
    _updateVerts: function(t) {
     var e = t._renderData,
      i = t.node,
      n = i.width,
      r = i.height,
      s = i.anchorX * n,
      a = i.anchorY * r,
      o = e._data;
     o[0].x = -s, o[0].y = -a, o[1].x = n - s, o[1].y = r - a
    },
    _updateTexture: function(t) {
     n._updateTexture(t);
     var e = t._frame._texture;
     s.dropColorizedImage(e, t.node.color)
    },
    draw: function(t, e) {
     var i = e.node,
      n = i._worldMatrix,
      r = n.m00,
      s = n.m01,
      a = n.m04,
      o = n.m05,
      c = n.m12,
      h = n.m13;
     t.transform(r, s, a, o, c, h), t.scale(1, -1), t.globalAlpha = i.opacity / 255;
     var u = e._frame._texture,
      l = e._renderData._data,
      _ = u.getHtmlElementObj(),
      d = l[0].x,
      f = l[0].y,
      p = l[1].x - d,
      m = l[1].y - f;
     return f = -f - m, t.drawImage(_, d, f, p, m), 1
    }
   }, n)
  }), {
   "../../../../platform/js": 128,
   "../../../utils/label/ttf": 156,
   "../utils": 148
  }],
  143: [(function(t, e, i) {
   t("../../../components/CCMask");
   var n = t("./graphics"),
    r = {
     updateRenderData: function(t) {},
     draw: function(t, e) {
      t.save(), n.draw(t, e._graphics), t.clip()
     }
    };
   e.exports = {
    beforeHandler: r,
    afterHandler: {
     updateRenderData: function(t) {},
     draw: function(t, e) {
      t.restore()
     }
    }
   }
  }), {
   "../../../components/CCMask": 59,
   "./graphics": 138
  }],
  144: [(function(t, e, i) {
   var n = t("../../../../components/CCSprite"),
    r = n.Type,
    s = n.FillType,
    a = t("./simple"),
    o = t("./sliced"),
    c = t("./tiled");
   e.exports = {
    getAssembler: function(t) {
     switch (t.type) {
      case r.SIMPLE:
       return a;
      case r.SLICED:
       return o;
      case r.TILED:
       return c;
      case r.FILLED:
       return t._fillType, s.RADIAL, null
     }
    },
    createData: function(t) {
     return t._assembler.createData(t)
    }
   }
  }), {
   "../../../../components/CCSprite": 64,
   "./simple": 145,
   "./sliced": 146,
   "./tiled": 147
  }],
  145: [(function(t, e, i) {
   var n = t("../utils"),
    r = {
     createData: function(t) {
      var e = t.requestRenderData();
      return e.dataLength = 2, e
     },
     updateRenderData: function(t) {
      t._material || t._activateMaterial();
      var e = t._renderData;
      e.uvDirty && this.updateUVs(t), e.vertDirty && this.updateVerts(t)
     },
     updateUVs: function(t) {
      var e = t.spriteFrame,
       i = t._renderData,
       n = i._data,
       r = e._rect;
      e._texture;
      if (e._rotated) {
       var s = r.x,
        a = r.height,
        o = r.y,
        c = r.width;
       n[0].u = s, n[0].v = c, n[1].u = a, n[1].v = o
      } else {
       var h = r.x,
        u = r.width,
        l = r.y,
        _ = r.height;
       n[0].u = h, n[0].v = l, n[1].u = u, n[1].v = _
      }
      i.uvDirty = !1
     },
     updateVerts: function(t) {
      var e = t._renderData,
       i = t.node,
       n = e._data,
       r = i.width,
       s = i.height,
       a = i.anchorX * r,
       o = i.anchorY * s,
       c = void 0,
       h = void 0,
       u = void 0,
       l = void 0;
      if (t.trim) c = -a, h = -o, u = r, l = s;
      else {
       var _ = t.spriteFrame,
        d = _._originalSize.width,
        f = _._originalSize.height,
        p = _._rect.width,
        m = _._rect.height,
        v = _._offset,
        g = r / d,
        y = s / f,
        T = v.x + (d - p) / 2,
        E = (v.x, v.y + (f - m) / 2);
       v.y;
       c = T * g - a, h = E * y - o, u = r, l = s
      }
      n[0].x = c, n[0].y = h, n[1].x = u, n[1].y = l, e.vertDirty = !1
     },
     draw: function(t, e) {
      var i = e.node,
       r = i._worldMatrix,
       s = r.m00,
       a = r.m01,
       o = r.m04,
       c = r.m05,
       h = r.m12,
       u = r.m13;
      t.transform(s, a, o, c, h, u), t.scale(1, -1), t.globalAlpha = i.opacity / 255;
      var l = e._spriteFrame._texture,
       _ = e._renderData._data,
       d = n.getColorizedImage(l, i._color),
       f = _[0].x,
       p = _[0].y,
       m = _[1].x,
       v = _[1].y;
      p = -p - v;
      var g = _[0].u,
       y = _[0].v,
       T = _[1].u,
       E = _[1].v;
      return t.drawImage(d, g, y, T, E, f, p, m, v), 1
     }
    };
   e.exports = r
  }), {
   "../utils": 148
  }],
  146: [(function(t, e, i) {
   var n = t("../utils"),
    r = {
     createData: function(t) {
      var e = t.requestRenderData();
      return e.dataLength = 4, e
     },
     updateRenderData: t("./simple").updateRenderData,
     updateUVs: function(t) {
      var e = t.spriteFrame,
       i = t._renderData,
       n = e._rect,
       r = (e._texture, e.insetLeft),
       s = e.insetRight,
       a = n.width - r - s,
       o = e.insetTop,
       c = e.insetBottom,
       h = n.height - o - c,
       u = i._data;
      e._rotated ? (u[0].u = n.x, u[1].u = c + n.x, u[2].u = c + h + n.x, u[3].u = n.x + n.height, u[3].v = n.y, u[2].v = r + n.y, u[1].v = r + a + n.y, u[0].v = n.y + n.width) : (u[0].u = n.x, u[1].u = r + n.x, u[2].u = r + a + n.x, u[3].u = n.x + n.width, u[3].v = n.y, u[2].v = o + n.y, u[1].v = o + h + n.y, u[0].v = n.y + n.height), i.uvDirty = !1
     },
     updateVerts: function(t) {
      var e = t._renderData,
       i = e._data,
       n = t.node,
       r = n.width,
       s = n.height,
       a = n.anchorX * r,
       o = n.anchorY * s,
       c = t.spriteFrame,
       h = (c._rect, c.insetLeft),
       u = c.insetRight,
       l = c.insetTop,
       _ = c.insetBottom,
       d = r - h - u,
       f = s - l - _,
       p = r / (h + u),
       m = s / (l + _);
      p = isNaN(p) || p > 1 ? 1 : p, m = isNaN(m) || m > 1 ? 1 : m, d = d < 0 ? 0 : d, f = f < 0 ? 0 : f, i[0].x = -a, i[0].y = -o, i[1].x = h * p - a, i[1].y = _ * m - o, i[2].x = i[1].x + d, i[2].y = i[1].y + f, i[3].x = r - a, i[3].y = s - o, e.vertDirty = !1
     },
     draw: function(t, e) {
      var i = e.node,
       r = i._worldMatrix,
       s = r.m00,
       a = r.m01,
       o = r.m04,
       c = r.m05,
       h = r.m12,
       u = r.m13;
      t.transform(s, a, o, c, h, u), t.scale(1, -1), t.globalAlpha = i.opacity / 255;
      for (var l = e._spriteFrame._texture, _ = e._renderData._data, d = n.getColorizedImage(l, i._color), f = 0, p = void 0, m = void 0, v = void 0, g = void 0, y = void 0, T = void 0, E = void 0, x = void 0, C = void 0, A = void 0, b = void 0, S = void 0, w = 0; w < 3; ++w) {
       g = _[w], v = _[w + 1];
       for (var D = 0; D < 3; ++D) p = _[D], m = _[D + 1], y = p.x, T = g.y, E = m.x - y, T = -T - (x = v.y - T), C = p.u, A = v.v, b = m.u - C, S = g.v - A, b > 0 && S > 0 && E > 0 && x > 0 && (t.drawImage(d, C, A, b, S, y, T, E, x), f++)
      }
      return f
     }
    };
   e.exports = r
  }), {
   "../utils": 148,
   "./simple": 145
  }],
  147: [(function(t, e, i) {
   var n = t("../utils"),
    r = (t("./simple"), {
     createData: function(t) {
      return t.requestRenderData()
     },
     updateRenderData: function(t) {
      t._material || t._activateMaterial()
     },
     draw: function(t, e) {
      var i = e.node,
       r = i._worldMatrix,
       s = r.m00,
       a = r.m01,
       o = r.m04,
       c = r.m05,
       h = r.m12,
       u = r.m13;
      t.transform(s, a, o, c, h, u), t.scale(1, -1), t.globalAlpha = i.opacity / 255;
      var l = e.spriteFrame,
       _ = l._rect,
       d = l._texture,
       f = _.x,
       p = _.y,
       m = l._rotated ? _.height : _.width,
       v = l._rotated ? _.width : _.height,
       g = n.getFrameCache(d, i._color, f, p, m, v),
       y = i.width,
       T = i.height,
       E = -i.anchorX * y,
       x = -i.anchorY * T;
      return x = -x - T, t.translate(E, x), t.fillStyle = t.createPattern(g, "repeat"), t.fillRect(0, 0, y, T), 1
     }
    });
   e.exports = r
  }), {
   "../utils": 148,
   "./simple": 145
  }],
  148: [(function(t, e, i) {
   function n(t, e, i, n, r, s, a) {
    if (cc.sys.browserType === cc.sys.BROWSER_TYPE_WECHAT_GAME_SUB) return t;
    var o = e._image,
     c = t.getContext("2d");
    return t.width = s, t.height = a, c.globalCompositeOperation = "source-over", c.fillStyle = "rgb(" + i.r + "," + i.g + "," + i.b + ")", c.fillRect(0, 0, s, a), c.globalCompositeOperation = "multiply", c.drawImage(o, n, r, s, a, 0, 0, s, a), c.globalCompositeOperation = "destination-atop", c.drawImage(o, n, r, s, a, 0, 0, s, a), t
   }
   var r = {
    canvasMap: {},
    canvasUsed: {},
    canvasPool: [],
    checking: !1,
    check: function() {
     var t = !1;
     for (var e in this.canvasUsed)
      if (t = !0, this.canvasUsed[e]) this.canvasUsed[e] = !1;
      else {
       var i = this.canvasMap[e];
       i.width = 0, i.height = 0, this.canvasPool.length < 32 && this.canvasPool.push(i), delete this.canvasMap[e], delete this.canvasUsed[e]
      } t || (cc.director.off(cc.Director.EVENT_AFTER_DRAW, this.check, this), this.checking = !1)
    },
    startCheck: function() {
     cc.director.on(cc.Director.EVENT_AFTER_DRAW, this.check, this), this.checking = !0
    },
    getCanvas: function(t) {
     return this.canvasUsed[t] = !0, this.canvasMap[t]
    },
    cacheCanvas: function(t, e) {
     this.canvasMap[e] = t, this.canvasUsed[e] = !0, this.checking || this.startCheck()
    },
    dropImage: function(t) {
     this.canvasMap[t] && delete this.canvasMap[t]
    }
   };
   e.exports = {
    getColorizedImage: function(t, e) {
     if (!t) return null;
     if (0 === t.width || 0 === t.height) return t._image;
     var i = 16777215 & e._val;
     if (16777215 === i) return t._image;
     var s = t.url + i,
      a = r.getCanvas(s);
     return a || (n(a = r.canvasPool.pop() || document.createElement("canvas"), t, e, 0, 0, t.width, t.height), r.cacheCanvas(a, s)), a
    },
    getFrameCache: function(t, e, i, s, a, o) {
     if (!t || !t.url || i < 0 || s < 0 || a <= 0 || o <= 0) return null;
     var c = t.url,
      h = !1,
      u = 16777215 & e._val;
     if (16777215 !== u && (c += u, h = !0), (0 !== i || 0 !== s && a !== t.width && o !== t.height) && (c += "_" + i + "_" + s + "_" + a + "_" + o, h = !0), !h) return t._image;
     var l = r.getCanvas(c);
     return l || (n(l = r.canvasPool.pop() || document.createElement("canvas"), t, e, i, s, a, o), r.cacheCanvas(l, c)), l
    },
    dropColorizedImage: function(t, e) {
     var i = t.url + (16777215 & e._val);
     r.dropImage(i)
    }
   }
  }), {}],
  149: [(function(t, e, i) {
   var n = t("./render-engine");
   n.math.vec3.create();
   cc.renderer = e.exports = {
    renderEngine: n,
    Texture2D: null,
    canvas: null,
    device: null,
    scene: null,
    drawCalls: 0,
    _walker: null,
    _cameraNode: null,
    _camera: null,
    _forward: null,
    initWebGL: function(e, i) {
     t("./webgl/assemblers");
     var r = t("./webgl/render-component-walker");
     this.Texture2D = n.Texture2D, this.canvas = e, this.device = new n.Device(e, i), this.scene = new n.Scene, this._walker = new r(this.device, this.scene);
     var s = (function(t) {
      return {
       defaultTexture: new n.Texture2D(t, {
        images: [],
        width: 128,
        height: 128,
        wrapS: n.gfx.WRAP_REPEAT,
        wrapT: n.gfx.WRAP_REPEAT,
        format: n.gfx.TEXTURE_FMT_RGB8,
        mipmap: !1
       }),
       programTemplates: n.shaders.templates,
       programChunks: n.shaders.chunks
      }
     })(this.device);
     this._forward = new n.ForwardRenderer(this.device, s)
    },
    initCanvas: function(e) {
     var i = t("./canvas");
     this.Texture2D = n.Texture2D, this.canvas = e, this.device = new n.Device(e), this._camera = {
      a: 1,
      b: 0,
      c: 0,
      d: 1,
      tx: 0,
      ty: 0
     }, this._walker = new i.RenderComponentWalker(this.device, this._camera), this._forward = new i.ForwardRenderer
    },
    updateCameraViewport: function() {
     if (cc.director) {
      var t = cc.director.getScene();
      t.scaleX = t.scaleY = 1
     }
     if (cc.game.renderType === cc.game.RENDER_TYPE_CANVAS) {
      var e = cc.view.getViewportRect();
      this.device.setViewport(e.x, e.y, e.width, e.height), this._camera.a = cc.view.getScaleX(), this._camera.d = cc.view.getScaleY(), this._camera.tx = e.x, this._camera.ty = e.y + e.height
     } else {}
    },
    render: function(t) {
     this.device._stats.drawcalls = 0, t && (this._walker.visit(t), this._forward.render(this.scene), this.drawCalls = this.device._stats.drawcalls)
    },
    clear: function() {
     this._walker.reset(), this._forward._reset()
    }
   }
  }), {
   "./canvas": 136,
   "./render-engine": 150,
   "./webgl/assemblers": 160,
   "./webgl/render-component-walker": 175
  }],
  150: [(function(t, e, i) {
   "use strict";
   var n = Math.PI / 180,
    r = 180 / Math.PI,
    s = 1e-6;

   function a(t) {
    return t * n
   }
   var o = Math.random;

   function c(t, e) {
    return Math.random() * (e - t) + t
   }
   var h = 32,
    u = -1 << h - 1;

   function l(t) {
    var e = 32;
    return (t &= -t) && e--, 65535 & t && (e -= 16), 16711935 & t && (e -= 8), 252645135 & t && (e -= 4), 858993459 & t && (e -= 2), 1431655765 & t && (e -= 1), e
   }
   var _ = new Array(256);
   (function(t) {
    for (var e = 0; e < 256; ++e) {
     var i = e,
      n = e,
      r = 7;
     for (i >>>= 1; i; i >>>= 1) n <<= 1, n |= 1 & i, --r;
     t[e] = n << r & 255
    }
   })(_);
   var d = Object.freeze({
     INT_BITS: h,
     INT_MAX: 2147483647,
     INT_MIN: u,
     sign: function(t) {
      return (t > 0) - (t < 0)
     },
     abs: function(t) {
      var e = t >> h - 1;
      return (t ^ e) - e
     },
     min: function(t, e) {
      return e ^ (t ^ e) & -(t < e)
     },
     max: function(t, e) {
      return t ^ (t ^ e) & -(t < e)
     },
     isPow2: function(t) {
      return !(t & t - 1 || !t)
     },
     log2: function(t) {
      var e, i;
      return e = (t > 65535) << 4, e |= i = ((t >>>= e) > 255) << 3, e |= i = ((t >>>= i) > 15) << 2, (e |= i = ((t >>>= i) > 3) << 1) | (t >>>= i) >> 1
     },
     log10: function(t) {
      return t >= 1e9 ? 9 : t >= 1e8 ? 8 : t >= 1e7 ? 7 : t >= 1e6 ? 6 : t >= 1e5 ? 5 : t >= 1e4 ? 4 : t >= 1e3 ? 3 : t >= 100 ? 2 : t >= 10 ? 1 : 0
     },
     popCount: function(t) {
      return 16843009 * ((t = (858993459 & (t -= t >>> 1 & 1431655765)) + (t >>> 2 & 858993459)) + (t >>> 4) & 252645135) >>> 24
     },
     countTrailingZeros: l,
     nextPow2: function(t) {
      return t += 0 === t, --t, t |= t >>> 1, t |= t >>> 2, t |= t >>> 4, t |= t >>> 8, 1 + (t |= t >>> 16)
     },
     prevPow2: function(t) {
      return t |= t >>> 1, t |= t >>> 2, t |= t >>> 4, t |= t >>> 8, (t |= t >>> 16) - (t >>> 1)
     },
     parity: function(t) {
      return t ^= t >>> 16, t ^= t >>> 8, t ^= t >>> 4, 27030 >>> (t &= 15) & 1
     },
     reverse: function(t) {
      return _[255 & t] << 24 | _[t >>> 8 & 255] << 16 | _[t >>> 16 & 255] << 8 | _[t >>> 24 & 255]
     },
     interleave2: function(t, e) {
      return (t = 1431655765 & ((t = 858993459 & ((t = 252645135 & ((t = 16711935 & ((t &= 65535) | t << 8)) | t << 4)) | t << 2)) | t << 1)) | (e = 1431655765 & ((e = 858993459 & ((e = 252645135 & ((e = 16711935 & ((e &= 65535) | e << 8)) | e << 4)) | e << 2)) | e << 1)) << 1
     },
     deinterleave2: function(t, e) {
      return (t = 65535 & ((t = 16711935 & ((t = 252645135 & ((t = 858993459 & ((t = t >>> e & 1431655765) | t >>> 1)) | t >>> 2)) | t >>> 4)) | t >>> 16)) << 16 >> 16
     },
     interleave3: function(t, e, i) {
      return t = 1227133513 & ((t = 3272356035 & ((t = 251719695 & ((t = 4278190335 & ((t &= 1023) | t << 16)) | t << 8)) | t << 4)) | t << 2), (t |= (e = 1227133513 & ((e = 3272356035 & ((e = 251719695 & ((e = 4278190335 & ((e &= 1023) | e << 16)) | e << 8)) | e << 4)) | e << 2)) << 1) | (i = 1227133513 & ((i = 3272356035 & ((i = 251719695 & ((i = 4278190335 & ((i &= 1023) | i << 16)) | i << 8)) | i << 4)) | i << 2)) << 2
     },
     deinterleave3: function(t, e) {
      return (t = 1023 & ((t = 4278190335 & ((t = 251719695 & ((t = 3272356035 & ((t = t >>> e & 1227133513) | t >>> 2)) | t >>> 4)) | t >>> 8)) | t >>> 16)) << 22 >> 22
     },
     nextCombination: function(t) {
      var e = t | t - 1;
      return e + 1 | (~e & -~e) - 1 >>> l(t) + 1
     }
    }),
    f = new Array(2),
    p = function(t, e) {
     this.x = t, this.y = e
    };
   p.prototype.toJSON = function() {
    return f[0] = this.x, f[1] = this.y, f
   };
   var m = {
    create: function() {
     return new p(0, 0)
    },
    new: function(t, e) {
     return new p(t, e)
    },
    clone: function(t) {
     return new p(t.x, t.y)
    },
    copy: function(t, e) {
     return t.x = e.x, t.y = e.y, t
    },
    set: function(t, e, i) {
     return t.x = e, t.y = i, t
    },
    add: function(t, e, i) {
     return t.x = e.x + i.x, t.y = e.y + i.y, t
    },
    subtract: function(t, e, i) {
     return t.x = e.x - i.x, t.y = e.y - i.y, t
    }
   };
   m.sub = m.subtract, m.multiply = function(t, e, i) {
    return t.x = e.x * i.x, t.y = e.y * i.y, t
   }, m.mul = m.multiply, m.divide = function(t, e, i) {
    return t.x = e.x / i.x, t.y = e.y / i.y, t
   }, m.div = m.divide, m.ceil = function(t, e) {
    return t.x = Math.ceil(e.x), t.y = Math.ceil(e.y), t
   }, m.floor = function(t, e) {
    return t.x = Math.floor(e.x), t.y = Math.floor(e.y), t
   }, m.min = function(t, e, i) {
    return t.x = Math.min(e.x, i.x), t.y = Math.min(e.y, i.y), t
   }, m.max = function(t, e, i) {
    return t.x = Math.max(e.x, i.x), t.y = Math.max(e.y, i.y), t
   }, m.round = function(t, e) {
    return t.x = Math.round(e.x), t.y = Math.round(e.y), t
   }, m.scale = function(t, e, i) {
    return t.x = e.x * i, t.y = e.y * i, t
   }, m.scaleAndAdd = function(t, e, i, n) {
    return t.x = e.x + i.x * n, t.y = e.y + i.y * n, t
   }, m.distance = function(t, e) {
    var i = e.x - t.x,
     n = e.y - t.y;
    return Math.sqrt(i * i + n * n)
   }, m.dist = m.distance, m.squaredDistance = function(t, e) {
    var i = e.x - t.x,
     n = e.y - t.y;
    return i * i + n * n
   }, m.sqrDist = m.squaredDistance, m.length = function(t) {
    var e = t.x,
     i = t.y;
    return Math.sqrt(e * e + i * i)
   }, m.len = m.length, m.squaredLength = function(t) {
    var e = t.x,
     i = t.y;
    return e * e + i * i
   }, m.sqrLen = m.squaredLength, m.negate = function(t, e) {
    return t.x = -e.x, t.y = -e.y, t
   }, m.inverse = function(t, e) {
    return t.x = 1 / e.x, t.y = 1 / e.y, t
   }, m.inverseSafe = function(t, e) {
    var i = e.x,
     n = e.y;
    return Math.abs(i) < s ? t.x = 0 : t.x = 1 / i, Math.abs(n) < s ? t.y = 0 : t.y = 1 / e.y, t
   }, m.normalize = function(t, e) {
    var i = e.x,
     n = e.y,
     r = i * i + n * n;
    return r > 0 && (r = 1 / Math.sqrt(r), t.x = e.x * r, t.y = e.y * r), t
   }, m.dot = function(t, e) {
    return t.x * e.x + t.y * e.y
   }, m.cross = function(t, e, i) {
    var n = e.x * i.y - e.y * i.x;
    return t.x = t.y = 0, t.z = n, t
   }, m.lerp = function(t, e, i, n) {
    var r = e.x,
     s = e.y;
    return t.x = r + n * (i.x - r), t.y = s + n * (i.y - s), t
   }, m.random = function(t, e) {
    e = e || 1;
    var i = 2 * o() * Math.PI;
    return t.x = Math.cos(i) * e, t.y = Math.sin(i) * e, t
   }, m.transformMat2 = function(t, e, i) {
    var n = e.x,
     r = e.y;
    return t.x = i.m00 * n + i.m02 * r, t.y = i.m01 * n + i.m03 * r, t
   }, m.transformMat23 = function(t, e, i) {
    var n = e.x,
     r = e.y;
    return t.x = i.m00 * n + i.m02 * r + i.m04, t.y = i.m01 * n + i.m03 * r + i.m05, t
   }, m.transformMat3 = function(t, e, i) {
    var n = e.x,
     r = e.y;
    return t.x = i.m00 * n + i.m03 * r + i.m06, t.y = i.m01 * n + i.m04 * r + i.m07, t
   }, m.transformMat4 = function(t, e, i) {
    var n = e.x,
     r = e.y;
    return t.x = i.m00 * n + i.m04 * r + i.m12, t.y = i.m01 * n + i.m05 * r + i.m13, t
   }, m.forEach = (function() {
    var t = m.create();
    return function(e, i, n, r, s, a) {
     var o, c;
     for (i || (i = 2), n || (n = 0), c = r ? Math.min(r * i + n, e.length) : e.length, o = n; o < c; o += i) t.x = e[o], t.y = e[o + 1], s(t, t, a), e[o] = t.x, e[o + 1] = t.y;
     return e
    }
   })(), m.str = function(t) {
    return "vec2(" + t.x + ", " + t.y + ")"
   }, m.array = function(t, e) {
    return t[0] = e.x, t[1] = e.y, t
   }, m.exactEquals = function(t, e) {
    return t.x === e.x && t.y === e.y
   }, m.equals = function(t, e) {
    var i = t.x,
     n = t.y,
     r = e.x,
     a = e.y;
    return Math.abs(i - r) <= s * Math.max(1, Math.abs(i), Math.abs(r)) && Math.abs(n - a) <= s * Math.max(1, Math.abs(n), Math.abs(a))
   };
   var v = new Array(3),
    g = function(t, e, i) {
     this.x = t, this.y = e, this.z = i
    };
   g.prototype.toJSON = function() {
    return v[0] = this.x, v[1] = this.y, v[2] = this.z, v
   };
   var y = {
    create: function() {
     return new g(0, 0, 0)
    },
    new: function(t, e, i) {
     return new g(t, e, i)
    },
    clone: function(t) {
     return new g(t.x, t.y, t.z)
    },
    copy: function(t, e) {
     return t.x = e.x, t.y = e.y, t.z = e.z, t
    },
    set: function(t, e, i, n) {
     return t.x = e, t.y = i, t.z = n, t
    },
    add: function(t, e, i) {
     return t.x = e.x + i.x, t.y = e.y + i.y, t.z = e.z + i.z, t
    },
    subtract: function(t, e, i) {
     return t.x = e.x - i.x, t.y = e.y - i.y, t.z = e.z - i.z, t
    }
   };
   y.sub = y.subtract, y.multiply = function(t, e, i) {
    return t.x = e.x * i.x, t.y = e.y * i.y, t.z = e.z * i.z, t
   }, y.mul = y.multiply, y.divide = function(t, e, i) {
    return t.x = e.x / i.x, t.y = e.y / i.y, t.z = e.z / i.z, t
   }, y.div = y.divide, y.ceil = function(t, e) {
    return t.x = Math.ceil(e.x), t.y = Math.ceil(e.y), t.z = Math.ceil(e.z), t
   }, y.floor = function(t, e) {
    return t.x = Math.floor(e.x), t.y = Math.floor(e.y), t.z = Math.floor(e.z), t
   }, y.min = function(t, e, i) {
    return t.x = Math.min(e.x, i.x), t.y = Math.min(e.y, i.y), t.z = Math.min(e.z, i.z), t
   }, y.max = function(t, e, i) {
    return t.x = Math.max(e.x, i.x), t.y = Math.max(e.y, i.y), t.z = Math.max(e.z, i.z), t
   }, y.round = function(t, e) {
    return t.x = Math.round(e.x), t.y = Math.round(e.y), t.z = Math.round(e.z), t
   }, y.scale = function(t, e, i) {
    return t.x = e.x * i, t.y = e.y * i, t.z = e.z * i, t
   }, y.scaleAndAdd = function(t, e, i, n) {
    return t.x = e.x + i.x * n, t.y = e.y + i.y * n, t.z = e.z + i.z * n, t
   }, y.distance = function(t, e) {
    var i = e.x - t.x,
     n = e.y - t.y,
     r = e.z - t.z;
    return Math.sqrt(i * i + n * n + r * r)
   }, y.dist = y.distance, y.squaredDistance = function(t, e) {
    var i = e.x - t.x,
     n = e.y - t.y,
     r = e.z - t.z;
    return i * i + n * n + r * r
   }, y.sqrDist = y.squaredDistance, y.length = function(t) {
    var e = t.x,
     i = t.y,
     n = t.z;
    return Math.sqrt(e * e + i * i + n * n)
   }, y.len = y.length, y.squaredLength = function(t) {
    var e = t.x,
     i = t.y,
     n = t.z;
    return e * e + i * i + n * n
   }, y.sqrLen = y.squaredLength, y.negate = function(t, e) {
    return t.x = -e.x, t.y = -e.y, t.z = -e.z, t
   }, y.inverse = function(t, e) {
    return t.x = 1 / e.x, t.y = 1 / e.y, t.z = 1 / e.z, t
   }, y.inverseSafe = function(t, e) {
    var i = e.x,
     n = e.y,
     r = e.z;
    return Math.abs(i) < s ? t.x = 0 : t.x = 1 / i, Math.abs(n) < s ? t.y = 0 : t.y = 1 / n, Math.abs(r) < s ? t.z = 0 : t.z = 1 / r, t
   }, y.normalize = function(t, e) {
    var i = e.x,
     n = e.y,
     r = e.z,
     s = i * i + n * n + r * r;
    return s > 0 && (s = 1 / Math.sqrt(s), t.x = i * s, t.y = n * s, t.z = r * s), t
   }, y.dot = function(t, e) {
    return t.x * e.x + t.y * e.y + t.z * e.z
   }, y.cross = function(t, e, i) {
    var n = e.x,
     r = e.y,
     s = e.z,
     a = i.x,
     o = i.y,
     c = i.z;
    return t.x = r * c - s * o, t.y = s * a - n * c, t.z = n * o - r * a, t
   }, y.lerp = function(t, e, i, n) {
    var r = e.x,
     s = e.y,
     a = e.z;
    return t.x = r + n * (i.x - r), t.y = s + n * (i.y - s), t.z = a + n * (i.z - a), t
   }, y.hermite = function(t, e, i, n, r, s) {
    var a = s * s,
     o = a * (2 * s - 3) + 1,
     c = a * (s - 2) + s,
     h = a * (s - 1),
     u = a * (3 - 2 * s);
    return t.x = e.x * o + i.x * c + n.x * h + r.x * u, t.y = e.y * o + i.y * c + n.y * h + r.y * u, t.z = e.z * o + i.z * c + n.z * h + r.z * u, t
   }, y.bezier = function(t, e, i, n, r, s) {
    var a = 1 - s,
     o = a * a,
     c = s * s,
     h = o * a,
     u = 3 * s * o,
     l = 3 * c * a,
     _ = c * s;
    return t.x = e.x * h + i.x * u + n.x * l + r.x * _, t.y = e.y * h + i.y * u + n.y * l + r.y * _, t.z = e.z * h + i.z * u + n.z * l + r.z * _, t
   }, y.random = function(t, e) {
    e = e || 1;
    var i = 2 * o() * Math.PI,
     n = 2 * o() - 1,
     r = Math.sqrt(1 - n * n) * e;
    return t.x = Math.cos(i) * r, t.y = Math.sin(i) * r, t.z = n * e, t
   }, y.transformMat4 = function(t, e, i) {
    var n = e.x,
     r = e.y,
     s = e.z,
     a = i.m03 * n + i.m07 * r + i.m11 * s + i.m15;
    return a = a || 1, t.x = (i.m00 * n + i.m04 * r + i.m08 * s + i.m12) / a, t.y = (i.m01 * n + i.m05 * r + i.m09 * s + i.m13) / a, t.z = (i.m02 * n + i.m06 * r + i.m10 * s + i.m14) / a, t
   }, y.transformMat3 = function(t, e, i) {
    var n = e.x,
     r = e.y,
     s = e.z;
    return t.x = n * i.m00 + r * i.m03 + s * i.m06, t.y = n * i.m01 + r * i.m04 + s * i.m07, t.z = n * i.m02 + r * i.m05 + s * i.m08, t
   }, y.transformQuat = function(t, e, i) {
    var n = e.x,
     r = e.y,
     s = e.z,
     a = i.x,
     o = i.y,
     c = i.z,
     h = i.w,
     u = h * n + o * s - c * r,
     l = h * r + c * n - a * s,
     _ = h * s + a * r - o * n,
     d = -a * n - o * r - c * s;
    return t.x = u * h + d * -a + l * -c - _ * -o, t.y = l * h + d * -o + _ * -a - u * -c, t.z = _ * h + d * -c + u * -o - l * -a, t
   }, y.rotateX = function(t, e, i, n) {
    var r = [],
     s = [];
    return r.x = e.x - i.x, r.y = e.y - i.y, r.z = e.z - i.z, s.x = r.x, s.y = r.y * Math.cos(n) - r.z * Math.sin(n), s.z = r.y * Math.sin(n) + r.z * Math.cos(n), t.x = s.x + i.x, t.y = s.y + i.y, t.z = s.z + i.z, t
   }, y.rotateY = function(t, e, i, n) {
    var r = [],
     s = [];
    return r.x = e.x - i.x, r.y = e.y - i.y, r.z = e.z - i.z, s.x = r.z * Math.sin(n) + r.x * Math.cos(n), s.y = r.y, s.z = r.z * Math.cos(n) - r.x * Math.sin(n), t.x = s.x + i.x, t.y = s.y + i.y, t.z = s.z + i.z, t
   }, y.rotateZ = function(t, e, i, n) {
    var r = [],
     s = [];
    return r.x = e.x - i.x, r.y = e.y - i.y, r.z = e.z - i.z, s.x = r.x * Math.cos(n) - r.y * Math.sin(n), s.y = r.x * Math.sin(n) + r.y * Math.cos(n), s.z = r.z, t.x = s.x + i.x, t.y = s.y + i.y, t.z = s.z + i.z, t
   }, y.forEach = (function() {
    var t = y.create();
    return function(e, i, n, r, s, a) {
     var o, c;
     for (i || (i = 3), n || (n = 0), c = r ? Math.min(r * i + n, e.length) : e.length, o = n; o < c; o += i) t.x = e[o], t.y = e[o + 1], t.z = e[o + 2], s(t, t, a), e[o] = t.x, e[o + 1] = t.y, e[o + 2] = t.z;
     return e
    }
   })(), y.angle = (function() {
    var t = y.create(),
     e = y.create();
    return function(i, n) {
     y.copy(t, i), y.copy(e, n), y.normalize(t, t), y.normalize(e, e);
     var r = y.dot(t, e);
     return r > 1 ? 0 : r < -1 ? Math.PI : Math.acos(r)
    }
   })(), y.str = function(t) {
    return "vec3(" + t.x + ", " + t.y + ", " + t.z + ")"
   }, y.array = function(t, e) {
    return t[0] = e.x, t[1] = e.y, t[2] = e.z, t
   }, y.exactEquals = function(t, e) {
    return t.x === e.x && t.y === e.y && t.z === e.z
   }, y.equals = function(t, e) {
    var i = t.x,
     n = t.y,
     r = t.z,
     a = e.x,
     o = e.y,
     c = e.z;
    return Math.abs(i - a) <= s * Math.max(1, Math.abs(i), Math.abs(a)) && Math.abs(n - o) <= s * Math.max(1, Math.abs(n), Math.abs(o)) && Math.abs(r - c) <= s * Math.max(1, Math.abs(r), Math.abs(c))
   };
   var T = new Array(4),
    E = function(t, e, i, n) {
     this.x = t, this.y = e, this.z = i, this.w = n
    };
   E.prototype.toJSON = function() {
    return T[0] = this.x, T[1] = this.y, T[2] = this.z, T[3] = this.w, T
   };
   var x = {
    create: function() {
     return new E(0, 0, 0, 0)
    },
    new: function(t, e, i, n) {
     return new E(t, e, i, n)
    },
    clone: function(t) {
     return new E(t.x, t.y, t.z, t.w)
    },
    copy: function(t, e) {
     return t.x = e.x, t.y = e.y, t.z = e.z, t.w = e.w, t
    },
    set: function(t, e, i, n, r) {
     return t.x = e, t.y = i, t.z = n, t.w = r, t
    },
    add: function(t, e, i) {
     return t.x = e.x + i.x, t.y = e.y + i.y, t.z = e.z + i.z, t.w = e.w + i.w, t
    },
    subtract: function(t, e, i) {
     return t.x = e.x - i.x, t.y = e.y - i.y, t.z = e.z - i.z, t.w = e.w - i.w, t
    }
   };
   x.sub = x.subtract, x.multiply = function(t, e, i) {
    return t.x = e.x * i.x, t.y = e.y * i.y, t.z = e.z * i.z, t.w = e.w * i.w, t
   }, x.mul = x.multiply, x.divide = function(t, e, i) {
    return t.x = e.x / i.x, t.y = e.y / i.y, t.z = e.z / i.z, t.w = e.w / i.w, t
   }, x.div = x.divide, x.ceil = function(t, e) {
    return t.x = Math.ceil(e.x), t.y = Math.ceil(e.y), t.z = Math.ceil(e.z), t.w = Math.ceil(e.w), t
   }, x.floor = function(t, e) {
    return t.x = Math.floor(e.x), t.y = Math.floor(e.y), t.z = Math.floor(e.z), t.w = Math.floor(e.w), t
   }, x.min = function(t, e, i) {
    return t.x = Math.min(e.x, i.x), t.y = Math.min(e.y, i.y), t.z = Math.min(e.z, i.z), t.w = Math.min(e.w, i.w), t
   }, x.max = function(t, e, i) {
    return t.x = Math.max(e.x, i.x), t.y = Math.max(e.y, i.y), t.z = Math.max(e.z, i.z), t.w = Math.max(e.w, i.w), t
   }, x.round = function(t, e) {
    return t.x = Math.round(e.x), t.y = Math.round(e.y), t.z = Math.round(e.z), t.w = Math.round(e.w), t
   }, x.scale = function(t, e, i) {
    return t.x = e.x * i, t.y = e.y * i, t.z = e.z * i, t.w = e.w * i, t
   }, x.scaleAndAdd = function(t, e, i, n) {
    return t.x = e.x + i.x * n, t.y = e.y + i.y * n, t.z = e.z + i.z * n, t.w = e.w + i.w * n, t
   }, x.distance = function(t, e) {
    var i = e.x - t.x,
     n = e.y - t.y,
     r = e.z - t.z,
     s = e.w - t.w;
    return Math.sqrt(i * i + n * n + r * r + s * s)
   }, x.dist = x.distance, x.squaredDistance = function(t, e) {
    var i = e.x - t.x,
     n = e.y - t.y,
     r = e.z - t.z,
     s = e.w - t.w;
    return i * i + n * n + r * r + s * s
   }, x.sqrDist = x.squaredDistance, x.length = function(t) {
    var e = t.x,
     i = t.y,
     n = t.z,
     r = t.w;
    return Math.sqrt(e * e + i * i + n * n + r * r)
   }, x.len = x.length, x.squaredLength = function(t) {
    var e = t.x,
     i = t.y,
     n = t.z,
     r = t.w;
    return e * e + i * i + n * n + r * r
   }, x.sqrLen = x.squaredLength, x.negate = function(t, e) {
    return t.x = -e.x, t.y = -e.y, t.z = -e.z, t.w = -e.w, t
   }, x.inverse = function(t, e) {
    return t.x = 1 / e.x, t.y = 1 / e.y, t.z = 1 / e.z, t.w = 1 / e.w, t
   }, x.inverseSafe = function(t, e) {
    var i = e.x,
     n = e.y,
     r = e.z,
     a = e.w;
    return Math.abs(i) < s ? t.x = 0 : t.x = 1 / i, Math.abs(n) < s ? t.y = 0 : t.y = 1 / n, Math.abs(r) < s ? t.z = 0 : t.z = 1 / r, Math.abs(a) < s ? t.w = 0 : t.w = 1 / a, t
   }, x.normalize = function(t, e) {
    var i = e.x,
     n = e.y,
     r = e.z,
     s = e.w,
     a = i * i + n * n + r * r + s * s;
    return a > 0 && (a = 1 / Math.sqrt(a), t.x = i * a, t.y = n * a, t.z = r * a, t.w = s * a), t
   }, x.dot = function(t, e) {
    return t.x * e.x + t.y * e.y + t.z * e.z + t.w * e.w
   }, x.lerp = function(t, e, i, n) {
    var r = e.x,
     s = e.y,
     a = e.z,
     o = e.w;
    return t.x = r + n * (i.x - r), t.y = s + n * (i.y - s), t.z = a + n * (i.z - a), t.w = o + n * (i.w - o), t
   }, x.random = function(t, e) {
    return e = e || 1, t.x = o(), t.y = o(), t.z = o(), t.w = o(), x.normalize(t, t), x.scale(t, t, e), t
   }, x.transformMat4 = function(t, e, i) {
    var n = e.x,
     r = e.y,
     s = e.z,
     a = e.w;
    return t.x = i.m00 * n + i.m04 * r + i.m08 * s + i.m12 * a, t.y = i.m01 * n + i.m05 * r + i.m09 * s + i.m13 * a, t.z = i.m02 * n + i.m06 * r + i.m10 * s + i.m14 * a, t.w = i.m03 * n + i.m07 * r + i.m11 * s + i.m15 * a, t
   }, x.transformQuat = function(t, e, i) {
    var n = e.x,
     r = e.y,
     s = e.z,
     a = i.x,
     o = i.y,
     c = i.z,
     h = i.w,
     u = h * n + o * s - c * r,
     l = h * r + c * n - a * s,
     _ = h * s + a * r - o * n,
     d = -a * n - o * r - c * s;
    return t.x = u * h + d * -a + l * -c - _ * -o, t.y = l * h + d * -o + _ * -a - u * -c, t.z = _ * h + d * -c + u * -o - l * -a, t.w = e.w, t
   }, x.forEach = (function() {
    var t = x.create();
    return function(e, i, n, r, s, a) {
     var o, c;
     for (i || (i = 4), n || (n = 0), c = r ? Math.min(r * i + n, e.length) : e.length, o = n; o < c; o += i) t.x = e[o], t.y = e[o + 1], t.z = e[o + 2], t.w = e[o + 3], s(t, t, a), e[o] = t.x, e[o + 1] = t.y, e[o + 2] = t.z, e[o + 3] = t.w;
     return e
    }
   })(), x.str = function(t) {
    return "vec4(" + t.x + ", " + t.y + ", " + t.z + ", " + t.w + ")"
   }, x.array = function(t, e) {
    return t[0] = e.x, t[1] = e.y, t[2] = e.z, t[3] = e.w, t
   }, x.exactEquals = function(t, e) {
    return t.x === e.x && t.y === e.y && t.z === e.z && t.w === e.w
   }, x.equals = function(t, e) {
    var i = t.x,
     n = t.y,
     r = t.z,
     a = t.w,
     o = e.x,
     c = e.y,
     h = e.z,
     u = e.w;
    return Math.abs(i - o) <= s * Math.max(1, Math.abs(i), Math.abs(o)) && Math.abs(n - c) <= s * Math.max(1, Math.abs(n), Math.abs(c)) && Math.abs(r - h) <= s * Math.max(1, Math.abs(r), Math.abs(h)) && Math.abs(a - u) <= s * Math.max(1, Math.abs(a), Math.abs(u))
   };
   var C = new Array(9),
    A = function(t, e, i, n, r, s, a, o, c) {
     this.m00 = t, this.m01 = e, this.m02 = i, this.m03 = n, this.m04 = r, this.m05 = s, this.m06 = a, this.m07 = o, this.m08 = c
    };
   A.prototype.toJSON = function() {
    return C[0] = this.m00, C[1] = this.m01, C[2] = this.m02, C[3] = this.m03, C[4] = this.m04, C[5] = this.m05, C[6] = this.m06, C[7] = this.m07, C[8] = this.m08, C
   };
   var b = {
    create: function() {
     return new A(1, 0, 0, 0, 1, 0, 0, 0, 1)
    },
    new: function(t, e, i, n, r, s, a, o, c) {
     return new A(t, e, i, n, r, s, a, o, c)
    },
    clone: function(t) {
     return new A(t.m00, t.m01, t.m02, t.m03, t.m04, t.m05, t.m06, t.m07, t.m08)
    },
    copy: function(t, e) {
     return t.m00 = e.m00, t.m01 = e.m01, t.m02 = e.m02, t.m03 = e.m03, t.m04 = e.m04, t.m05 = e.m05, t.m06 = e.m06, t.m07 = e.m07, t.m08 = e.m08, t
    },
    set: function(t, e, i, n, r, s, a, o, c, h) {
     return t.m00 = e, t.m01 = i, t.m02 = n, t.m03 = r, t.m04 = s, t.m05 = a, t.m06 = o, t.m07 = c, t.m08 = h, t
    },
    identity: function(t) {
     return t.m00 = 1, t.m01 = 0, t.m02 = 0, t.m03 = 0, t.m04 = 1, t.m05 = 0, t.m06 = 0, t.m07 = 0, t.m08 = 1, t
    },
    transpose: function(t, e) {
     if (t === e) {
      var i = e.m01,
       n = e.m02,
       r = e.m05;
      t.m01 = e.m03, t.m02 = e.m06, t.m03 = i, t.m05 = e.m07, t.m06 = n, t.m07 = r
     } else t.m00 = e.m00, t.m01 = e.m03, t.m02 = e.m06, t.m03 = e.m01, t.m04 = e.m04, t.m05 = e.m07, t.m06 = e.m02, t.m07 = e.m05, t.m08 = e.m08;
     return t
    },
    invert: function(t, e) {
     var i = e.m00,
      n = e.m01,
      r = e.m02,
      s = e.m03,
      a = e.m04,
      o = e.m05,
      c = e.m06,
      h = e.m07,
      u = e.m08,
      l = u * a - o * h,
      _ = -u * s + o * c,
      d = h * s - a * c,
      f = i * l + n * _ + r * d;
     return f ? (f = 1 / f, t.m00 = l * f, t.m01 = (-u * n + r * h) * f, t.m02 = (o * n - r * a) * f, t.m03 = _ * f, t.m04 = (u * i - r * c) * f, t.m05 = (-o * i + r * s) * f, t.m06 = d * f, t.m07 = (-h * i + n * c) * f, t.m08 = (a * i - n * s) * f, t) : null
    },
    adjoint: function(t, e) {
     var i = e.m00,
      n = e.m01,
      r = e.m02,
      s = e.m03,
      a = e.m04,
      o = e.m05,
      c = e.m06,
      h = e.m07,
      u = e.m08;
     return t.m00 = a * u - o * h, t.m01 = r * h - n * u, t.m02 = n * o - r * a, t.m03 = o * c - s * u, t.m04 = i * u - r * c, t.m05 = r * s - i * o, t.m06 = s * h - a * c, t.m07 = n * c - i * h, t.m08 = i * a - n * s, t
    },
    determinant: function(t) {
     var e = t.m00,
      i = t.m01,
      n = t.m02,
      r = t.m03,
      s = t.m04,
      a = t.m05,
      o = t.m06,
      c = t.m07,
      h = t.m08;
     return e * (h * s - a * c) + i * (-h * r + a * o) + n * (c * r - s * o)
    },
    multiply: function(t, e, i) {
     var n = e.m00,
      r = e.m01,
      s = e.m02,
      a = e.m03,
      o = e.m04,
      c = e.m05,
      h = e.m06,
      u = e.m07,
      l = e.m08,
      _ = i.m00,
      d = i.m01,
      f = i.m02,
      p = i.m03,
      m = i.m04,
      v = i.m05,
      g = i.m06,
      y = i.m07,
      T = i.m08;
     return t.m00 = _ * n + d * a + f * h, t.m01 = _ * r + d * o + f * u, t.m02 = _ * s + d * c + f * l, t.m03 = p * n + m * a + v * h, t.m04 = p * r + m * o + v * u, t.m05 = p * s + m * c + v * l, t.m06 = g * n + y * a + T * h, t.m07 = g * r + y * o + T * u, t.m08 = g * s + y * c + T * l, t
    }
   };
   b.mul = b.multiply, b.translate = function(t, e, i) {
    var n = e.m00,
     r = e.m01,
     s = e.m02,
     a = e.m03,
     o = e.m04,
     c = e.m05,
     h = e.m06,
     u = e.m07,
     l = e.m08,
     _ = i.x,
     d = i.y;
    return t.m00 = n, t.m01 = r, t.m02 = s, t.m03 = a, t.m04 = o, t.m05 = c, t.m06 = _ * n + d * a + h, t.m07 = _ * r + d * o + u, t.m08 = _ * s + d * c + l, t
   }, b.rotate = function(t, e, i) {
    var n = e.m00,
     r = e.m01,
     s = e.m02,
     a = e.m03,
     o = e.m04,
     c = e.m05,
     h = e.m06,
     u = e.m07,
     l = e.m08,
     _ = Math.sin(i),
     d = Math.cos(i);
    return t.m00 = d * n + _ * a, t.m01 = d * r + _ * o, t.m02 = d * s + _ * c, t.m03 = d * a - _ * n, t.m04 = d * o - _ * r, t.m05 = d * c - _ * s, t.m06 = h, t.m07 = u, t.m08 = l, t
   }, b.scale = function(t, e, i) {
    var n = i.x,
     r = i.y;
    return t.m00 = n * e.m00, t.m01 = n * e.m01, t.m02 = n * e.m02, t.m03 = r * e.m03, t.m04 = r * e.m04, t.m05 = r * e.m05, t.m06 = e.m06, t.m07 = e.m07, t.m08 = e.m08, t
   }, b.fromMat4 = function(t, e) {
    return t.m00 = e.m00, t.m01 = e.m01, t.m02 = e.m02, t.m03 = e.m04, t.m04 = e.m05, t.m05 = e.m06, t.m06 = e.m08, t.m07 = e.m09, t.m08 = e.m10, t
   }, b.fromTranslation = function(t, e) {
    return t.m00 = 1, t.m01 = 0, t.m02 = 0, t.m03 = 0, t.m04 = 1, t.m05 = 0, t.m06 = e.x, t.m07 = e.y, t.m08 = 1, t
   }, b.fromRotation = function(t, e) {
    var i = Math.sin(e),
     n = Math.cos(e);
    return t.m00 = n, t.m01 = i, t.m02 = 0, t.m03 = -i, t.m04 = n, t.m05 = 0, t.m06 = 0, t.m07 = 0, t.m08 = 1, t
   }, b.fromScaling = function(t, e) {
    return t.m00 = e.x, t.m01 = 0, t.m02 = 0, t.m03 = 0, t.m04 = e.y, t.m05 = 0, t.m06 = 0, t.m07 = 0, t.m08 = 1, t
   }, b.fromMat2d = function(t, e) {
    return t.m00 = e.m00, t.m01 = e.m01, t.m02 = 0, t.m03 = e.m02, t.m04 = e.m03, t.m05 = 0, t.m06 = e.m04, t.m07 = e.m05, t.m08 = 1, t
   }, b.fromQuat = function(t, e) {
    var i = e.x,
     n = e.y,
     r = e.z,
     s = e.w,
     a = i + i,
     o = n + n,
     c = r + r,
     h = i * a,
     u = n * a,
     l = n * o,
     _ = r * a,
     d = r * o,
     f = r * c,
     p = s * a,
     m = s * o,
     v = s * c;
    return t.m00 = 1 - l - f, t.m03 = u - v, t.m06 = _ + m, t.m01 = u + v, t.m04 = 1 - h - f, t.m07 = d - p, t.m02 = _ - m, t.m05 = d + p, t.m08 = 1 - h - l, t
   }, b.fromViewUp = (function() {
    var t = y.new(0, 1, 0),
     e = y.create(),
     i = y.create();
    return function(n, r, a) {
     return y.sqrLen(r) < s * s ? (b.identity(n), n) : (a = a || t, y.cross(e, a, r), y.sqrLen(e) < s * s ? (b.identity(n), n) : (y.cross(i, r, e), b.set(n, e.x, e.y, e.z, i.x, i.y, i.z, r.x, r.y, r.z), n))
    }
   })(), b.normalFromMat4 = function(t, e) {
    var i = e.m00,
     n = e.m01,
     r = e.m02,
     s = e.m03,
     a = e.m04,
     o = e.m05,
     c = e.m06,
     h = e.m07,
     u = e.m08,
     l = e.m09,
     _ = e.m10,
     d = e.m11,
     f = e.m12,
     p = e.m13,
     m = e.m14,
     v = e.m15,
     g = i * o - n * a,
     y = i * c - r * a,
     T = i * h - s * a,
     E = n * c - r * o,
     x = n * h - s * o,
     C = r * h - s * c,
     A = u * p - l * f,
     b = u * m - _ * f,
     S = u * v - d * f,
     w = l * m - _ * p,
     D = l * v - d * p,
     R = _ * v - d * m,
     M = g * R - y * D + T * w + E * S - x * b + C * A;
    return M ? (M = 1 / M, t.m00 = (o * R - c * D + h * w) * M, t.m01 = (c * S - a * R - h * b) * M, t.m02 = (a * D - o * S + h * A) * M, t.m03 = (r * D - n * R - s * w) * M, t.m04 = (i * R - r * S + s * b) * M, t.m05 = (n * S - i * D - s * A) * M, t.m06 = (p * C - m * x + v * E) * M, t.m07 = (m * T - f * C - v * y) * M, t.m08 = (f * x - p * T + v * g) * M, t) : null
   }, b.str = function(t) {
    return "mat3(" + t.m00 + ", " + t.m01 + ", " + t.m02 + ", " + t.m03 + ", " + t.m04 + ", " + t.m05 + ", " + t.m06 + ", " + t.m07 + ", " + t.m08 + ")"
   }, b.array = function(t, e) {
    return t[0] = e.m00, t[1] = e.m01, t[2] = e.m02, t[3] = e.m03, t[4] = e.m04, t[5] = e.m05, t[6] = e.m06, t[7] = e.m07, t[8] = e.m08, t
   }, b.frob = function(t) {
    return Math.sqrt(Math.pow(t.m00, 2) + Math.pow(t.m01, 2) + Math.pow(t.m02, 2) + Math.pow(t.m03, 2) + Math.pow(t.m04, 2) + Math.pow(t.m05, 2) + Math.pow(t.m06, 2) + Math.pow(t.m07, 2) + Math.pow(t.m08, 2))
   }, b.add = function(t, e, i) {
    return t.m00 = e.m00 + i.m00, t.m01 = e.m01 + i.m01, t.m02 = e.m02 + i.m02, t.m03 = e.m03 + i.m03, t.m04 = e.m04 + i.m04, t.m05 = e.m05 + i.m05, t.m06 = e.m06 + i.m06, t.m07 = e.m07 + i.m07, t.m08 = e.m08 + i.m08, t
   }, b.subtract = function(t, e, i) {
    return t.m00 = e.m00 - i.m00, t.m01 = e.m01 - i.m01, t.m02 = e.m02 - i.m02, t.m03 = e.m03 - i.m03, t.m04 = e.m04 - i.m04, t.m05 = e.m05 - i.m05, t.m06 = e.m06 - i.m06, t.m07 = e.m07 - i.m07, t.m08 = e.m08 - i.m08, t
   }, b.sub = b.subtract, b.multiplyScalar = function(t, e, i) {
    return t.m00 = e.m00 * i, t.m01 = e.m01 * i, t.m02 = e.m02 * i, t.m03 = e.m03 * i, t.m04 = e.m04 * i, t.m05 = e.m05 * i, t.m06 = e.m06 * i, t.m07 = e.m07 * i, t.m08 = e.m08 * i, t
   }, b.multiplyScalarAndAdd = function(t, e, i, n) {
    return t.m00 = e.m00 + i.m00 * n, t.m01 = e.m01 + i.m01 * n, t.m02 = e.m02 + i.m02 * n, t.m03 = e.m03 + i.m03 * n, t.m04 = e.m04 + i.m04 * n, t.m05 = e.m05 + i.m05 * n, t.m06 = e.m06 + i.m06 * n, t.m07 = e.m07 + i.m07 * n, t.m08 = e.m08 + i.m08 * n, t
   }, b.exactEquals = function(t, e) {
    return t.m00 === e.m00 && t.m01 === e.m01 && t.m02 === e.m02 && t.m03 === e.m03 && t.m04 === e.m04 && t.m05 === e.m05 && t.m06 === e.m06 && t.m07 === e.m07 && t.m08 === e.m08
   }, b.equals = function(t, e) {
    var i = t.m00,
     n = t.m01,
     r = t.m02,
     a = t.m03,
     o = t.m04,
     c = t.m05,
     h = t.m06,
     u = t.m07,
     l = t.m08,
     _ = e.m00,
     d = e.m01,
     f = e.m02,
     p = e.m03,
     m = e.m04,
     v = e.m05,
     g = e.m06,
     y = e.m07,
     T = e.m08;
    return Math.abs(i - _) <= s * Math.max(1, Math.abs(i), Math.abs(_)) && Math.abs(n - d) <= s * Math.max(1, Math.abs(n), Math.abs(d)) && Math.abs(r - f) <= s * Math.max(1, Math.abs(r), Math.abs(f)) && Math.abs(a - p) <= s * Math.max(1, Math.abs(a), Math.abs(p)) && Math.abs(o - m) <= s * Math.max(1, Math.abs(o), Math.abs(m)) && Math.abs(c - v) <= s * Math.max(1, Math.abs(c), Math.abs(v)) && Math.abs(h - g) <= s * Math.max(1, Math.abs(h), Math.abs(g)) && Math.abs(u - y) <= s * Math.max(1, Math.abs(u), Math.abs(y)) && Math.abs(l - T) <= s * Math.max(1, Math.abs(l), Math.abs(T))
   };
   var S = new Array(4),
    w = function(t, e, i, n) {
     this.x = t, this.y = e, this.z = i, this.w = n
    };
   w.prototype.toJSON = function() {
    return S[0] = this.x, S[1] = this.y, S[2] = this.z, S[3] = this.w, S
   };
   var D = {
    create: function() {
     return new w(0, 0, 0, 1)
    },
    new: function(t, e, i, n) {
     return new w(t, e, i, n)
    },
    clone: function(t) {
     return new w(t.x, t.y, t.z, t.w)
    }
   };
   D.copy = x.copy, D.set = x.set, D.identity = function(t) {
    return t.x = 0, t.y = 0, t.z = 0, t.w = 1, t
   }, D.rotationTo = (function() {
    var t = y.create(),
     e = y.new(1, 0, 0),
     i = y.new(0, 1, 0);
    return function(n, r, s) {
     var a = y.dot(r, s);
     return a < -.999999 ? (y.cross(t, e, r), y.length(t) < 1e-6 && y.cross(t, i, r), y.normalize(t, t), D.fromAxisAngle(n, t, Math.PI), n) : a > .999999 ? (n.x = 0, n.y = 0, n.z = 0, n.w = 1, n) : (y.cross(t, r, s), n.x = t.x, n.y = t.y, n.z = t.z, n.w = 1 + a, D.normalize(n, n))
    }
   })(), D.getAxisAngle = function(t, e) {
    var i = 2 * Math.acos(e.w),
     n = Math.sin(i / 2);
    return 0 != n ? (t.x = e.x / n, t.y = e.y / n, t.z = e.z / n) : (t.x = 1, t.y = 0, t.z = 0), i
   }, D.multiply = function(t, e, i) {
    var n = e.x,
     r = e.y,
     s = e.z,
     a = e.w,
     o = i.x,
     c = i.y,
     h = i.z,
     u = i.w;
    return t.x = n * u + a * o + r * h - s * c, t.y = r * u + a * c + s * o - n * h, t.z = s * u + a * h + n * c - r * o, t.w = a * u - n * o - r * c - s * h, t
   }, D.mul = D.multiply, D.scale = x.scale, D.rotateX = function(t, e, i) {
    i *= .5;
    var n = e.x,
     r = e.y,
     s = e.z,
     a = e.w,
     o = Math.sin(i),
     c = Math.cos(i);
    return t.x = n * c + a * o, t.y = r * c + s * o, t.z = s * c - r * o, t.w = a * c - n * o, t
   }, D.rotateY = function(t, e, i) {
    i *= .5;
    var n = e.x,
     r = e.y,
     s = e.z,
     a = e.w,
     o = Math.sin(i),
     c = Math.cos(i);
    return t.x = n * c - s * o, t.y = r * c + a * o, t.z = s * c + n * o, t.w = a * c - r * o, t
   }, D.rotateZ = function(t, e, i) {
    i *= .5;
    var n = e.x,
     r = e.y,
     s = e.z,
     a = e.w,
     o = Math.sin(i),
     c = Math.cos(i);
    return t.x = n * c + r * o, t.y = r * c - n * o, t.z = s * c + a * o, t.w = a * c - s * o, t
   }, D.rotateAround = (function() {
    var t = y.create(),
     e = D.create();
    return function(i, n, r, s) {
     return D.invert(e, n), y.transformQuat(t, r, e), D.fromAxisAngle(e, t, s), D.mul(i, n, e), i
    }
   })(), D.rotateAroundLocal = (function() {
    var t = D.create();
    return function(e, i, n, r) {
     return D.fromAxisAngle(t, n, r), D.mul(e, i, t), e
    }
   })(), D.calculateW = function(t, e) {
    var i = e.x,
     n = e.y,
     r = e.z;
    return t.x = i, t.y = n, t.z = r, t.w = Math.sqrt(Math.abs(1 - i * i - n * n - r * r)), t
   }, D.dot = x.dot, D.lerp = x.lerp, D.slerp = function(t, e, i, n) {
    var r, s, a, o, c, h = e.x,
     u = e.y,
     l = e.z,
     _ = e.w,
     d = i.x,
     f = i.y,
     p = i.z,
     m = i.w;
    return (s = h * d + u * f + l * p + _ * m) < 0 && (s = -s, d = -d, f = -f, p = -p, m = -m), 1 - s > 1e-6 ? (r = Math.acos(s), a = Math.sin(r), o = Math.sin((1 - n) * r) / a, c = Math.sin(n * r) / a) : (o = 1 - n, c = n), t.x = o * h + c * d, t.y = o * u + c * f, t.z = o * l + c * p, t.w = o * _ + c * m, t
   }, D.sqlerp = (function() {
    var t = D.create(),
     e = D.create();
    return function(i, n, r, s, a, o) {
     return D.slerp(t, n, a, o), D.slerp(e, r, s, o), D.slerp(i, t, e, 2 * o * (1 - o)), i
    }
   })(), D.invert = function(t, e) {
    var i = e.x,
     n = e.y,
     r = e.z,
     s = e.w,
     a = i * i + n * n + r * r + s * s,
     o = a ? 1 / a : 0;
    return t.x = -i * o, t.y = -n * o, t.z = -r * o, t.w = s * o, t
   }, D.conjugate = function(t, e) {
    return t.x = -e.x, t.y = -e.y, t.z = -e.z, t.w = e.w, t
   }, D.length = x.length, D.len = D.length, D.squaredLength = x.squaredLength, D.sqrLen = D.squaredLength, D.normalize = x.normalize, D.fromAxes = (function() {
    var t = b.create();
    return function(e, i, n, r) {
     return b.set(t, i.x, i.y, i.z, n.x, n.y, n.z, r.x, r.y, r.z), D.normalize(e, D.fromMat3(e, t))
    }
   })(), D.fromViewUp = (function() {
    var t = b.create();
    return function(e, i, n) {
     return b.fromViewUp(t, i, n), t ? D.normalize(e, D.fromMat3(e, t)) : null
    }
   })(), D.fromAxisAngle = function(t, e, i) {
    i *= .5;
    var n = Math.sin(i);
    return t.x = n * e.x, t.y = n * e.y, t.z = n * e.z, t.w = Math.cos(i), t
   }, D.fromMat3 = function(t, e) {
    var i = e.m00,
     n = e.m03,
     r = e.m06,
     s = e.m01,
     a = e.m04,
     o = e.m07,
     c = e.m02,
     h = e.m05,
     u = e.m08,
     l = i + a + u;
    if (l > 0) {
     var _ = .5 / Math.sqrt(l + 1);
     t.w = .25 / _, t.x = (h - o) * _, t.y = (r - c) * _, t.z = (s - n) * _
    } else if (i > a && i > u) {
     var d = 2 * Math.sqrt(1 + i - a - u);
     t.w = (h - o) / d, t.x = .25 * d, t.y = (n + s) / d, t.z = (r + c) / d
    } else if (a > u) {
     var f = 2 * Math.sqrt(1 + a - i - u);
     t.w = (r - c) / f, t.x = (n + s) / f, t.y = .25 * f, t.z = (o + h) / f
    } else {
     var p = 2 * Math.sqrt(1 + u - i - a);
     t.w = (s - n) / p, t.x = (r + c) / p, t.y = (o + h) / p, t.z = .25 * p
    }
    return t
   }, D.fromEuler = function(t, e, i, n) {
    var r = .5 * Math.PI / 180;
    e *= r, i *= r, n *= r;
    var s = Math.sin(e),
     a = Math.cos(e),
     o = Math.sin(i),
     c = Math.cos(i),
     h = Math.sin(n),
     u = Math.cos(n);
    return t.x = s * c * u - a * o * h, t.y = a * o * u + s * c * h, t.z = a * c * h - s * o * u, t.w = a * c * u + s * o * h, t
   }, D.str = function(t) {
    return "quat(" + t.x + ", " + t.y + ", " + t.z + ", " + t.w + ")"
   }, D.array = function(t, e) {
    return t[0] = e.x, t[1] = e.y, t[2] = e.z, t[3] = e.w, t
   }, D.exactEquals = x.exactEquals, D.equals = x.equals;
   var R = new Array(4),
    M = function(t, e, i, n) {
     this.m00 = t, this.m01 = e, this.m02 = i, this.m03 = n
    };
   M.prototype.toJSON = function() {
    return R[0] = this.m00, R[1] = this.m01, R[2] = this.m02, R[3] = this.m03, R
   };
   var I = {
    create: function() {
     return new M(1, 0, 0, 1)
    },
    new: function(t, e, i, n) {
     return new M(t, e, i, n)
    },
    clone: function(t) {
     return new M(t.m00, t.m01, t.m02, t.m03)
    },
    copy: function(t, e) {
     return t.m00 = e.m00, t.m01 = e.m01, t.m02 = e.m02, t.m03 = e.m03, t
    },
    identity: function(t) {
     return t.m00 = 1, t.m01 = 0, t.m02 = 0, t.m03 = 1, t
    },
    set: function(t, e, i, n, r) {
     return t.m00 = e, t.m01 = i, t.m02 = n, t.m03 = r, t
    },
    transpose: function(t, e) {
     if (t === e) {
      var i = e.m01;
      t.m01 = e.m02, t.m02 = i
     } else t.m00 = e.m00, t.m01 = e.m02, t.m02 = e.m01, t.m03 = e.m03;
     return t
    },
    invert: function(t, e) {
     var i = e.m00,
      n = e.m01,
      r = e.m02,
      s = e.m03,
      a = i * s - r * n;
     return a ? (a = 1 / a, t.m00 = s * a, t.m01 = -n * a, t.m02 = -r * a, t.m03 = i * a, t) : null
    },
    adjoint: function(t, e) {
     var i = e.m00;
     return t.m00 = e.m03, t.m01 = -e.m01, t.m02 = -e.m02, t.m03 = i, t
    },
    determinant: function(t) {
     return t.m00 * t.m03 - t.m02 * t.m01
    },
    multiply: function(t, e, i) {
     var n = e.m00,
      r = e.m01,
      s = e.m02,
      a = e.m03,
      o = i.m00,
      c = i.m01,
      h = i.m02,
      u = i.m03;
     return t.m00 = n * o + s * c, t.m01 = r * o + a * c, t.m02 = n * h + s * u, t.m03 = r * h + a * u, t
    }
   };
   I.mul = I.multiply, I.rotate = function(t, e, i) {
    var n = e.m00,
     r = e.m01,
     s = e.m02,
     a = e.m03,
     o = Math.sin(i),
     c = Math.cos(i);
    return t.m00 = n * c + s * o, t.m01 = r * c + a * o, t.m02 = n * -o + s * c, t.m03 = r * -o + a * c, t
   }, I.scale = function(t, e, i) {
    var n = e.m00,
     r = e.m01,
     s = e.m02,
     a = e.m03,
     o = i.x,
     c = i.y;
    return t.m00 = n * o, t.m01 = r * o, t.m02 = s * c, t.m03 = a * c, t
   }, I.fromRotation = function(t, e) {
    var i = Math.sin(e),
     n = Math.cos(e);
    return t.m00 = n, t.m01 = i, t.m02 = -i, t.m03 = n, t
   }, I.fromScaling = function(t, e) {
    return t.m00 = e.x, t.m01 = 0, t.m02 = 0, t.m03 = e.y, t
   }, I.str = function(t) {
    return "mat2(" + t.m00 + ", " + t.m01 + ", " + t.m02 + ", " + t.m03 + ")"
   }, I.array = function(t, e) {
    return t[0] = e.m00, t[1] = e.m01, t[2] = e.m02, t[3] = e.m03, t
   }, I.frob = function(t) {
    return Math.sqrt(Math.pow(t.m00, 2) + Math.pow(t.m01, 2) + Math.pow(t.m02, 2) + Math.pow(t.m03, 2))
   }, I.LDU = function(t, e, i, n) {
    t.m02 = n.m02 / n.m00, i.m00 = n.m00, i.m01 = n.m01, i.m03 = n.m03 - t.m02 * i.m01
   }, I.add = function(t, e, i) {
    return t.m00 = e.m00 + i.m00, t.m01 = e.m01 + i.m01, t.m02 = e.m02 + i.m02, t.m03 = e.m03 + i.m03, t
   }, I.subtract = function(t, e, i) {
    return t.m00 = e.m00 - i.m00, t.m01 = e.m01 - i.m01, t.m02 = e.m02 - i.m02, t.m03 = e.m03 - i.m03, t
   }, I.sub = I.subtract, I.exactEquals = function(t, e) {
    return t.m00 === e.m00 && t.m01 === e.m01 && t.m02 === e.m02 && t.m03 === e.m03
   }, I.equals = function(t, e) {
    var i = t.m00,
     n = t.m01,
     r = t.m02,
     a = t.m03,
     o = e.m00,
     c = e.m01,
     h = e.m02,
     u = e.m03;
    return Math.abs(i - o) <= s * Math.max(1, Math.abs(i), Math.abs(o)) && Math.abs(n - c) <= s * Math.max(1, Math.abs(n), Math.abs(c)) && Math.abs(r - h) <= s * Math.max(1, Math.abs(r), Math.abs(h)) && Math.abs(a - u) <= s * Math.max(1, Math.abs(a), Math.abs(u))
   }, I.multiplyScalar = function(t, e, i) {
    return t.m00 = e.m00 * i, t.m01 = e.m01 * i, t.m02 = e.m02 * i, t.m03 = e.m03 * i, t
   }, I.multiplyScalarAndAdd = function(t, e, i, n) {
    return t.m00 = e.m00 + i.m00 * n, t.m01 = e.m01 + i.m01 * n, t.m02 = e.m02 + i.m02 * n, t.m03 = e.m03 + i.m03 * n, t
   };
   var L = new Array(6),
    O = function(t, e, i, n, r, s) {
     this.m00 = t, this.m01 = e, this.m02 = i, this.m03 = n, this.m04 = r, this.m05 = s
    };
   O.prototype.toJSON = function() {
    return L[0] = this.m00, L[1] = this.m01, L[2] = this.m02, L[3] = this.m03, L[4] = this.m04, L[5] = this.m05, L
   };
   var P = {
    create: function() {
     return new O(1, 0, 0, 1, 0, 0)
    },
    new: function(t, e, i, n, r, s) {
     return new O(t, e, i, n, r, s)
    },
    clone: function(t) {
     return new O(t.m00, t.m01, t.m02, t.m03, t.m04, t.m05)
    },
    copy: function(t, e) {
     return t.m00 = e.m00, t.m01 = e.m01, t.m02 = e.m02, t.m03 = e.m03, t.m04 = e.m04, t.m05 = e.m05, t
    },
    identity: function(t) {
     return t.m00 = 1, t.m01 = 0, t.m02 = 0, t.m03 = 1, t.m04 = 0, t.m05 = 0, t
    },
    set: function(t, e, i, n, r, s, a) {
     return t.m00 = e, t.m01 = i, t.m02 = n, t.m03 = r, t.m04 = s, t.m05 = a, t
    },
    invert: function(t, e) {
     var i = e.m00,
      n = e.m01,
      r = e.m02,
      s = e.m03,
      a = e.m04,
      o = e.m05,
      c = i * s - n * r;
     return c ? (c = 1 / c, t.m00 = s * c, t.m01 = -n * c, t.m02 = -r * c, t.m03 = i * c, t.m04 = (r * o - s * a) * c, t.m05 = (n * a - i * o) * c, t) : null
    },
    determinant: function(t) {
     return t.m00 * t.m03 - t.m01 * t.m02
    },
    multiply: function(t, e, i) {
     var n = e.m00,
      r = e.m01,
      s = e.m02,
      a = e.m03,
      o = e.m04,
      c = e.m05,
      h = i.m00,
      u = i.m01,
      l = i.m02,
      _ = i.m03,
      d = i.m04,
      f = i.m05;
     return t.m00 = n * h + s * u, t.m01 = r * h + a * u, t.m02 = n * l + s * _, t.m03 = r * l + a * _, t.m04 = n * d + s * f + o, t.m05 = r * d + a * f + c, t
    }
   };
   P.mul = P.multiply, P.rotate = function(t, e, i) {
    var n = e.m00,
     r = e.m01,
     s = e.m02,
     a = e.m03,
     o = e.m04,
     c = e.m05,
     h = Math.sin(i),
     u = Math.cos(i);
    return t.m00 = n * u + s * h, t.m01 = r * u + a * h, t.m02 = n * -h + s * u, t.m03 = r * -h + a * u, t.m04 = o, t.m05 = c, t
   }, P.scale = function(t, e, i) {
    var n = e.m00,
     r = e.m01,
     s = e.m02,
     a = e.m03,
     o = e.m04,
     c = e.m05,
     h = i.x,
     u = i.y;
    return t.m00 = n * h, t.m01 = r * h, t.m02 = s * u, t.m03 = a * u, t.m04 = o, t.m05 = c, t
   }, P.translate = function(t, e, i) {
    var n = e.m00,
     r = e.m01,
     s = e.m02,
     a = e.m03,
     o = e.m04,
     c = e.m05,
     h = i.x,
     u = i.y;
    return t.m00 = n, t.m01 = r, t.m02 = s, t.m03 = a, t.m04 = n * h + s * u + o, t.m05 = r * h + a * u + c, t
   }, P.fromRotation = function(t, e) {
    var i = Math.sin(e),
     n = Math.cos(e);
    return t.m00 = n, t.m01 = i, t.m02 = -i, t.m03 = n, t.m04 = 0, t.m05 = 0, t
   }, P.fromScaling = function(t, e) {
    return t.m00 = e.m00, t.m01 = 0, t.m02 = 0, t.m03 = e.m01, t.m04 = 0, t.m05 = 0, t
   }, P.fromTranslation = function(t, e) {
    return t.m00 = 1, t.m01 = 0, t.m02 = 0, t.m03 = 1, t.m04 = e.x, t.m05 = e.y, t
   }, P.str = function(t) {
    return "mat23(" + t.m00 + ", " + t.m01 + ", " + t.m02 + ", " + t.m03 + ", " + t.m04 + ", " + t.m05 + ")"
   }, P.array = function(t, e) {
    return t[0] = e.m00, t[1] = e.m01, t[2] = e.m02, t[3] = e.m03, t[4] = e.m04, t[5] = e.m05, t
   }, P.array4x4 = function(t, e) {
    return t[0] = e.m00, t[1] = e.m01, t[2] = 0, t[3] = 0, t[4] = e.m02, t[5] = e.m03, t[6] = 0, t[7] = 0, t[8] = 0, t[9] = 0, t[10] = 1, t[11] = 0, t[12] = e.m04, t[13] = e.m05, t[14] = 0, t[15] = 1, t
   }, P.frob = function(t) {
    return Math.sqrt(Math.pow(t.m00, 2) + Math.pow(t.m01, 2) + Math.pow(t.m02, 2) + Math.pow(t.m03, 2) + Math.pow(t.m04, 2) + Math.pow(t.m05, 2) + 1)
   }, P.add = function(t, e, i) {
    return t.m00 = e.m00 + i.m00, t.m01 = e.m01 + i.m01, t.m02 = e.m02 + i.m02, t.m03 = e.m03 + i.m03, t.m04 = e.m04 + i.m04, t.m05 = e.m05 + i.m05, t
   }, P.subtract = function(t, e, i) {
    return t.m00 = e.m00 - i.m00, t.m01 = e.m01 - i.m01, t.m02 = e.m02 - i.m02, t.m03 = e.m03 - i.m03, t.m04 = e.m04 - i.m04, t.m05 = e.m05 - i.m05, t
   }, P.sub = P.subtract, P.multiplyScalar = function(t, e, i) {
    return t.m00 = e.m00 * i, t.m01 = e.m01 * i, t.m02 = e.m02 * i, t.m03 = e.m03 * i, t.m04 = e.m04 * i, t.m05 = e.m05 * i, t
   }, P.multiplyScalarAndAdd = function(t, e, i, n) {
    return t.m00 = e.m00 + i.m00 * n, t.m01 = e.m01 + i.m01 * n, t.m02 = e.m02 + i.m02 * n, t.m03 = e.m03 + i.m03 * n, t.m04 = e.m04 + i.m04 * n, t.m05 = e.m05 + i.m05 * n, t
   }, P.exactEquals = function(t, e) {
    return t.m00 === e.m00 && t.m01 === e.m01 && t.m02 === e.m02 && t.m03 === e.m03 && t.m04 === e.m04 && t.m05 === e.m05
   }, P.equals = function(t, e) {
    var i = t.m00,
     n = t.m01,
     r = t.m02,
     a = t.m03,
     o = t.m04,
     c = t.m05,
     h = e.m00,
     u = e.m01,
     l = e.m02,
     _ = e.m03,
     d = e.m04,
     f = e.m05;
    return Math.abs(i - h) <= s * Math.max(1, Math.abs(i), Math.abs(h)) && Math.abs(n - u) <= s * Math.max(1, Math.abs(n), Math.abs(u)) && Math.abs(r - l) <= s * Math.max(1, Math.abs(r), Math.abs(l)) && Math.abs(a - _) <= s * Math.max(1, Math.abs(a), Math.abs(_)) && Math.abs(o - d) <= s * Math.max(1, Math.abs(o), Math.abs(d)) && Math.abs(c - f) <= s * Math.max(1, Math.abs(c), Math.abs(f))
   };
   var F = new Array(16),
    N = function(t, e, i, n, r, s, a, o, c, h, u, l, _, d, f, p) {
     this.m00 = t, this.m01 = e, this.m02 = i, this.m03 = n, this.m04 = r, this.m05 = s, this.m06 = a, this.m07 = o, this.m08 = c, this.m09 = h, this.m10 = u, this.m11 = l, this.m12 = _, this.m13 = d, this.m14 = f, this.m15 = p
    };
   N.prototype.toJSON = function() {
    return F[0] = this.m00, F[1] = this.m01, F[2] = this.m02, F[3] = this.m03, F[4] = this.m04, F[5] = this.m05, F[6] = this.m06, F[7] = this.m07, F[8] = this.m08, F[9] = this.m09, F[10] = this.m10, F[11] = this.m11, F[12] = this.m12, F[13] = this.m13, F[14] = this.m14, F[15] = this.m15, F
   };
   var B = {
    create: function() {
     return new N(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1)
    },
    new: function(t, e, i, n, r, s, a, o, c, h, u, l, _, d, f, p) {
     return new N(t, e, i, n, r, s, a, o, c, h, u, l, _, d, f, p)
    },
    clone: function(t) {
     return new N(t.m00, t.m01, t.m02, t.m03, t.m04, t.m05, t.m06, t.m07, t.m08, t.m09, t.m10, t.m11, t.m12, t.m13, t.m14, t.m15)
    },
    copy: function(t, e) {
     return t.m00 = e.m00, t.m01 = e.m01, t.m02 = e.m02, t.m03 = e.m03, t.m04 = e.m04, t.m05 = e.m05, t.m06 = e.m06, t.m07 = e.m07, t.m08 = e.m08, t.m09 = e.m09, t.m10 = e.m10, t.m11 = e.m11, t.m12 = e.m12, t.m13 = e.m13, t.m14 = e.m14, t.m15 = e.m15, t
    },
    set: function(t, e, i, n, r, s, a, o, c, h, u, l, _, d, f, p, m) {
     return t.m00 = e, t.m01 = i, t.m02 = n, t.m03 = r, t.m04 = s, t.m05 = a, t.m06 = o, t.m07 = c, t.m08 = h, t.m09 = u, t.m10 = l, t.m11 = _, t.m12 = d, t.m13 = f, t.m14 = p, t.m15 = m, t
    },
    identity: function(t) {
     return t.m00 = 1, t.m01 = 0, t.m02 = 0, t.m03 = 0, t.m04 = 0, t.m05 = 1, t.m06 = 0, t.m07 = 0, t.m08 = 0, t.m09 = 0, t.m10 = 1, t.m11 = 0, t.m12 = 0, t.m13 = 0, t.m14 = 0, t.m15 = 1, t
    },
    transpose: function(t, e) {
     if (t === e) {
      var i = e.m01,
       n = e.m02,
       r = e.m03,
       s = e.m06,
       a = e.m07,
       o = e.m11;
      t.m01 = e.m04, t.m02 = e.m08, t.m03 = e.m12, t.m04 = i, t.m06 = e.m09, t.m07 = e.m13, t.m08 = n, t.m09 = s, t.m11 = e.m14, t.m12 = r, t.m13 = a, t.m14 = o
     } else t.m00 = e.m00, t.m01 = e.m04, t.m02 = e.m08, t.m03 = e.m12, t.m04 = e.m01, t.m05 = e.m05, t.m06 = e.m09, t.m07 = e.m13, t.m08 = e.m02, t.m09 = e.m06, t.m10 = e.m10, t.m11 = e.m14, t.m12 = e.m03, t.m13 = e.m07, t.m14 = e.m11, t.m15 = e.m15;
     return t
    },
    invert: function(t, e) {
     var i = e.m00,
      n = e.m01,
      r = e.m02,
      s = e.m03,
      a = e.m04,
      o = e.m05,
      c = e.m06,
      h = e.m07,
      u = e.m08,
      l = e.m09,
      _ = e.m10,
      d = e.m11,
      f = e.m12,
      p = e.m13,
      m = e.m14,
      v = e.m15,
      g = i * o - n * a,
      y = i * c - r * a,
      T = i * h - s * a,
      E = n * c - r * o,
      x = n * h - s * o,
      C = r * h - s * c,
      A = u * p - l * f,
      b = u * m - _ * f,
      S = u * v - d * f,
      w = l * m - _ * p,
      D = l * v - d * p,
      R = _ * v - d * m,
      M = g * R - y * D + T * w + E * S - x * b + C * A;
     return M ? (M = 1 / M, t.m00 = (o * R - c * D + h * w) * M, t.m01 = (r * D - n * R - s * w) * M, t.m02 = (p * C - m * x + v * E) * M, t.m03 = (_ * x - l * C - d * E) * M, t.m04 = (c * S - a * R - h * b) * M, t.m05 = (i * R - r * S + s * b) * M, t.m06 = (m * T - f * C - v * y) * M, t.m07 = (u * C - _ * T + d * y) * M, t.m08 = (a * D - o * S + h * A) * M, t.m09 = (n * S - i * D - s * A) * M, t.m10 = (f * x - p * T + v * g) * M, t.m11 = (l * T - u * x - d * g) * M, t.m12 = (o * b - a * w - c * A) * M, t.m13 = (i * w - n * b + r * A) * M, t.m14 = (p * y - f * E - m * g) * M, t.m15 = (u * E - l * y + _ * g) * M, t) : null
    },
    adjoint: function(t, e) {
     var i = e.m00,
      n = e.m01,
      r = e.m02,
      s = e.m03,
      a = e.m04,
      o = e.m05,
      c = e.m06,
      h = e.m07,
      u = e.m08,
      l = e.m09,
      _ = e.m10,
      d = e.m11,
      f = e.m12,
      p = e.m13,
      m = e.m14,
      v = e.m15;
     return t.m00 = o * (_ * v - d * m) - l * (c * v - h * m) + p * (c * d - h * _), t.m01 = -(n * (_ * v - d * m) - l * (r * v - s * m) + p * (r * d - s * _)), t.m02 = n * (c * v - h * m) - o * (r * v - s * m) + p * (r * h - s * c), t.m03 = -(n * (c * d - h * _) - o * (r * d - s * _) + l * (r * h - s * c)), t.m04 = -(a * (_ * v - d * m) - u * (c * v - h * m) + f * (c * d - h * _)), t.m05 = i * (_ * v - d * m) - u * (r * v - s * m) + f * (r * d - s * _), t.m06 = -(i * (c * v - h * m) - a * (r * v - s * m) + f * (r * h - s * c)), t.m07 = i * (c * d - h * _) - a * (r * d - s * _) + u * (r * h - s * c), t.m08 = a * (l * v - d * p) - u * (o * v - h * p) + f * (o * d - h * l), t.m09 = -(i * (l * v - d * p) - u * (n * v - s * p) + f * (n * d - s * l)), t.m10 = i * (o * v - h * p) - a * (n * v - s * p) + f * (n * h - s * o), t.m11 = -(i * (o * d - h * l) - a * (n * d - s * l) + u * (n * h - s * o)), t.m12 = -(a * (l * m - _ * p) - u * (o * m - c * p) + f * (o * _ - c * l)), t.m13 = i * (l * m - _ * p) - u * (n * m - r * p) + f * (n * _ - r * l), t.m14 = -(i * (o * m - c * p) - a * (n * m - r * p) + f * (n * c - r * o)), t.m15 = i * (o * _ - c * l) - a * (n * _ - r * l) + u * (n * c - r * o), t
    },
    determinant: function(t) {
     var e = t.m00,
      i = t.m01,
      n = t.m02,
      r = t.m03,
      s = t.m04,
      a = t.m05,
      o = t.m06,
      c = t.m07,
      h = t.m08,
      u = t.m09,
      l = t.m10,
      _ = t.m11,
      d = t.m12,
      f = t.m13,
      p = t.m14,
      m = t.m15;
     return (e * a - i * s) * (l * m - _ * p) - (e * o - n * s) * (u * m - _ * f) + (e * c - r * s) * (u * p - l * f) + (i * o - n * a) * (h * m - _ * d) - (i * c - r * a) * (h * p - l * d) + (n * c - r * o) * (h * f - u * d)
    },
    multiply: function(t, e, i) {
     var n = e.m00,
      r = e.m01,
      s = e.m02,
      a = e.m03,
      o = e.m04,
      c = e.m05,
      h = e.m06,
      u = e.m07,
      l = e.m08,
      _ = e.m09,
      d = e.m10,
      f = e.m11,
      p = e.m12,
      m = e.m13,
      v = e.m14,
      g = e.m15,
      y = i.m00,
      T = i.m01,
      E = i.m02,
      x = i.m03;
     return t.m00 = y * n + T * o + E * l + x * p, t.m01 = y * r + T * c + E * _ + x * m, t.m02 = y * s + T * h + E * d + x * v, t.m03 = y * a + T * u + E * f + x * g, y = i.m04, T = i.m05, E = i.m06, x = i.m07, t.m04 = y * n + T * o + E * l + x * p, t.m05 = y * r + T * c + E * _ + x * m, t.m06 = y * s + T * h + E * d + x * v, t.m07 = y * a + T * u + E * f + x * g, y = i.m08, T = i.m09, E = i.m10, x = i.m11, t.m08 = y * n + T * o + E * l + x * p, t.m09 = y * r + T * c + E * _ + x * m, t.m10 = y * s + T * h + E * d + x * v, t.m11 = y * a + T * u + E * f + x * g, y = i.m12, T = i.m13, E = i.m14, x = i.m15, t.m12 = y * n + T * o + E * l + x * p, t.m13 = y * r + T * c + E * _ + x * m, t.m14 = y * s + T * h + E * d + x * v, t.m15 = y * a + T * u + E * f + x * g, t
    }
   };
   B.mul = B.multiply, B.translate = function(t, e, i) {
    var n, r, s, a, o, c, h, u, l, _, d, f, p = i.x,
     m = i.y,
     v = i.z;
    return e === t ? (t.m12 = e.m00 * p + e.m04 * m + e.m08 * v + e.m12, t.m13 = e.m01 * p + e.m05 * m + e.m09 * v + e.m13, t.m14 = e.m02 * p + e.m06 * m + e.m10 * v + e.m14, t.m15 = e.m03 * p + e.m07 * m + e.m11 * v + e.m15) : (n = e.m00, r = e.m01, s = e.m02, a = e.m03, o = e.m04, c = e.m05, h = e.m06, u = e.m07, l = e.m08, _ = e.m09, d = e.m10, f = e.m11, t.m00 = n, t.m01 = r, t.m02 = s, t.m03 = a, t.m04 = o, t.m05 = c, t.m06 = h, t.m07 = u, t.m08 = l, t.m09 = _, t.m10 = d, t.m11 = f, t.m12 = n * p + o * m + l * v + e.m12, t.m13 = r * p + c * m + _ * v + e.m13, t.m14 = s * p + h * m + d * v + e.m14, t.m15 = a * p + u * m + f * v + e.m15), t
   }, B.scale = function(t, e, i) {
    var n = i.x,
     r = i.y,
     s = i.z;
    return t.m00 = e.m00 * n, t.m01 = e.m01 * n, t.m02 = e.m02 * n, t.m03 = e.m03 * n, t.m04 = e.m04 * r, t.m05 = e.m05 * r, t.m06 = e.m06 * r, t.m07 = e.m07 * r, t.m08 = e.m08 * s, t.m09 = e.m09 * s, t.m10 = e.m10 * s, t.m11 = e.m11 * s, t.m12 = e.m12, t.m13 = e.m13, t.m14 = e.m14, t.m15 = e.m15, t
   }, B.rotate = function(t, e, i, n) {
    var r, a, o, c, h, u, l, _, d, f, p, m, v, g, y, T, E, x, C, A, b, S, w, D, R = n.x,
     M = n.y,
     I = n.z,
     L = Math.sqrt(R * R + M * M + I * I);
    return Math.abs(L) < s ? null : (R *= L = 1 / L, M *= L, I *= L, r = Math.sin(i), o = 1 - (a = Math.cos(i)), c = e.m00, h = e.m01, u = e.m02, l = e.m03, _ = e.m04, d = e.m05, f = e.m06, p = e.m07, m = e.m08, v = e.m09, g = e.m10, y = e.m11, T = R * R * o + a, E = M * R * o + I * r, x = I * R * o - M * r, C = R * M * o - I * r, A = M * M * o + a, b = I * M * o + R * r, S = R * I * o + M * r, w = M * I * o - R * r, D = I * I * o + a, t.m00 = c * T + _ * E + m * x, t.m01 = h * T + d * E + v * x, t.m02 = u * T + f * E + g * x, t.m03 = l * T + p * E + y * x, t.m04 = c * C + _ * A + m * b, t.m05 = h * C + d * A + v * b, t.m06 = u * C + f * A + g * b, t.m07 = l * C + p * A + y * b, t.m08 = c * S + _ * w + m * D, t.m09 = h * S + d * w + v * D, t.m10 = u * S + f * w + g * D, t.m11 = l * S + p * w + y * D, e !== t && (t.m12 = e.m12, t.m13 = e.m13, t.m14 = e.m14, t.m15 = e.m15), t)
   }, B.rotateX = function(t, e, i) {
    var n = Math.sin(i),
     r = Math.cos(i),
     s = e.m04,
     a = e.m05,
     o = e.m06,
     c = e.m07,
     h = e.m08,
     u = e.m09,
     l = e.m10,
     _ = e.m11;
    return e !== t && (t.m00 = e.m00, t.m01 = e.m01, t.m02 = e.m02, t.m03 = e.m03, t.m12 = e.m12, t.m13 = e.m13, t.m14 = e.m14, t.m15 = e.m15), t.m04 = s * r + h * n, t.m05 = a * r + u * n, t.m06 = o * r + l * n, t.m07 = c * r + _ * n, t.m08 = h * r - s * n, t.m09 = u * r - a * n, t.m10 = l * r - o * n, t.m11 = _ * r - c * n, t
   }, B.rotateY = function(t, e, i) {
    var n = Math.sin(i),
     r = Math.cos(i),
     s = e.m00,
     a = e.m01,
     o = e.m02,
     c = e.m03,
     h = e.m08,
     u = e.m09,
     l = e.m10,
     _ = e.m11;
    return e !== t && (t.m04 = e.m04, t.m05 = e.m05, t.m06 = e.m06, t.m07 = e.m07, t.m12 = e.m12, t.m13 = e.m13, t.m14 = e.m14, t.m15 = e.m15), t.m00 = s * r - h * n, t.m01 = a * r - u * n, t.m02 = o * r - l * n, t.m03 = c * r - _ * n, t.m08 = s * n + h * r, t.m09 = a * n + u * r, t.m10 = o * n + l * r, t.m11 = c * n + _ * r, t
   }, B.rotateZ = function(t, e, i) {
    var n = Math.sin(i),
     r = Math.cos(i),
     s = e.m00,
     a = e.m01,
     o = e.m02,
     c = e.m03,
     h = e.m04,
     u = e.m05,
     l = e.m06,
     _ = e.m07;
    return e !== t && (t.m08 = e.m08, t.m09 = e.m09, t.m10 = e.m10, t.m11 = e.m11, t.m12 = e.m12, t.m13 = e.m13, t.m14 = e.m14, t.m15 = e.m15), t.m00 = s * r + h * n, t.m01 = a * r + u * n, t.m02 = o * r + l * n, t.m03 = c * r + _ * n, t.m04 = h * r - s * n, t.m05 = u * r - a * n, t.m06 = l * r - o * n, t.m07 = _ * r - c * n, t
   }, B.fromTranslation = function(t, e) {
    return t.m00 = 1, t.m01 = 0, t.m02 = 0, t.m03 = 0, t.m04 = 0, t.m05 = 1, t.m06 = 0, t.m07 = 0, t.m08 = 0, t.m09 = 0, t.m10 = 1, t.m11 = 0, t.m12 = e.x, t.m13 = e.y, t.m14 = e.z, t.m15 = 1, t
   }, B.fromScaling = function(t, e) {
    return t.m00 = e.x, t.m01 = 0, t.m02 = 0, t.m03 = 0, t.m04 = 0, t.m05 = e.y, t.m06 = 0, t.m07 = 0, t.m08 = 0, t.m09 = 0, t.m10 = e.z, t.m11 = 0, t.m12 = 0, t.m13 = 0, t.m14 = 0, t.m15 = 1, t
   }, B.fromRotation = function(t, e, i) {
    var n, r, a, o = i.x,
     c = i.y,
     h = i.z,
     u = Math.sqrt(o * o + c * c + h * h);
    return Math.abs(u) < s ? null : (o *= u = 1 / u, c *= u, h *= u, n = Math.sin(e), a = 1 - (r = Math.cos(e)), t.m00 = o * o * a + r, t.m01 = c * o * a + h * n, t.m02 = h * o * a - c * n, t.m03 = 0, t.m04 = o * c * a - h * n, t.m05 = c * c * a + r, t.m06 = h * c * a + o * n, t.m07 = 0, t.m08 = o * h * a + c * n, t.m09 = c * h * a - o * n, t.m10 = h * h * a + r, t.m11 = 0, t.m12 = 0, t.m13 = 0, t.m14 = 0, t.m15 = 1, t)
   }, B.fromXRotation = function(t, e) {
    var i = Math.sin(e),
     n = Math.cos(e);
    return t.m00 = 1, t.m01 = 0, t.m02 = 0, t.m03 = 0, t.m04 = 0, t.m05 = n, t.m06 = i, t.m07 = 0, t.m08 = 0, t.m09 = -i, t.m10 = n, t.m11 = 0, t.m12 = 0, t.m13 = 0, t.m14 = 0, t.m15 = 1, t
   }, B.fromYRotation = function(t, e) {
    var i = Math.sin(e),
     n = Math.cos(e);
    return t.m00 = n, t.m01 = 0, t.m02 = -i, t.m03 = 0, t.m04 = 0, t.m05 = 1, t.m06 = 0, t.m07 = 0, t.m08 = i, t.m09 = 0, t.m10 = n, t.m11 = 0, t.m12 = 0, t.m13 = 0, t.m14 = 0, t.m15 = 1, t
   }, B.fromZRotation = function(t, e) {
    var i = Math.sin(e),
     n = Math.cos(e);
    return t.m00 = n, t.m01 = i, t.m02 = 0, t.m03 = 0, t.m04 = -i, t.m05 = n, t.m06 = 0, t.m07 = 0, t.m08 = 0, t.m09 = 0, t.m10 = 1, t.m11 = 0, t.m12 = 0, t.m13 = 0, t.m14 = 0, t.m15 = 1, t
   }, B.fromRT = function(t, e, i) {
    var n = e.x,
     r = e.y,
     s = e.z,
     a = e.w,
     o = n + n,
     c = r + r,
     h = s + s,
     u = n * o,
     l = n * c,
     _ = n * h,
     d = r * c,
     f = r * h,
     p = s * h,
     m = a * o,
     v = a * c,
     g = a * h;
    return t.m00 = 1 - (d + p), t.m01 = l + g, t.m02 = _ - v, t.m03 = 0, t.m04 = l - g, t.m05 = 1 - (u + p), t.m06 = f + m, t.m07 = 0, t.m08 = _ + v, t.m09 = f - m, t.m10 = 1 - (u + d), t.m11 = 0, t.m12 = i.x, t.m13 = i.y, t.m14 = i.z, t.m15 = 1, t
   }, B.getTranslation = function(t, e) {
    return t.x = e.m12, t.y = e.m13, t.z = e.m14, t
   }, B.getScaling = function(t, e) {
    var i = e.m00,
     n = e.m01,
     r = e.m02,
     s = e.m04,
     a = e.m05,
     o = e.m06,
     c = e.m08,
     h = e.m09,
     u = e.m10;
    return t.x = Math.sqrt(i * i + n * n + r * r), t.y = Math.sqrt(s * s + a * a + o * o), t.z = Math.sqrt(c * c + h * h + u * u), t
   }, B.getRotation = function(t, e) {
    var i = e.m00 + e.m05 + e.m10,
     n = 0;
    return i > 0 ? (n = 2 * Math.sqrt(i + 1), t.w = .25 * n, t.x = (e.m06 - e.m09) / n, t.y = (e.m08 - e.m02) / n, t.z = (e.m01 - e.m04) / n) : e.m00 > e.m05 & e.m00 > e.m10 ? (n = 2 * Math.sqrt(1 + e.m00 - e.m05 - e.m10), t.w = (e.m06 - e.m09) / n, t.x = .25 * n, t.y = (e.m01 + e.m04) / n, t.z = (e.m08 + e.m02) / n) : e.m05 > e.m10 ? (n = 2 * Math.sqrt(1 + e.m05 - e.m00 - e.m10), t.w = (e.m08 - e.m02) / n, t.x = (e.m01 + e.m04) / n, t.y = .25 * n, t.z = (e.m06 + e.m09) / n) : (n = 2 * Math.sqrt(1 + e.m10 - e.m00 - e.m05), t.w = (e.m01 - e.m04) / n, t.x = (e.m08 + e.m02) / n, t.y = (e.m06 + e.m09) / n, t.z = .25 * n), t
   }, B.fromRTS = function(t, e, i, n) {
    var r = e.x,
     s = e.y,
     a = e.z,
     o = e.w,
     c = r + r,
     h = s + s,
     u = a + a,
     l = r * c,
     _ = r * h,
     d = r * u,
     f = s * h,
     p = s * u,
     m = a * u,
     v = o * c,
     g = o * h,
     y = o * u,
     T = n.x,
     E = n.y,
     x = n.z;
    return t.m00 = (1 - (f + m)) * T, t.m01 = (_ + y) * T, t.m02 = (d - g) * T, t.m03 = 0, t.m04 = (_ - y) * E, t.m05 = (1 - (l + m)) * E, t.m06 = (p + v) * E, t.m07 = 0, t.m08 = (d + g) * x, t.m09 = (p - v) * x, t.m10 = (1 - (l + f)) * x, t.m11 = 0, t.m12 = i.x, t.m13 = i.y, t.m14 = i.z, t.m15 = 1, t
   }, B.fromRTSOrigin = function(t, e, i, n, r) {
    var s = e.x,
     a = e.y,
     o = e.z,
     c = e.w,
     h = s + s,
     u = a + a,
     l = o + o,
     _ = s * h,
     d = s * u,
     f = s * l,
     p = a * u,
     m = a * l,
     v = o * l,
     g = c * h,
     y = c * u,
     T = c * l,
     E = n.x,
     x = n.y,
     C = n.z,
     A = r.x,
     b = r.y,
     S = r.z;
    return t.m00 = (1 - (p + v)) * E, t.m01 = (d + T) * E, t.m02 = (f - y) * E, t.m03 = 0, t.m04 = (d - T) * x, t.m05 = (1 - (_ + v)) * x, t.m06 = (m + g) * x, t.m07 = 0, t.m08 = (f + y) * C, t.m09 = (m - g) * C, t.m10 = (1 - (_ + p)) * C, t.m11 = 0, t.m12 = i.x + A - (t.m00 * A + t.m04 * b + t.m08 * S), t.m13 = i.y + b - (t.m01 * A + t.m05 * b + t.m09 * S), t.m14 = i.z + S - (t.m02 * A + t.m06 * b + t.m10 * S), t.m15 = 1, t
   }, B.fromQuat = function(t, e) {
    var i = e.x,
     n = e.y,
     r = e.z,
     s = e.w,
     a = i + i,
     o = n + n,
     c = r + r,
     h = i * a,
     u = n * a,
     l = n * o,
     _ = r * a,
     d = r * o,
     f = r * c,
     p = s * a,
     m = s * o,
     v = s * c;
    return t.m00 = 1 - l - f, t.m01 = u + v, t.m02 = _ - m, t.m03 = 0, t.m04 = u - v, t.m05 = 1 - h - f, t.m06 = d + p, t.m07 = 0, t.m08 = _ + m, t.m09 = d - p, t.m10 = 1 - h - l, t.m11 = 0, t.m12 = 0, t.m13 = 0, t.m14 = 0, t.m15 = 1, t
   }, B.frustum = function(t, e, i, n, r, s, a) {
    var o = 1 / (i - e),
     c = 1 / (r - n),
     h = 1 / (s - a);
    return t.m00 = 2 * s * o, t.m01 = 0, t.m02 = 0, t.m03 = 0, t.m04 = 0, t.m05 = 2 * s * c, t.m06 = 0, t.m07 = 0, t.m08 = (i + e) * o, t.m09 = (r + n) * c, t.m10 = (a + s) * h, t.m11 = -1, t.m12 = 0, t.m13 = 0, t.m14 = a * s * 2 * h, t.m15 = 0, t
   }, B.perspective = function(t, e, i, n, r) {
    var s = 1 / Math.tan(e / 2),
     a = 1 / (n - r);
    return t.m00 = s / i, t.m01 = 0, t.m02 = 0, t.m03 = 0, t.m04 = 0, t.m05 = s, t.m06 = 0, t.m07 = 0, t.m08 = 0, t.m09 = 0, t.m10 = (r + n) * a, t.m11 = -1, t.m12 = 0, t.m13 = 0, t.m14 = 2 * r * n * a, t.m15 = 0, t
   }, B.perspectiveFromFieldOfView = function(t, e, i, n) {
    var r = Math.tan(e.upDegrees * Math.PI / 180),
     s = Math.tan(e.downDegrees * Math.PI / 180),
     a = Math.tan(e.leftDegrees * Math.PI / 180),
     o = Math.tan(e.rightDegrees * Math.PI / 180),
     c = 2 / (a + o),
     h = 2 / (r + s);
    return t.m00 = c, t.m01 = 0, t.m02 = 0, t.m03 = 0, t.m04 = 0, t.m05 = h, t.m06 = 0, t.m07 = 0, t.m08 = -(a - o) * c * .5, t.m09 = (r - s) * h * .5, t.m10 = n / (i - n), t.m11 = -1, t.m12 = 0, t.m13 = 0, t.m14 = n * i / (i - n), t.m15 = 0, t
   }, B.ortho = function(t, e, i, n, r, s, a) {
    var o = 1 / (e - i),
     c = 1 / (n - r),
     h = 1 / (s - a);
    return t.m00 = -2 * o, t.m01 = 0, t.m02 = 0, t.m03 = 0, t.m04 = 0, t.m05 = -2 * c, t.m06 = 0, t.m07 = 0, t.m08 = 0, t.m09 = 0, t.m10 = 2 * h, t.m11 = 0, t.m12 = (e + i) * o, t.m13 = (r + n) * c, t.m14 = (a + s) * h, t.m15 = 1, t
   }, B.lookAt = function(t, e, i, n) {
    var r, a, o, c, h, u, l, _, d, f, p = e.x,
     m = e.y,
     v = e.z,
     g = n.x,
     y = n.y,
     T = n.z,
     E = i.x,
     x = i.y,
     C = i.z;
    return Math.abs(p - E) < s && Math.abs(m - x) < s && Math.abs(v - C) < s ? B.identity(t) : (l = p - E, _ = m - x, d = v - C, r = y * (d *= f = 1 / Math.sqrt(l * l + _ * _ + d * d)) - T * (_ *= f), a = T * (l *= f) - g * d, o = g * _ - y * l, (f = Math.sqrt(r * r + a * a + o * o)) ? (r *= f = 1 / f, a *= f, o *= f) : (r = 0, a = 0, o = 0), c = _ * o - d * a, h = d * r - l * o, u = l * a - _ * r, (f = Math.sqrt(c * c + h * h + u * u)) ? (c *= f = 1 / f, h *= f, u *= f) : (c = 0, h = 0, u = 0), t.m00 = r, t.m01 = c, t.m02 = l, t.m03 = 0, t.m04 = a, t.m05 = h, t.m06 = _, t.m07 = 0, t.m08 = o, t.m09 = u, t.m10 = d, t.m11 = 0, t.m12 = -(r * p + a * m + o * v), t.m13 = -(c * p + h * m + u * v), t.m14 = -(l * p + _ * m + d * v), t.m15 = 1, t)
   }, B.str = function(t) {
    return "mat4(" + t.m00 + ", " + t.m01 + ", " + t.m02 + ", " + t.m03 + ", " + t.m04 + ", " + t.m05 + ", " + t.m06 + ", " + t.m07 + ", " + t.m08 + ", " + t.m09 + ", " + t.m10 + ", " + t.m11 + ", " + t.m12 + ", " + t.m13 + ", " + t.m14 + ", " + t.m15 + ")"
   }, B.array = function(t, e) {
    return t[0] = e.m00, t[1] = e.m01, t[2] = e.m02, t[3] = e.m03, t[4] = e.m04, t[5] = e.m05, t[6] = e.m06, t[7] = e.m07, t[8] = e.m08, t[9] = e.m09, t[10] = e.m10, t[11] = e.m11, t[12] = e.m12, t[13] = e.m13, t[14] = e.m14, t[15] = e.m15, t
   }, B.frob = function(t) {
    return Math.sqrt(Math.pow(t.m00, 2) + Math.pow(t.m01, 2) + Math.pow(t.m02, 2) + Math.pow(t.m03, 2) + Math.pow(t.m04, 2) + Math.pow(t.m05, 2) + Math.pow(t.m06, 2) + Math.pow(t.m07, 2) + Math.pow(t.m08, 2) + Math.pow(t.m09, 2) + Math.pow(t.m10, 2) + Math.pow(t.m11, 2) + Math.pow(t.m12, 2) + Math.pow(t.m13, 2) + Math.pow(t.m14, 2) + Math.pow(t.m15, 2))
   }, B.add = function(t, e, i) {
    return t.m00 = e.m00 + i.m00, t.m01 = e.m01 + i.m01, t.m02 = e.m02 + i.m02, t.m03 = e.m03 + i.m03, t.m04 = e.m04 + i.m04, t.m05 = e.m05 + i.m05, t.m06 = e.m06 + i.m06, t.m07 = e.m07 + i.m07, t.m08 = e.m08 + i.m08, t.m09 = e.m09 + i.m09, t.m10 = e.m10 + i.m10, t.m11 = e.m11 + i.m11, t.m12 = e.m12 + i.m12, t.m13 = e.m13 + i.m13, t.m14 = e.m14 + i.m14, t.m15 = e.m15 + i.m15, t
   }, B.subtract = function(t, e, i) {
    return t.m00 = e.m00 - i.m00, t.m01 = e.m01 - i.m01, t.m02 = e.m02 - i.m02, t.m03 = e.m03 - i.m03, t.m04 = e.m04 - i.m04, t.m05 = e.m05 - i.m05, t.m06 = e.m06 - i.m06, t.m07 = e.m07 - i.m07, t.m08 = e.m08 - i.m08, t.m09 = e.m09 - i.m09, t.m10 = e.m10 - i.m10, t.m11 = e.m11 - i.m11, t.m12 = e.m12 - i.m12, t.m13 = e.m13 - i.m13, t.m14 = e.m14 - i.m14, t.m15 = e.m15 - i.m15, t
   }, B.sub = B.subtract, B.multiplyScalar = function(t, e, i) {
    return t.m00 = e.m00 * i, t.m01 = e.m01 * i, t.m02 = e.m02 * i, t.m03 = e.m03 * i, t.m04 = e.m04 * i, t.m05 = e.m05 * i, t.m06 = e.m06 * i, t.m07 = e.m07 * i, t.m08 = e.m08 * i, t.m09 = e.m09 * i, t.m10 = e.m10 * i, t.m11 = e.m11 * i, t.m12 = e.m12 * i, t.m13 = e.m13 * i, t.m14 = e.m14 * i, t.m15 = e.m15 * i, t
   }, B.multiplyScalarAndAdd = function(t, e, i, n) {
    return t.m00 = e.m00 + i.m00 * n, t.m01 = e.m01 + i.m01 * n, t.m02 = e.m02 + i.m02 * n, t.m03 = e.m03 + i.m03 * n, t.m04 = e.m04 + i.m04 * n, t.m05 = e.m05 + i.m05 * n, t.m06 = e.m06 + i.m06 * n, t.m07 = e.m07 + i.m07 * n, t.m08 = e.m08 + i.m08 * n, t.m09 = e.m09 + i.m09 * n, t.m10 = e.m10 + i.m10 * n, t.m11 = e.m11 + i.m11 * n, t.m12 = e.m12 + i.m12 * n, t.m13 = e.m13 + i.m13 * n, t.m14 = e.m14 + i.m14 * n, t.m15 = e.m15 + i.m15 * n, t
   }, B.exactEquals = function(t, e) {
    return t.m00 === e.m00 && t.m01 === e.m01 && t.m02 === e.m02 && t.m03 === e.m03 && t.m04 === e.m04 && t.m05 === e.m05 && t.m06 === e.m06 && t.m07 === e.m07 && t.m08 === e.m08 && t.m09 === e.m09 && t.m10 === e.m10 && t.m11 === e.m11 && t.m12 === e.m12 && t.m13 === e.m13 && t.m14 === e.m14 && t.m15 === e.m15
   }, B.equals = function(t, e) {
    var i = t.m00,
     n = t.m01,
     r = t.m02,
     a = t.m03,
     o = t.m04,
     c = t.m05,
     h = t.m06,
     u = t.m07,
     l = t.m08,
     _ = t.m09,
     d = t.m10,
     f = t.m11,
     p = t.m12,
     m = t.m13,
     v = t.m14,
     g = t.m15,
     y = e.m00,
     T = e.m01,
     E = e.m02,
     x = e.m03,
     C = e.m04,
     A = e.m05,
     b = e.m06,
     S = e.m07,
     w = e.m08,
     D = e.m09,
     R = e.m10,
     M = e.m11,
     I = e.m12,
     L = e.m13,
     O = e.m14,
     P = e.m15;
    return Math.abs(i - y) <= s * Math.max(1, Math.abs(i), Math.abs(y)) && Math.abs(n - T) <= s * Math.max(1, Math.abs(n), Math.abs(T)) && Math.abs(r - E) <= s * Math.max(1, Math.abs(r), Math.abs(E)) && Math.abs(a - x) <= s * Math.max(1, Math.abs(a), Math.abs(x)) && Math.abs(o - C) <= s * Math.max(1, Math.abs(o), Math.abs(C)) && Math.abs(c - A) <= s * Math.max(1, Math.abs(c), Math.abs(A)) && Math.abs(h - b) <= s * Math.max(1, Math.abs(h), Math.abs(b)) && Math.abs(u - S) <= s * Math.max(1, Math.abs(u), Math.abs(S)) && Math.abs(l - w) <= s * Math.max(1, Math.abs(l), Math.abs(w)) && Math.abs(_ - D) <= s * Math.max(1, Math.abs(_), Math.abs(D)) && Math.abs(d - R) <= s * Math.max(1, Math.abs(d), Math.abs(R)) && Math.abs(f - M) <= s * Math.max(1, Math.abs(f), Math.abs(M)) && Math.abs(p - I) <= s * Math.max(1, Math.abs(p), Math.abs(I)) && Math.abs(m - L) <= s * Math.max(1, Math.abs(m), Math.abs(L)) && Math.abs(v - O) <= s * Math.max(1, Math.abs(v), Math.abs(O)) && Math.abs(g - P) <= s * Math.max(1, Math.abs(g), Math.abs(P))
   };
   var k = new Array(3),
    z = function(t, e, i) {
     this.r = t, this.g = e, this.b = i
    };
   z.prototype.toJSON = function() {
    return k[0] = this.r, k[1] = this.g, k[2] = this.b, k
   };
   var U = {
    create: function() {
     return new z(1, 1, 1)
    },
    new: function(t, e, i) {
     return new z(t, e, i)
    },
    clone: function(t) {
     return new z(t.r, t.g, t.b, t.a)
    },
    copy: function(t, e) {
     return t.r = e.r, t.g = e.g, t.b = e.b, t
    },
    set: function(t, e, i, n) {
     return t.r = e, t.g = i, t.b = n, t
    },
    fromHex: function(t, e) {
     var i = (e >> 16) / 255,
      n = (e >> 8 & 255) / 255,
      r = (255 & e) / 255;
     return t.r = i, t.g = n, t.b = r, t
    },
    add: function(t, e, i) {
     return t.r = e.r + i.r, t.g = e.g + i.g, t.b = e.b + i.b, t
    },
    subtract: function(t, e, i) {
     return t.r = e.r - i.r, t.g = e.g - i.g, t.b = e.b - i.b, t
    }
   };
   U.sub = U.subtract, U.multiply = function(t, e, i) {
    return t.r = e.r * i.r, t.g = e.g * i.g, t.b = e.b * i.b, t
   }, U.mul = U.multiply, U.divide = function(t, e, i) {
    return t.r = e.r / i.r, t.g = e.g / i.g, t.b = e.b / i.b, t
   }, U.div = U.divide, U.scale = function(t, e, i) {
    return t.r = e.r * i, t.g = e.g * i, t.b = e.b * i, t
   }, U.lerp = function(t, e, i, n) {
    var r = e.r,
     s = e.g,
     a = e.b;
    return t.r = r + n * (i.r - r), t.g = s + n * (i.g - s), t.b = a + n * (i.b - a), t
   }, U.str = function(t) {
    return "color3(" + t.r + ", " + t.g + ", " + t.b + ")"
   }, U.array = function(t, e) {
    return t[0] = e.r, t[1] = e.g, t[2] = e.b, t
   }, U.exactEquals = function(t, e) {
    return t.r === e.r && t.g === e.g && t.b === e.b
   }, U.equals = function(t, e) {
    var i = t.r,
     n = t.g,
     r = t.b,
     a = e.r,
     o = e.g,
     c = e.b;
    return Math.abs(i - a) <= s * Math.max(1, Math.abs(i), Math.abs(a)) && Math.abs(n - o) <= s * Math.max(1, Math.abs(n), Math.abs(o)) && Math.abs(r - c) <= s * Math.max(1, Math.abs(r), Math.abs(c))
   }, U.hex = function(t) {
    return 255 * t.r << 16 | 255 * t.g << 8 | 255 * t.b
   };
   var W = new Array(4),
    H = function(t, e, i, n) {
     this.r = t, this.g = e, this.b = i, this.a = n
    };
   H.prototype.toJSON = function() {
    return W[0] = this.r, W[1] = this.g, W[2] = this.b, W[3] = this.a, W
   };
   var G = {
    create: function() {
     return new H(1, 1, 1, 1)
    },
    new: function(t, e, i, n) {
     return new H(t, e, i, n)
    },
    clone: function(t) {
     return new H(t.r, t.g, t.b, t.a)
    },
    copy: function(t, e) {
     return t.r = e.r, t.g = e.g, t.b = e.b, t.a = e.a, t
    },
    set: function(t, e, i, n, r) {
     return t.r = e, t.g = i, t.b = n, t.a = r, t
    },
    fromHex: function(t, e) {
     var i = (e >> 24) / 255,
      n = (e >> 16 & 255) / 255,
      r = (e >> 8 & 255) / 255,
      s = (255 & e) / 255;
     return t.r = i, t.g = n, t.b = r, t.a = s, t
    },
    add: function(t, e, i) {
     return t.r = e.r + i.r, t.g = e.g + i.g, t.b = e.b + i.b, t.a = e.a + i.a, t
    },
    subtract: function(t, e, i) {
     return t.r = e.r - i.r, t.g = e.g - i.g, t.b = e.b - i.b, t.a = e.a - i.a, t
    }
   };
   G.sub = G.subtract, G.multiply = function(t, e, i) {
    return t.r = e.r * i.r, t.g = e.g * i.g, t.b = e.b * i.b, t.a = e.a * i.a, t
   }, G.mul = G.multiply, G.divide = function(t, e, i) {
    return t.r = e.r / i.r, t.g = e.g / i.g, t.b = e.b / i.b, t.a = e.a / i.a, t
   }, G.div = G.divide, G.scale = function(t, e, i) {
    return t.r = e.r * i, t.g = e.g * i, t.b = e.b * i, t.a = e.a * i, t
   }, G.lerp = function(t, e, i, n) {
    var r = e.r,
     s = e.g,
     a = e.b,
     o = e.a;
    return t.r = r + n * (i.r - r), t.g = s + n * (i.g - s), t.b = a + n * (i.b - a), t.a = o + n * (i.a - o), t
   }, G.str = function(t) {
    return "color4(" + t.r + ", " + t.g + ", " + t.b + ", " + t.a + ")"
   }, G.array = function(t, e) {
    return t[0] = e.r, t[1] = e.g, t[2] = e.b, t[3] = e.a, t
   }, G.exactEquals = function(t, e) {
    return t.r === e.r && t.g === e.g && t.b === e.b && t.a === e.a
   }, G.equals = function(t, e) {
    var i = t.r,
     n = t.g,
     r = t.b,
     a = t.a,
     o = e.r,
     c = e.g,
     h = e.b,
     u = e.a;
    return Math.abs(i - o) <= s * Math.max(1, Math.abs(i), Math.abs(o)) && Math.abs(n - c) <= s * Math.max(1, Math.abs(n), Math.abs(c)) && Math.abs(r - h) <= s * Math.max(1, Math.abs(r), Math.abs(h)) && Math.abs(a - u) <= s * Math.max(1, Math.abs(a), Math.abs(u))
   }, G.hex = function(t) {
    return (255 * t.r << 24 | 255 * t.g << 16 | 255 * t.b << 8 | 255 * t.a) >>> 0
   };
   var V = d,
    j = Object.freeze({
     bits: V,
     vec2: m,
     vec3: y,
     vec4: x,
     quat: D,
     mat2: I,
     mat23: P,
     mat3: b,
     mat4: B,
     color3: U,
     color4: G,
     EPSILON: s,
     equals: function(t, e) {
      return Math.abs(t - e) <= s * Math.max(1, Math.abs(t), Math.abs(e))
     },
     approx: function(t, e, i) {
      return i = i || s, Math.abs(t - e) <= i
     },
     clamp: function(t, e, i) {
      return t < e ? e : t > i ? i : t
     },
     clamp01: function(t) {
      return t < 0 ? 0 : t > 1 ? 1 : t
     },
     lerp: function(t, e, i) {
      return t + (e - t) * i
     },
     toRadian: a,
     toDegree: function(t) {
      return t * r
     },
     random: o,
     randomRange: c,
     randomRangeInt: function(t, e) {
      return Math.floor(c(t, e))
     },
     nextPow2: function(t) {
      return --t, t |= t >> 1, t |= t >> 2, t |= t >> 4, t |= t >> 8, t |= t >> 16, ++t
     }
    }),
    Y = {
     PROJ_PERSPECTIVE: 0,
     PROJ_ORTHO: 1,
     LIGHT_DIRECTIONAL: 0,
     LIGHT_POINT: 1,
     LIGHT_SPOT: 2,
     SHADOW_NONE: 0,
     SHADOW_HARD: 1,
     SHADOW_SOFT: 2,
     PARAM_INT: 0,
     PARAM_INT2: 1,
     PARAM_INT3: 2,
     PARAM_INT4: 3,
     PARAM_FLOAT: 4,
     PARAM_FLOAT2: 5,
     PARAM_FLOAT3: 6,
     PARAM_FLOAT4: 7,
     PARAM_COLOR3: 8,
     PARAM_COLOR4: 9,
     PARAM_MAT2: 10,
     PARAM_MAT3: 11,
     PARAM_MAT4: 12,
     PARAM_TEXTURE_2D: 13,
     PARAM_TEXTURE_CUBE: 14,
     CLEAR_COLOR: 1,
     CLEAR_DEPTH: 2,
     CLEAR_STENCIL: 4
    },
    X = [
     [9728, 9984, 9986],
     [9729, 9985, 9987]
    ],
    q = [{
     format: 6407,
     internalFormat: 33776,
     pixelType: null
    }, {
     format: 6408,
     internalFormat: 33777,
     pixelType: null
    }, {
     format: 6408,
     internalFormat: 33778,
     pixelType: null
    }, {
     format: 6408,
     internalFormat: 33779,
     pixelType: null
    }, {
     format: 6407,
     internalFormat: 36196,
     pixelType: null
    }, {
     format: 6407,
     internalFormat: 35841,
     pixelType: null
    }, {
     format: 6408,
     internalFormat: 35843,
     pixelType: null
    }, {
     format: 6407,
     internalFormat: 35840,
     pixelType: null
    }, {
     format: 6408,
     internalFormat: 35842,
     pixelType: null
    }, {
     format: 6406,
     internalFormat: 6406,
     pixelType: 5121
    }, {
     format: 6409,
     internalFormat: 6409,
     pixelType: 5121
    }, {
     format: 6410,
     internalFormat: 6410,
     pixelType: 5121
    }, {
     format: 6407,
     internalFormat: 6407,
     pixelType: 33635
    }, {
     format: 6408,
     internalFormat: 6408,
     pixelType: 32820
    }, {
     format: 6408,
     internalFormat: 6408,
     pixelType: 32819
    }, {
     format: 6407,
     internalFormat: 6407,
     pixelType: 5121
    }, {
     format: 6408,
     internalFormat: 6408,
     pixelType: 5121
    }, {
     format: 6407,
     internalFormat: 6407,
     pixelType: 36193
    }, {
     format: 6408,
     internalFormat: 6408,
     pixelType: 36193
    }, {
     format: 6407,
     internalFormat: 6407,
     pixelType: 5126
    }, {
     format: 6408,
     internalFormat: 6408,
     pixelType: 5126
    }, {
     format: null,
     internalFormat: null,
     pixelType: null
    }, {
     format: null,
     internalFormat: null,
     pixelType: null
    }, {
     format: null,
     internalFormat: null,
     pixelType: null
    }, {
     format: null,
     internalFormat: null,
     pixelType: null
    }, {
     format: 6402,
     internalFormat: 6402,
     pixelType: 5123
    }, {
     format: 6402,
     internalFormat: 6402,
     pixelType: 5125
    }, {
     format: null,
     internalFormat: null,
     pixelType: null
    }],
    Z = {
     USAGE_STATIC: 35044,
     USAGE_DYNAMIC: 35048,
     USAGE_STREAM: 35040,
     INDEX_FMT_UINT8: 5121,
     INDEX_FMT_UINT16: 5123,
     INDEX_FMT_UINT32: 5125,
     ATTR_POSITION: "a_position",
     ATTR_NORMAL: "a_normal",
     ATTR_TANGENT: "a_tangent",
     ATTR_BITANGENT: "a_bitangent",
     ATTR_WEIGHTS: "a_weights",
     ATTR_JOINTS: "a_joints",
     ATTR_COLOR: "a_color",
     ATTR_COLOR0: "a_color0",
     ATTR_COLOR1: "a_color1",
     ATTR_UV: "a_uv",
     ATTR_UV0: "a_uv0",
     ATTR_UV1: "a_uv1",
     ATTR_UV2: "a_uv2",
     ATTR_UV3: "a_uv3",
     ATTR_UV4: "a_uv4",
     ATTR_UV5: "a_uv5",
     ATTR_UV6: "a_uv6",
     ATTR_UV7: "a_uv7",
     ATTR_TYPE_INT8: 5120,
     ATTR_TYPE_UINT8: 5121,
     ATTR_TYPE_INT16: 5122,
     ATTR_TYPE_UINT16: 5123,
     ATTR_TYPE_INT32: 5124,
     ATTR_TYPE_UINT32: 5125,
     ATTR_TYPE_FLOAT32: 5126,
     FILTER_NEAREST: 0,
     FILTER_LINEAR: 1,
     WRAP_REPEAT: 10497,
     WRAP_CLAMP: 33071,
     WRAP_MIRROR: 33648,
     TEXTURE_FMT_RGB_DXT1: 0,
     TEXTURE_FMT_RGBA_DXT1: 1,
     TEXTURE_FMT_RGBA_DXT3: 2,
     TEXTURE_FMT_RGBA_DXT5: 3,
     TEXTURE_FMT_RGB_ETC1: 4,
     TEXTURE_FMT_RGB_PVRTC_2BPPV1: 5,
     TEXTURE_FMT_RGBA_PVRTC_2BPPV1: 6,
     TEXTURE_FMT_RGB_PVRTC_4BPPV1: 7,
     TEXTURE_FMT_RGBA_PVRTC_4BPPV1: 8,
     TEXTURE_FMT_A8: 9,
     TEXTURE_FMT_L8: 10,
     TEXTURE_FMT_L8_A8: 11,
     TEXTURE_FMT_R5_G6_B5: 12,
     TEXTURE_FMT_R5_G5_B5_A1: 13,
     TEXTURE_FMT_R4_G4_B4_A4: 14,
     TEXTURE_FMT_RGB8: 15,
     TEXTURE_FMT_RGBA8: 16,
     TEXTURE_FMT_RGB16F: 17,
     TEXTURE_FMT_RGBA16F: 18,
     TEXTURE_FMT_RGB32F: 19,
     TEXTURE_FMT_RGBA32F: 20,
     TEXTURE_FMT_R32F: 21,
     TEXTURE_FMT_111110F: 22,
     TEXTURE_FMT_SRGB: 23,
     TEXTURE_FMT_SRGBA: 24,
     TEXTURE_FMT_D16: 25,
     TEXTURE_FMT_D32: 26,
     TEXTURE_FMT_D24S8: 27,
     DS_FUNC_NEVER: 512,
     DS_FUNC_LESS: 513,
     DS_FUNC_EQUAL: 514,
     DS_FUNC_LEQUAL: 515,
     DS_FUNC_GREATER: 516,
     DS_FUNC_NOTEQUAL: 517,
     DS_FUNC_GEQUAL: 518,
     DS_FUNC_ALWAYS: 519,
     RB_FMT_RGBA4: 32854,
     RB_FMT_RGB5_A1: 32855,
     RB_FMT_RGB565: 36194,
     RB_FMT_D16: 33189,
     RB_FMT_S8: 36168,
     RB_FMT_D24S8: 34041,
     BLEND_FUNC_ADD: 32774,
     BLEND_FUNC_SUBTRACT: 32778,
     BLEND_FUNC_REVERSE_SUBTRACT: 32779,
     BLEND_ZERO: 0,
     BLEND_ONE: 1,
     BLEND_SRC_COLOR: 768,
     BLEND_ONE_MINUS_SRC_COLOR: 769,
     BLEND_DST_COLOR: 774,
     BLEND_ONE_MINUS_DST_COLOR: 775,
     BLEND_SRC_ALPHA: 770,
     BLEND_ONE_MINUS_SRC_ALPHA: 771,
     BLEND_DST_ALPHA: 772,
     BLEND_ONE_MINUS_DST_ALPHA: 773,
     BLEND_CONSTANT_COLOR: 32769,
     BLEND_ONE_MINUS_CONSTANT_COLOR: 32770,
     BLEND_CONSTANT_ALPHA: 32771,
     BLEND_ONE_MINUS_CONSTANT_ALPHA: 32772,
     BLEND_SRC_ALPHA_SATURATE: 776,
     STENCIL_OP_KEEP: 7680,
     STENCIL_OP_ZERO: 0,
     STENCIL_OP_REPLACE: 7681,
     STENCIL_OP_INCR: 7682,
     STENCIL_OP_INCR_WRAP: 34055,
     STENCIL_OP_DECR: 7683,
     STENCIL_OP_DECR_WRAP: 34056,
     STENCIL_OP_INVERT: 5386,
     CULL_NONE: 0,
     CULL_FRONT: 1028,
     CULL_BACK: 1029,
     CULL_FRONT_AND_BACK: 1032,
     PT_POINTS: 0,
     PT_LINES: 1,
     PT_LINE_LOOP: 2,
     PT_LINE_STRIP: 3,
     PT_TRIANGLES: 4,
     PT_TRIANGLE_STRIP: 5,
     PT_TRIANGLE_FAN: 6
    };

   function K(t) {
    return t === Z.ATTR_TYPE_INT8 ? 1 : t === Z.ATTR_TYPE_UINT8 ? 1 : t === Z.ATTR_TYPE_INT16 ? 2 : t === Z.ATTR_TYPE_UINT16 ? 2 : t === Z.ATTR_TYPE_INT32 ? 4 : t === Z.ATTR_TYPE_UINT32 ? 4 : t === Z.ATTR_TYPE_FLOAT32 ? 4 : (console.warn("Unknown ATTR_TYPE: " + t), 0)
   }

   function J(t, e, i) {
    void 0 === i && (i = -1);
    var n = X[e][i + 1];
    return void 0 === n ? (console.warn("Unknown FILTER: " + e), -1 === i ? t.LINEAR : t.LINEAR_MIPMAP_LINEAR) : n
   }

   function Q(t) {
    var e = q[t];
    return void 0 === e ? (console.warn("Unknown TEXTURE_FMT: " + t), q[Z.TEXTURE_FMT_RGBA8]) : e
   }
   var $ = function(t) {
    this._attr2el = {}, this._elements = [], this._bytes = 0;
    for (var e = 0, i = 0, n = t.length; i < n; ++i) {
     var r = t[i],
      s = {
       name: r.name,
       offset: e,
       stride: 0,
       stream: -1,
       type: r.type,
       num: r.num,
       normalize: void 0 !== r.normalize && r.normalize,
       bytes: r.num * K(r.type)
      };
     this._attr2el[s.name] = s, this._elements.push(s), this._bytes += s.bytes, e += s.bytes
    }
    for (var a = 0, o = this._elements.length; a < o; ++a) {
     this._elements[a].stride = this._bytes
    }
   };
   $.prototype.element = function(t) {
    return this._attr2el[t]
   };
   var tt = function(t, e, i, n, r) {
     this._device = t, this._format = e, this._usage = i, this._numIndices = r, this._bytesPerIndex = 0, e === Z.INDEX_FMT_UINT8 ? this._bytesPerIndex = 1 : e === Z.INDEX_FMT_UINT16 ? this._bytesPerIndex = 2 : e === Z.INDEX_FMT_UINT32 && (this._bytesPerIndex = 4), this._bytes = this._bytesPerIndex * r, this._glID = t._gl.createBuffer(), this.update(0, n), t._stats.ib += this._bytes
    },
    et = {
     count: {
      configurable: !0
     }
    };
   tt.prototype.destroy = function() {
    -1 !== this._glID ? (this._device._gl.deleteBuffer(this._glID), this._device._stats.ib -= this.bytes, this._glID = -1) : console.error("The buffer already destroyed")
   }, tt.prototype.update = function(t, e) {
    if (-1 !== this._glID)
     if (e && e.byteLength + t > this._bytes) console.error("Failed to update data, bytes exceed.");
     else {
      var i = this._device._gl,
       n = this._usage;
      i.bindBuffer(i.ELEMENT_ARRAY_BUFFER, this._glID), e ? t ? i.bufferSubData(i.ELEMENT_ARRAY_BUFFER, t, e) : i.bufferData(i.ELEMENT_ARRAY_BUFFER, e, n) : this._bytes ? i.bufferData(i.ELEMENT_ARRAY_BUFFER, this._bytes, n) : console.warn("bufferData should not submit 0 bytes data"), this._device._restoreIndexBuffer()
     }
    else console.error("The buffer is destroyed")
   }, et.count.get = function() {
    return this._numIndices
   }, Object.defineProperties(tt.prototype, et);
   var it = function(t, e, i, n, r) {
     this._device = t, this._format = e, this._usage = i, this._numVertices = r, this._bytes = this._format._bytes * r, this._glID = t._gl.createBuffer(), this.update(0, n), t._stats.vb += this._bytes
    },
    nt = {
     count: {
      configurable: !0
     }
    };
   it.prototype.destroy = function() {
    -1 !== this._glID ? (this._device._gl.deleteBuffer(this._glID), this._device._stats.vb -= this.bytes, this._glID = -1) : console.error("The buffer already destroyed")
   }, it.prototype.update = function(t, e) {
    if (-1 !== this._glID)
     if (e && e.byteLength + t > this._bytes) console.error("Failed to update data, bytes exceed.");
     else {
      var i = this._device._gl,
       n = this._usage;
      i.bindBuffer(i.ARRAY_BUFFER, this._glID), e ? t ? i.bufferSubData(i.ARRAY_BUFFER, t, e) : i.bufferData(i.ARRAY_BUFFER, e, n) : this._bytes ? i.bufferData(i.ARRAY_BUFFER, this._bytes, n) : console.warn("bufferData should not submit 0 bytes data"), i.bindBuffer(i.ARRAY_BUFFER, null)
     }
    else console.error("The buffer is destroyed")
   }, nt.count.get = function() {
    return this._numVertices
   }, Object.defineProperties(it.prototype, nt);
   var rt = 0;

   function st(t, e, i) {
    i.split("\n").forEach((function(i) {
     if (!(i.length < 5)) {
      var n = /^ERROR\:\s+(\d+)\:(\d+)\:\s*(.*)$/.exec(i);
      n ? t.push({
       type: e,
       fileID: 0 | n[1],
       line: 0 | n[2],
       message: n[3].trim()
      }) : i.length > 0 && t.push({
       type: e,
       fileID: -1,
       line: 0,
       message: i
      })
     }
    }))
   }
   var at = function(t, e) {
     this._device = t, this._attributes = [], this._uniforms = [], this._samplers = [], this._errors = [], this._linked = !1, this._vertSource = e.vert, this._fragSource = e.frag, this._glID = null, this._id = rt++
    },
    ot = {
     id: {
      configurable: !0
     }
    };

   function ct(t, e, i) {
    var n = t.createShader(e);
    return t.shaderSource(n, i), t.compileShader(n), n
   }
   ot.id.get = function() {
    return this._id
   }, at.prototype.link = function() {
    if (!this._linked) {
     var t = this._device._gl,
      e = ct(t, t.VERTEX_SHADER, this._vertSource),
      i = ct(t, t.FRAGMENT_SHADER, this._fragSource),
      n = t.createProgram();
     t.attachShader(n, e), t.attachShader(n, i), t.linkProgram(n);
     var r = !1,
      s = this._errors;
     if (t.getShaderParameter(e, t.COMPILE_STATUS) || (st(s, "vs", t.getShaderInfoLog(e)), r = !0), t.getShaderParameter(i, t.COMPILE_STATUS) || (st(s, "fs", t.getShaderInfoLog(i)), r = !0), t.deleteShader(e), t.deleteShader(i), r) s.forEach((function(t) {
      console.error("Failed to compile " + t.type + " " + t.fileID + " (ln " + t.line + "): " + t.message)
     }));
     else if (t.getProgramParameter(n, t.LINK_STATUS) || (console.error("Failed to link shader program: " + t.getProgramInfoLog(n)), r = !0), !r) {
      this._glID = n;
      for (var a = t.getProgramParameter(n, t.ACTIVE_ATTRIBUTES), o = 0; o < a; ++o) {
       var c = t.getActiveAttrib(n, o),
        h = t.getAttribLocation(n, c.name);
       this._attributes.push({
        name: c.name,
        location: h,
        type: c.type
       })
      }
      for (var u = t.getProgramParameter(n, t.ACTIVE_UNIFORMS), l = 0; l < u; ++l) {
       var _ = t.getActiveUniform(n, l),
        d = _.name,
        f = t.getUniformLocation(n, d),
        p = "[0]" === d.substr(d.length - 3);
       p && (d = d.substr(0, d.length - 3)), this._uniforms.push({
        name: d,
        location: f,
        type: _.type,
        size: p ? _.size : void 0
       })
      }
      this._linked = !0
     }
    }
   }, at.prototype.destroy = function() {
    this._device._gl.deleteProgram(this._glID), this._linked = !1, this._glID = null, this._attributes = [], this._uniforms = [], this._samplers = []
   }, Object.defineProperties(at.prototype, ot);
   var ht = function(t) {
    this._device = t, this._width = 4, this._height = 4, this._hasMipmap = !1, this._compressed = !1, this._anisotropy = 1, this._minFilter = Z.FILTER_LINEAR, this._magFilter = Z.FILTER_LINEAR, this._mipFilter = Z.FILTER_LINEAR, this._wrapS = Z.WRAP_REPEAT, this._wrapT = Z.WRAP_REPEAT, this._format = Z.TEXTURE_FMT_RGBA8, this._target = -1
   };

   function ut(t) {
    return !(t & t - 1 || !t)
   }
   ht.prototype.destroy = function() {
    -1 !== this._glID ? (this._device._gl.deleteTexture(this._glID), this._device._stats.tex -= this.bytes, this._glID = -1) : console.error("The texture already destroyed")
   };
   var lt = (function(t) {
     function e(e, i) {
      t.call(this, e);
      var n = this._device._gl;
      this._target = n.TEXTURE_2D, this._glID = n.createTexture(), i.images = i.images || [null], this.update(i)
     }
     return t && (e.__proto__ = t), e.prototype = Object.create(t && t.prototype), e.prototype.constructor = e, e.prototype.update = function(t) {
      var e = this._device._gl,
       i = this._hasMipmap;
      t && (void 0 !== t.width && (this._width = t.width), void 0 !== t.height && (this._height = t.height), void 0 !== t.anisotropy && (this._anisotropy = t.anisotropy), void 0 !== t.minFilter && (this._minFilter = t.minFilter), void 0 !== t.magFilter && (this._magFilter = t.magFilter), void 0 !== t.mipFilter && (this._mipFilter = t.mipFilter), void 0 !== t.wrapS && (this._wrapS = t.wrapS), void 0 !== t.wrapT && (this._wrapT = t.wrapT), void 0 !== t.format && (this._format = t.format, this._compressed = this._format >= Z.TEXTURE_FMT_RGB_DXT1 && this._format <= Z.TEXTURE_FMT_RGBA_PVRTC_4BPPV1), void 0 !== t.mipmap && (this._hasMipmap = t.mipmap, i = t.mipmap), void 0 !== t.images && t.images.length > 1 && (i = !1, (t.width > t.height ? t.width : t.height) >> t.images.length - 1 != 1 && console.error("texture-2d mipmap is invalid, should have a 1x1 mipmap.")));
      ut(this._width) && ut(this._height) || (i = !1), e.activeTexture(e.TEXTURE0), e.bindTexture(e.TEXTURE_2D, this._glID), void 0 !== t.images && t.images.length > 0 && this._setMipmap(t.images, t.flipY, t.premultiplyAlpha), this._setTexInfo(), i && (e.hint(e.GENERATE_MIPMAP_HINT, e.NICEST), e.generateMipmap(e.TEXTURE_2D)), this._device._restoreTexture(0)
     }, e.prototype.updateSubImage = function(t) {
      var e = this._device._gl,
       i = Q(this._format);
      e.activeTexture(e.TEXTURE0), e.bindTexture(e.TEXTURE_2D, this._glID), this._setSubImage(i, t), this._device._restoreTexture(0)
     }, e.prototype.updateImage = function(t) {
      var e = this._device._gl,
       i = Q(this._format);
      e.activeTexture(e.TEXTURE0), e.bindTexture(e.TEXTURE_2D, this._glID), this._setImage(i, t), this._device._restoreTexture(0)
     }, e.prototype._setSubImage = function(t, e) {
      var i = this._device._gl,
       n = e.flipY,
       r = e.premultiplyAlpha,
       s = e.image;
      !s || ArrayBuffer.isView(s) || s instanceof ArrayBuffer ? (void 0 === n ? i.pixelStorei(i.UNPACK_FLIP_Y_WEBGL, !1) : i.pixelStorei(i.UNPACK_FLIP_Y_WEBGL, n), void 0 === r ? i.pixelStorei(i.UNPACK_PREMULTIPLY_ALPHA_WEBGL, !1) : i.pixelStorei(i.UNPACK_PREMULTIPLY_ALPHA_WEBGL, r), this._compressed ? i.compressedTexSubImage2D(i.TEXTURE_2D, e.level, e.x, e.y, e.width, e.height, t.format, s) : i.texSubImage2D(i.TEXTURE_2D, e.level, e.x, e.y, e.width, e.height, t.format, t.pixelType, s)) : (void 0 === n ? i.pixelStorei(i.UNPACK_FLIP_Y_WEBGL, !0) : i.pixelStorei(i.UNPACK_FLIP_Y_WEBGL, n), void 0 === r ? i.pixelStorei(i.UNPACK_PREMULTIPLY_ALPHA_WEBGL, !1) : i.pixelStorei(i.UNPACK_PREMULTIPLY_ALPHA_WEBGL, r), i.texSubImage2D(i.TEXTURE_2D, e.level, e.x, e.y, t.format, t.pixelType, s))
     }, e.prototype._setImage = function(t, e) {
      var i = this._device._gl,
       n = e.flipY,
       r = e.premultiplyAlpha,
       s = e.image;
      !s || ArrayBuffer.isView(s) || s instanceof ArrayBuffer ? (void 0 === n ? i.pixelStorei(i.UNPACK_FLIP_Y_WEBGL, !1) : i.pixelStorei(i.UNPACK_FLIP_Y_WEBGL, n), void 0 === r ? i.pixelStorei(i.UNPACK_PREMULTIPLY_ALPHA_WEBGL, !1) : i.pixelStorei(i.UNPACK_PREMULTIPLY_ALPHA_WEBGL, r), this._compressed ? i.compressedTexImage2D(i.TEXTURE_2D, e.level, t.internalFormat, e.width, e.height, 0, s) : i.texImage2D(i.TEXTURE_2D, e.level, t.internalFormat, e.width, e.height, 0, t.format, t.pixelType, s)) : (void 0 === n ? i.pixelStorei(i.UNPACK_FLIP_Y_WEBGL, !0) : i.pixelStorei(i.UNPACK_FLIP_Y_WEBGL, n), void 0 === r ? i.pixelStorei(i.UNPACK_PREMULTIPLY_ALPHA_WEBGL, !1) : i.pixelStorei(i.UNPACK_PREMULTIPLY_ALPHA_WEBGL, r), i.texImage2D(i.TEXTURE_2D, e.level, t.internalFormat, t.format, t.pixelType, s))
     }, e.prototype._setMipmap = function(t, e, i) {
      for (var n = Q(this._format), r = {
        width: this._width,
        height: this._height,
        flipY: e,
        premultiplyAlpha: i,
        level: 0,
        image: null
       }, s = 0; s < t.length; ++s) r.level = s, r.width = this._width >> s, r.height = this._height >> s, r.image = t[s], this._setImage(n, r)
     }, e.prototype._setTexInfo = function() {
      var t = this._device._gl,
       e = ut(this._width) && ut(this._height);
      e || this._wrapS === Z.WRAP_CLAMP && this._wrapT === Z.WRAP_CLAMP || (console.warn("WebGL1 doesn't support all wrap modes with NPOT textures"), this._wrapS = Z.WRAP_CLAMP, this._wrapT = Z.WRAP_CLAMP);
      var i = this._hasMipmap ? this._mipFilter : -1;
      e || -1 === i || (console.warn("NPOT textures do not support mipmap filter"), i = -1), t.texParameteri(t.TEXTURE_2D, t.TEXTURE_MIN_FILTER, J(t, this._minFilter, i)), t.texParameteri(t.TEXTURE_2D, t.TEXTURE_MAG_FILTER, J(t, this._magFilter, -1)), t.texParameteri(t.TEXTURE_2D, t.TEXTURE_WRAP_S, this._wrapS), t.texParameteri(t.TEXTURE_2D, t.TEXTURE_WRAP_T, this._wrapT);
      var n = this._device.ext("EXT_texture_filter_anisotropic");
      n && n.TEXTURE_MAX_ANISOTROPY_EXT && t.texParameteri(t.TEXTURE_2D, n.TEXTURE_MAX_ANISOTROPY_EXT, this._anisotropy)
     }, e
    })(ht),
    _t = (function(t) {
     function e(e, i) {
      t.call(this, e);
      var n = this._device._gl;
      this._target = n.TEXTURE_CUBE_MAP, this._glID = n.createTexture(), this.update(i)
     }
     return t && (e.__proto__ = t), e.prototype = Object.create(t && t.prototype), e.prototype.constructor = e, e.prototype.update = function(t) {
      var e = this._device._gl,
       i = this._hasMipmap;
      t && (void 0 !== t.width && (this._width = t.width), void 0 !== t.height && (this._height = t.height), void 0 !== t.anisotropy && (this._anisotropy = t.anisotropy), void 0 !== t.minFilter && (this._minFilter = t.minFilter), void 0 !== t.magFilter && (this._magFilter = t.magFilter), void 0 !== t.mipFilter && (this._mipFilter = t.mipFilter), void 0 !== t.wrapS && (this._wrapS = t.wrapS), void 0 !== t.wrapT && (this._wrapT = t.wrapT), void 0 !== t.format && (this._format = t.format, this._compressed = this._format >= Z.TEXTURE_FMT_RGB_DXT1 && this._format <= Z.TEXTURE_FMT_RGBA_PVRTC_4BPPV1), void 0 !== t.mipmap && (this._hasMipmap = t.mipmap, i = t.mipmap), void 0 !== t.images && t.images.length > 1 && (i = !1, t.width !== t.height && console.warn("texture-cube width and height should be identical."), t.width >> t.images.length - 1 != 1 && console.error("texture-cube mipmap is invalid. please set mipmap as 1x1, 2x2, 4x4 ... nxn"))), ut(this._width) && ut(this._height) || (i = !1), e.activeTexture(e.TEXTURE0), e.bindTexture(e.TEXTURE_CUBE_MAP, this._glID), void 0 !== t.images && t.images.length > 0 && this._setMipmap(t.images, t.flipY, t.premultiplyAlpha), this._setTexInfo(), i && (e.hint(e.GENERATE_MIPMAP_HINT, e.NICEST), e.generateMipmap(e.TEXTURE_CUBE_MAP)), this._device._restoreTexture(0)
     }, e.prototype.updateSubImage = function(t) {
      var e = this._device._gl,
       i = Q(this._format);
      e.activeTexture(e.TEXTURE0), e.bindTexture(e.TEXTURE_CUBE_MAP, this._glID), this._setSubImage(i, t), this._device._restoreTexture(0)
     }, e.prototype.updateImage = function(t) {
      var e = this._device._gl,
       i = Q(this._format);
      e.activeTexture(e.TEXTURE0), e.bindTexture(e.TEXTURE_CUBE_MAP, this._glID), this._setImage(i, t), this._device._restoreTexture(0)
     }, e.prototype._setSubImage = function(t, e) {
      var i = this._device._gl,
       n = e.flipY,
       r = e.premultiplyAlpha,
       s = e.faceIndex,
       a = e.image;
      void 0 === n ? i.pixelStorei(i.UNPACK_FLIP_Y_WEBGL, !1) : i.pixelStorei(i.UNPACK_FLIP_Y_WEBGL, n), void 0 === r ? i.pixelStorei(i.UNPACK_PREMULTIPLY_ALPHA_WEBGL, !1) : i.pixelStorei(i.UNPACK_PREMULTIPLY_ALPHA_WEBGL, r), !a || ArrayBuffer.isView(a) || a instanceof ArrayBuffer ? this._compressed ? i.compressedTexSubImage2D(i.TEXTURE_CUBE_MAP_POSITIVE_X + s, e.level, e.x, e.y, e.width, e.height, t.format, a) : i.texSubImage2D(i.TEXTURE_CUBE_MAP_POSITIVE_X + s, e.level, e.x, e.y, e.width, e.height, t.format, t.pixelType, a) : i.texSubImage2D(i.TEXTURE_CUBE_MAP_POSITIVE_X + s, e.level, e.x, e.y, t.format, t.pixelType, a)
     }, e.prototype._setImage = function(t, e) {
      var i = this._device._gl,
       n = e.flipY,
       r = e.premultiplyAlpha,
       s = e.faceIndex,
       a = e.image;
      void 0 === n ? i.pixelStorei(i.UNPACK_FLIP_Y_WEBGL, !1) : i.pixelStorei(i.UNPACK_FLIP_Y_WEBGL, n), void 0 === r ? i.pixelStorei(i.UNPACK_PREMULTIPLY_ALPHA_WEBGL, !1) : i.pixelStorei(i.UNPACK_PREMULTIPLY_ALPHA_WEBGL, r), !a || ArrayBuffer.isView(a) || a instanceof ArrayBuffer ? this._compressed ? i.compressedTexImage2D(i.TEXTURE_CUBE_MAP_POSITIVE_X + s, e.level, t.internalFormat, e.width, e.height, 0, a) : i.texImage2D(i.TEXTURE_CUBE_MAP_POSITIVE_X + s, e.level, t.internalFormat, e.width, e.height, 0, t.format, t.pixelType, a) : i.texImage2D(i.TEXTURE_CUBE_MAP_POSITIVE_X + s, e.level, t.internalFormat, t.format, t.pixelType, a)
     }, e.prototype._setMipmap = function(t, e, i) {
      for (var n = Q(this._format), r = {
        width: this._width,
        height: this._height,
        faceIndex: 0,
        flipY: e,
        premultiplyAlpha: i,
        level: 0,
        image: null
       }, s = 0; s < t.length; ++s) {
       var a = t[s];
       r.level = s, r.width = this._width >> s, r.height = this._height >> s;
       for (var o = 0; o < 6; ++o) r.faceIndex = o, r.image = a[o], this._setImage(n, r)
      }
     }, e.prototype._setTexInfo = function() {
      var t = this._device._gl,
       e = ut(this._width) && ut(this._height);
      e || this._wrapS === Z.WRAP_CLAMP && this._wrapT === Z.WRAP_CLAMP || (console.warn("WebGL1 doesn't support all wrap modes with NPOT textures"), this._wrapS = Z.WRAP_CLAMP, this._wrapT = Z.WRAP_CLAMP);
      var i = this._hasMipmap ? this._mipFilter : -1;
      e || -1 === i || (console.warn("NPOT textures do not support mipmap filter"), i = -1), t.texParameteri(t.TEXTURE_CUBE_MAP, t.TEXTURE_MIN_FILTER, J(t, this._minFilter, i)), t.texParameteri(t.TEXTURE_CUBE_MAP, t.TEXTURE_MAG_FILTER, J(t, this._magFilter, -1)), t.texParameteri(t.TEXTURE_CUBE_MAP, t.TEXTURE_WRAP_S, this._wrapS), t.texParameteri(t.TEXTURE_CUBE_MAP, t.TEXTURE_WRAP_T, this._wrapT);
      var n = this._device.ext("EXT_texture_filter_anisotropic");
      n && n.TEXTURE_MAX_ANISOTROPY_EXT && t.texParameteri(t.TEXTURE_CUBE_MAP, n.TEXTURE_MAX_ANISOTROPY_EXT, this._anisotropy)
     }, e
    })(ht),
    dt = function(t, e, i, n) {
     this._device = t, this._format = e, this._width = i, this._height = n;
     var r = t._gl;
     this._glID = r.createRenderbuffer(), r.bindRenderbuffer(r.RENDERBUFFER, this._glID), r.renderbufferStorage(r.RENDERBUFFER, e, i, n), r.bindRenderbuffer(r.RENDERBUFFER, null)
    };
   dt.prototype.destroy = function() {
    if (null !== this._glID) {
     var t = this._device._gl;
     t.bindRenderbuffer(t.RENDERBUFFER, null), t.deleteRenderbuffer(this._glID), this._glID = null
    } else console.error("The render-buffer already destroyed")
   };
   var ft = function(t, e, i, n) {
    this._device = t, this._width = e, this._height = i, this._colors = n.colors || [], this._depth = n.depth || null, this._stencil = n.stencil || null, this._depthStencil = n.depthStencil || null, this._glID = t._gl.createFramebuffer()
   };
   ft.prototype.destroy = function() {
    null !== this._glID ? (this._device._gl.deleteFramebuffer(this._glID), this._glID = null) : console.error("The frame-buffer already destroyed")
   };
   var pt = {
     blend: !1,
     blendSep: !1,
     blendColor: 4294967295,
     blendEq: Z.BLEND_FUNC_ADD,
     blendAlphaEq: Z.BLEND_FUNC_ADD,
     blendSrc: Z.BLEND_ONE,
     blendDst: Z.BLEND_ZERO,
     blendSrcAlpha: Z.BLEND_ONE,
     blendDstAlpha: Z.BLEND_ZERO,
     depthTest: !1,
     depthWrite: !1,
     depthFunc: Z.DS_FUNC_LESS,
     stencilTest: !1,
     stencilSep: !1,
     stencilFuncFront: Z.DS_FUNC_ALWAYS,
     stencilRefFront: 0,
     stencilMaskFront: 255,
     stencilFailOpFront: Z.STENCIL_OP_KEEP,
     stencilZFailOpFront: Z.STENCIL_OP_KEEP,
     stencilZPassOpFront: Z.STENCIL_OP_KEEP,
     stencilWriteMaskFront: 255,
     stencilFuncBack: Z.DS_FUNC_ALWAYS,
     stencilRefBack: 0,
     stencilMaskBack: 255,
     stencilFailOpBack: Z.STENCIL_OP_KEEP,
     stencilZFailOpBack: Z.STENCIL_OP_KEEP,
     stencilZPassOpBack: Z.STENCIL_OP_KEEP,
     stencilWriteMaskBack: 255,
     cullMode: Z.CULL_BACK,
     primitiveType: Z.PT_TRIANGLES,
     maxStream: -1,
     vertexBuffers: [],
     vertexBufferOffsets: [],
     indexBuffer: null,
     maxTextureSlot: -1,
     textureUnits: [],
     program: null
    },
    mt = function(t) {
     this.vertexBuffers = new Array(t._caps.maxVertexStreams), this.vertexBufferOffsets = new Array(t._caps.maxVertexStreams), this.textureUnits = new Array(t._caps.maxTextureUnits), this.set(pt)
    };
   mt.initDefault = function(t) {
    pt.vertexBuffers = new Array(t._caps.maxVertexStreams), pt.vertexBufferOffsets = new Array(t._caps.maxVertexStreams), pt.textureUnits = new Array(t._caps.maxTextureUnits)
   }, mt.prototype.reset = function() {
    this.set(pt)
   }, mt.prototype.set = function(t) {
    this.blend = t.blend, this.blendSep = t.blendSep, this.blendColor = t.blendColor, this.blendEq = t.blendEq, this.blendAlphaEq = t.blendAlphaEq, this.blendSrc = t.blendSrc, this.blendDst = t.blendDst, this.blendSrcAlpha = t.blendSrcAlpha, this.blendDstAlpha = t.blendDstAlpha, this.depthTest = t.depthTest, this.depthWrite = t.depthWrite, this.depthFunc = t.depthFunc, this.stencilTest = t.stencilTest, this.stencilSep = t.stencilSep, this.stencilFuncFront = t.stencilFuncFront, this.stencilRefFront = t.stencilRefFront, this.stencilMaskFront = t.stencilMaskFront, this.stencilFailOpFront = t.stencilFailOpFront, this.stencilZFailOpFront = t.stencilZFailOpFront, this.stencilZPassOpFront = t.stencilZPassOpFront, this.stencilWriteMaskFront = t.stencilWriteMaskFront, this.stencilFuncBack = t.stencilFuncBack, this.stencilRefBack = t.stencilRefBack, this.stencilMaskBack = t.stencilMaskBack, this.stencilFailOpBack = t.stencilFailOpBack, this.stencilZFailOpBack = t.stencilZFailOpBack, this.stencilZPassOpBack = t.stencilZPassOpBack, this.stencilWriteMaskBack = t.stencilWriteMaskBack, this.cullMode = t.cullMode, this.primitiveType = t.primitiveType, this.maxStream = t.maxStream;
    for (var e = 0; e < t.vertexBuffers.length; ++e) this.vertexBuffers[e] = t.vertexBuffers[e];
    for (var i = 0; i < t.vertexBufferOffsets.length; ++i) this.vertexBufferOffsets[i] = t.vertexBufferOffsets[i];
    this.indexBuffer = t.indexBuffer, this.maxTextureSlot = t.maxTextureSlot;
    for (var n = 0; n < t.textureUnits.length; ++n) this.textureUnits[n] = t.textureUnits[n];
    this.program = t.program
   };
   var vt = {
     5124: function(t, e, i) {
      t.uniform1i(e, i)
     },
     5126: function(t, e, i) {
      t.uniform1f(e, i)
     },
     35664: function(t, e, i) {
      t.uniform2fv(e, i)
     },
     35665: function(t, e, i) {
      t.uniform3fv(e, i)
     },
     35666: function(t, e, i) {
      t.uniform4fv(e, i)
     },
     35667: function(t, e, i) {
      t.uniform2iv(e, i)
     },
     35668: function(t, e, i) {
      t.uniform3iv(e, i)
     },
     35669: function(t, e, i) {
      t.uniform4iv(e, i)
     },
     35670: function(t, e, i) {
      t.uniform1i(e, i)
     },
     35671: function(t, e, i) {
      t.uniform2iv(e, i)
     },
     35672: function(t, e, i) {
      t.uniform3iv(e, i)
     },
     35673: function(t, e, i) {
      t.uniform4iv(e, i)
     },
     35674: function(t, e, i) {
      t.uniformMatrix2fv(e, !1, i)
     },
     35675: function(t, e, i) {
      t.uniformMatrix3fv(e, !1, i)
     },
     35676: function(t, e, i) {
      t.uniformMatrix4fv(e, !1, i)
     },
     35678: function(t, e, i) {
      t.uniform1i(e, i)
     },
     35680: function(t, e, i) {
      t.uniform1i(e, i)
     }
    },
    gt = {};

   function yt(t, e, i, n) {
    void 0 === n && (n = 0), i instanceof lt ? t.framebufferTexture2D(t.FRAMEBUFFER, e, t.TEXTURE_2D, i._glID, 0) : i instanceof _t ? t.framebufferTexture2D(t.FRAMEBUFFER, e, t.TEXTURE_CUBE_MAP_POSITIVE_X + n, i._glID, 0) : t.framebufferRenderbuffer(t.FRAMEBUFFER, e, t.RENDERBUFFER, i._glID)
   }
   gt[5124] = function(t, e, i) {
    t.uniform1iv(e, i)
   }, gt[5126] = function(t, e, i) {
    t.uniform1fv(e, i)
   }, gt[35664] = function(t, e, i) {
    t.uniform2fv(e, i)
   }, gt[35665] = function(t, e, i) {
    t.uniform3fv(e, i)
   }, gt[35666] = function(t, e, i) {
    t.uniform4fv(e, i)
   }, gt[35667] = function(t, e, i) {
    t.uniform2iv(e, i)
   }, gt[35668] = function(t, e, i) {
    t.uniform3iv(e, i)
   }, gt[35669] = function(t, e, i) {
    t.uniform4iv(e, i)
   }, gt[35670] = function(t, e, i) {
    t.uniform1iv(e, i)
   }, gt[35671] = function(t, e, i) {
    t.uniform2iv(e, i)
   }, gt[35672] = function(t, e, i) {
    t.uniform3iv(e, i)
   }, gt[35673] = function(t, e, i) {
    t.uniform4iv(e, i)
   }, gt[35674] = function(t, e, i) {
    t.uniformMatrix2fv(e, !1, i)
   }, gt[35675] = function(t, e, i) {
    t.uniformMatrix3fv(e, !1, i)
   }, gt[35676] = function(t, e, i) {
    t.uniformMatrix4fv(e, !1, i)
   }, gt[35678] = function(t, e, i) {
    t.uniform1iv(e, i)
   }, gt[35680] = function(t, e, i) {
    t.uniform1iv(e, i)
   };
   var Tt = function(t, e) {
    var i;
    void 0 === (e = e || {}).alpha && (e.alpha = !1), void 0 === e.stencil && (e.stencil = !0), void 0 === e.depth && (e.depth = !0), void 0 === e.antialias && (e.antialias = !1), void 0 === e.preserveDrawingBuffer && (e.preserveDrawingBuffer = !1);
    try {
     i = t.getContext("webgl", e) || t.getContext("experimental-webgl", e) || t.getContext("webkit-3d", e) || t.getContext("moz-webgl", e)
    } catch (t) {
     return void console.error(t)
    }
    this._gl = i, this._extensions = {}, this._caps = {}, this._stats = {
     texture: 0,
     vb: 0,
     ib: 0,
     drawcalls: 0
    }, this._initExtensions(["EXT_texture_filter_anisotropic", "EXT_shader_texture_lod", "OES_standard_derivatives", "OES_texture_float", "OES_texture_float_linear", "OES_texture_half_float", "OES_texture_half_float_linear", "OES_vertex_array_object", "WEBGL_compressed_texture_atc", "WEBGL_compressed_texture_etc1", "WEBGL_compressed_texture_pvrtc", "WEBGL_compressed_texture_s3tc", "WEBGL_depth_texture", "WEBGL_draw_buffers"]), this._initCaps(), this._initStates(), mt.initDefault(this), this._current = new mt(this), this._next = new mt(this), this._uniforms = {}, this._vx = this._vy = this._vw = this._vh = 0, this._sx = this._sy = this._sw = this._sh = 0, this._framebuffer = null, this._enabledAttributes = new Array(this._caps.maxVertexAttribs), this._newAttributes = new Array(this._caps.maxVertexAttribs);
    for (var n = 0; n < this._caps.maxVertexAttribs; ++n) this._enabledAttributes[n] = 0, this._newAttributes[n] = 0
   };
   Tt.prototype._initExtensions = function(t) {
    for (var e = this._gl, i = 0; i < t.length; ++i)
     for (var n = t[i], r = ["", "WEBKIT_", "MOZ_"], s = 0; s < r.length; s++) try {
      var a = e.getExtension(r[s] + n);
      a && (this._extensions[n] = a)
     } catch (t) {
      console.error(t)
     }
   }, Tt.prototype._initCaps = function() {
    var t = this._gl,
     e = this.ext("WEBGL_draw_buffers");
    this._caps.maxVertexStreams = 4, this._caps.maxVertexTextures = t.getParameter(t.MAX_VERTEX_TEXTURE_IMAGE_UNITS), this._caps.maxFragUniforms = t.getParameter(t.MAX_FRAGMENT_UNIFORM_VECTORS), this._caps.maxTextureUnits = t.getParameter(t.MAX_TEXTURE_IMAGE_UNITS), this._caps.maxVertexAttribs = t.getParameter(t.MAX_VERTEX_ATTRIBS), this._caps.maxDrawBuffers = e ? t.getParameter(e.MAX_DRAW_BUFFERS_WEBGL) : 1, this._caps.maxColorAttachments = e ? t.getParameter(e.MAX_COLOR_ATTACHMENTS_WEBGL) : 1
   }, Tt.prototype._initStates = function() {
    var t = this._gl;
    t.disable(t.BLEND), t.blendFunc(t.ONE, t.ZERO), t.blendEquation(t.FUNC_ADD), t.blendColor(1, 1, 1, 1), t.colorMask(!0, !0, !0, !0), t.enable(t.CULL_FACE), t.cullFace(t.BACK), t.disable(t.DEPTH_TEST), t.depthFunc(t.LESS), t.depthMask(!1), t.disable(t.POLYGON_OFFSET_FILL), t.depthRange(0, 1), t.disable(t.STENCIL_TEST), t.stencilFunc(t.ALWAYS, 0, 255), t.stencilMask(255), t.stencilOp(t.KEEP, t.KEEP, t.KEEP), t.clearDepth(1), t.clearColor(0, 0, 0, 0), t.clearStencil(0), t.disable(t.SCISSOR_TEST)
   }, Tt.prototype._restoreTexture = function(t) {
    var e = this._gl,
     i = this._current.textureUnits[t];
    i && -1 !== i._glID ? e.bindTexture(i._target, i._glID) : e.bindTexture(e.TEXTURE_2D, null)
   }, Tt.prototype._restoreIndexBuffer = function() {
    var t = this._gl,
     e = this._current.indexBuffer;
    e && -1 !== e._glID ? t.bindBuffer(t.ELEMENT_ARRAY_BUFFER, e._glID) : t.bindBuffer(t.ELEMENT_ARRAY_BUFFER, null)
   }, Tt.prototype.ext = function(t) {
    return this._extensions[t]
   }, Tt.prototype.setFrameBuffer = function(t) {
    if (this._framebuffer !== t) {
     this._framebuffer = t;
     var e = this._gl;
     if (null !== t) {
      e.bindFramebuffer(e.FRAMEBUFFER, t._glID);
      for (var i = this._framebuffer._colors.length, n = 0; n < i; ++n) {
       var r = this._framebuffer._colors[n];
       yt(e, e.COLOR_ATTACHMENT0 + n, r)
      }
      for (var s = i; s < this._caps.maxColorAttachments; ++s) e.framebufferTexture2D(e.FRAMEBUFFER, e.COLOR_ATTACHMENT0 + s, e.TEXTURE_2D, null, 0);
      this._framebuffer._depth && yt(e, e.DEPTH_ATTACHMENT, this._framebuffer._depth), this._framebuffer._stencil && yt(e, e.STENCIL_ATTACHMENT, t._stencil), this._framebuffer._depthStencil && yt(e, e.DEPTH_STENCIL_ATTACHMENT, t._depthStencil)
     } else e.bindFramebuffer(e.FRAMEBUFFER, null)
    }
   }, Tt.prototype.setViewport = function(t, e, i, n) {
    this._vx === t && this._vy === e && this._vw === i && this._vh === n || (this._gl.viewport(t, e, i, n), this._vx = t, this._vy = e, this._vw = i, this._vh = n)
   }, Tt.prototype.setScissor = function(t, e, i, n) {
    this._sx === t && this._sy === e && this._sw === i && this._sh === n || (this._gl.scissor(t, e, i, n), this._sx = t, this._sy = e, this._sw = i, this._sh = n)
   }, Tt.prototype.clear = function(t) {
    var e = this._gl,
     i = 0;
    void 0 !== t.color && (i |= e.COLOR_BUFFER_BIT, e.clearColor(t.color[0], t.color[1], t.color[2], t.color[3])), void 0 !== t.depth && (i |= e.DEPTH_BUFFER_BIT, e.clearDepth(t.depth), e.enable(e.DEPTH_TEST), e.depthMask(!0), e.depthFunc(e.ALWAYS)), void 0 !== t.stencil && (i |= e.STENCIL_BUFFER_BIT, e.clearStencil(t.stencil)), e.clear(i), void 0 !== t.depth && (!1 === this._current.depthTest ? e.disable(e.DEPTH_TEST) : (!1 === this._current.depthWrite && e.depthMask(!1), this._current.depthFunc !== Z.DS_FUNC_ALWAYS && e.depthFunc(this._current.depthFunc)))
   }, Tt.prototype.enableBlend = function() {
    this._next.blend = !0
   }, Tt.prototype.enableDepthTest = function() {
    this._next.depthTest = !0
   }, Tt.prototype.enableDepthWrite = function() {
    this._next.depthWrite = !0
   }, Tt.prototype.enableStencilTest = function() {
    this._next.stencilTest = !0
   }, Tt.prototype.setStencilFunc = function(t, e, i) {
    this._next.stencilSep = !1, this._next.stencilFuncFront = this._next.stencilFuncBack = t, this._next.stencilRefFront = this._next.stencilRefBack = e, this._next.stencilMaskFront = this._next.stencilMaskBack = i
   }, Tt.prototype.setStencilFuncFront = function(t, e, i) {
    this._next.stencilSep = !0, this._next.stencilFuncFront = t, this._next.stencilRefFront = e, this._next.stencilMaskFront = i
   }, Tt.prototype.setStencilFuncBack = function(t, e, i) {
    this._next.stencilSep = !0, this._next.stencilFuncBack = t, this._next.stencilRefBack = e, this._next.stencilMaskBack = i
   }, Tt.prototype.setStencilOp = function(t, e, i, n) {
    this._next.stencilFailOpFront = this._next.stencilFailOpBack = t, this._next.stencilZFailOpFront = this._next.stencilZFailOpBack = e, this._next.stencilZPassOpFront = this._next.stencilZPassOpBack = i, this._next.stencilWriteMaskFront = this._next.stencilWriteMaskBack = n
   }, Tt.prototype.setStencilOpFront = function(t, e, i, n) {
    this._next.stencilSep = !0, this._next.stencilFailOpFront = t, this._next.stencilZFailOpFront = e, this._next.stencilZPassOpFront = i, this._next.stencilWriteMaskFront = n
   }, Tt.prototype.setStencilOpBack = function(t, e, i, n) {
    this._next.stencilSep = !0, this._next.stencilFailOpBack = t, this._next.stencilZFailOpBack = e, this._next.stencilZPassOpBack = i, this._next.stencilWriteMaskBack = n
   }, Tt.prototype.setDepthFunc = function(t) {
    this._next.depthFunc = t
   }, Tt.prototype.setBlendColor32 = function(t) {
    this._next.blendColor = t
   }, Tt.prototype.setBlendColor = function(t, e, i, n) {
    this._next.blendColor = (255 * t << 24 | 255 * e << 16 | 255 * i << 8 | 255 * n) >>> 0
   }, Tt.prototype.setBlendFunc = function(t, e) {
    this._next.blendSep = !1, this._next.blendSrc = t, this._next.blendDst = e
   }, Tt.prototype.setBlendFuncSep = function(t, e, i, n) {
    this._next.blendSep = !0, this._next.blendSrc = t, this._next.blendDst = e, this._next.blendSrcAlpha = i, this._next.blendDstAlpha = n
   }, Tt.prototype.setBlendEq = function(t) {
    this._next.blendSep = !1, this._next.blendEq = t
   }, Tt.prototype.setBlendEqSep = function(t, e) {
    this._next.blendSep = !0, this._next.blendEq = t, this._next.blendAlphaEq = e
   }, Tt.prototype.setCullMode = function(t) {
    this._next.cullMode = t
   }, Tt.prototype.setVertexBuffer = function(t, e, i) {
    void 0 === i && (i = 0), this._next.vertexBuffers[t] = e, this._next.vertexBufferOffsets[t] = i, this._next.maxStream < t && (this._next.maxStream = t)
   }, Tt.prototype.setIndexBuffer = function(t) {
    this._next.indexBuffer = t
   }, Tt.prototype.setProgram = function(t) {
    this._next.program = t
   }, Tt.prototype.setTexture = function(t, e, i) {
    i >= this._caps.maxTextureUnits ? console.warn("Can not set texture " + t + " at stage " + i + ", max texture exceed: " + this._caps.maxTextureUnits) : (this._next.textureUnits[i] = e, this.setUniform(t, i), this._next.maxTextureSlot < i && (this._next.maxTextureSlot = i))
   }, Tt.prototype.setTextureArray = function(t, e, i) {
    var n = e.length;
    if (n >= this._caps.maxTextureUnits) console.warn("Can not set " + n + " textures for " + t + ", max texture exceed: " + this._caps.maxTextureUnits);
    else {
     for (var r = 0; r < n; ++r) {
      var s = i[r];
      this._next.textureUnits[s] = e[r]
     }
     this.setUniform(t, i)
    }
   }, Tt.prototype.setUniform = function(t, e) {
    var i = this._uniforms[t];
    if (i) {
     var n = i.value,
      r = !1;
     if (i.isArray)
      for (var s = 0, a = n.length; s < a; s++) n[s] !== e[s] && (r = !0, n[s] = e[s]);
     else n !== e && (r = !0, i.value = e);
     r && (i.dirty = !0)
    } else {
     var o = e,
      c = !1;
     e instanceof Float32Array || Array.isArray(e) ? (o = new Float32Array(e), c = !0) : e instanceof Int32Array && (o = new Int32Array(e), c = !0), i = {
      dirty: !0,
      value: o,
      isArray: c
     }
    }
    this._uniforms[t] = i
   }, Tt.prototype.setPrimitiveType = function(t) {
    this._next.primitiveType = t
   }, Tt.prototype.draw = function(t, e) {
    var i = this._gl,
     n = this._current,
     r = this._next;
    (function(t, e, i) {
     if (e.blend !== i.blend) return i.blend ? (t.enable(t.BLEND), i.blendSrc !== Z.BLEND_CONSTANT_COLOR && i.blendSrc !== Z.BLEND_ONE_MINUS_CONSTANT_COLOR && i.blendDst !== Z.BLEND_CONSTANT_COLOR && i.blendDst !== Z.BLEND_ONE_MINUS_CONSTANT_COLOR || t.blendColor((i.blendColor >> 24) / 255, (i.blendColor >> 16 & 255) / 255, (i.blendColor >> 8 & 255) / 255, (255 & i.blendColor) / 255), void(i.blendSep ? (t.blendFuncSeparate(i.blendSrc, i.blendDst, i.blendSrcAlpha, i.blendDstAlpha), t.blendEquationSeparate(i.blendEq, i.blendAlphaEq)) : (t.blendFunc(i.blendSrc, i.blendDst), t.blendEquation(i.blendEq)))) : void t.disable(t.BLEND);
     !1 !== i.blend && (e.blendColor !== i.blendColor && t.blendColor((i.blendColor >> 24) / 255, (i.blendColor >> 16 & 255) / 255, (i.blendColor >> 8 & 255) / 255, (255 & i.blendColor) / 255), e.blendSep === i.blendSep ? i.blendSep ? (e.blendSrc === i.blendSrc && e.blendDst === i.blendDst && e.blendSrcAlpha === i.blendSrcAlpha && e.blendDstAlpha === i.blendDstAlpha || t.blendFuncSeparate(i.blendSrc, i.blendDst, i.blendSrcAlpha, i.blendDstAlpha), e.blendEq === i.blendEq && e.blendAlphaEq === i.blendAlphaEq || t.blendEquationSeparate(i.blendEq, i.blendAlphaEq)) : (e.blendSrc === i.blendSrc && e.blendDst === i.blendDst || t.blendFunc(i.blendSrc, i.blendDst), e.blendEq !== i.blendEq && t.blendEquation(i.blendEq)) : i.blendSep ? (t.blendFuncSeparate(i.blendSrc, i.blendDst, i.blendSrcAlpha, i.blendDstAlpha), t.blendEquationSeparate(i.blendEq, i.blendAlphaEq)) : (t.blendFunc(i.blendSrc, i.blendDst), t.blendEquation(i.blendEq)))
    })(i, n, r), (function(t, e, i) {
     if (e.depthTest !== i.depthTest) return i.depthTest ? (t.enable(t.DEPTH_TEST), t.depthFunc(i.depthFunc), void t.depthMask(i.depthWrite)) : void t.disable(t.DEPTH_TEST);
     e.depthWrite !== i.depthWrite && t.depthMask(i.depthWrite), !1 !== i.depthTest ? e.depthFunc !== i.depthFunc && t.depthFunc(i.depthFunc) : i.depthWrite && (i.depthTest = !0, i.depthFunc = Z.DS_FUNC_ALWAYS, t.enable(t.DEPTH_TEST), t.depthFunc(i.depthFunc))
    })(i, n, r), (function(t, e, i) {
     if (i.stencilTest !== e.stencilTest) return i.stencilTest ? (t.enable(t.STENCIL_TEST), void(i.stencilSep ? (t.stencilFuncSeparate(t.FRONT, i.stencilFuncFront, i.stencilRefFront, i.stencilMaskFront), t.stencilMaskSeparate(t.FRONT, i.stencilWriteMaskFront), t.stencilOpSeparate(t.FRONT, i.stencilFailOpFront, i.stencilZFailOpFront, i.stencilZPassOpFront), t.stencilFuncSeparate(t.BACK, i.stencilFuncBack, i.stencilRefBack, i.stencilMaskBack), t.stencilMaskSeparate(t.BACK, i.stencilWriteMaskBack), t.stencilOpSeparate(t.BACK, i.stencilFailOpBack, i.stencilZFailOpBack, i.stencilZPassOpBack)) : (t.stencilFunc(i.stencilFuncFront, i.stencilRefFront, i.stencilMaskFront), t.stencilMask(i.stencilWriteMaskFront), t.stencilOp(i.stencilFailOpFront, i.stencilZFailOpFront, i.stencilZPassOpFront)))) : void t.disable(t.STENCIL_TEST);
     i.stencilTest && (e.stencilSep === i.stencilSep ? i.stencilSep ? (e.stencilFuncFront === i.stencilFuncFront && e.stencilRefFront === i.stencilRefFront && e.stencilMaskFront === i.stencilMaskFront || t.stencilFuncSeparate(t.FRONT, i.stencilFuncFront, i.stencilRefFront, i.stencilMaskFront), e.stencilWriteMaskFront !== i.stencilWriteMaskFront && t.stencilMaskSeparate(t.FRONT, i.stencilWriteMaskFront), e.stencilFailOpFront === i.stencilFailOpFront && e.stencilZFailOpFront === i.stencilZFailOpFront && e.stencilZPassOpFront === i.stencilZPassOpFront || t.stencilOpSeparate(t.FRONT, i.stencilFailOpFront, i.stencilZFailOpFront, i.stencilZPassOpFront), e.stencilFuncBack === i.stencilFuncBack && e.stencilRefBack === i.stencilRefBack && e.stencilMaskBack === i.stencilMaskBack || t.stencilFuncSeparate(t.BACK, i.stencilFuncBack, i.stencilRefBack, i.stencilMaskBack), e.stencilWriteMaskBack !== i.stencilWriteMaskBack && t.stencilMaskSeparate(t.BACK, i.stencilWriteMaskBack), e.stencilFailOpBack === i.stencilFailOpBack && e.stencilZFailOpBack === i.stencilZFailOpBack && e.stencilZPassOpBack === i.stencilZPassOpBack || t.stencilOpSeparate(t.BACK, i.stencilFailOpBack, i.stencilZFailOpBack, i.stencilZPassOpBack)) : (e.stencilFuncFront === i.stencilFuncFront && e.stencilRefFront === i.stencilRefFront && e.stencilMaskFront === i.stencilMaskFront || t.stencilFunc(i.stencilFuncFront, i.stencilRefFront, i.stencilMaskFront), e.stencilWriteMaskFront !== i.stencilWriteMaskFront && t.stencilMask(i.stencilWriteMaskFront), e.stencilFailOpFront === i.stencilFailOpFront && e.stencilZFailOpFront === i.stencilZFailOpFront && e.stencilZPassOpFront === i.stencilZPassOpFront || t.stencilOp(i.stencilFailOpFront, i.stencilZFailOpFront, i.stencilZPassOpFront)) : i.stencilSep ? (t.stencilFuncSeparate(t.FRONT, i.stencilFuncFront, i.stencilRefFront, i.stencilMaskFront), t.stencilMaskSeparate(t.FRONT, i.stencilWriteMaskFront), t.stencilOpSeparate(t.FRONT, i.stencilFailOpFront, i.stencilZFailOpFront, i.stencilZPassOpFront), t.stencilFuncSeparate(t.BACK, i.stencilFuncBack, i.stencilRefBack, i.stencilMaskBack), t.stencilMaskSeparate(t.BACK, i.stencilWriteMaskBack), t.stencilOpSeparate(t.BACK, i.stencilFailOpBack, i.stencilZFailOpBack, i.stencilZPassOpBack)) : (t.stencilFunc(i.stencilFuncFront, i.stencilRefFront, i.stencilMaskFront), t.stencilMask(i.stencilWriteMaskFront), t.stencilOp(i.stencilFailOpFront, i.stencilZFailOpFront, i.stencilZPassOpFront)))
    })(i, n, r), (function(t, e, i) {
     e.cullMode !== i.cullMode && (i.cullMode !== Z.CULL_NONE ? (t.enable(t.CULL_FACE), t.cullFace(i.cullMode)) : t.disable(t.CULL_FACE))
    })(i, n, r), (function(t, e, i, n) {
     var r = !1;
     if (-1 !== n.maxStream) {
      if (i.maxStream !== n.maxStream) r = !0;
      else if (i.program !== n.program) r = !0;
      else
       for (var s = 0; s < n.maxStream + 1; ++s)
        if (i.vertexBuffers[s] !== n.vertexBuffers[s] || i.vertexBufferOffsets[s] !== n.vertexBufferOffsets[s]) {
         r = !0;
         break
        } if (r) {
       for (var a = 0; a < t._caps.maxVertexAttribs; ++a) t._newAttributes[a] = 0;
       for (var o = 0; o < n.maxStream + 1; ++o) {
        var c = n.vertexBuffers[o],
         h = n.vertexBufferOffsets[o];
        if (c) {
         e.bindBuffer(e.ARRAY_BUFFER, c._glID);
         for (var u = 0; u < n.program._attributes.length; ++u) {
          var l = n.program._attributes[u],
           _ = c._format.element(l.name);
          _ ? (0 === t._enabledAttributes[l.location] && (e.enableVertexAttribArray(l.location), t._enabledAttributes[l.location] = 1), t._newAttributes[l.location] = 1, e.vertexAttribPointer(l.location, _.num, _.type, _.normalize, _.stride, _.offset + h * _.stride)) : console.warn("Can not find vertex attribute: " + l.name)
         }
        }
       }
       for (var d = 0; d < t._caps.maxVertexAttribs; ++d) t._enabledAttributes[d] !== t._newAttributes[d] && (e.disableVertexAttribArray(d), t._enabledAttributes[d] = 0)
      }
     } else console.warn("VertexBuffer not assigned, please call setVertexBuffer before every draw.")
    })(this, i, n, r), n.indexBuffer !== r.indexBuffer && i.bindBuffer(i.ELEMENT_ARRAY_BUFFER, r.indexBuffer ? r.indexBuffer._glID : null);
    var s = !1;
    n.program !== r.program && (r.program._linked ? i.useProgram(r.program._glID) : console.warn("Failed to use program: has not linked yet."), s = !0), (function(t, e, i) {
     for (var n = 0; n < i.maxTextureSlot + 1; ++n)
      if (e.textureUnits[n] !== i.textureUnits[n]) {
       var r = i.textureUnits[n];
       r && -1 !== r._glID && (t.activeTexture(t.TEXTURE0 + n), t.bindTexture(r._target, r._glID))
      }
    })(i, n, r);
    for (var a = 0; a < r.program._uniforms.length; ++a) {
     var o = r.program._uniforms[a],
      c = this._uniforms[o.name];
     if (c && (s || c.dirty)) {
      c.dirty = !1;
      var h = void 0 === o.size ? vt[o.type] : gt[o.type];
      h ? h(i, o.location, c.value) : console.warn("Can not find commit function for uniform " + o.name)
     }
    }
    r.indexBuffer ? i.drawElements(this._next.primitiveType, e, r.indexBuffer._format, t * r.indexBuffer._bytesPerIndex) : i.drawArrays(this._next.primitiveType, t, e), this._stats.drawcalls += 1, n.set(r), r.reset()
   };
   var Et = {
    VertexFormat: $,
    IndexBuffer: tt,
    VertexBuffer: it,
    Program: at,
    Texture: ht,
    Texture2D: lt,
    TextureCube: _t,
    RenderBuffer: dt,
    FrameBuffer: ft,
    Device: Tt,
    attrTypeBytes: K,
    glFilter: J,
    glTextureFmt: Q
   };
   Object.assign(Et, Z);
   var xt = function(t, e, i) {
    void 0 === i && (i = Et.PT_TRIANGLES), this._vertexBuffer = t, this._indexBuffer = e, this._primitiveType = i, this._start = 0, this._count = -1
   };
   xt.prototype.getPrimitiveCount = function() {
    return -1 !== this._count ? this._count : this._indexBuffer ? this._indexBuffer.count : this._vertexBuffer.count
   };
   var Ct = function(t) {
    this._programName = t, this._cullMode = Et.CULL_BACK, this._blend = !1, this._blendEq = Et.BLEND_FUNC_ADD, this._blendAlphaEq = Et.BLEND_FUNC_ADD, this._blendSrc = Et.BLEND_ONE, this._blendDst = Et.BLEND_ZERO, this._blendSrcAlpha = Et.BLEND_ONE, this._blendDstAlpha = Et.BLEND_ZERO, this._blendColor = 4294967295, this._depthTest = !1, this._depthWrite = !1, this._depthFunc = Et.DS_FUNC_LESS, this._stencilTest = !1, this._stencilFuncFront = Et.DS_FUNC_ALWAYS, this._stencilRefFront = 0, this._stencilMaskFront = 255, this._stencilFailOpFront = Et.STENCIL_OP_KEEP, this._stencilZFailOpFront = Et.STENCIL_OP_KEEP, this._stencilZPassOpFront = Et.STENCIL_OP_KEEP, this._stencilWriteMaskFront = 255, this._stencilFuncBack = Et.DS_FUNC_ALWAYS, this._stencilRefBack = 0, this._stencilMaskBack = 255, this._stencilFailOpBack = Et.STENCIL_OP_KEEP, this._stencilZFailOpBack = Et.STENCIL_OP_KEEP, this._stencilZPassOpBack = Et.STENCIL_OP_KEEP, this._stencilWriteMaskBack = 255
   };
   Ct.prototype.copy = function(t) {
    this._programName = t._programName, this._cullMode = t._cullMode, this._blend = t._blend, this._blendEq = t._blendEq, this._blendAlphaEq = t._blendAlphaEq, this._blendSrc = t._blendSrc, this._blendDst = t._blendDst, this._blendSrcAlpha = t._blendSrcAlpha, this._blendDstAlpha = t._blendDstAlpha, this._blendColor = t._blendColor, this._depthTest = t._depthTest, this._depthWrite = t._depthWrite, this._depthFunc = t._depthFunc, this._stencilTest = t._stencilTest, this._stencilFuncFront = t._stencilFuncFront, this._stencilRefFront = t._stencilRefFront, this._stencilMaskFront = t._stencilMaskFront, this._stencilFailOpFront = t._stencilFailOpFront, this._stencilZFailOpFront = t._stencilZFailOpFront, this._stencilZPassOpFront = t._stencilZPassOpFront, this._stencilWriteMaskFront = t._stencilWriteMaskFront, this._stencilFuncBack = t._stencilFuncBack, this._stencilRefBack = t._stencilRefBack, this._stencilMaskBack = t._stencilMaskBack, this._stencilFailOpBack = t._stencilFailOpBack, this._stencilZFailOpBack = t._stencilZFailOpBack, this._stencilZPassOpBack = t._stencilZPassOpBack, this._stencilWriteMaskBack = t._stencilWriteMaskBack
   }, Ct.prototype.setCullMode = function(t) {
    this._cullMode = t
   }, Ct.prototype.setBlend = function(t, e, i, n, r, s, a) {
    void 0 === t && (t = Et.BLEND_FUNC_ADD), void 0 === e && (e = Et.BLEND_ONE), void 0 === i && (i = Et.BLEND_ZERO), void 0 === n && (n = Et.BLEND_FUNC_ADD), void 0 === r && (r = Et.BLEND_ONE), void 0 === s && (s = Et.BLEND_ZERO), void 0 === a && (a = 4294967295), this._blend = !0, this._blendEq = t, this._blendSrc = e, this._blendDst = i, this._blendAlphaEq = n, this._blendSrcAlpha = r, this._blendDstAlpha = s, this._blendColor = a
   }, Ct.prototype.setDepth = function(t, e, i) {
    void 0 === t && (t = !1), void 0 === e && (e = !1), void 0 === i && (i = Et.DS_FUNC_LESS), this._depthTest = t, this._depthWrite = e, this._depthFunc = i
   }, Ct.prototype.setStencilFront = function(t, e, i, n, r, s, a) {
    void 0 === t && (t = Et.DS_FUNC_ALWAYS), void 0 === e && (e = 0), void 0 === i && (i = 255), void 0 === n && (n = Et.STENCIL_OP_KEEP), void 0 === r && (r = Et.STENCIL_OP_KEEP), void 0 === s && (s = Et.STENCIL_OP_KEEP), void 0 === a && (a = 255), this._stencilTest = !0, this._stencilFuncFront = t, this._stencilRefFront = e, this._stencilMaskFront = i, this._stencilFailOpFront = n, this._stencilZFailOpFront = r, this._stencilZPassOpFront = s, this._stencilWriteMaskFront = a
   }, Ct.prototype.setStencilBack = function(t, e, i, n, r, s, a) {
    void 0 === t && (t = Et.DS_FUNC_ALWAYS), void 0 === e && (e = 0), void 0 === i && (i = 255), void 0 === n && (n = Et.STENCIL_OP_KEEP), void 0 === r && (r = Et.STENCIL_OP_KEEP), void 0 === s && (s = Et.STENCIL_OP_KEEP), void 0 === a && (a = 255), this._stencilTest = !0, this._stencilFuncBack = t, this._stencilRefBack = e, this._stencilMaskBack = i, this._stencilFailOpBack = n, this._stencilZFailOpBack = r, this._stencilZPassOpBack = s, this._stencilWriteMaskBack = a
   }, Ct.prototype.disableStencilTest = function() {
    this._stencilTest = !1
   };
   var At = 0,
    bt = {},
    St = {
     addStage: function(t) {
      if (void 0 === bt[t]) {
       var e = 1 << At;
       bt[t] = e, At += 1
      }
     },
     stageID: function(t) {
      var e = bt[t];
      return void 0 === e ? -1 : e
     },
     stageIDs: function(t) {
      for (var e = 0, i = 0; i < t.length; ++i) {
       var n = bt[t[i]];
       void 0 !== n && (e |= n)
      }
      return e
     }
    },
    wt = 0,
    Dt = function(t, e, i, n) {
     void 0 === n && (n = 0), this._id = wt++, this._stageIDs = St.stageIDs(t), this._parameters = e, this._passes = i, this._layer = n
    },
    Rt = {
     passes: {
      configurable: !0
     },
     stageIDs: {
      configurable: !0
     }
    };
   Dt.prototype.copy = function(t) {
    this._id = t._id, this._stageIDs = t._stageIDs, this._parameters = [];
    for (var e = 0; e < t._parameters.length; ++e) {
     var i = t._parameters[e];
     this._parameters.push({
      name: i.name,
      type: i.type
     })
    }
    for (var n = 0; n < t._passes.length; ++n) {
     var r = this._passes[n];
     r || (r = new Fe.Pass, this._passes.push(r)), r.copy(t._passes[n])
    }
    this._passes.length = t._passes.length, this._layer = t._layer
   }, Dt.prototype.setStages = function(t) {
    this._stageIDs = St.stageIDs(t)
   }, Rt.passes.get = function() {
    return this._passes
   }, Rt.stageIDs.get = function() {
    return this._stageIDs
   }, Object.defineProperties(Dt.prototype, Rt);
   var Mt = function(t, e, i) {
    void 0 === e && (e = {}), void 0 === i && (i = []), this._techniques = t, this._properties = e, this._defines = i
   };
   Mt.prototype.clear = function() {
    this._techniques.length = 0, this._properties = null, this._defines.length = 0
   }, Mt.prototype.getTechnique = function(t) {
    for (var e = St.stageID(t), i = 0; i < this._techniques.length; ++i) {
     var n = this._techniques[i];
     if (n.stageIDs & e) return n
    }
    return null
   }, Mt.prototype.getProperty = function(t) {
    return this._properties[t]
   }, Mt.prototype.setProperty = function(t, e) {
    this._properties[t] = e
   }, Mt.prototype.getDefine = function(t) {
    for (var e = 0; e < this._defines.length; ++e) {
     var i = this._defines[e];
     if (i.name === t) return i.value
    }
    return console.warn("Failed to get define " + t + ", define not found."), null
   }, Mt.prototype.define = function(t, e) {
    for (var i = 0; i < this._defines.length; ++i) {
     var n = this._defines[i];
     if (n.name === t) return void(n.value = e)
    }
    console.warn("Failed to set define " + t + ", define not found.")
   }, Mt.prototype.extractDefines = function(t) {
    void 0 === t && (t = {});
    for (var e = 0; e < this._defines.length; ++e) {
     var i = this._defines[e];
     t[i.name] = i.value
    }
    return t
   };
   var It = B.create(),
    Lt = 0,
    Ot = function() {
     this._id = Lt++, this._rect = {
      x: 0,
      y: 0,
      w: 1,
      h: 1
     }, this._color = G.new(.3, .3, .3, 1), this._depth = 1, this._stencil = 0, this._clearFlags = Y.CLEAR_COLOR | Y.CLEAR_DEPTH, this._matView = B.create(), this._matProj = B.create(), this._matViewProj = B.create(), this._matInvViewProj = B.create(), this._stages = [], this._cullingMask = 1, this._framebuffer = null, this._shadowLight = null
    };
   Ot.prototype.getForward = function(t) {
    return y.set(t, -this._matView.m02, -this._matView.m06, -this._matView.m10)
   }, Ot.prototype.getPosition = function(t) {
    return B.invert(It, this._matView), B.getTranslation(t, It)
   };
   var Pt = y.new(0, 0, -1),
    Ft = B.create(),
    Nt = b.create(),
    Bt = y.create();
   var kt = function() {
     this._poolID = -1, this._node = null, this._type = Y.LIGHT_DIRECTIONAL, this._color = U.new(1, 1, 1), this._intensity = 1, this._range = 1, this._spotAngle = a(60), this._spotExp = 1, this._directionUniform = new Float32Array(3), this._positionUniform = new Float32Array(3), this._colorUniform = new Float32Array([this._color.r * this._intensity, this._color.g * this._intensity, this._color.b * this._intensity]), this._spotUniform = new Float32Array([Math.cos(.5 * this._spotAngle), this._spotExp]), this._shadowType = Y.SHADOW_NONE, this._shadowFrameBuffer = null, this._shadowMap = null, this._shadowMapDirty = !1, this._shadowDepthBuffer = null, this._shadowResolution = 1024, this._shadowBias = 5e-5, this._shadowDarkness = 1, this._shadowMinDepth = 1, this._shadowMaxDepth = 1e3, this._shadowDepthScale = 50, this._frustumEdgeFalloff = 0, this._viewProjMatrix = B.create(), this._spotAngleScale = 1, this._shadowFustumSize = 80
    },
    zt = {
     color: {
      configurable: !0
     },
     intensity: {
      configurable: !0
     },
     type: {
      configurable: !0
     },
     spotAngle: {
      configurable: !0
     },
     spotExp: {
      configurable: !0
     },
     range: {
      configurable: !0
     },
     shadowType: {
      configurable: !0
     },
     shadowMap: {
      configurable: !0
     },
     viewProjMatrix: {
      configurable: !0
     },
     shadowResolution: {
      configurable: !0
     },
     shadowBias: {
      configurable: !0
     },
     shadowDarkness: {
      configurable: !0
     },
     shadowMinDepth: {
      configurable: !0
     },
     shadowMaxDepth: {
      configurable: !0
     },
     shadowDepthScale: {
      configurable: !0
     },
     frustumEdgeFalloff: {
      configurable: !0
     }
    };
   kt.prototype.setNode = function(t) {
    this._node = t
   }, kt.prototype.setColor = function(t, e, i) {
    U.set(this._color, t, e, i), this._colorUniform[0] = t * this._intensity, this._colorUniform[1] = e * this._intensity, this._colorUniform[2] = i * this._intensity
   }, zt.color.get = function() {
    return this._color
   }, kt.prototype.setIntensity = function(t) {
    this._intensity = t, this._colorUniform[0] = t * this._color.r, this._colorUniform[1] = t * this._color.g, this._colorUniform[2] = t * this._color.b
   }, zt.intensity.get = function() {
    return this._intensity
   }, kt.prototype.setType = function(t) {
    this._type = t
   }, zt.type.get = function() {
    return this._type
   }, kt.prototype.setSpotAngle = function(t) {
    this._spotAngle = t, this._spotUniform[0] = Math.cos(.5 * this._spotAngle)
   }, zt.spotAngle.get = function() {
    return this._spotAngle
   }, kt.prototype.setSpotExp = function(t) {
    this._spotExp = t, this._spotUniform[1] = t
   }, zt.spotExp.get = function() {
    return this._spotExp
   }, kt.prototype.setRange = function(t) {
    this._range = t
   }, zt.range.get = function() {
    return this._range
   }, kt.prototype.setShadowType = function(t) {
    this._shadowType === Y.SHADOW_NONE && t !== Y.SHADOW_NONE && (this._shadowMapDirty = !0), this._shadowType = t
   }, zt.shadowType.get = function() {
    return this._shadowType
   }, zt.shadowMap.get = function() {
    return this._shadowMap
   }, zt.viewProjMatrix.get = function() {
    return this._viewProjMatrix
   }, kt.prototype.setShadowResolution = function(t) {
    this._shadowResolution !== t && (this._shadowMapDirty = !0), this._shadowResolution = t
   }, zt.shadowResolution.get = function() {
    return this._shadowResolution
   }, kt.prototype.setShadowBias = function(t) {
    this._shadowBias = t
   }, zt.shadowBias.get = function() {
    return this._shadowBias
   }, kt.prototype.setShadowDarkness = function(t) {
    this._shadowDarkness = t
   }, zt.shadowDarkness.get = function() {
    return this._shadowDarkness
   }, kt.prototype.setShadowMinDepth = function(t) {
    this._shadowMinDepth = t
   }, zt.shadowMinDepth.get = function() {
    return this._type === Y.LIGHT_DIRECTIONAL ? 1 : this._shadowMinDepth
   }, kt.prototype.setShadowMaxDepth = function(t) {
    this._shadowMaxDepth = t
   }, zt.shadowMaxDepth.get = function() {
    return this._type === Y.LIGHT_DIRECTIONAL ? 1 : this._shadowMaxDepth
   }, kt.prototype.setShadowDepthScale = function(t) {
    this._shadowDepthScale = t
   }, zt.shadowDepthScale.get = function() {
    return this._shadowDepthScale
   }, kt.prototype.setFrustumEdgeFalloff = function(t) {
    this._frustumEdgeFalloff = t
   }, zt.frustumEdgeFalloff.get = function() {
    return this._frustumEdgeFalloff
   }, kt.prototype.extractView = function(t, e) {
    switch (t._shadowLight = this, t._rect.x = 0, t._rect.y = 0, t._rect.w = this._shadowResolution, t._rect.h = this._shadowResolution, G.set(t._color, 1, 1, 1, 1), t._depth = 1, t._stencil = 0, t._clearFlags = Y.CLEAR_COLOR | Y.CLEAR_DEPTH, t._stages = e, t._framebuffer = this._shadowFrameBuffer, this._type) {
     case Y.LIGHT_SPOT:
      (function(t, e, i) {
       t._node.getWorldRT(e), B.invert(e, e), B.perspective(i, t._spotAngle * t._spotAngleScale, 1, t._shadowMinDepth, t._shadowMaxDepth)
      })(this, t._matView, t._matProj);
      break;
     case Y.LIGHT_DIRECTIONAL:
      (function(t, e, i) {
       t._node.getWorldRT(e), B.invert(e, e);
       var n = t._shadowFustumSize / 2;
       B.ortho(i, -n, n, -n, n, t._shadowMinDepth, t._shadowMaxDepth)
      })(this, t._matView, t._matProj);
      break;
     case Y.LIGHT_POINT:
      t._matView, t._matProj;
      break;
     default:
      console.warn("shadow of this light type is not supported")
    }
    B.mul(t._matViewProj, t._matProj, t._matView), this._viewProjMatrix = t._matViewProj, B.invert(t._matInvViewProj, t._matViewProj)
   }, kt.prototype._updateLightPositionAndDirection = function() {
    this._node.getWorldMatrix(Ft), b.fromMat4(Nt, Ft), y.transformMat3(Bt, Pt, Nt), y.array(this._directionUniform, Bt);
    var t = this._positionUniform;
    t[0] = Ft.m12, t[1] = Ft.m13, t[2] = Ft.m14
   }, kt.prototype._generateShadowMap = function(t) {
    this._shadowMap = new Et.Texture2D(t, {
     width: this._shadowResolution,
     height: this._shadowResolution,
     format: Et.TEXTURE_FMT_RGBA8,
     wrapS: Et.WRAP_CLAMP,
     wrapT: Et.WRAP_CLAMP
    }), this._shadowDepthBuffer = new Et.RenderBuffer(t, Et.RB_FMT_D16, this._shadowResolution, this._shadowResolution), this._shadowFrameBuffer = new Et.FrameBuffer(t, this._shadowResolution, this._shadowResolution, {
     colors: [this._shadowMap],
     depth: this._shadowDepthBuffer
    })
   }, kt.prototype._destroyShadowMap = function() {
    this._shadowMap && (this._shadowMap.destroy(), this._shadowDepthBuffer.destroy(), this._shadowFrameBuffer.destroy(), this._shadowMap = null, this._shadowDepthBuffer = null, this._shadowFrameBuffer = null)
   }, kt.prototype.update = function(t) {
    this._updateLightPositionAndDirection(), this._shadowType === Y.SHADOW_NONE ? this._destroyShadowMap() : this._shadowMapDirty && (this._destroyShadowMap(), this._generateShadowMap(t), this._shadowMapDirty = !1)
   }, Object.defineProperties(kt.prototype, zt);
   var Ut = B.create(),
    Wt = B.create(),
    Ht = B.create(),
    Gt = B.create(),
    Vt = y.create(),
    jt = function() {
     this._poolID = -1, this._node = null, this._projection = Y.PROJ_PERSPECTIVE, this._color = G.new(.2, .3, .47, 1), this._depth = 1, this._stencil = 0, this._clearFlags = Y.CLEAR_COLOR | Y.CLEAR_DEPTH, this._cullingMask = 1, this._stages = [], this._framebuffer = null, this._near = .01, this._far = 1e3, this._fov = Math.PI / 4, this._rect = {
      x: 0,
      y: 0,
      w: 1,
      h: 1
     }, this._orthoHeight = 10
    },
    Yt = {
     cullingMask: {
      configurable: !0
     }
    };
   Yt.cullingMask.get = function() {
    return this._cullingMask
   }, Yt.cullingMask.set = function(t) {
    this._cullingMask = t
   }, jt.prototype.getNode = function() {
    return this._node
   }, jt.prototype.setNode = function(t) {
    this._node = t
   }, jt.prototype.getType = function() {
    return this._projection
   }, jt.prototype.setType = function(t) {
    this._projection = t
   }, jt.prototype.getOrthoHeight = function() {
    return this._orthoHeight
   }, jt.prototype.setOrthoHeight = function(t) {
    this._orthoHeight = t
   }, jt.prototype.getFov = function() {
    return this._fov
   }, jt.prototype.setFov = function(t) {
    this._fov = t
   }, jt.prototype.getNear = function() {
    return this._near
   }, jt.prototype.setNear = function(t) {
    this._near = t
   }, jt.prototype.getFar = function() {
    return this._far
   }, jt.prototype.setFar = function(t) {
    this._far = t
   }, jt.prototype.getColor = function(t) {
    return G.copy(t, this._color)
   }, jt.prototype.setColor = function(t, e, i, n) {
    G.set(this._color, t, e, i, n)
   }, jt.prototype.getDepth = function() {
    return this._depth
   }, jt.prototype.setDepth = function(t) {
    this._depth = t
   }, jt.prototype.getStencil = function() {
    return this._stencil
   }, jt.prototype.setStencil = function(t) {
    this._stencil = t
   }, jt.prototype.getClearFlags = function() {
    return this._clearFlags
   }, jt.prototype.setClearFlags = function(t) {
    this._clearFlags = t
   }, jt.prototype.getRect = function(t) {
    return t.x = this._rect.x, t.y = this._rect.y, t.w = this._rect.w, t.h = this._rect.h, t
   }, jt.prototype.setRect = function(t, e, i, n) {
    this._rect.x = t, this._rect.y = e, this._rect.w = i, this._rect.h = n
   }, jt.prototype.getStages = function() {
    return this._stages
   }, jt.prototype.setStages = function(t) {
    this._stages = t
   }, jt.prototype.getFramebuffer = function() {
    return this._framebuffer
   }, jt.prototype.setFramebuffer = function(t) {
    this._framebuffer = t
   }, jt.prototype.extractView = function(t, e, i) {
    t._rect.x = this._rect.x * e, t._rect.y = this._rect.y * i, t._rect.w = this._rect.w * e, t._rect.h = this._rect.h * i, t._color = this._color, t._depth = this._depth, t._stencil = this._stencil, t._clearFlags = this._clearFlags, t._cullingMask = this._cullingMask, t._stages = this._stages, t._framebuffer = this._framebuffer, this._node.getWorldRT(t._matView), B.invert(t._matView, t._matView);
    var n = e / i;
    if (this._projection === Y.PROJ_PERSPECTIVE) B.perspective(t._matProj, this._fov, n, this._near, this._far);
    else {
     var r = this._orthoHeight * n,
      s = this._orthoHeight;
     B.ortho(t._matProj, -r, r, -s, s, this._near, this._far)
    }
    B.mul(t._matViewProj, t._matProj, t._matView), B.invert(t._matInvViewProj, t._matViewProj)
   }, jt.prototype.screenToWorld = function(t, e, i, n) {
    var r = i / n,
     s = this._rect.x * i,
     a = this._rect.y * n,
     o = this._rect.w * i,
     c = this._rect.h * n;
    if (this._node.getWorldRT(Ut), B.invert(Ut, Ut), this._projection === Y.PROJ_PERSPECTIVE) B.perspective(Wt, this._fov, r, this._near, this._far);
    else {
     var h = this._orthoHeight * r,
      u = this._orthoHeight;
     B.ortho(Wt, -h, h, -u, u, this._near, this._far)
    }
    if (B.mul(Ht, Wt, Ut), B.invert(Gt, Ht), this._projection === Y.PROJ_PERSPECTIVE) y.set(t, 2 * (e.x - s) / o - 1, 2 * (e.y - a) / c - 1, 1), y.transformMat4(t, t, Gt), this._node.getWorldPos(Vt), y.lerp(t, Vt, t, e.z / this._far);
    else {
     var l = this._farClip - this._nearClip;
     y.set(t, 2 * (e.x - s) / o - 1, 2 * (e.y - a) / c - 1, (this._far - e.z) / l * 2 - 1), y.transformMat4(t, t, Gt)
    }
    return t
   }, jt.prototype.worldToScreen = function(t, e, i, n) {
    var r = i / n,
     s = this._rect.x * i,
     a = this._rect.y * n,
     o = this._rect.w * i,
     c = this._rect.h * n;
    if (this._node.getWorldRT(Ut), B.invert(Ut, Ut), this._projection === Y.PROJ_PERSPECTIVE) B.perspective(Wt, this._fov, r, this._near, this._far);
    else {
     var h = this._orthoHeight * r,
      u = this._orthoHeight;
     B.ortho(Wt, -h, h, -u, u, this._near, this._far)
    }
    B.mul(Ht, Wt, Ut);
    var l = e.x * Ht.m03 + e.y * Ht.m07 + e.z * Ht.m11 + Ht.m15;
    return y.transformMat4(t, e, Ht), t.x = s + .5 * (t.x / l + 1) * o, t.y = a + .5 * (t.y / l + 1) * c, t
   }, Object.defineProperties(jt.prototype, Yt);
   var Xt = function() {
     this._poolID = -1, this._node = null, this._inputAssemblers = [], this._effects = [], this._defines = [], this._dynamicIA = !1, this._cullingMask = -1
    },
    qt = {
     inputAssemblerCount: {
      configurable: !0
     },
     dynamicIA: {
      configurable: !0
     },
     drawItemCount: {
      configurable: !0
     },
     cullingMask: {
      configurable: !0
     }
    };
   qt.inputAssemblerCount.get = function() {
    return this._inputAssemblers.length
   }, qt.dynamicIA.get = function() {
    return this._dynamicIA
   }, qt.drawItemCount.get = function() {
    return this._dynamicIA ? 1 : this._inputAssemblers.length
   }, qt.cullingMask.get = function() {
    return this._cullingMask
   }, qt.cullingMask.set = function(t) {
    this._cullingMask = t
   }, Xt.prototype.setNode = function(t) {
    this._node = t
   }, Xt.prototype.setDynamicIA = function(t) {
    this._dynamicIA = t
   }, Xt.prototype.addInputAssembler = function(t) {
    -1 === this._inputAssemblers.indexOf(t) && this._inputAssemblers.push(t)
   }, Xt.prototype.clearInputAssemblers = function() {
    this._inputAssemblers.length = 0
   }, Xt.prototype.addEffect = function(t) {
    if (-1 === this._effects.indexOf(t)) {
     this._effects.push(t);
     var e = Object.create(null);
     t.extractDefines(e), this._defines.push(e)
    }
   }, Xt.prototype.clearEffects = function() {
    this._effects.length = 0, this._defines.length = 0
   }, Xt.prototype.extractDrawItem = function(t, e) {
    return this._dynamicIA ? (t.model = this, t.node = this._node, t.ia = null, t.effect = this._effects[0], void(t.defines = t.effect.extractDefines(this._defines[0]))) : e >= this._inputAssemblers.length ? (t.model = null, t.node = null, t.ia = null, t.effect = null, void(t.defines = null)) : (t.model = this, t.node = this._node, t.ia = this._inputAssemblers[e], e < this._effects.length ? (i = this._effects[e], n = this._defines[e]) : (i = this._effects[this._effects.length - 1], n = this._defines[this._effects.length - 1]), t.effect = i, void(t.defines = i.extractDefines(n)));
    var i, n
   }, Object.defineProperties(Xt.prototype, qt);
   var Zt = 32,
    Kt = [1, 10, 100, 1e3, 1e4, 1e5, 1e6, 1e7, 1e8, 1e9];

   function Jt(t) {
    return t < 1e5 ? t < 100 ? t < 10 ? 0 : 1 : t < 1e4 ? t < 1e3 ? 2 : 3 : 4 : t < 1e7 ? t < 1e6 ? 5 : 6 : t < 1e9 ? t < 1e8 ? 7 : 8 : 9
   }

   function Qt(t, e) {
    if (t === e) return 0;
    if (~~t === t && ~~e === e) {
     if (0 === t || 0 === e) return t < e ? -1 : 1;
     if (t < 0 || e < 0) {
      if (e >= 0) return -1;
      if (t >= 0) return 1;
      t = -t, e = -e
     }
     var i = Jt(t),
      n = Jt(e),
      r = 0;
     return i < n ? (t *= Kt[n - i - 1], e /= 10, r = -1) : i > n && (e *= Kt[i - n - 1], t /= 10, r = 1), t === e ? r : t < e ? -1 : 1
    }
    var s = String(t),
     a = String(e);
    return s === a ? 0 : s < a ? -1 : 1
   }

   function $t(t, e, i, n) {
    var r = e + 1;
    if (r === i) return 1;
    if (n(t[r++], t[e]) < 0) {
     for (; r < i && n(t[r], t[r - 1]) < 0;) r++;
     (function(t, e, i) {
      i--;
      for (; e < i;) {
       var n = t[e];
       t[e++] = t[i], t[i--] = n
      }
     })(t, e, r)
    } else
     for (; r < i && n(t[r], t[r - 1]) >= 0;) r++;
    return r - e
   }

   function te(t, e, i, n, r) {
    for (n === e && n++; n < i; n++) {
     for (var s = t[n], a = e, o = n; a < o;) {
      var c = a + o >>> 1;
      r(s, t[c]) < 0 ? o = c : a = c + 1
     }
     var h = n - a;
     switch (h) {
      case 3:
       t[a + 3] = t[a + 2];
      case 2:
       t[a + 2] = t[a + 1];
      case 1:
       t[a + 1] = t[a];
       break;
      default:
       for (; h > 0;) t[a + h] = t[a + h - 1], h--
     }
     t[a] = s
    }
   }

   function ee(t, e, i, n, r, s) {
    var a = 0,
     o = 0,
     c = 1;
    if (s(t, e[i + r]) > 0) {
     for (o = n - r; c < o && s(t, e[i + r + c]) > 0;) a = c, (c = 1 + (c << 1)) <= 0 && (c = o);
     c > o && (c = o), a += r, c += r
    } else {
     for (o = r + 1; c < o && s(t, e[i + r - c]) <= 0;) a = c, (c = 1 + (c << 1)) <= 0 && (c = o);
     c > o && (c = o);
     var h = a;
     a = r - c, c = r - h
    }
    for (a++; a < c;) {
     var u = a + (c - a >>> 1);
     s(t, e[i + u]) > 0 ? a = u + 1 : c = u
    }
    return c
   }

   function ie(t, e, i, n, r, s) {
    var a = 0,
     o = 0,
     c = 1;
    if (s(t, e[i + r]) < 0) {
     for (o = r + 1; c < o && s(t, e[i + r - c]) < 0;) a = c, (c = 1 + (c << 1)) <= 0 && (c = o);
     c > o && (c = o);
     var h = a;
     a = r - c, c = r - h
    } else {
     for (o = n - r; c < o && s(t, e[i + r + c]) >= 0;) a = c, (c = 1 + (c << 1)) <= 0 && (c = o);
     c > o && (c = o), a += r, c += r
    }
    for (a++; a < c;) {
     var u = a + (c - a >>> 1);
     s(t, e[i + u]) < 0 ? c = u : a = u + 1
    }
    return c
   }
   var ne = function(t, e) {
    this.array = t, this.compare = e, this.minGallop = 7, this.length = t.length, this.tmpStorageLength = 256, this.length < 512 && (this.tmpStorageLength = this.length >>> 1), this.tmp = new Array(this.tmpStorageLength), this.stackLength = this.length < 120 ? 5 : this.length < 1542 ? 10 : this.length < 119151 ? 19 : 40, this.runStart = new Array(this.stackLength), this.runLength = new Array(this.stackLength), this.stackSize = 0
   };

   function re(t, e, i, n) {
    if (!Array.isArray(t)) throw new TypeError("Can only sort arrays");
    void 0 === e && (e = 0), void 0 === i && (i = t.length), void 0 === n && (n = Qt);
    var r = i - e;
    if (!(r < 2)) {
     var s = 0;
     if (r < Zt) te(t, e, i, e + (s = $t(t, e, i, n)), n);
     else {
      var a = new ne(t, n),
       o = (function(t) {
        for (var e = 0; t >= Zt;) e |= 1 & t, t >>= 1;
        return t + e
       })(r);
      do {
       if ((s = $t(t, e, i, n)) < o) {
        var c = r;
        c > o && (c = o), te(t, e, e + c, e + s, n), s = c
       }
       a.pushRun(e, s), a.mergeRuns(), r -= s, e += s
      } while (0 !== r);
      a.forceMergeRuns()
     }
    }
   }
   ne.prototype.pushRun = function(t, e) {
    this.runStart[this.stackSize] = t, this.runLength[this.stackSize] = e, this.stackSize += 1
   }, ne.prototype.mergeRuns = function() {
    for (; this.stackSize > 1;) {
     var t = this.stackSize - 2;
     if (t >= 1 && this.runLength[t - 1] <= this.runLength[t] + this.runLength[t + 1] || t >= 2 && this.runLength[t - 2] <= this.runLength[t] + this.runLength[t - 1]) this.runLength[t - 1] < this.runLength[t + 1] && t--;
     else if (this.runLength[t] > this.runLength[t + 1]) break;
     this.mergeAt(t)
    }
   }, ne.prototype.forceMergeRuns = function() {
    for (; this.stackSize > 1;) {
     var t = this.stackSize - 2;
     t > 0 && this.runLength[t - 1] < this.runLength[t + 1] && t--, this.mergeAt(t)
    }
   }, ne.prototype.mergeAt = function(t) {
    var e = this.compare,
     i = this.array,
     n = this.runStart[t],
     r = this.runLength[t],
     s = this.runStart[t + 1],
     a = this.runLength[t + 1];
    this.runLength[t] = r + a, t === this.stackSize - 3 && (this.runStart[t + 1] = this.runStart[t + 2], this.runLength[t + 1] = this.runLength[t + 2]), this.stackSize--;
    var o = ie(i[s], i, n, r, 0, e);
    n += o, 0 !== (r -= o) && 0 !== (a = ee(i[n + r - 1], i, s, a, a - 1, e)) && (r <= a ? this.mergeLow(n, r, s, a) : this.mergeHigh(n, r, s, a))
   }, ne.prototype.mergeLow = function(t, e, i, n) {
    var r = this.compare,
     s = this.array,
     a = this.tmp,
     o = 0;
    for (o = 0; o < e; o++) a[o] = s[t + o];
    var c = 0,
     h = i,
     u = t;
    if (s[u++] = s[h++], 0 != --n)
     if (1 !== e) {
      for (var l = this.minGallop;;) {
       var _ = 0,
        d = 0,
        f = !1;
       do {
        if (r(s[h], a[c]) < 0) {
         if (s[u++] = s[h++], d++, _ = 0, 0 == --n) {
          f = !0;
          break
         }
        } else if (s[u++] = a[c++], _++, d = 0, 1 == --e) {
         f = !0;
         break
        }
       } while ((_ | d) < l);
       if (f) break;
       do {
        if (0 !== (_ = ie(s[h], a, c, e, 0, r))) {
         for (o = 0; o < _; o++) s[u + o] = a[c + o];
         if (u += _, c += _, (e -= _) <= 1) {
          f = !0;
          break
         }
        }
        if (s[u++] = s[h++], 0 == --n) {
         f = !0;
         break
        }
        if (0 !== (d = ee(a[c], s, h, n, 0, r))) {
         for (o = 0; o < d; o++) s[u + o] = s[h + o];
         if (u += d, h += d, 0 === (n -= d)) {
          f = !0;
          break
         }
        }
        if (s[u++] = a[c++], 1 == --e) {
         f = !0;
         break
        }
        l--
       } while (_ >= 7 || d >= 7);
       if (f) break;
       l < 0 && (l = 0), l += 2
      }
      if (this.minGallop = l, l < 1 && (this.minGallop = 1), 1 === e) {
       for (o = 0; o < n; o++) s[u + o] = s[h + o];
       s[u + n] = a[c]
      } else {
       if (0 === e) throw new Error("mergeLow preconditions were not respected");
       for (o = 0; o < e; o++) s[u + o] = a[c + o]
      }
     } else {
      for (o = 0; o < n; o++) s[u + o] = s[h + o];
      s[u + n] = a[c]
     }
    else
     for (o = 0; o < e; o++) s[u + o] = a[c + o]
   }, ne.prototype.mergeHigh = function(t, e, i, n) {
    var r = this.compare,
     s = this.array,
     a = this.tmp,
     o = 0;
    for (o = 0; o < n; o++) a[o] = s[i + o];
    var c = t + e - 1,
     h = n - 1,
     u = i + n - 1,
     l = 0,
     _ = 0;
    if (s[u--] = s[c--], 0 != --e)
     if (1 !== n) {
      for (var d = this.minGallop;;) {
       var f = 0,
        p = 0,
        m = !1;
       do {
        if (r(a[h], s[c]) < 0) {
         if (s[u--] = s[c--], f++, p = 0, 0 == --e) {
          m = !0;
          break
         }
        } else if (s[u--] = a[h--], p++, f = 0, 1 == --n) {
         m = !0;
         break
        }
       } while ((f | p) < d);
       if (m) break;
       do {
        if (0 !== (f = e - ie(a[h], s, t, e, e - 1, r))) {
         for (e -= f, _ = (u -= f) + 1, l = (c -= f) + 1, o = f - 1; o >= 0; o--) s[_ + o] = s[l + o];
         if (0 === e) {
          m = !0;
          break
         }
        }
        if (s[u--] = a[h--], 1 == --n) {
         m = !0;
         break
        }
        if (0 !== (p = n - ee(s[c], a, 0, n, n - 1, r))) {
         for (n -= p, _ = (u -= p) + 1, l = (h -= p) + 1, o = 0; o < p; o++) s[_ + o] = a[l + o];
         if (n <= 1) {
          m = !0;
          break
         }
        }
        if (s[u--] = s[c--], 0 == --e) {
         m = !0;
         break
        }
        d--
       } while (f >= 7 || p >= 7);
       if (m) break;
       d < 0 && (d = 0), d += 2
      }
      if (this.minGallop = d, d < 1 && (this.minGallop = 1), 1 === n) {
       for (_ = (u -= e) + 1, l = (c -= e) + 1, o = e - 1; o >= 0; o--) s[_ + o] = s[l + o];
       s[u] = a[h]
      } else {
       if (0 === n) throw new Error("mergeHigh preconditions were not respected");
       for (l = u - (n - 1), o = 0; o < n; o++) s[l + o] = a[o]
      }
     } else {
      for (_ = (u -= e) + 1, l = (c -= e) + 1, o = e - 1; o >= 0; o--) s[_ + o] = s[l + o];
      s[u] = a[h]
     }
    else
     for (l = u - (n - 1), o = 0; o < n; o++) s[l + o] = a[o]
   };
   var se = function(t) {
     this._count = 0, this._data = new Array(t)
    },
    ae = {
     length: {
      configurable: !0
     },
     data: {
      configurable: !0
     }
    };
   se.prototype._resize = function(t) {
    if (t > this._data.length)
     for (var e = this._data.length; e < t; ++e) this._data[e] = void 0
   }, ae.length.get = function() {
    return this._count
   }, ae.data.get = function() {
    return this._data
   }, se.prototype.reset = function() {
    for (var t = 0; t < this._count; ++t) this._data[t] = void 0;
    this._count = 0
   }, se.prototype.push = function(t) {
    this._count >= this._data.length && this._resize(2 * this._data.length), this._data[this._count] = t, ++this._count
   }, se.prototype.pop = function() {
    --this._count, this._count < 0 && (this._count = 0);
    var t = this._data[this._count];
    return this._data[this._count] = void 0, t
   }, se.prototype.fastRemove = function(t) {
    if (!(t >= this._count)) {
     var e = this._count - 1;
     this._data[t] = this._data[e], this._data[e] = void 0, this._count -= 1
    }
   }, se.prototype.indexOf = function(t) {
    var e = this._data.indexOf(t);
    return e >= this._count ? -1 : e
   }, se.prototype.sort = function(t) {
    return re(this._data, 0, this._count, t)
   }, Object.defineProperties(se.prototype, ae);
   var oe = function(t, e) {
    this._fn = t, this._idx = e - 1, this._frees = new Array(e);
    for (var i = 0; i < e; ++i) this._frees[i] = t()
   };
   oe.prototype._expand = function(t) {
    var e = this._frees;
    this._frees = new Array(t);
    for (var i = t - e.length, n = 0; n < i; ++n) this._frees[n] = this._fn();
    for (var r = i, s = 0; r < t; ++r, ++s) this._frees[r] = e[s];
    this._idx += i
   }, oe.prototype.alloc = function() {
    this._idx < 0 && this._expand(Math.round(1.2 * this._frees.length) + 1);
    var t = this._frees[this._idx];
    return this._frees[this._idx] = null, --this._idx, t
   }, oe.prototype.free = function(t) {
    ++this._idx, this._frees[this._idx] = t
   };
   var ce = function(t, e) {
     this._fn = t, this._count = 0, this._head = null, this._tail = null, this._pool = new oe(t, e)
    },
    he = {
     head: {
      configurable: !0
     },
     tail: {
      configurable: !0
     },
     length: {
      configurable: !0
     }
    };
   he.head.get = function() {
    return this._head
   }, he.tail.get = function() {
    return this._tail
   }, he.length.get = function() {
    return this._count
   }, ce.prototype.add = function() {
    var t = this._pool.alloc();
    return this._tail ? (this._tail._next = t, t._prev = this._tail) : this._head = t, this._tail = t, this._count += 1, t
   }, ce.prototype.remove = function(t) {
    t._prev ? t._prev._next = t._next : this._head = t._next, t._next ? t._next._prev = t._prev : this._tail = t._prev, t._next = null, t._prev = null, this._pool.free(t), this._count -= 1
   }, ce.prototype.forEach = function(t, e) {
    var i = this._head;
    if (i) {
     e && (t = t.bind(e));
     for (var n = 0, r = i; i;) r = i._next, t(i, n, this), i = r, ++n
    }
   }, Object.defineProperties(ce.prototype, he);
   var ue = function(t, e) {
     this._fn = t, this._count = 0, this._data = new Array(e);
     for (var i = 0; i < e; ++i) this._data[i] = t()
    },
    le = {
     length: {
      configurable: !0
     },
     data: {
      configurable: !0
     }
    };
   le.length.get = function() {
    return this._count
   }, le.data.get = function() {
    return this._data
   }, ue.prototype.reset = function() {
    this._count = 0
   }, ue.prototype.resize = function(t) {
    if (t > this._data.length)
     for (var e = this._data.length; e < t; ++e) this._data[e] = this._fn()
   }, ue.prototype.add = function() {
    return this._count >= this._data.length && this.resize(2 * this._data.length), this._data[this._count++]
   }, ue.prototype.remove = function(t) {
    if (!(t >= this._count)) {
     var e = this._count - 1,
      i = this._data[t];
     this._data[t] = this._data[e], this._data[e] = i, this._count -= 1
    }
   }, ue.prototype.sort = function(t) {
    return re(this._data, 0, this._count, t)
   }, Object.defineProperties(ue.prototype, le);
   for (var _e = Array(8), de = 0; de < 8; ++de) _e[de] = [];
   var fe = function() {
    this._lights = new se(16), this._models = new se(16), this._cameras = new se(16), this._debugCamera = null, this._views = []
   };
   fe.prototype._add = function(t, e) {
    -1 === e._poolID && (t.push(e), e._poolID = t.length - 1)
   }, fe.prototype._remove = function(t, e) {
    -1 !== e._poolID && (t.data[t.length - 1]._poolID = e._poolID, t.fastRemove(e._poolID), e._poolID = -1)
   }, fe.prototype.reset = function() {
    for (var t = 0; t < this._models.length; ++t) {
     this._models.data[t]._cullingMask = -1
    }
   }, fe.prototype.setDebugCamera = function(t) {
    this._debugCamera = t
   }, fe.prototype.getCameraCount = function() {
    return this._cameras.length
   }, fe.prototype.getCamera = function(t) {
    return this._cameras.data[t]
   }, fe.prototype.addCamera = function(t) {
    this._add(this._cameras, t)
   }, fe.prototype.removeCamera = function(t) {
    this._remove(this._cameras, t)
   }, fe.prototype.getModelCount = function() {
    return this._models.length
   }, fe.prototype.getModel = function(t) {
    return this._models.data[t]
   }, fe.prototype.addModel = function(t) {
    this._add(this._models, t)
   }, fe.prototype.removeModel = function(t) {
    this._remove(this._models, t)
   }, fe.prototype.getLightCount = function() {
    return this._lights.length
   }, fe.prototype.getLight = function(t) {
    return this._lights.data[t]
   }, fe.prototype.addLight = function(t) {
    this._add(this._lights, t)
   }, fe.prototype.removeLight = function(t) {
    this._remove(this._lights, t)
   }, fe.prototype.addView = function(t) {
    -1 === this._views.indexOf(t) && this._views.push(t)
   }, fe.prototype.removeView = function(t) {
    var e = this._views.indexOf(t); - 1 !== e && this._views.splice(e, 1)
   };
   var pe = 0;

   function me(t, e) {
    var i = {},
     n = t;
    for (var r in e) Number.isInteger(e[r]) && (i[r] = e[r]);
    for (var s in i) {
     var a = new RegExp(s, "g");
     n = n.replace(a, i[s])
    }
    return n
   }

   function ve(t) {
    return t.replace(/#pragma for (\w+) in range\(\s*(\d+)\s*,\s*(\d+)\s*\)([\s\S]+?)#pragma endFor/g, (function(t, e, i, n, r) {
     var s = "",
      a = parseInt(i),
      o = parseInt(n);
     (a.isNaN || o.isNaN) && console.error("Unroll For Loops Error: begin and end of range must be an int num.");
     for (var c = a; c < o; ++c) s += r.replace(new RegExp("{" + e + "}", "g"), c);
     return s
    }))
   }
   var ge = function(t, e, i) {
    void 0 === e && (e = []), void 0 === i && (i = {}), this._device = t, this._precision = "precision highp float;\n", this._templates = {};
    for (var n = 0; n < e.length; ++n) {
     var r = e[n];
     this.define(r.name, r.vert, r.frag, r.defines)
    }
    this._chunks = {}, Object.assign(this._chunks, i), this._cache = {}
   };
   ge.prototype.define = function(t, e, i, n) {
    if (this._templates[t]) console.warn("Failed to define shader " + t + ": already exists.");
    else {
     for (var r = ++pe, s = 0, a = function(t) {
       var e = n[t];
       e._offset = s;
       var i = 1;
       void 0 !== e.min && void 0 !== e.max ? (i = Math.ceil(.5 * (e.max - e.min)), e._map = function(t) {
        return t - this._min << e._offset
       }.bind(e)) : e._map = function(t) {
        return t ? 1 << e._offset : 0
       }.bind(e), s += i, e._offset = s
      }, o = 0; o < n.length; ++o) a(o);
     e = this._precision + e, i = this._precision + i, this._templates[t] = {
      id: r,
      name: t,
      vert: e,
      frag: i,
      defines: n
     }
    }
   }, ge.prototype.getKey = function(t, e) {
    for (var i = this._templates[t], n = 0, r = 0; r < i.defines.length; ++r) {
     var s = i.defines[r],
      a = e[s.name];
     void 0 !== a && (n |= s._map(a))
    }
    return n << 8 | i.id
   }, ge.prototype.getProgram = function(t, e) {
    var i = this.getKey(t, e),
     n = this._cache[i];
    if (n) return n;
    var r = this._templates[t],
     s = (function(t) {
      var e = [];
      for (var i in t) !0 === t[i] && e.push("#define " + i);
      return e.join("\n")
     })(e) + "\n",
     a = me(r.vert, e);
    a = s + ve(a);
    var o = me(r.frag, e);
    return o = s + ve(o), (n = new Et.Program(this._device, {
     vert: a,
     frag: o
    })).link(), this._cache[i] = n, n
   };
   var ye = b.create(),
    Te = B.create(),
    Ee = new ue(function() {
     return {
      stage: null,
      items: null
     }
    }, 8),
    xe = new ue(function() {
     return new Float32Array(2)
    }, 8),
    Ce = new ue(function() {
     return new Float32Array(3)
    }, 8),
    Ae = new ue(function() {
     return new Float32Array(4)
    }, 8),
    be = new ue(function() {
     return new Float32Array(9)
    }, 8),
    Se = new ue(function() {
     return new Float32Array(16)
    }, 8),
    we = new ue(function() {
     return new Float32Array(64)
    }, 8),
    De = new ue(function() {
     return new Int32Array(2)
    }, 8),
    Re = new ue(function() {
     return new Int32Array(3)
    }, 8),
    Me = new ue(function() {
     return new Int32Array(4)
    }, 8),
    Ie = new ue(function() {
     return new Int32Array(64)
    }, 8),
    Le = {};
   Le[Y.PARAM_INT] = function(t) {
    return t
   }, Le[Y.PARAM_INT2] = function(t) {
    return m.array(De.add(), t)
   }, Le[Y.PARAM_INT3] = function(t) {
    return y.array(Re.add(), t)
   }, Le[Y.PARAM_INT4] = function(t) {
    return x.array(Me.add(), t)
   }, Le[Y.PARAM_FLOAT] = function(t) {
    return t
   }, Le[Y.PARAM_FLOAT2] = function(t) {
    return m.array(xe.add(), t)
   }, Le[Y.PARAM_FLOAT3] = function(t) {
    return y.array(Ce.add(), t)
   }, Le[Y.PARAM_FLOAT4] = function(t) {
    return x.array(Ae.add(), t)
   }, Le[Y.PARAM_COLOR3] = function(t) {
    return U.array(Ce.add(), t)
   }, Le[Y.PARAM_COLOR4] = function(t) {
    return G.array(Ae.add(), t)
   }, Le[Y.PARAM_MAT2] = function(t) {
    return I.array(Ae.add(), t)
   }, Le[Y.PARAM_MAT3] = function(t) {
    return b.array(be.add(), t)
   }, Le[Y.PARAM_MAT4] = function(t) {
    return B.array(Se.add(), t)
   };
   var Oe = {};
   Oe[Y.PARAM_INT] = {
    func: function(t) {
     for (var e = Ie.add(), i = 0; i < t.length; ++i) e[i] = t[i];
     return e
    },
    size: 1
   }, Oe[Y.PARAM_INT2] = {
    func: function(t) {
     for (var e = Ie.add(), i = 0; i < t.length; ++i) e[2 * i] = t[i].x, e[2 * i + 1] = t[i].y;
     return e
    },
    size: 2
   }, Oe[Y.PARAM_INT3] = {
    func: void 0,
    size: 3
   }, Oe[Y.PARAM_INT4] = {
    func: function(t) {
     for (var e = Ie.add(), i = 0; i < t.length; ++i) {
      var n = t[i];
      e[4 * i] = n.x, e[4 * i + 1] = n.y, e[4 * i + 2] = n.z, e[4 * i + 3] = n.w
     }
     return e
    },
    size: 4
   }, Oe[Y.PARAM_FLOAT] = {
    func: function(t) {
     for (var e = we.add(), i = 0; i < t.length; ++i) e[i] = t[i];
     return e
    },
    size: 1
   }, Oe[Y.PARAM_FLOAT2] = {
    func: function(t) {
     for (var e = we.add(), i = 0; i < t.length; ++i) e[2 * i] = t[i].x, e[2 * i + 1] = t[i].y;
     return e
    },
    size: 2
   }, Oe[Y.PARAM_FLOAT3] = {
    func: void 0,
    size: 3
   }, Oe[Y.PARAM_FLOAT4] = {
    func: function(t) {
     for (var e = we.add(), i = 0; i < t.length; ++i) {
      var n = t[i];
      e[4 * i] = n.x, e[4 * i + 1] = n.y, e[4 * i + 2] = n.z, e[4 * i + 3] = n.w
     }
     return e
    },
    size: 4
   }, Oe[Y.PARAM_COLOR3] = {
    func: void 0,
    size: 3
   }, Oe[Y.PARAM_COLOR4] = {
    func: function(t) {
     for (var e = we.add(), i = 0; i < t.length; ++i) {
      var n = t[i];
      e[4 * i] = n.r, e[4 * i + 1] = n.g, e[4 * i + 2] = n.b, e[4 * i + 3] = n.a
     }
     return e
    },
    size: 4
   }, Oe[Y.PARAM_MAT2] = {
    func: function(t) {
     for (var e = we.add(), i = 0; i < t.length; ++i) {
      var n = t[i];
      e[4 * i] = n.m00, e[4 * i + 1] = n.m01, e[4 * i + 2] = n.m02, e[4 * i + 3] = n.m03
     }
     return e
    },
    size: 4
   }, Oe[Y.PARAM_MAT3] = {
    func: void 0,
    size: 9
   }, Oe[Y.PARAM_MAT4] = {
    func: function(t) {
     for (var e = we.add(), i = 0; i < t.length; ++i) {
      var n = t[i];
      e[16 * i] = n.m00, e[16 * i + 1] = n.m01, e[16 * i + 2] = n.m02, e[16 * i + 3] = n.m03, e[16 * i + 4] = n.m04, e[16 * i + 5] = n.m05, e[16 * i + 6] = n.m06, e[16 * i + 7] = n.m07, e[16 * i + 8] = n.m08, e[16 * i + 9] = n.m09, e[16 * i + 10] = n.m10, e[16 * i + 11] = n.m11, e[16 * i + 12] = n.m12, e[16 * i + 13] = n.m13, e[16 * i + 14] = n.m14, e[16 * i + 15] = n.m15
     }
     return e
    },
    size: 16
   };
   var Pe = function(t, e) {
    var i;
    this._device = t, this._programLib = new ge(t, e.programTemplates, e.programChunks), this._opts = e, this._type2defaultValue = ((i = {})[Y.PARAM_INT] = 0, i[Y.PARAM_INT2] = m.new(0, 0), i[Y.PARAM_INT3] = y.new(0, 0, 0), i[Y.PARAM_INT4] = x.new(0, 0, 0, 0), i[Y.PARAM_FLOAT] = 0, i[Y.PARAM_FLOAT2] = m.new(0, 0), i[Y.PARAM_FLOAT3] = y.new(0, 0, 0), i[Y.PARAM_FLOAT4] = x.new(0, 0, 0, 0), i[Y.PARAM_COLOR3] = U.new(0, 0, 0), i[Y.PARAM_COLOR4] = G.new(0, 0, 0, 1), i[Y.PARAM_MAT2] = I.create(), i[Y.PARAM_MAT3] = b.create(), i[Y.PARAM_MAT4] = B.create(), i[Y.PARAM_TEXTURE_2D] = e.defaultTexture, i[Y.PARAM_TEXTURE_CUBE] = e.defaultTextureCube, i), this._stage2fn = {}, this._usedTextureUnits = 0, this._viewPools = new ue(function() {
     return new Ot
    }, 8), this._drawItemsPools = new ue(function() {
     return {
      model: null,
      node: null,
      ia: null,
      effect: null,
      defines: null
     }
    }, 100), this._stageItemsPools = new ue(function() {
     return new ue(function() {
      return {
       model: null,
       node: null,
       ia: null,
       effect: null,
       defines: null,
       technique: null,
       sortKey: -1
      }
     }, 100)
    }, 16)
   };
   Pe.prototype._resetTextuerUnit = function() {
    this._usedTextureUnits = 0
   }, Pe.prototype._allocTextuerUnit = function() {
    var t = this._device,
     e = this._usedTextureUnits;
    return e >= t._caps.maxTextureUnits && console.warn("Trying to use " + e + " texture units while this GPU supports only " + t._caps.maxTextureUnits), this._usedTextureUnits += 1, e
   }, Pe.prototype._registerStage = function(t, e) {
    this._stage2fn[t] = e
   }, Pe.prototype._reset = function() {
    this._viewPools.reset(), this._stageItemsPools.reset()
   }, Pe.prototype._requestView = function() {
    return this._viewPools.add()
   }, Pe.prototype._render = function(t, e) {
    var i = this._device;
    i.setFrameBuffer(t._framebuffer), i.setViewport(t._rect.x, t._rect.y, t._rect.w, t._rect.h);
    var n = {};
    t._clearFlags & Y.CLEAR_COLOR && (n.color = [t._color.r, t._color.g, t._color.b, t._color.a]), t._clearFlags & Y.CLEAR_DEPTH && (n.depth = t._depth), t._clearFlags & Y.CLEAR_STENCIL && (n.stencil = t._stencil), i.clear(n), this._drawItemsPools.reset();
    for (var r = 0; r < e._models.length; ++r) {
     var s = e._models.data[r];
     if (0 != (s._cullingMask & t._cullingMask))
      for (var a = 0; a < s.drawItemCount; ++a) {
       var o = this._drawItemsPools.add();
       s.extractDrawItem(o, a)
      }
    }
    Ee.reset();
    for (var c = 0; c < t._stages.length; ++c) {
     var h = t._stages[c],
      u = this._stageItemsPools.add();
     u.reset();
     for (var l = 0; l < this._drawItemsPools.length; ++l) {
      var _ = this._drawItemsPools.data[l],
       d = _.effect.getTechnique(h);
      if (d) {
       var f = u.add();
       f.model = _.model, f.node = _.node, f.ia = _.ia, f.effect = _.effect, f.defines = _.defines, f.technique = d, f.sortKey = -1
      }
     }
     var p = Ee.add();
     p.stage = h, p.items = u
    }
    for (var m = 0; m < Ee.length; ++m) {
     var v = Ee.data[m];
     (0, this._stage2fn[v.stage])(t, v.items)
    }
   }, Pe.prototype._draw = function(t) {
    var e = this._device,
     i = this._programLib,
     n = t.node,
     r = t.ia,
     s = t.effect,
     a = t.technique,
     o = t.defines;
    xe.reset(), Ce.reset(), Ae.reset(), be.reset(), Se.reset(), we.reset(), De.reset(), Re.reset(), Me.reset(), Ie.reset(), n.getWorldMatrix(Te), e.setUniform("model", B.array(Se.add(), Te));
    var c = b.invert(ye, b.fromMat4(ye, Te));
    c && (b.transpose(ye, c), e.setUniform("normalMatrix", b.array(be.add(), ye)));
    for (var h = 0; h < a._parameters.length; ++h) {
     var u = a._parameters[h],
      l = s.getProperty(u.name);
     if (void 0 === l && (l = u.val), void 0 === l && (l = this._type2defaultValue[u.type]), void 0 !== l)
      if (u.type === Y.PARAM_TEXTURE_2D || u.type === Y.PARAM_TEXTURE_CUBE)
       if (void 0 !== u.size) {
        if (u.size !== l.length) {
         console.error("The length of texture array (" + l.length + ") is not corrent(expect " + u.size + ").");
         continue
        }
        for (var _ = Ie.add(), d = 0; d < l.length; ++d) _[d] = this._allocTextuerUnit();
        e.setTextureArray(u.name, l, _)
       } else e.setTexture(u.name, l, this._allocTextuerUnit());
     else {
      var f = void 0;
      if (void 0 !== u.size) {
       var p = Oe[u.type];
       if (void 0 === p.func) {
        console.error("Uniform array of color3/int3/float3/mat3 can not be supportted!");
        continue
       }
       if (u.size * p.size > 64) {
        console.error("Uniform array is too long!");
        continue
       }
       f = p.func(l)
      } else {
       f = (0, Le[u.type])(l)
      }
      e.setUniform(u.name, f)
     } else console.warn("Failed to set technique property " + u.name + ", value not found.")
    }
    for (var m = 0; m < a._passes.length; ++m) {
     var v = a._passes[m],
      g = r.getPrimitiveCount();
     e.setVertexBuffer(0, r._vertexBuffer), r._indexBuffer && e.setIndexBuffer(r._indexBuffer), e.setPrimitiveType(r._primitiveType);
     var y = i.getProgram(v._programName, o);
     e.setProgram(y), e.setCullMode(v._cullMode), v._blend && (e.enableBlend(), e.setBlendFuncSep(v._blendSrc, v._blendDst, v._blendSrcAlpha, v._blendDstAlpha), e.setBlendEqSep(v._blendEq, v._blendAlphaEq), e.setBlendColor32(v._blendColor)), v._depthTest && (e.enableDepthTest(), e.setDepthFunc(v._depthFunc)), v._depthWrite && e.enableDepthWrite(), v._stencilTest && (e.enableStencilTest(), e.setStencilFuncFront(v._stencilFuncFront, v._stencilRefFront, v._stencilMaskFront), e.setStencilOpFront(v._stencilFailOpFront, v._stencilZFailOpFront, v._stencilZPassOpFront, v._stencilWriteMaskFront), e.setStencilFuncBack(v._stencilFuncBack, v._stencilRefBack, v._stencilMaskBack), e.setStencilOpBack(v._stencilFailOpBack, v._stencilZFailOpBack, v._stencilZPassOpBack, v._stencilWriteMaskBack)), e.draw(r._start, g), this._resetTextuerUnit()
    }
   };
   var Fe = {
    addStage: St.addStage,
    createIA: function(t, e) {
     if (!e.positions) return console.error("The data must have positions field"), null;
     for (var i = [], n = e.positions.length / 3, r = 0; r < n; ++r) i.push(e.positions[3 * r], e.positions[3 * r + 1], e.positions[3 * r + 2]), e.normals && i.push(e.normals[3 * r], e.normals[3 * r + 1], e.normals[3 * r + 2]), e.uvs && i.push(e.uvs[2 * r], e.uvs[2 * r + 1]);
     var s = [];
     s.push({
      name: Et.ATTR_POSITION,
      type: Et.ATTR_TYPE_FLOAT32,
      num: 3
     }), e.normals && s.push({
      name: Et.ATTR_NORMAL,
      type: Et.ATTR_TYPE_FLOAT32,
      num: 3
     }), e.uvs && s.push({
      name: Et.ATTR_UV0,
      type: Et.ATTR_TYPE_FLOAT32,
      num: 2
     });
     var a = new Et.VertexBuffer(t, new Et.VertexFormat(s), Et.USAGE_STATIC, new Float32Array(i), n),
      o = null;
     return e.indices && (o = new Et.IndexBuffer(t, Et.INDEX_FMT_UINT16, Et.USAGE_STATIC, new Uint16Array(e.indices), e.indices.length)), new xt(a, o)
    },
    Pass: Ct,
    Technique: Dt,
    Effect: Mt,
    InputAssembler: xt,
    View: Ot,
    Light: kt,
    Camera: jt,
    Model: Xt,
    Scene: fe,
    Base: Pe,
    ProgramLib: ge
   };
   Object.assign(Fe, Y);
   var Ne, Be = new Float32Array(16),
    ke = new Float32Array(16),
    ze = new Float32Array(16),
    Ue = (function(t) {
     function e(e, i) {
      t.call(this, e, i), this._registerStage("transparent", this._transparentStage.bind(this))
     }
     return t && (e.__proto__ = t), e.prototype = Object.create(t && t.prototype), e.prototype.constructor = e, e.prototype.reset = function() {
      this._reset()
     }, e.prototype.render = function(t) {
      this._reset(), t._cameras.sort((function(t, e) {
       return t._depth > e._depth ? 1 : t._depth < e._depth ? -1 : 0
      }));
      for (var e = 0; e < t._cameras.length; ++e) {
       var i = t._cameras.data[e];
       i._poolID = e, this.renderCamera(i, t)
      }
     }, e.prototype.renderCamera = function(t, e) {
      var i = this._device._gl.canvas,
       n = t.view,
       r = t.dirty;
      if (n || (n = this._requestView(), r = !0), r) {
       var s = i.width,
        a = i.height;
       t._framebuffer && (s = t._framebuffer._width, a = t._framebuffer._height), t.extractView(n, s, a)
      }
      this._render(n, e)
     }, e.prototype._transparentStage = function(t, e) {
      this._device.setUniform("view", B.array(Be, t._matView)), this._device.setUniform("proj", B.array(ke, t._matProj)), this._device.setUniform("viewProj", B.array(ze, t._matViewProj));
      for (var i = 0; i < e.length; ++i) {
       var n = e.data[i];
       this._draw(n)
      }
     }, e
    })(Fe.Base),
    We = {
     chunks: {},
     templates: [{
      name: "gray_sprite",
      vert: "\n \nuniform mat4 viewProj;\nattribute vec3 a_position;\nattribute mediump vec2 a_uv0;\nvarying mediump vec2 uv0;\nvoid main () {\n  vec4 pos = viewProj * vec4(a_position, 1);\n  gl_Position = pos;\n  uv0 = a_uv0;\n}",
      frag: "\n \nuniform sampler2D texture;\nvarying mediump vec2 uv0;\nuniform lowp vec4 color;\nvoid main () {\n  vec4 c = color * texture2D(texture, uv0);\n  float gray = 0.2126*c.r + 0.7152*c.g + 0.0722*c.b;\n  gl_FragColor = vec4(gray, gray, gray, c.a);\n}",
      defines: []
     }, {
      name: "sprite",
      vert: "\n \nuniform mat4 viewProj;\n#ifdef use2DPos\nattribute vec2 a_position;\n#else\nattribute vec3 a_position;\n#endif\nattribute lowp vec4 a_color;\n#ifdef useModel\n  uniform mat4 model;\n#endif\n#ifdef useTexture\n  attribute mediump vec2 a_uv0;\n  varying mediump vec2 uv0;\n#endif\n#ifndef useColor\nvarying lowp vec4 v_fragmentColor;\n#endif\nvoid main () {\n  mat4 mvp;\n  #ifdef useModel\n    mvp = viewProj * model;\n  #else\n    mvp = viewProj;\n  #endif\n  #ifdef use2DPos\n  vec4 pos = mvp * vec4(a_position, 0, 1);\n  #else\n  vec4 pos = mvp * vec4(a_position, 1);\n  #endif\n  #ifndef useColor\n  v_fragmentColor = a_color;\n  #endif\n  #ifdef useTexture\n    uv0 = a_uv0;\n  #endif\n  gl_Position = pos;\n}",
      frag: "\n \n#ifdef useTexture\n  uniform sampler2D texture;\n  varying mediump vec2 uv0;\n#endif\n#ifdef alphaTest\n  uniform lowp float alphaThreshold;\n#endif\n#ifdef useColor\n  uniform lowp vec4 color;\n#else\n  varying lowp vec4 v_fragmentColor;\n#endif\nvoid main () {\n  #ifdef useColor\n    vec4 o = color;\n  #else\n    vec4 o = v_fragmentColor;\n  #endif\n  #ifdef useTexture\n    o *= texture2D(texture, uv0);\n  #endif\n  #ifdef alphaTest\n    if (o.a <= alphaThreshold)\n      discard;\n  #endif\n  gl_FragColor = o;\n}",
      defines: [{
       name: "useTexture"
      }, {
       name: "useModel"
      }, {
       name: "alphaTest"
      }, {
       name: "use2DPos"
      }, {
       name: "useColor"
      }]
     }]
    },
    He = function() {
     this.material = null, this.vertexCount = 0, this.indiceCount = 0
    },
    Ge = new oe(function() {
     return {
      x: 0,
      y: 0,
      u: 0,
      v: 0,
      color: 0
     }
    }, 128),
    Ve = (function(t) {
     function e() {
      t.call(this), this._data = [], this._indices = [], this._pivotX = 0, this._pivotY = 0, this._width = 0, this._height = 0, this.uvDirty = !0, this.vertDirty = !0
     }
     t && (e.__proto__ = t), e.prototype = Object.create(t && t.prototype), e.prototype.constructor = e;
     var i = {
      type: {
       configurable: !0
      },
      dataLength: {
       configurable: !0
      }
     };
     return i.type.get = function() {
      return e.type
     }, i.dataLength.get = function() {
      return this._data.length
     }, i.dataLength.set = function(t) {
      var e = this._data;
      if (e.length !== t) {
       for (var i = t; i < e.length; i++) Ge.free(e[i]);
       for (var n = e.length; n < t; n++) e[n] = Ge.alloc();
       e.length = t
      }
     }, e.prototype.updateSizeNPivot = function(t, e, i, n) {
      t === this._width && e === this._height && i === this._pivotX && n === this._pivotY || (this._width = t, this._height = e, this._pivotX = i, this._pivotY = n, this.vertDirty = !0)
     }, e.alloc = function() {
      return Ne.alloc()
     }, e.free = function(t) {
      if (t instanceof e) {
       for (var i = t.length - 1; i > 0; i--) Ge.free(t._data[i]);
       t._data.length = 0, t._indices.length = 0, t.material = null, t.uvDirty = !0, t.vertDirty = !0, t.vertexCount = 0, t.indiceCount = 0, Ne.free(t)
      }
     }, Object.defineProperties(e.prototype, i), e
    })(He);
   Ve.type = "RenderData", Ne = new oe(function() {
    return new Ve
   }, 32);
   var je = (function(t) {
    function e() {
     t.call(this), this.ia = null
    }
    t && (e.__proto__ = t), e.prototype = Object.create(t && t.prototype), e.prototype.constructor = e;
    var i = {
     type: {
      configurable: !0
     }
    };
    return i.type.get = function() {
     return e.type
    }, Object.defineProperties(e.prototype, i), e
   })(He);
   je.type = "IARenderData";
   var Ye = function(t) {
    void 0 === t && (t = !0), this._loaded = !1, this._persist = t
   };
   Ye.prototype.unload = function() {
    this._loaded = !1
   }, Ye.prototype.reload = function() {};
   var Xe = (function(t) {
    function e(e) {
     void 0 === e && (e = !0), t.call(this, e), this._texture = null
    }
    return t && (e.__proto__ = t), e.prototype = Object.create(t && t.prototype), e.prototype.constructor = e, e.prototype.getImpl = function() {
     return this._texture
    }, e.prototype.getId = function() {}, e.prototype.destroy = function() {
     this._texture && this._texture.destroy()
    }, e
   })(Ye);

   function qe(t) {
    var e = t._programName + t._cullMode;
    return t._blend && (e += t._blendEq + t._blendAlphaEq + t._blendSrc + t._blendDst + t._blendSrcAlpha + t._blendDstAlpha + t._blendColor), t._depthTest && (e += t._depthWrite + t._depthFunc), t._stencilTest && (e += t._stencilFuncFront + t._stencilRefFront + t._stencilMaskFront + t._stencilFailOpFront + t._stencilZFailOpFront + t._stencilZPassOpFront + t._stencilWriteMaskFront + t._stencilFuncBack + t._stencilRefBack + t._stencilMaskBack + t._stencilFailOpBack + t._stencilZFailOpBack + t._stencilZPassOpBack + t._stencilWriteMaskBack), e
   }
   var Ze = (function(t) {
     function e(e) {
      void 0 === e && (e = !1), t.call(this, e), this._effect = null, this._texIds = {}, this._hash = ""
     }
     t && (e.__proto__ = t), e.prototype = Object.create(t && t.prototype), e.prototype.constructor = e;
     var i = {
      hash: {
       configurable: !0
      }
     };
     return i.hash.get = function() {
      return this._hash
     }, e.prototype.updateHash = function(t) {
      this._hash = t || (function(t) {
       var e, i, n, r, s, a, o = t._effect,
        c = "";
       if (o)
        for (c += (function(t) {
          for (var e = "", i = 0; i < t.length; i++) e += t[i].name + t[i].value;
          return e
         })(o._defines), e = 0; e < o._techniques.length; e++) {
         for (c += (n = o._techniques[e]).stageIDs, i = 0; i < n.passes.length; i++) c += qe(n.passes[i]);
         for (i = 0; i < n._parameters.length; i++)
          if (a = (r = n._parameters[i]).name, s = o._properties[a]) switch (r.type) {
           case Fe.PARAM_INT:
           case Fe.PARAM_FLOAT:
            c += s + ";";
            break;
           case Fe.PARAM_INT2:
           case Fe.PARAM_FLOAT2:
            c += s.x + "," + s.y + ";";
            break;
           case Fe.PARAM_INT4:
           case Fe.PARAM_FLOAT4:
            c += s.x + "," + s.y + "," + s.z + "," + s.w + ";";
            break;
           case Fe.PARAM_COLOR4:
            c += s.r + "," + s.g + "," + s.b + "," + s.a + ";";
            break;
           case Fe.PARAM_MAT2:
            c += s.m00 + "," + s.m01 + "," + s.m02 + "," + s.m03 + ";";
            break;
           case Fe.PARAM_TEXTURE_2D:
           case Fe.PARAM_TEXTURE_CUBE:
            c += t._texIds[a] + ";";
            break;
           case Fe.PARAM_INT3:
           case Fe.PARAM_FLOAT3:
           case Fe.PARAM_COLOR3:
           case Fe.PARAM_MAT3:
           case Fe.PARAM_MAT4:
            c += JSON.stringify(s) + ";"
          }
        }
       return c ? (function(t, e) {
        for (var i, n = t.length, r = e ^ n, s = 0; n >= 4;) i = 1540483477 * (65535 & (i = 255 & t.charCodeAt(s) | (255 & t.charCodeAt(++s)) << 8 | (255 & t.charCodeAt(++s)) << 16 | (255 & t.charCodeAt(++s)) << 24)) + ((1540483477 * (i >>> 16) & 65535) << 16), r = 1540483477 * (65535 & r) + ((1540483477 * (r >>> 16) & 65535) << 16) ^ (i = 1540483477 * (65535 & (i ^= i >>> 24)) + ((1540483477 * (i >>> 16) & 65535) << 16)), n -= 4, ++s;
        switch (n) {
         case 3:
          r ^= (255 & t.charCodeAt(s + 2)) << 16;
         case 2:
          r ^= (255 & t.charCodeAt(s + 1)) << 8;
         case 1:
          r = 1540483477 * (65535 & (r ^= 255 & t.charCodeAt(s))) + ((1540483477 * (r >>> 16) & 65535) << 16)
        }
        return r = 1540483477 * (65535 & (r ^= r >>> 13)) + ((1540483477 * (r >>> 16) & 65535) << 16), (r ^= r >>> 15) >>> 0
       })(c, 666) : c
      })(this)
     }, Object.defineProperties(e.prototype, i), e
    })(Ye),
    Ke = (function(t) {
     function e() {
      t.call(this, !1);
      var e = new Fe.Pass("sprite");
      e.setDepth(!1, !1), e.setCullMode(Et.CULL_NONE), e.setBlend(Et.BLEND_FUNC_ADD, Et.BLEND_SRC_ALPHA, Et.BLEND_ONE_MINUS_SRC_ALPHA, Et.BLEND_FUNC_ADD, Et.BLEND_SRC_ALPHA, Et.BLEND_ONE_MINUS_SRC_ALPHA);
      var i = new Fe.Technique(["transparent"], [{
       name: "texture",
       type: Fe.PARAM_TEXTURE_2D
      }, {
       name: "color",
       type: Fe.PARAM_COLOR4
      }], [e]);
      this._color = {
       r: 1,
       g: 1,
       b: 1,
       a: 1
      }, this._effect = new Fe.Effect([i], {
       color: this._color
      }, [{
       name: "useTexture",
       value: !0
      }, {
       name: "useModel",
       value: !1
      }, {
       name: "alphaTest",
       value: !1
      }, {
       name: "use2DPos",
       value: !0
      }, {
       name: "useColor",
       value: !0
      }]), this._mainTech = i, this._texture = null
     }
     t && (e.__proto__ = t), e.prototype = Object.create(t && t.prototype), e.prototype.constructor = e;
     var i = {
      effect: {
       configurable: !0
      },
      useTexture: {
       configurable: !0
      },
      useModel: {
       configurable: !0
      },
      use2DPos: {
       configurable: !0
      },
      useColor: {
       configurable: !0
      },
      texture: {
       configurable: !0
      },
      color: {
       configurable: !0
      }
     };
     return i.effect.get = function() {
      return this._effect
     }, i.useTexture.get = function() {
      return this._effect.getDefine("useTexture")
     }, i.useTexture.set = function(t) {
      this._effect.define("useTexture", t)
     }, i.useModel.get = function() {
      return this._effect.getDefine("useModel")
     }, i.useModel.set = function(t) {
      this._effect.define("useModel", t)
     }, i.use2DPos.get = function() {
      return this._effect.getDefine("use2DPos")
     }, i.use2DPos.set = function(t) {
      this._effect.define("use2DPos", t)
     }, i.useColor.get = function() {
      return this._effect.getDefine("useColor")
     }, i.useColor.set = function(t) {
      this._effect.define("useColor", t)
     }, i.texture.get = function() {
      return this._texture
     }, i.texture.set = function(t) {
      this._texture !== t && (this._texture = t, this._effect.setProperty("texture", t.getImpl()), this._texIds.texture = t.getId())
     }, i.color.get = function() {
      return this._color
     }, i.color.set = function(t) {
      var e = this._color;
      e.r = t.r / 255, e.g = t.g / 255, e.b = t.b / 255, e.a = t.a / 255, this._effect.setProperty("color", e)
     }, e.prototype.clone = function() {
      var t = new e;
      return t._mainTech.copy(this._mainTech), t.texture = this.texture, t.useTexture = this.useTexture, t.useModel = this.useModel, t.use2DPos = this.use2DPos, t.useColor = this.useColor, t.updateHash(), t
     }, Object.defineProperties(e.prototype, i), e
    })(Ze),
    Je = (function(t) {
     function e() {
      t.call(this, !1);
      var e = new Fe.Pass("gray_sprite");
      e.setDepth(!1, !1), e.setCullMode(Et.CULL_NONE), e.setBlend(Et.BLEND_FUNC_ADD, Et.BLEND_SRC_ALPHA, Et.BLEND_ONE_MINUS_SRC_ALPHA, Et.BLEND_FUNC_ADD, Et.BLEND_SRC_ALPHA, Et.BLEND_ONE_MINUS_SRC_ALPHA);
      var i = new Fe.Technique(["transparent"], [{
       name: "texture",
       type: Fe.PARAM_TEXTURE_2D
      }, {
       name: "color",
       type: Fe.PARAM_COLOR4
      }], [e]);
      this._color = {
       r: 1,
       g: 1,
       b: 1,
       a: 1
      }, this._effect = new Fe.Effect([i], {
       color: this._color
      }, []), this._mainTech = i, this._texture = null
     }
     t && (e.__proto__ = t), e.prototype = Object.create(t && t.prototype), e.prototype.constructor = e;
     var i = {
      effect: {
       configurable: !0
      },
      texture: {
       configurable: !0
      },
      color: {
       configurable: !0
      }
     };
     return i.effect.get = function() {
      return this._effect
     }, i.texture.get = function() {
      return this._texture
     }, i.texture.set = function(t) {
      this._texture !== t && (this._texture = t, this._effect.setProperty("texture", t.getImpl()), this._texIds.texture = t.getId())
     }, i.color.get = function() {
      return this._color
     }, i.color.set = function(t) {
      var e = this._color;
      e.r = t.r / 255, e.g = t.g / 255, e.b = t.b / 255, e.a = t.a / 255, this._effect.setProperty("color", e)
     }, e.prototype.clone = function() {
      var t = new e;
      return t._mainTech.copy(this._mainTech), t.texture = this.texture, t.color = this.color, t.updateHash(), t
     }, Object.defineProperties(e.prototype, i), e
    })(Ze),
    Qe = (function(t) {
     function e() {
      t.call(this, !1), this._pass = new Fe.Pass("sprite"), this._pass.setDepth(!1, !1), this._pass.setCullMode(Et.CULL_NONE), this._pass.setBlend(Et.BLEND_FUNC_ADD, Et.BLEND_SRC_ALPHA, Et.BLEND_ONE_MINUS_SRC_ALPHA, Et.BLEND_FUNC_ADD, Et.BLEND_SRC_ALPHA, Et.BLEND_ONE_MINUS_SRC_ALPHA);
      var e = new Fe.Technique(["transparent"], [{
       name: "texture",
       type: Fe.PARAM_TEXTURE_2D
      }, {
       name: "alphaThreshold",
       type: Fe.PARAM_FLOAT
      }, {
       name: "color",
       type: Fe.PARAM_COLOR4
      }], [this._pass]);
      this._effect = new Fe.Effect([e], {
       color: {
        r: 1,
        g: 1,
        b: 1,
        a: 1
       }
      }, [{
       name: "useTexture",
       value: !0
      }, {
       name: "useModel",
       value: !1
      }, {
       name: "alphaTest",
       value: !0
      }, {
       name: "use2DPos",
       value: !0
      }, {
       name: "useColor",
       value: !0
      }]), this._mainTech = e, this._texture = null
     }
     t && (e.__proto__ = t), e.prototype = Object.create(t && t.prototype), e.prototype.constructor = e;
     var i = {
      effect: {
       configurable: !0
      },
      useTexture: {
       configurable: !0
      },
      useModel: {
       configurable: !0
      },
      useColor: {
       configurable: !0
      },
      texture: {
       configurable: !0
      },
      alphaThreshold: {
       configurable: !0
      }
     };
     return i.effect.get = function() {
      return this._effect
     }, i.useTexture.get = function() {
      this._effect.getDefine("useTexture")
     }, i.useTexture.set = function(t) {
      this._effect.define("useTexture", t)
     }, i.useModel.get = function() {
      this._effect.getDefine("useModel")
     }, i.useModel.set = function(t) {
      this._effect.define("useModel", t)
     }, i.useColor.get = function() {
      this._effect.getDefine("useColor")
     }, i.useColor.set = function(t) {
      this._effect.define("useColor", t)
     }, i.texture.get = function() {
      return this._texture
     }, i.texture.set = function(t) {
      this._texture !== t && (this._texture = t, this._effect.setProperty("texture", t.getImpl()), this._texIds.texture = t.getId())
     }, i.alphaThreshold.get = function() {
      return this._effect.getProperty("alphaThreshold")
     }, i.alphaThreshold.set = function(t) {
      this._effect.setProperty("alphaThreshold", t)
     }, e.prototype.clone = function() {
      var t = new e;
      return t._mainTech.copy(this._mainTech), t.useTexture = this.useTexture, t.useModel = this.useModel, t.useColor = this.useColor, t.texture = this.texture, t.alphaThreshold = this.alphaThreshold, t.updateHash(), t
     }, Object.defineProperties(e.prototype, i), e
    })(Ze),
    $e = function(t) {
     var e;
     try {
      e = t.getContext("2d")
     } catch (t) {
      return void console.error(t)
     }
     this._canvas = t, this._ctx = e, this._caps = {}, this._stats = {
      drawcalls: 0
     }, this._vx = this._vy = this._vw = this._vh = 0, this._sx = this._sy = this._sw = this._sh = 0
    };
   $e.prototype._restoreTexture = function(t) {}, $e.prototype.setViewport = function(t, e, i, n) {
    this._vx === t && this._vy === e && this._vw === i && this._vh === n || (this._vx = t, this._vy = e, this._vw = i, this._vh = n)
   }, $e.prototype.setScissor = function(t, e, i, n) {
    this._sx === t && this._sy === e && this._sw === i && this._sh === n || (this._sx = t, this._sy = e, this._sw = i, this._sh = n)
   }, $e.prototype.clear = function(t) {
    var e = this._ctx;
    e.clearRect(this._vx, this._vy, this._vw, this._vh), !t || 0 === t[0] && 0 === t[1] && 0 === t[2] || (e.fillStyle = "rgb(" + t[0] + "," + t[1] + "," + t[2] + ")", e.globalAlpha = t[3], e.fillRect(this._vx, this._vy, this._vw, this._vh))
   };
   var ti = function(t, e) {
    this._device = t, this._width = 4, this._height = 4, this._image = null, e && (void 0 !== e.width && (this._width = e.width), void 0 !== e.height && (this._height = e.height), this.updateImage(e))
   };
   ti.prototype.update = function(t) {
    this.updateImage(t)
   }, ti.prototype.updateImage = function(t) {
    if (t.images && t.images[0]) {
     var e = t.images[0];
     e && e !== this._image && (this._image = e)
    }
   }, ti.prototype.destroy = function() {
    this._image = null
   };
   var ei = {
     Device: $e,
     Texture2D: ti
    },
    ii = Fe.Scene,
    ni = Fe.Camera,
    ri = Fe.View,
    si = Et.Texture2D,
    ai = Et.Device,
    oi = Fe.Model,
    ci = Fe.InputAssembler;
   Fe.addStage("transparent");
   var hi = {
    Device: ai,
    ForwardRenderer: Ue,
    Texture2D: si,
    canvas: ei,
    Scene: ii,
    Camera: ni,
    View: ri,
    Model: oi,
    RenderData: Ve,
    IARenderData: je,
    InputAssembler: ci,
    Asset: Ye,
    TextureAsset: Xe,
    Material: Ze,
    SpriteMaterial: Ke,
    GraySpriteMaterial: Je,
    StencilMaterial: Qe,
    shaders: We,
    RecyclePool: ue,
    Pool: oe,
    math: j,
    renderer: Fe,
    gfx: Et
   };
   e.exports = hi
  }), {}],
  151: [(function(t, e, i) {
   var n = 0,
    r = 1,
    s = 2,
    a = r | s,
    o = 4,
    c = 8,
    h = 16,
    u = 32,
    l = 64,
    _ = 128,
    d = 256,
    f = 512,
    p = 1024,
    m = null;

   function v() {
    this._func = x, this._next = null
   }
   var g = v.prototype;
   g._doNothing = function() {}, g._localTransform = function(t) {
    t._updateLocalMatrix(), t._renderFlag &= ~r, this._next._func(t)
   }, g._worldTransform = function(t) {
    m.worldMatDirty++;
    var e = t._matrix,
     i = t._position;
    e.m12 = i.x, e.m13 = i.y, (function(t, e, i) {
     var n = e.m00,
      r = e.m01,
      s = e.m04,
      a = e.m05,
      o = e.m12,
      c = e.m13,
      h = i.m00,
      u = i.m01,
      l = i.m04,
      _ = i.m05,
      d = i.m12,
      f = i.m13;
     0 !== u || 0 !== l ? (t.m00 = n * h + r * l, t.m01 = n * u + r * _, t.m04 = s * h + a * l, t.m05 = s * u + a * _, t.m12 = h * o + l * c + d, t.m13 = u * o + _ * c + f) : (t.m00 = n * h, t.m01 = r * _, t.m04 = s * h, t.m05 = a * _, t.m12 = h * o + d, t.m13 = _ * c + f)
    })(t._worldMatrix, e, t._parent._worldMatrix), t._renderFlag &= ~s, this._next._func(t), m.worldMatDirty--
   }, g._color = function(t) {
    var e = t._renderComponent;
    e ? e._updateColor() : t._renderFlag &= ~h, this._next._func(t)
   }, g._opacity = function(t) {
    m.parentOpacityDirty++, t._renderFlag &= ~c, this._next._func(t), m.parentOpacityDirty--
   }, g._updateRenderData = function(t) {
    var e = t._renderComponent;
    e._assembler.updateRenderData(e), t._renderFlag &= ~o, this._next._func(t)
   }, g._render = function(t) {
    var e = t._renderComponent;
    m._commitComp(e, e._assembler, t._cullingMask), this._next._func(t)
   }, g._customIARender = function(t) {
    var e = t._renderComponent;
    m._commitIA(e, e._assembler, t._cullingMask), this._next._func(t)
   }, g._children = function(t) {
    for (var e = m.parentOpacity, i = m.parentOpacity *= t._opacity / 255, n = (m.worldMatDirty ? s : 0) | (m.parentOpacityDirty ? h : 0), r = t._children, a = 0, o = r.length; a < o; a++) {
     var c = r[a];
     if (c._renderFlag |= n, c._activeInHierarchy && 0 !== c._opacity) {
      var u = c._color._val;
      c._color._fastSetA(c._opacity * i), T[c._renderFlag]._func(c), c._color._val = u
     }
    }
    m.parentOpacity = e, this._next._func(t)
   }, g._postUpdateRenderData = function(t) {
    var e = t._renderComponent;
    e._postAssembler && e._postAssembler.updateRenderData(e), t._renderFlag &= ~d, this._next._func(t)
   }, g._postRender = function(t) {
    var e = t._renderComponent;
    m._commitComp(e, e._postAssembler, t._cullingMask), this._next._func(t)
   };
   var y = new v;
   y._func = y._doNothing, y._next = y;
   var T = {};

   function E(t, e) {
    var i = new v;
    switch (i._next = e || y, t) {
     case n:
      i._func = i._doNothing;
      break;
     case r:
      i._func = i._localTransform;
      break;
     case s:
      i._func = i._worldTransform;
      break;
     case h:
      i._func = i._color;
      break;
     case c:
      i._func = i._opacity;
      break;
     case o:
      i._func = i._updateRenderData;
      break;
     case u:
      i._func = i._render;
      break;
     case l:
      i._func = i._customIARender;
      break;
     case _:
      i._func = i._children;
      break;
     case d:
      i._func = i._postUpdateRenderData;
      break;
     case f:
      i._func = i._postRender
    }
    return i
   }

   function x(t) {
    var e = t._renderFlag;
    (T[e] = (function(t) {
     for (var e = null, i = p; i > 0;) i & t && (e = E(i, e)), i >>= 1;
     return e
    })(e))._func(t)
   }
   v.flows = T, v.createFlow = E, v.render = function(t) {
    t._renderFlag & s ? (m.worldMatDirty++, t._calculWorldMatrix(), t._renderFlag &= ~s, T[t._renderFlag]._func(t), m.worldMatDirty--) : T[t._renderFlag]._func(t)
   }, v.init = function(t) {
    m = t, T[0] = y;
    for (var e = 1; e < p; e++) T[e] = new v
   }, v.FLAG_DONOTHING = n, v.FLAG_LOCAL_TRANSFORM = r, v.FLAG_WORLD_TRANSFORM = s, v.FLAG_TRANSFORM = a, v.FLAG_COLOR = h, v.FLAG_OPACITY = c, v.FLAG_UPDATE_RENDER_DATA = o, v.FLAG_RENDER = u, v.FLAG_CUSTOM_IA_RENDER = l, v.FLAG_CHILDREN = _, v.FLAG_POST_UPDATE_RENDER_DATA = d, v.FLAG_POST_RENDER = f, v.FLAG_FINAL = p, e.exports = cc.RenderFlow = v
  }), {}],
  152: [(function(t, e, i) {
   var n = t("../../../assets/CCRenderTexture"),
    r = 2;

   function s(t, e) {
    var i = new n;
    i.initWithSize(t, e), i.update(), this._texture = i, this._x = r, this._y = r, this._nexty = r, this._width = t, this._height = e, this._innerTextureInfos = {}, this._innerSpriteFrames = []
   }
   s.DEFAULT_HASH = (new n)._getHash(), cc.js.mixin(s.prototype, {
    insertSpriteFrame: function(t) {
     var e = t._rect,
      i = t._texture,
      n = this._innerTextureInfos[i._id],
      s = e.x,
      a = e.y;
     if (n) s += n.x, a += n.y;
     else {
      var o = i.width,
       c = i.height;
      if (this._x + o + r > this._width && (this._x = r, this._y = this._nexty), this._y + c > this._nexty && (this._nexty = this._y + c + r), this._nexty > this._height) return null;
      this._texture.drawTextureAt(i, this._x - 1, this._y), this._texture.drawTextureAt(i, this._x + 1, this._y), this._texture.drawTextureAt(i, this._x, this._y - 1), this._texture.drawTextureAt(i, this._x, this._y + 1), this._texture.drawTextureAt(i, this._x, this._y), this._innerTextureInfos[i._id] = {
       x: this._x,
       y: this._y,
       texture: i
      }, s += this._x, a += this._y, this._x += o + r, this._dirty = !0
     }
     var h = {
      x: s,
      y: a,
      texture: this._texture
     };
     return this._innerSpriteFrames.push(t), h
    },
    update: function() {
     this._dirty && (this._texture.update(), this._dirty = !1)
    },
    reset: function() {
     this._x = r, this._y = r, this._nexty = r;
     for (var t = this._innerSpriteFrames, e = 0, i = t.length; e < i; e++) {
      var n = t[e];
      n.isValid && n._resetDynamicAtlasFrame()
     }
     this._innerSpriteFrames.length = 0, this._innerTextureInfos = {}
    },
    destroy: function() {
     this.reset(), this._texture.destroy()
    }
   }), e.exports = s
  }), {
   "../../../assets/CCRenderTexture": 37
  }],
  153: [(function(t, e, i) {
   var n = t("./atlas"),
    r = [],
    s = -1,
    a = 5,
    o = 2048,
    c = 512;

   function h() {
    var t = r[++s];
    return t || (t = new n(o, o), r.push(t)), t
   }

   function u() {
    _.reset()
   }
   var l = !1,
    _ = {
     get enabled() {
      return l
     },
     set enabled(t) {
      l !== t && (t ? (this.reset(), cc.director.on(cc.Director.EVENT_BEFORE_SCENE_LAUNCH, u)) : cc.director.off(cc.Director.EVENT_BEFORE_SCENE_LAUNCH, u), l = t)
     },
     get maxAtlasCount() {
      return a
     },
     set maxAtlasCount(t) {
      a = t
     },
     get textureSize() {
      return o
     },
     set textureSize(t) {
      o = t
     },
     get maxFrameSize() {
      return c
     },
     set maxFrameSize(t) {
      c = t
     },
     insertSpriteFrame: function(t) {
      if (!l || s === a || !t || t._original) return null;
      var e = t._texture;
      if (e instanceof cc.RenderTexture) return null;
      var i = e.width,
       o = e.height;
      if (i > c || o > c || i <= 8 || o <= 8 || e._getHash() !== n.DEFAULT_HASH) return null;
      var u = r[s];
      u || (u = h());
      var _ = u.insertSpriteFrame(t);
      return _ || s === a ? _ : (u = h()).insertSpriteFrame(t)
     },
     reset: function() {
      for (var t = 0, e = r.length; t < e; t++) r[t].destroy();
      r.length = 0, s = -1
     },
     showDebug: !1,
     update: function() {
      if (this.enabled)
       for (var t = 0; t <= s; t++) r[t].update()
     }
    };
   e.exports = cc.dynamicAtlasManager = _
  }), {
   "./atlas": 152
  }],
  154: [(function(t, e, i) {
   var n = t("../../../platform/CCMacro"),
    r = t("../../../components/CCLabel").Overflow,
    s = t("../../../utils/text-utils"),
    a = function() {
     this._u = 0, this._v = 0, this._width = 0, this._height = 0, this._offsetX = 0, this._offsetY = 0, this._textureID = 0, this._validDefinition = !1, this._xAdvance = 0
    };
   cc.FontAtlas = function(t) {
    this._letterDefinitions = {}
   }, cc.FontAtlas.prototype = {
    constructor: cc.FontAtlas,
    addLetterDefinitions: function(t, e) {
     this._letterDefinitions[t] = e
    },
    cloneLetterDefinition: function() {
     var t = {};
     for (var e in this._letterDefinitions) {
      var i = new a;
      cc.js.mixin(i, this._letterDefinitions[e]), t[e] = i
     }
     return t
    },
    assignLetterDefinitions: function(t) {
     for (var e in this._letterDefinitions) {
      var i = t[e],
       n = this._letterDefinitions[e];
      cc.js.mixin(n, i)
     }
    },
    scaleFontLetterDefinition: function(t) {
     for (var e in this._letterDefinitions) {
      var i = this._letterDefinitions[e];
      i._width *= t, i._height *= t, i._offsetX *= t, i._offsetY *= t, i._xAdvance *= t
     }
    },
    getLetterDefinitionForChar: function(t) {
     return this._letterDefinitions.hasOwnProperty(t.charCodeAt(0)) ? this._letterDefinitions[t.charCodeAt(0)] : null
    }
   };
   var o = function() {
     this._char = "", this._valid = !0, this._positionX = 0, this._positionY = 0, this._lineIndex = 0
    },
    c = cc.rect(),
    h = null,
    u = [],
    l = [],
    _ = [],
    d = [],
    f = cc.size(),
    p = null,
    m = null,
    v = 0,
    g = 0,
    y = 0,
    T = 0,
    E = 0,
    x = 1,
    C = null,
    A = "",
    b = 0,
    S = 0,
    w = cc.size(),
    D = 0,
    R = 0,
    M = 0,
    I = 0,
    L = 0,
    O = !1,
    P = 0,
    F = 0,
    N = 0;
   e.exports = {
    updateRenderData: function(t) {
     t._renderData.vertDirty && h !== t && (h = t, this._updateProperties(), this._updateContent(), h._actualFontSize = b, h.node.setContentSize(w), h._renderData.vertDirty = h._renderData.uvDirty = !1, h = null, this._resetProperties())
    },
    _updateFontScale: function() {
     x = b / S
    },
    _updateProperties: function() {
     var t = h.font;
     if (C = t.spriteFrame, m = t._fntConfig, !(p = h._fontAtlas)) {
      p = new cc.FontAtlas(m);
      var e = m.fontDefDictionary;
      for (var i in e) {
       var n = new a,
        s = e[i].rect;
       n._offsetX = e[i].xOffset, n._offsetY = e[i].yOffset, n._width = s.width, n._height = s.height, n._u = s.x, n._v = s.y, n._textureID = 0, n._validDefinition = !0, n._xAdvance = e[i].xAdvance, p.addLetterDefinitions(i, n)
      }
      h._fontAtlas = p
     }
     A = h.string.toString(), b = h.fontSize, S = m.fontSize, w.width = h.node._contentSize.width, w.height = h.node._contentSize.height, D = h.horizontalAlign, R = h.verticalAlign, M = h.spacingX, L = h.overflow, I = h._lineHeight, O = L !== r.NONE && (L === r.RESIZE_HEIGHT || h.enableWrapText), this._setupBMFontOverflowMetrics()
    },
    _resetProperties: function() {
     p = null, m = null, C = null
    },
    _updateContent: function() {
     this._updateFontScale(), this._computeHorizontalKerningForText(), this._alignText()
    },
    _computeHorizontalKerningForText: function() {
     for (var t = A, e = t.length, i = m.kerningDict, n = u, r = -1, s = 0; s < e; ++s) {
      var a = t.charCodeAt(s),
       o = i[r << 16 | 65535 & a] || 0;
      n[s] = s < e - 1 ? o : 0, r = a
     }
    },
    _multilineTextWrap: function(t) {
     var e = A.length,
      i = 0,
      n = 0,
      r = 0,
      a = 0,
      o = 0,
      c = 0,
      h = 0,
      l = null,
      d = cc.v2(0, 0);
     this._updateFontScale();
     for (var f = p._letterDefinitions, y = 0; y < e;) {
      var C = A.charAt(y);
      if ("\n" !== C) {
       for (var b = t(A, y, e), S = c, D = h, R = o, L = n, B = !1, k = 0; k < b; ++k) {
        var z = y + k;
        if ("\r" !== (C = A.charAt(z)))
         if (l = p.getLetterDefinitionForChar(C)) {
          var U = L + l._offsetX * x;
          if (O && N > 0 && n > 0 && U + l._width * x > N && !s.isUnicodeSpace(C)) {
           _.push(o), o = 0, i++, n = 0, r -= I * x + 0, B = !0;
           break
          }
          d.x = U, d.y = r - l._offsetY * x, this._recordLetterInfo(f, d, C, z, i), z + 1 < u.length && z < e - 1 && (L += u[z + 1]), L += l._xAdvance * x + M, R = d.x + l._width * x, S < d.y && (S = d.y), D > d.y - l._height * x && (D = d.y - l._height * x)
         } else this._recordPlaceholderInfo(z, C), console.log("Can't find letter definition in texture atlas " + m.atlasName + " for letter:" + C);
        else this._recordPlaceholderInfo(z, C)
       }
       B || (n = L, o = R, c < S && (c = S), h > D && (h = D), a < o && (a = o), y += b)
      } else _.push(o), o = 0, i++, n = 0, r -= I * x + 0, this._recordPlaceholderInfo(y, C), y++
     }
     return _.push(o), g = (v = i + 1) * I * x, v > 1 && (g += 0 * (v - 1)), w.width = P, w.height = F, P <= 0 && (w.width = parseFloat(a.toFixed(2))), F <= 0 && (w.height = parseFloat(g.toFixed(2))), T = w.height, E = 0, c > 0 && (T = w.height + c), h < -g && (E = g + h), !0
    },
    _getFirstCharLen: function() {
     return 1
    },
    _getFirstWordLen: function(t, e, i) {
     var n = t.charAt(e);
     if (s.isUnicodeCJK(n) || "\n" === n || s.isUnicodeSpace(n)) return 1;
     var r = 1,
      a = p.getLetterDefinitionForChar(n);
     if (!a) return r;
     for (var o = a._xAdvance * x + M, c = e + 1; c < i && (n = t.charAt(c), a = p.getLetterDefinitionForChar(n)); ++c) {
      if (o + a._offsetX * x + a._width * x > N && !s.isUnicodeSpace(n) && N > 0) return r;
      if (o += a._xAdvance * x + M, "\n" === n || s.isUnicodeSpace(n) || s.isUnicodeCJK(n)) break;
      r++
     }
     return r
    },
    _multilineTextWrapByWord: function() {
     return this._multilineTextWrap(this._getFirstWordLen)
    },
    _multilineTextWrapByChar: function() {
     return this._multilineTextWrap(this._getFirstCharLen)
    },
    _recordPlaceholderInfo: function(t, e) {
     if (t >= l.length) {
      var i = new o;
      l.push(i)
     }
     l[t]._char = e, l[t]._valid = !1
    },
    _recordLetterInfo: function(t, e, i, n, r) {
     if (n >= l.length) {
      var s = new o;
      l.push(s)
     }
     i = i.charCodeAt(0), l[n]._lineIndex = r, l[n]._char = i, l[n]._valid = t[i]._validDefinition, l[n]._positionX = e.x, l[n]._positionY = e.y
    },
    _alignText: function() {
     g = 0, _.length = 0, this._multilineTextWrapByWord(), this._computeAlignmentOffset(), L === r.SHRINK && b > 0 && this._isVerticalClamp() && this._shrinkLabelToContentSize(this._isVerticalClamp), this._updateQuads() || L === r.SHRINK && this._shrinkLabelToContentSize(this._isHorizontalClamp)
    },
    _scaleFontSizeDown: function(t) {
     var e = !0;
     t || (t = .1, e = !1), b = t, e && this._updateContent()
    },
    _shrinkLabelToContentSize: function(t) {
     for (var e = b, i = I, n = p, r = 0, s = n.cloneLetterDefinition(), a = !0; t();) {
      var o = e - ++r;
      if (a = !1, o <= 0) break;
      var c = o / e;
      n.assignLetterDefinitions(s), n.scaleFontLetterDefinition(c), I = i * c, this._multilineTextWrapByWord(), this._computeAlignmentOffset()
     }
     I = i, n.assignLetterDefinitions(s), a || e - r >= 0 && this._scaleFontSizeDown(e - r)
    },
    _isVerticalClamp: function() {
     return g > w.height
    },
    _isHorizontalClamp: function() {
     for (var t = p._letterDefinitions, e = !1, i = 0, n = A.length; i < n; ++i) {
      var r = l[i];
      if (r._valid) {
       var s = t[r._char],
        a = r._positionX + s._width / 2 * x,
        o = r._lineIndex;
       if (P > 0)
        if (O) {
         if (_[o] > w.width && (a > w.width || a < 0)) {
          e = !0;
          break
         }
        } else if (a > w.width) {
        e = !0;
        break
       }
      }
     }
     return e
    },
    _isHorizontalClamped: function(t, e) {
     var i = _[e],
      n = t > w.width || t < 0;
     return O ? i > w.width && n : n
    },
    _updateQuads: function() {
     var t = p._letterDefinitions,
      e = C._texture,
      i = h.node,
      n = h._renderData;
     n.dataLength = n.vertexCount = n.indiceCount = 0;
     for (var s = w, a = i._anchorPoint.x * s.width, o = i._anchorPoint.y * s.height, u = !0, _ = 0, f = A.length; _ < f; ++_) {
      var m = l[_];
      if (m._valid) {
       var v = t[m._char];
       c.height = v._height, c.width = v._width, c.x = v._u, c.y = v._v;
       var g = m._positionY + y;
       if (F > 0) {
        if (g > T) {
         var b = g - T;
         c.y += b, c.height -= b, g -= b
        }
        g - v._height * x < E && (c.height = g < E ? 0 : g - E)
       }
       var S = m._lineIndex,
        D = m._positionX + v._width / 2 * x + d[S];
       if (P > 0 && this._isHorizontalClamped(D, S))
        if (L === r.CLAMP) c.width = 0;
        else if (L === r.SHRINK) {
        if (w.width > v._width) {
         u = !1;
         break
        }
        c.width = 0
       }
       if (c.height > 0 && c.width > 0) {
        var R = C.isRotated(),
         M = C._originalSize,
         I = C._rect,
         O = C._offset,
         N = O.x + (M.width - I.width) / 2,
         B = O.y - (M.height - I.height) / 2;
        if (R) {
         var k = c.x;
         c.x = I.x + I.height - c.y - c.height - B, c.y = k + I.y - N, c.y < 0 && (c.height = c.height + B)
        } else c.x += I.x - N, c.y += I.y + B;
        var z = m._positionX + d[m._lineIndex];
        this.appendQuad(n, e, c, R, z - a, g - o, x)
       }
      }
     }
     return u
    },
    appendQuad: function(t, e, i, n, r, s, a) {},
    _computeAlignmentOffset: function() {
     switch (d.length = 0, D) {
      case n.TextAlignment.LEFT:
       for (var t = 0; t < v; ++t) d.push(0);
       break;
      case n.TextAlignment.CENTER:
       for (var e = 0, i = _.length; e < i; e++) d.push((w.width - _[e]) / 2);
       break;
      case n.TextAlignment.RIGHT:
       for (var r = 0, s = _.length; r < s; r++) d.push(w.width - _[r])
     }
     switch (R) {
      case n.VerticalTextAlignment.TOP:
       y = w.height;
       break;
      case n.VerticalTextAlignment.CENTER:
       y = (w.height + g) / 2;
       break;
      case n.VerticalTextAlignment.BOTTOM:
       y = g
     }
    },
    _setupBMFontOverflowMetrics: function() {
     var t = w.width,
      e = w.height;
     L === r.RESIZE_HEIGHT && (e = 0), L === r.NONE && (t = 0, e = 0), P = t, F = e, f.width = t, f.height = e, N = t
    }
   }
  }), {
   "../../../components/CCLabel": 56,
   "../../../platform/CCMacro": 113,
   "../../../utils/text-utils": 193
  }],
  155: [(function(t, e, i) {
   function n() {
    this._rect = null, this.uv = [], this._texture = null, this._original = null
   }
   n.prototype = {
    constructor: n,
    getRect: function() {
     return cc.rect(this._rect)
    },
    setRect: function(t) {
     this._rect = t, this._texture && this._calculateUV()
    },
    _setDynamicAtlasFrame: function(t) {
     t && (this._original = {
      _texture: this._texture,
      _x: this._rect.x,
      _y: this._rect.y
     }, this._texture = t.texture, this._rect.x = t.x, this._rect.y = t.y, this._calculateUV())
    },
    _resetDynamicAtlasFrame: function() {
     this._original && (this._rect.x = this._original._x, this._rect.y = this._original._y, this._texture = this._original._texture, this._original = null, this._calculateUV())
    },
    _refreshTexture: function(t) {
     this._texture = t, this._rect = cc.rect(0, 0, t.width, t.height), this._calculateUV()
    },
    _calculateUV: function() {
     var t = this._rect,
      e = this._texture,
      i = this.uv,
      n = e.width,
      r = e.height,
      s = 0 === n ? 0 : t.x / n,
      a = 0 === n ? 0 : (t.x + t.width) / n,
      o = 0 === r ? 0 : (t.y + t.height) / r,
      c = 0 === r ? 0 : t.y / r;
     i[0] = s, i[1] = o, i[2] = a, i[3] = o, i[4] = s, i[5] = c, i[6] = a, i[7] = c
    }
   }, e.exports = n
  }), {}],
  156: [(function(t, e, i) {
   var n = t("../../../platform/CCMacro"),
    r = t("../../../utils/text-utils"),
    s = t("../../../components/CCComponent"),
    a = t("../../../components/CCLabel"),
    o = t("../../../components/CCLabelOutline"),
    c = a.Overflow,
    h = cc.Color.WHITE,
    u = cc.js.isChildClassOf(o, s),
    l = null,
    _ = null,
    d = null,
    f = "",
    p = "",
    m = 0,
    v = 0,
    g = [],
    y = cc.size(),
    T = 0,
    E = 0,
    x = 0,
    C = null,
    A = "",
    b = c.NONE,
    S = !1,
    w = !1,
    D = null,
    R = 0,
    M = 0,
    I = !1,
    L = !1,
    O = !1,
    P = void 0,
    F = {
     pool: [],
     get: function() {
      var t = this.pool.pop();
      if (!t) {
       var e = document.createElement("canvas");
       t = {
        canvas: e,
        context: e.getContext("2d")
       }
      }
      return t
     },
     put: function(t) {
      this.pool.length >= 32 || this.pool.push(t)
     }
    };
   e.exports = {
    _getAssemblerData: function() {
     if (cc.game.renderType === cc.game.RENDER_TYPE_CANVAS) P = F.get();
     else if (!P) {
      var t = document.createElement("canvas");
      P = {
       canvas: t,
       context: t.getContext("2d")
      }
     }
     return P.canvas.width = P.canvas.height = 1, P
    },
    _resetAssemblerData: function(t) {
     cc.game.renderType === cc.game.RENDER_TYPE_CANVAS && t && F.put(t)
    },
    updateRenderData: function(t) {
     t._renderData.vertDirty && (this._updateFontFamily(t), this._updateProperties(t), this._calculateLabelFont(), this._calculateSplitedStrings(), this._updateLabelDimensions(), this._calculateTextBaseline(), this._updateTexture(t), this._calDynamicAtlas(t), t._actualFontSize = m, t.node.setContentSize(y), this._updateVerts(t), t._renderData.vertDirty = t._renderData.uvDirty = !1, l = null, _ = null, d = null)
    },
    _updateVerts: function() {},
    _updateFontFamily: function(t) {
     t.useSystemFont ? A = t.fontFamily : t.font ? t.font._nativeAsset ? A = t.font._nativeAsset : (A = cc.loader.getRes(t.font.nativeUrl)) || cc.loader.load(t.font.nativeUrl, (function(e, i) {
      A = i || "Arial", t.font._nativeAsset = i, t._updateRenderData(!0)
     })) : A = "Arial"
    },
    _updateProperties: function(t) {
     var e = t._assemblerData;
     l = e.context, _ = e.canvas, d = t._frame._original ? t._frame._original._texture : t._frame._texture, p = t.string.toString(), m = t._fontSize, v = m, b = t.overflow, y.width = t.node.width, y.height = t.node.height, T = t._lineHeight, E = t.horizontalAlign, x = t.verticalAlign, C = t.node.color, I = t._isBold, L = t._isItalic, O = t._isUnderline, S = b !== c.NONE && (b === c.RESIZE_HEIGHT || t.enableWrapText);
     var i = u && t.getComponent(o);
     i && i.enabled ? (w = !0, M = R = i.width, (D = cc.color(i.color)).a = D.a * t.node.color.a / 255) : (w = !1, M = 0)
    },
    _calculateFillTextStartPosition: function() {
     var t = this._getLineHeight(),
      e = g.length,
      i = void 0,
      r = void 0;
     return i = E === n.TextAlignment.RIGHT ? y.width - M : E === n.TextAlignment.CENTER ? y.width / 2 : 0 + M, r = x === n.VerticalTextAlignment.TOP ? 0 : x === n.VerticalTextAlignment.CENTER ? y.height / 2 - t * (e - 1) / 2 : y.height - t * (e - 1), cc.v2(i, r)
    },
    _updateTexture: function(t) {
     l.clearRect(0, 0, _.width, _.height), l.font = f;
     var e = this._calculateFillTextStartPosition(),
      i = this._getLineHeight();
     l.lineJoin = "round", l.fillStyle = "rgba(" + C.r + ", " + C.g + ", " + C.b + ", " + C.a / 255 + ")";
     for (var n = void 0, r = 0; r < g.length; ++r) {
      if (w) {
       var s = D || h;
       l.strokeStyle = "rgba(" + s.r + ", " + s.g + ", " + s.b + ", " + s.a / 255 + ")", l.lineWidth = 2 * R, l.strokeText(g[r], e.x, e.y + r * i)
      }
      l.fillText(g[r], e.x, e.y + r * i), O && (n = this._calculateUnderlineStartPosition(), l.save(), l.beginPath(), l.lineWidth = m / 8, l.strokeStyle = "rgba(" + C.r + ", " + C.g + ", " + C.b + ", " + C.a / 255 + ")", l.moveTo(n.x, n.y + r * i - 1), l.lineTo(n.x + _.width, n.y + r * i - 1), l.stroke(), l.restore())
     }
     d.handleLoadedTexture()
    },
    _calDynamicAtlas: function(t) {
     t.batchAsBitmap && (t._frame._original || t._frame.setRect(cc.rect(0, 0, _.width, _.height)), t._calDynamicAtlas())
    },
    _calculateUnderlineStartPosition: function() {
     var t, e = this._getLineHeight(),
      i = g.length,
      r = void 0;
     return t = 0 + M, r = x === n.VerticalTextAlignment.TOP ? m : x === n.VerticalTextAlignment.CENTER ? y.height / 2 - e * (i - 1) / 2 + m / 2 : y.height - e * (i - 1), cc.v2(t, r)
    },
    _updateLabelDimensions: function() {
     var t = p.split("\n");
     if (b === c.RESIZE_HEIGHT) y.height = g.length * this._getLineHeight();
     else if (b === c.NONE) {
      g = t;
      for (var e = 0, i = 0, n = 0; n < t.length; ++n) {
       var s = r.safeMeasureText(l, t[n]);
       e = e > s ? e : s
      }
      i = g.length * this._getLineHeight(), y.width = parseFloat(e.toFixed(2)) + 2 * M, y.height = parseFloat(i.toFixed(2)), L && (y.width += v * Math.tan(.20943951))
     }
     _.width = y.width, _.height = y.height
    },
    _calculateTextBaseline: function() {
     this._node;
     var t = void 0,
      e = void 0;
     t = E === n.TextAlignment.RIGHT ? "right" : E === n.TextAlignment.CENTER ? "center" : "left", l.textAlign = t, e = x === n.VerticalTextAlignment.TOP ? "top" : x === n.VerticalTextAlignment.CENTER ? "middle" : "bottom", l.textBaseline = e
    },
    _calculateSplitedStrings: function() {
     var t = p.split("\n");
     if (S) {
      g = [];
      for (var e = y.width - 2 * M, i = 0; i < t.length; ++i) {
       var n = r.safeMeasureText(l, t[i]),
        s = r.fragmentText(t[i], n, e, this._measureText(l));
       g = g.concat(s)
      }
     } else g = t
    },
    _getFontDesc: function() {
     var t = m.toString() + "px ";
     return t += A, I && (t = "bold " + t), t
    },
    _getLineHeight: function() {
     var t = T;
     return 0 | (t = 0 === t ? m : t * m / v)
    },
    _calculateParagraphLength: function(t, e) {
     for (var i = [], n = 0; n < t.length; ++n) {
      var s = r.safeMeasureText(e, t[n]);
      i.push(s)
     }
     return i
    },
    _measureText: function(t) {
     return function(e) {
      return r.safeMeasureText(t, e)
     }
    },
    _calculateLabelFont: function() {
     if (f = this._getFontDesc(), l.font = f, b === c.SHRINK) {
      var t = p.split("\n"),
       e = this._calculateParagraphLength(t, l),
       i = 0,
       n = 0,
       s = 0;
      if (S) {
       var a = y.width - 2 * M,
        o = y.height - 2 * M;
       if (a < 0 || o < 0) return f = this._getFontDesc(), void(l.font = f);
       n = o + 1, s = a + 1;
       for (var h = m + 1, u = "", _ = !0, d = 0 | h; n > o || s > a;) {
        if (_ ? h = d / 2 | 0 : d = h = d - 1, h <= 0) {
         cc.logID(4003);
         break
        }
        for (m = h, f = this._getFontDesc(), l.font = f, n = 0, i = 0; i < t.length; ++i) {
         var g = 0,
          T = r.safeMeasureText(l, t[i]);
         for (u = r.fragmentText(t[i], T, a, this._measureText(l)); g < u.length;) {
          s = r.safeMeasureText(l, u[g]), n += this._getLineHeight(), ++g
         }
        }
        _ && (n > o ? d = 0 | h : (_ = !1, n = o + 1))
       }
      } else {
       for (n = t.length * this._getLineHeight(), i = 0; i < t.length; ++i) s < e[i] && (s = e[i]);
       var E = (y.width - 2 * M) / s,
        x = y.height / n;
       m = v * Math.min(1, E, x) | 0, f = this._getFontDesc(), l.font = f
      }
     }
    }
   }
  }), {
   "../../../components/CCComponent": 54,
   "../../../components/CCLabel": 56,
   "../../../components/CCLabelOutline": 57,
   "../../../platform/CCMacro": 113,
   "../../../utils/text-utils": 193
  }],
  157: [(function(t, e, i) {
   "use strict";

   function n(t, e, i) {
    i = i || 2;
    var n, o, c, h, u, d, p, m = e && e.length,
     v = m ? e[0] * i : t.length,
     g = r(t, 0, v, i, !0),
     y = [];
    if (!g) return y;
    if (m && (g = (function(t, e, i, n) {
      var a, o, c, h, u, d = [];
      for (a = 0, o = e.length; a < o; a++) c = e[a] * n, h = a < o - 1 ? e[a + 1] * n : t.length, (u = r(t, c, h, n, !1)) === u.next && (u.steiner = !0), d.push(f(u));
      for (d.sort(l), a = 0; a < d.length; a++) _(d[a], i), i = s(i, i.next);
      return i
     })(t, e, g, i)), t.length > 80 * i) {
     n = c = t[0], o = h = t[1];
     for (var T = i; T < v; T += i) u = t[T], d = t[T + 1], u < n && (n = u), d < o && (o = d), u > c && (c = u), d > h && (h = d);
     p = Math.max(c - n, h - o)
    }
    return a(g, y, i, n, o, p), y
   }

   function r(t, e, i, n, r) {
    var s, a;
    if (r === b(t, e, i, n) > 0)
     for (s = e; s < i; s += n) a = x(s, t[s], t[s + 1], a);
    else
     for (s = i - n; s >= e; s -= n) a = x(s, t[s], t[s + 1], a);
    return a && g(a, a.next) && (C(a), a = a.next), a
   }

   function s(t, e) {
    if (!t) return t;
    e || (e = t);
    var i, n = t;
    do {
     if (i = !1, n.steiner || !g(n, n.next) && 0 !== v(n.prev, n, n.next)) n = n.next;
     else {
      if (C(n), (n = e = n.prev) === n.next) return null;
      i = !0
     }
    } while (i || n !== e);
    return e
   }

   function a(t, e, i, n, r, l, _) {
    if (t) {
     !_ && l && (function(t, e, i, n) {
      var r = t;
      do {
       null === r.z && (r.z = d(r.x, r.y, e, i, n)), r.prevZ = r.prev, r.nextZ = r.next, r = r.next
      } while (r !== t);
      r.prevZ.nextZ = null, r.prevZ = null, (function(t) {
       var e, i, n, r, s, a, o, c, h = 1;
       do {
        for (i = t, t = null, s = null, a = 0; i;) {
         for (a++, n = i, o = 0, e = 0; e < h && (o++, n = n.nextZ); e++);
         for (c = h; o > 0 || c > 0 && n;) 0 === o ? (r = n, n = n.nextZ, c--) : 0 !== c && n ? i.z <= n.z ? (r = i, i = i.nextZ, o--) : (r = n, n = n.nextZ, c--) : (r = i, i = i.nextZ, o--), s ? s.nextZ = r : t = r, r.prevZ = s, s = r;
         i = n
        }
        s.nextZ = null, h *= 2
       } while (a > 1)
      })(r)
     })(t, n, r, l);
     for (var f, p, m = t; t.prev !== t.next;)
      if (f = t.prev, p = t.next, l ? c(t, n, r, l) : o(t)) e.push(f.i / i), e.push(t.i / i), e.push(p.i / i), C(t), t = p.next, m = p.next;
      else if ((t = p) === m) {
      _ ? 1 === _ ? a(t = h(t, e, i), e, i, n, r, l, 2) : 2 === _ && u(t, e, i, n, r, l) : a(s(t), e, i, n, r, l, 1);
      break
     }
    }
   }

   function o(t) {
    var e = t.prev,
     i = t,
     n = t.next;
    if (v(e, i, n) >= 0) return !1;
    for (var r = t.next.next; r !== t.prev;) {
     if (p(e.x, e.y, i.x, i.y, n.x, n.y, r.x, r.y) && v(r.prev, r, r.next) >= 0) return !1;
     r = r.next
    }
    return !0
   }

   function c(t, e, i, n) {
    var r = t.prev,
     s = t,
     a = t.next;
    if (v(r, s, a) >= 0) return !1;
    for (var o = r.x < s.x ? r.x < a.x ? r.x : a.x : s.x < a.x ? s.x : a.x, c = r.y < s.y ? r.y < a.y ? r.y : a.y : s.y < a.y ? s.y : a.y, h = r.x > s.x ? r.x > a.x ? r.x : a.x : s.x > a.x ? s.x : a.x, u = r.y > s.y ? r.y > a.y ? r.y : a.y : s.y > a.y ? s.y : a.y, l = d(o, c, e, i, n), _ = d(h, u, e, i, n), f = t.nextZ; f && f.z <= _;) {
     if (f !== t.prev && f !== t.next && p(r.x, r.y, s.x, s.y, a.x, a.y, f.x, f.y) && v(f.prev, f, f.next) >= 0) return !1;
     f = f.nextZ
    }
    for (f = t.prevZ; f && f.z >= l;) {
     if (f !== t.prev && f !== t.next && p(r.x, r.y, s.x, s.y, a.x, a.y, f.x, f.y) && v(f.prev, f, f.next) >= 0) return !1;
     f = f.prevZ
    }
    return !0
   }

   function h(t, e, i) {
    var n = t;
    do {
     var r = n.prev,
      s = n.next.next;
     !g(r, s) && y(r, n, n.next, s) && T(r, s) && T(s, r) && (e.push(r.i / i), e.push(n.i / i), e.push(s.i / i), C(n), C(n.next), n = t = s), n = n.next
    } while (n !== t);
    return n
   }

   function u(t, e, i, n, r, o) {
    var c = t;
    do {
     for (var h = c.next.next; h !== c.prev;) {
      if (c.i !== h.i && m(c, h)) {
       var u = E(c, h);
       return c = s(c, c.next), u = s(u, u.next), a(c, e, i, n, r, o), void a(u, e, i, n, r, o)
      }
      h = h.next
     }
     c = c.next
    } while (c !== t)
   }

   function l(t, e) {
    return t.x - e.x
   }

   function _(t, e) {
    if (e = (function(t, e) {
      var i, n = e,
       r = t.x,
       s = t.y,
       a = -1 / 0;
      do {
       if (s <= n.y && s >= n.next.y) {
        var o = n.x + (s - n.y) * (n.next.x - n.x) / (n.next.y - n.y);
        if (o <= r && o > a) {
         if (a = o, o === r) {
          if (s === n.y) return n;
          if (s === n.next.y) return n.next
         }
         i = n.x < n.next.x ? n : n.next
        }
       }
       n = n.next
      } while (n !== e);
      if (!i) return null;
      if (r === a) return i.prev;
      var c, h = i,
       u = i.x,
       l = i.y,
       _ = 1 / 0;
      n = i.next;
      for (; n !== h;) r >= n.x && n.x >= u && p(s < l ? r : a, s, u, l, s < l ? a : r, s, n.x, n.y) && ((c = Math.abs(s - n.y) / (r - n.x)) < _ || c === _ && n.x > i.x) && T(n, t) && (i = n, _ = c), n = n.next;
      return i
     })(t, e)) {
     var i = E(e, t);
     s(i, i.next)
    }
   }

   function d(t, e, i, n, r) {
    return (t = 1431655765 & ((t = 858993459 & ((t = 252645135 & ((t = 16711935 & ((t = 32767 * (t - i) / r) | t << 8)) | t << 4)) | t << 2)) | t << 1)) | (e = 1431655765 & ((e = 858993459 & ((e = 252645135 & ((e = 16711935 & ((e = 32767 * (e - n) / r) | e << 8)) | e << 4)) | e << 2)) | e << 1)) << 1
   }

   function f(t) {
    var e = t,
     i = t;
    do {
     e.x < i.x && (i = e), e = e.next
    } while (e !== t);
    return i
   }

   function p(t, e, i, n, r, s, a, o) {
    return (r - a) * (e - o) - (t - a) * (s - o) >= 0 && (t - a) * (n - o) - (i - a) * (e - o) >= 0 && (i - a) * (s - o) - (r - a) * (n - o) >= 0
   }

   function m(t, e) {
    return t.next.i !== e.i && t.prev.i !== e.i && !(function(t, e) {
     var i = t;
     do {
      if (i.i !== t.i && i.next.i !== t.i && i.i !== e.i && i.next.i !== e.i && y(i, i.next, t, e)) return !0;
      i = i.next
     } while (i !== t);
     return !1
    })(t, e) && T(t, e) && T(e, t) && (function(t, e) {
     var i = t,
      n = !1,
      r = (t.x + e.x) / 2,
      s = (t.y + e.y) / 2;
     do {
      i.y > s != i.next.y > s && r < (i.next.x - i.x) * (s - i.y) / (i.next.y - i.y) + i.x && (n = !n), i = i.next
     } while (i !== t);
     return n
    })(t, e)
   }

   function v(t, e, i) {
    return (e.y - t.y) * (i.x - e.x) - (e.x - t.x) * (i.y - e.y)
   }

   function g(t, e) {
    return t.x === e.x && t.y === e.y
   }

   function y(t, e, i, n) {
    return !!(g(t, e) && g(i, n) || g(t, n) && g(i, e)) || v(t, e, i) > 0 != v(t, e, n) > 0 && v(i, n, t) > 0 != v(i, n, e) > 0
   }

   function T(t, e) {
    return v(t.prev, t, t.next) < 0 ? v(t, e, t.next) >= 0 && v(t, t.prev, e) >= 0 : v(t, e, t.prev) < 0 || v(t, t.next, e) < 0
   }

   function E(t, e) {
    var i = new A(t.i, t.x, t.y),
     n = new A(e.i, e.x, e.y),
     r = t.next,
     s = e.prev;
    return t.next = e, e.prev = t, i.next = r, r.prev = i, n.next = i, i.prev = n, s.next = n, n.prev = s, n
   }

   function x(t, e, i, n) {
    var r = new A(t, e, i);
    return n ? (r.next = n.next, r.prev = n, n.next.prev = r, n.next = r) : (r.prev = r, r.next = r), r
   }

   function C(t) {
    t.next.prev = t.prev, t.prev.next = t.next, t.prevZ && (t.prevZ.nextZ = t.nextZ), t.nextZ && (t.nextZ.prevZ = t.prevZ)
   }

   function A(t, e, i) {
    this.i = t, this.x = e, this.y = i, this.prev = null, this.next = null, this.z = null, this.prevZ = null, this.nextZ = null, this.steiner = !1
   }

   function b(t, e, i, n) {
    for (var r = 0, s = e, a = i - n; s < i; s += n) r += (t[a] - t[s]) * (t[s + 1] + t[a + 1]), a = s;
    return r
   }
   e.exports = n, n.deviation = function(t, e, i, n) {
    var r = e && e.length,
     s = r ? e[0] * i : t.length,
     a = Math.abs(b(t, 0, s, i));
    if (r)
     for (var o = 0, c = e.length; o < c; o++) {
      var h = e[o] * i,
       u = o < c - 1 ? e[o + 1] * i : t.length;
      a -= Math.abs(b(t, h, u, i))
     }
    var l = 0;
    for (o = 0; o < n.length; o += 3) {
     var _ = n[o] * i,
      d = n[o + 1] * i,
      f = n[o + 2] * i;
     l += Math.abs((t[_] - t[f]) * (t[d + 1] - t[_ + 1]) - (t[_] - t[d]) * (t[f + 1] - t[_ + 1]))
    }
    return 0 === a && 0 === l ? 0 : Math.abs((l - a) / a)
   }, n.flatten = function(t) {
    for (var e = t[0][0].length, i = {
      vertices: [],
      holes: [],
      dimensions: e
     }, n = 0, r = 0; r < t.length; r++) {
     for (var s = 0; s < t[r].length; s++)
      for (var a = 0; a < e; a++) i.vertices.push(t[r][s][a]);
     r > 0 && (n += t[r - 1].length, i.holes.push(n))
    }
    return i
   }
  }), {}],
  158: [(function(t, e, i) {
   var n = t("../../../../graphics/helper"),
    r = t("../../../../graphics/types").PointFlags,
    s = t("../../mesh-buffer"),
    a = t("../../vertex-format").vfmtPosColor,
    o = t("../../../index"),
    c = o.renderEngine,
    h = c.IARenderData,
    u = c.InputAssembler,
    l = cc.Class({
     name: "cc.GraphicsPoint",
     extends: cc.Vec2,
     ctor: function(t, e) {
      this.reset()
     },
     reset: function() {
      this.dx = 0, this.dy = 0, this.dmx = 0, this.dmy = 0, this.flags = 0, this.len = 0
     }
    });

   function _() {
    this.reset()
   }

   function d(t) {
    this._tessTol = .25, this._distTol = .01, this._updatePathOffset = !1, this._paths = null, this._pathLength = 0, this._pathOffset = 0, this._points = null, this._pointsOffset = 0, this._commandx = 0, this._commandy = 0, this._paths = [], this._points = [], this._renderDatas = [], this._dataOffset = 0
   }
   cc.js.mixin(_.prototype, {
    reset: function() {
     this.closed = !1, this.nbevel = 0, this.complex = !0, this.points ? this.points.length = 0 : this.points = []
    }
   }), cc.js.mixin(d.prototype, {
    moveTo: function(t, e) {
     this._updatePathOffset && (this._pathOffset = this._pathLength, this._updatePathOffset = !1), this._addPath(), this._addPoint(t, e, r.PT_CORNER), this._commandx = t, this._commandy = e
    },
    lineTo: function(t, e) {
     this._addPoint(t, e, r.PT_CORNER), this._commandx = t, this._commandy = e
    },
    bezierCurveTo: function(t, e, i, s, a, o) {
     var c = this._curPath,
      h = c.points[c.points.length - 1];
     h.x !== t || h.y !== e || i !== a || s !== o ? (n.tesselateBezier(this, h.x, h.y, t, e, i, s, a, o, 0, r.PT_CORNER), this._commandx = a, this._commandy = o) : this.lineTo(a, o)
    },
    quadraticCurveTo: function(t, e, i, n) {
     var r = this._commandx,
      s = this._commandy;
     this.bezierCurveTo(r + 2 / 3 * (t - r), s + 2 / 3 * (e - s), i + 2 / 3 * (t - i), n + 2 / 3 * (e - n), i, n)
    },
    arc: function(t, e, i, r, s, a) {
     n.arc(this, t, e, i, r, s, a)
    },
    ellipse: function(t, e, i, r) {
     n.ellipse(this, t, e, i, r), this._curPath.complex = !1
    },
    circle: function(t, e, i) {
     n.ellipse(this, t, e, i, i), this._curPath.complex = !1
    },
    rect: function(t, e, i, n) {
     this.moveTo(t, e), this.lineTo(t, e + n), this.lineTo(t + i, e + n), this.lineTo(t + i, e), this.close(), this._curPath.complex = !1
    },
    roundRect: function(t, e, i, r, s) {
     n.roundRect(this, t, e, i, r, s), this._curPath.complex = !1
    },
    clear: function(t, e) {
     this._pathLength = 0, this._pathOffset = 0, this._pointsOffset = 0, this._dataOffset = 0, this._curPath = null;
     var i = this._renderDatas;
     if (e) {
      this._paths.length = 0, this._points.length = 0;
      for (var n = 0, r = i.length; n < r; n++) {
       var s = i[n];
       s.meshbuffer.destroy(), s.meshbuffer = null
      }
      i.length = 0
     } else
      for (var a = 0, o = i.length; a < o; a++) {
       i[a].meshbuffer.reset()
      }
    },
    close: function() {
     this._curPath.closed = !0
    },
    _addPath: function() {
     var t = this._pathLength,
      e = this._paths[t];
     return e ? e.reset() : (e = new _, this._paths.push(e)), this._pathLength++, this._curPath = e, e
    },
    _addPoint: function(t, e, i) {
     var n = this._curPath;
     if (n) {
      var r, s = this._points,
       a = n.points;
      (r = s[this._pointsOffset++]) ? (r.x = t, r.y = e) : (r = new l(t, e), s.push(r)), r.flags = i, a.push(r)
     }
    },
    requestRenderData: function() {
     var t = new h,
      e = new s(o._walker, a);
     t.meshbuffer = e, this._renderDatas.push(t);
     var i = new u;
     return i._vertexBuffer = e._vb, i._indexBuffer = e._ib, i._start = 0, t.ia = i, t
    },
    getRenderDatas: function() {
     return 0 === this._renderDatas.length && this.requestRenderData(), this._renderDatas
    }
   }), e.exports = d
  }), {
   "../../../../graphics/helper": 82,
   "../../../../graphics/types": 84,
   "../../../index": 149,
   "../../mesh-buffer": 173,
   "../../vertex-format": 177
  }],
  159: [(function(t, e, i) {
   var n = t("../../../../graphics/graphics"),
    r = t("../../../../graphics/types").PointFlags,
    s = n.LineJoin,
    a = n.LineCap,
    o = t("./earcut"),
    c = t("./impl"),
    h = Math.PI,
    u = Math.min,
    l = Math.max,
    _ = Math.ceil,
    d = Math.acos,
    f = Math.cos,
    p = Math.sin,
    m = Math.atan2,
    v = (Math.abs, null),
    g = null,
    y = 0;

   function T(t, e, i) {
    return t < e ? e : t > i ? i : t
   }
   var E = {
    useModel: !0,
    createImpl: function(t) {
     return new c(t)
    },
    updateRenderData: function(t) {
     for (var e = t._impl.getRenderDatas(), i = 0, n = e.length; i < n; i++) e[i].material = t.getMaterial()
    },
    fillBuffers: function(t, e) {
     e._flush();
     var i = e.node;
     e.node = t.node, this.renderIA(t, e), e.node = i
    },
    renderIA: function(t, e) {
     for (var i = t.node.color, n = (i.r, i.g, i.b, i.a, t._impl.getRenderDatas()), r = 0, s = n.length; r < s; r++) {
      var a = n[r],
       o = a.meshbuffer;
      a.ia._count = o.indiceStart, e._flushIA(a), o.uploadData()
     }
    },
    genRenderData: function(t, e) {
     var i = g.getRenderDatas(),
      n = i[g._dataOffset],
      r = n.meshbuffer,
      s = r.vertexStart + e;
     return (s > 65535 || 3 * s > 131070) && (++g._dataOffset, s = e, g._dataOffset < i.length ? n = i[g._dataOffset] : (n = g.requestRenderData(t), i[g._dataOffset] = n), n.material = t.getMaterial(), r = n.meshbuffer), s > r.vertexOffset && r.requestStatic(e, 3 * e), n
    },
    stroke: function(t) {
     y = t._strokeColor._val, this._flattenPaths(t._impl), this._expandStroke(t), t._impl._updatePathOffset = !0
    },
    fill: function(t) {
     y = t._fillColor._val, this._expandFill(t), t._impl._updatePathOffset = !0
    },
    _expandStroke: function(t) {
     var e = .5 * t.lineWidth,
      i = t.lineCap,
      n = t.lineJoin,
      o = t.miterLimit;
     g = t._impl;
     var c = (function(t, e, i) {
      var n = 2 * d(t / (t + i));
      return l(2, _(e / n))
     })(e, h, g._tessTol);
     this._calculateJoins(g, e, n, o);
     for (var u = g._paths, f = 0, p = g._pathOffset, m = g._pathLength; p < m; p++) {
      var y = u[p],
       T = y.points.length;
      n === s.ROUND ? f += 2 * (T + y.nbevel * (c + 2) + 1) : f += 2 * (T + 5 * y.nbevel + 1), y.closed || (i === a.ROUND ? f += 2 * (2 * c + 2) : f += 12)
     }
     for (var E = (v = this.genRenderData(t, f)).meshbuffer, x = E._vData, C = E._iData, A = g._pathOffset, b = g._pathLength; A < b; A++) {
      var S, w = u[A],
       D = w.points,
       R = D.length,
       M = E.vertexStart,
       I = void 0,
       L = void 0,
       O = void 0,
       P = void 0;
      if ((S = w.closed) ? (I = D[R - 1], L = D[0], O = 0, P = R) : (I = D[0], L = D[1], O = 1, P = R - 1), !S) {
       var F = L.sub(I);
       F.normalizeSelf();
       var N = F.x,
        B = F.y;
       i === a.BUTT ? this._buttCap(I, N, B, e, 0) : i === a.SQUARE ? this._buttCap(I, N, B, e, e) : i === a.ROUND && this._roundCapStart(I, N, B, e, c)
      }
      for (var k = O; k < P; ++k) n === s.ROUND ? this._roundJoin(I, L, e, e, c) : 0 != (L.flags & (r.PT_BEVEL | r.PT_INNERBEVEL)) ? this._bevelJoin(I, L, e, e) : (this._vset(L.x + L.dmx * e, L.y + L.dmy * e), this._vset(L.x - L.dmx * e, L.y - L.dmy * e)), I = L, L = D[k + 1];
      if (S) {
       var z = 3 * M;
       this._vset(x[z], x[z + 1]), this._vset(x[z + 3], x[z + 4])
      } else {
       var U = L.sub(I);
       U.normalizeSelf();
       var W = U.x,
        H = U.y;
       i === a.BUTT ? this._buttCap(L, W, H, e, 0) : i === a.BUTT || i === a.SQUARE ? this._buttCap(L, W, H, e, e) : i === a.ROUND && this._roundCapEnd(L, W, H, e, c)
      }
      for (var G = E.indiceStart, V = M + 2, j = E.vertexStart; V < j; V++) C[G++] = V - 2, C[G++] = V - 1, C[G++] = V;
      E.indiceStart = G
     }
     v = null, g = null
    },
    _expandFill: function(t) {
     for (var e = (g = t._impl)._paths, i = 0, n = g._pathOffset, r = g._pathLength; n < r; n++) {
      i += e[n].points.length
     }
     for (var s = (v = this.genRenderData(t, i)).meshbuffer, a = s._vData, c = s._iData, h = g._pathOffset, u = g._pathLength; h < u; h++) {
      var l = e[h],
       _ = l.points,
       d = _.length;
      if (0 !== d) {
       for (var f = s.vertexStart, p = 0; p < d; ++p) this._vset(_[p].x, _[p].y);
       var m = s.indiceStart;
       if (l.complex) {
        for (var y = [], T = f, E = s.vertexStart; T < E; T++) {
         var x = 3 * T;
         y.push(a[x]), y.push(a[x + 1])
        }
        var C = o(y, null, 2);
        if (!C || 0 === C.length) continue;
        for (var A = 0, b = C.length; A < b; A++) c[m++] = C[A] + f
       } else
        for (var S = f, w = f + 2, D = s.vertexStart; w < D; w++) c[m++] = S, c[m++] = w - 1, c[m++] = w;
       s.indiceStart = m
      }
     }
     v = null, g = null
    },
    _calculateJoins: function(t, e, i, n) {
     var a = 0;
     e > 0 && (a = 1 / e);
     for (var o = t._paths, c = t._pathOffset, h = t._pathLength; c < h; c++) {
      var _ = o[c],
       d = _.points,
       f = d.length,
       p = d[f - 1],
       m = d[0];
      _.nbevel = 0;
      for (var v = 0; v < f; v++) {
       var g, y, T = p.dy,
        E = -p.dx,
        x = m.dy,
        C = -m.dx;
       if (m.dmx = .5 * (T + x), m.dmy = .5 * (E + C), (g = m.dmx * m.dmx + m.dmy * m.dmy) > 1e-6) {
        var A = 1 / g;
        A > 600 && (A = 600), m.dmx *= A, m.dmy *= A
       }
       m.dx * p.dy - p.dx * m.dy > 0 && (0, m.flags |= r.PT_LEFT), g * (y = l(11, u(p.len, m.len) * a)) * y < 1 && (m.flags |= r.PT_INNERBEVEL), m.flags & r.PT_CORNER && (g * n * n < 1 || i === s.BEVEL || i === s.ROUND) && (m.flags |= r.PT_BEVEL), 0 != (m.flags & (r.PT_BEVEL | r.PT_INNERBEVEL)) && _.nbevel++, p = m, m = d[v + 1]
      }
     }
    },
    _flattenPaths: function(t) {
     for (var e = t._paths, i = t._pathOffset, n = t._pathLength; i < n; i++) {
      var r = e[i],
       s = r.points,
       a = s[s.length - 1],
       o = s[0];
      a.equals(o) && (r.closed = !0, s.pop(), a = s[s.length - 1]);
      for (var c = 0, h = s.length; c < h; c++) {
       var u = o.sub(a);
       a.len = u.mag(), (u.x || u.y) && u.normalizeSelf(), a.dx = u.x, a.dy = u.y, a = o, o = s[c + 1]
      }
     }
    },
    _chooseBevel: function(t, e, i, n) {
     var r = i.x,
      s = i.y,
      a = void 0,
      o = void 0,
      c = void 0,
      h = void 0;
     return 0 !== t ? (a = r + e.dy * n, o = s - e.dx * n, c = r + i.dy * n, h = s - i.dx * n) : (a = c = r + i.dmx * n, o = h = s + i.dmy * n), [a, o, c, h]
    },
    _buttCap: function(t, e, i, n, r) {
     var s = t.x - e * r,
      a = t.y - i * r,
      o = i,
      c = -e;
     this._vset(s + o * n, a + c * n), this._vset(s - o * n, a - c * n)
    },
    _roundCapStart: function(t, e, i, n, r) {
     for (var s = t.x, a = t.y, o = i, c = -e, u = 0; u < r; u++) {
      var l = u / (r - 1) * h,
       _ = f(l) * n,
       d = p(l) * n;
      this._vset(s - o * _ - e * d, a - c * _ - i * d), this._vset(s, a)
     }
     this._vset(s + o * n, a + c * n), this._vset(s - o * n, a - c * n)
    },
    _roundCapEnd: function(t, e, i, n, r) {
     var s = t.x,
      a = t.y,
      o = i,
      c = -e;
     this._vset(s + o * n, a + c * n), this._vset(s - o * n, a - c * n);
     for (var u = 0; u < r; u++) {
      var l = u / (r - 1) * h,
       _ = f(l) * n,
       d = p(l) * n;
      this._vset(s, a), this._vset(s - o * _ + e * d, a - c * _ + i * d)
     }
    },
    _roundJoin: function(t, e, i, n, s) {
     var a = t.dy,
      o = -t.dx,
      c = e.dy,
      u = -e.dx,
      l = e.x,
      d = e.y;
     if (0 != (e.flags & r.PT_LEFT)) {
      var v = this._chooseBevel(e.flags & r.PT_INNERBEVEL, t, e, i),
       g = v[0],
       y = v[1],
       E = v[2],
       x = v[3],
       C = m(-o, -a),
       A = m(-u, -c);
      A > C && (A -= 2 * h), this._vset(g, y), this._vset(l - a * n, e.y - o * n);
      for (var b = T(_((C - A) / h) * s, 2, s), S = 0; S < b; S++) {
       var w = C + S / (b - 1) * (A - C),
        D = l + f(w) * n,
        R = d + p(w) * n;
       this._vset(l, d), this._vset(D, R)
      }
      this._vset(E, x), this._vset(l - c * n, d - u * n)
     } else {
      var M = this._chooseBevel(e.flags & r.PT_INNERBEVEL, t, e, -n),
       I = M[0],
       L = M[1],
       O = M[2],
       P = M[3],
       F = m(o, a),
       N = m(u, c);
      N < F && (N += 2 * h), this._vset(l + a * n, d + o * n), this._vset(I, L);
      for (var B = T(_((N - F) / h) * s, 2, s), k = 0; k < B; k++) {
       var z = F + k / (B - 1) * (N - F),
        U = l + f(z) * i,
        W = d + p(z) * i;
       this._vset(U, W), this._vset(l, d)
      }
      this._vset(l + c * n, d + u * n), this._vset(O, P)
     }
    },
    _bevelJoin: function(t, e, i, n) {
     var s = void 0,
      a = void 0,
      o = void 0,
      c = void 0,
      h = void 0,
      u = void 0,
      l = void 0,
      _ = void 0,
      d = t.dy,
      f = -t.dx,
      p = e.dy,
      m = -e.dx;
     if (e.flags & r.PT_LEFT) {
      var v = this._chooseBevel(e.flags & r.PT_INNERBEVEL, t, e, i);
      h = v[0], u = v[1], l = v[2], _ = v[3], this._vset(h, u), this._vset(e.x - d * n, e.y - f * n), this._vset(l, _), this._vset(e.x - p * n, e.y - m * n)
     } else {
      var g = this._chooseBevel(e.flags & r.PT_INNERBEVEL, t, e, -n);
      s = g[0], a = g[1], o = g[2], c = g[3], this._vset(e.x + d * i, e.y + f * i), this._vset(s, a), this._vset(e.x + p * i, e.y + m * i), this._vset(o, c)
     }
    },
    _vset: function(t, e) {
     var i = v.meshbuffer,
      n = 3 * i.vertexStart,
      r = i._vData,
      s = i._uintVData;
     r[n] = t, r[n + 1] = e, s[n + 2] = y, i.vertexStart++, i._dirty = !0
    }
   };
   n._assembler = E, e.exports = E
  }), {
   "../../../../graphics/graphics": 81,
   "../../../../graphics/types": 84,
   "./earcut": 157,
   "./impl": 158
  }],
  160: [(function(t, e, i) {
   t("./sprite"), t("./mask-assembler"), t("./graphics"), t("./label"), t("./motion-streak")
  }), {
   "./graphics": 159,
   "./label": 162,
   "./mask-assembler": 164,
   "./motion-streak": 165,
   "./sprite": 167
  }],
  161: [(function(t, e, i) {
   var n = t("../../../../platform/js"),
    r = t("../../../utils/label/bmfont");
   e.exports = n.addon({
    createData: function(t) {
     return t.requestRenderData()
    },
    fillBuffers: function(t, e) {
     var i = t.node,
      n = t._renderData,
      r = n._data,
      s = i.color._val,
      a = i._worldMatrix,
      o = a.m00,
      c = a.m01,
      h = a.m04,
      u = a.m05,
      l = a.m12,
      _ = a.m13,
      d = e._meshBuffer,
      f = d.byteOffset >> 2,
      p = d.indiceOffset,
      m = d.vertexOffset,
      v = n.vertexCount;
     d.request(v, n.indiceCount);
     for (var g = d._vData, y = d._uintVData, T = d._iData, E = 0; E < v; E++) {
      var x = r[E];
      g[f++] = x.x * o + x.y * h + l, g[f++] = x.x * c + x.y * u + _, g[f++] = x.u, g[f++] = x.v, y[f++] = s
     }
     for (var C = 0, A = v / 4; C < A; C++) {
      var b = m + 4 * C;
      T[p++] = b, T[p++] = b + 1, T[p++] = b + 2, T[p++] = b + 1, T[p++] = b + 3, T[p++] = b + 2
     }
    },
    appendQuad: function(t, e, i, n, r, s, a) {
     var o = t.dataLength;
     t.dataLength += 4, t.vertexCount = t.dataLength, t.indiceCount = t.dataLength / 2 * 3;
     var c = t._data,
      h = e.width,
      u = e.height,
      l = i.width,
      _ = i.height,
      d = void 0,
      f = void 0,
      p = void 0,
      m = void 0;
     n ? (d = i.x / h, p = (i.x + _) / h, f = (i.y + l) / u, m = i.y / u, c[o].u = d, c[o].v = m, c[o + 1].u = d, c[o + 1].v = f, c[o + 2].u = p, c[o + 2].v = m, c[o + 3].u = p, c[o + 3].v = f) : (d = i.x / h, p = (i.x + l) / h, f = (i.y + _) / u, m = i.y / u, c[o].u = d, c[o].v = f, c[o + 1].u = p, c[o + 1].v = f, c[o + 2].u = d, c[o + 2].v = m, c[o + 3].u = p, c[o + 3].v = m), c[o].x = r, c[o].y = s - _ * a, c[o + 1].x = r + l * a, c[o + 1].y = s - _ * a, c[o + 2].x = r, c[o + 2].y = s, c[o + 3].x = r + l * a, c[o + 3].y = s
    }
   }, r)
  }), {
   "../../../../platform/js": 128,
   "../../../utils/label/bmfont": 154
  }],
  162: [(function(t, e, i) {
   var n = t("../../../../components/CCLabel"),
    r = t("./ttf"),
    s = t("./bmfont"),
    a = {
     getAssembler: function(t) {
      var e = r;
      return t.font instanceof cc.BitmapFont && (e = s), e
     },
     updateRenderData: function(t) {
      return t.__allocedDatas
     }
    };
   n._assembler = a, e.exports = a
  }), {
   "../../../../components/CCLabel": 56,
   "./bmfont": 161,
   "./ttf": 163
  }],
  163: [(function(t, e, i) {
   var n = t("../../../../platform/js"),
    r = t("../../../utils/label/ttf"),
    s = cc.Color.WHITE._val;
   e.exports = n.addon({
    createData: function(t) {
     var e = t.requestRenderData();
     return e.dataLength = 4, e.vertexCount = 4, e.indiceCount = 6, e
    },
    fillBuffers: function(t, e) {
     var i = t._renderData._data,
      n = t.node,
      r = s,
      a = n._worldMatrix,
      o = a.m00,
      c = a.m01,
      h = a.m04,
      u = a.m05,
      l = a.m12,
      _ = a.m13,
      d = e._meshBuffer,
      f = d.byteOffset >> 2,
      p = d.indiceOffset,
      m = d.vertexOffset;
     d.request(4, 6);
     for (var v = d._vData, g = d._uintVData, y = d._iData, T = 0; T < 4; T++) {
      var E = i[T];
      v[f++] = E.x * o + E.y * h + l, v[f++] = E.x * c + E.y * u + _, v[f++] = E.u, v[f++] = E.v, g[f++] = r
     }
     y[p++] = m, y[p++] = m + 1, y[p++] = m + 2, y[p++] = m + 1, y[p++] = m + 3, y[p++] = m + 2
    },
    _updateVerts: function(t) {
     var e = t._renderData,
      i = t._frame.uv,
      n = t.node,
      r = n.width,
      s = n.height,
      a = n.anchorX * r,
      o = n.anchorY * s,
      c = e._data;
     c[0].x = -a, c[0].y = -o, c[1].x = r - a, c[1].y = -o, c[2].x = -a, c[2].y = s - o, c[3].x = r - a, c[3].y = s - o, c[0].u = i[0], c[0].v = i[1], c[1].u = i[2], c[1].v = i[3], c[2].u = i[4], c[2].v = i[5], c[3].u = i[6], c[3].v = i[7]
    }
   }, r)
  }), {
   "../../../../platform/js": 128,
   "../../../utils/label/ttf": 156
  }],
  164: [(function(t, e, i) {
   var n = t("../stencil-manager"),
    r = t("../../../components/CCMask"),
    s = t("../../render-flow"),
    a = t("./sprite/simple"),
    o = t("./graphics"),
    c = n.sharedManager,
    h = {
     updateRenderData: function(t) {
      t._renderData || (o.updateRenderData(t._clearGraphics), t._type === r.Type.IMAGE_STENCIL ? t._renderData = a.createData(t) : t._renderData = t.requestRenderData());
      var e = t._renderData;
      if (t._type === r.Type.IMAGE_STENCIL)
       if (t.spriteFrame) {
        var i = t.node._contentSize,
         n = t.node._anchorPoint;
        e.updateSizeNPivot(i.width, i.height, n.x, n.y), e.dataLength = 4, a.updateRenderData(t), e.material = t._material
       } else t._material = null;
      else t._graphics._material = t._material, o.updateRenderData(t._graphics)
     },
     fillBuffers: function(t, e) {
      (t._type !== r.Type.IMAGE_STENCIL || t.spriteFrame) && (c.pushMask(t), c.clear(), o.fillBuffers(t._clearGraphics, e), c.enterLevel(), e.node = t.node, e.material = t._material, t._type === r.Type.IMAGE_STENCIL ? (a.fillBuffers(t, e), e._flush()) : o.fillBuffers(t._graphics, e), c.enableMask()), t.node._renderFlag |= s.FLAG_UPDATE_RENDER_DATA
     }
    },
    u = {
     fillBuffers: function(t, e) {
      (t._type !== r.Type.IMAGE_STENCIL || t.spriteFrame) && c.exitMask(), t.node._renderFlag |= s.FLAG_UPDATE_RENDER_DATA
     }
    };
   r._assembler = h, r._postAssembler = u, e.exports = {
    front: h,
    end: u
   }
  }), {
   "../../../components/CCMask": 59,
   "../../render-flow": 151,
   "../stencil-manager": 176,
   "./graphics": 159,
   "./sprite/simple": 170
  }],
  165: [(function(t, e, i) {
   var n = t("../../../components/CCMotionStreak"),
    r = t("../../render-flow");

   function s(t, e) {
    this.point = t || cc.v2(), this.dir = e || cc.v2(), this.distance = 0, this.time = 0
   }
   s.prototype.setPoint = function(t, e) {
    this.point.x = t, this.point.y = e
   }, s.prototype.setDir = function(t, e) {
    this.dir.x = t, this.dir.y = e
   };
   cc.v2(), cc.v2();
   var a = cc.v2(),
    o = cc.v2();

   function c(t, e) {
    return t.x = -e.y, t.y = e.x, t
   }
   var h = {
    updateRenderData: function(t) {
     var e = cc.director.getDeltaTime();
     this.update(t, e);
     var i = t._renderData,
      n = t.node._contentSize,
      r = t.node._anchorPoint;
     i.updateSizeNPivot(n.width, n.height, r.x, r.y), i.material = t.getMaterial()
    },
    update: function(t, e) {
     var i = t._renderData;
     i || (i = t._renderData = t.requestRenderData());
     var n = t._stroke / 2,
      r = t.node._worldMatrix,
      h = (r.m00, r.m01, r.m04, r.m05, r.m12),
      u = r.m13,
      l = t._points,
      _ = void 0;
     if (l.length > 1) {
      var d = l[0].point.x - h,
       f = l[0].point.y - u;
      d * d + f * f < t.minSeg && (_ = l[0])
     }
     if (_ || (_ = new s, l.splice(0, 0, _)), _.setPoint(h, u), _.time = t._fadeTime + e, i.dataLength = 0, !(l.length < 2)) {
      var p = i._data,
       m = t._color,
       v = m.r,
       g = m.g,
       y = m.b,
       T = m.a,
       E = l[1];
      E.distance = _.point.sub(E.point, o).mag(), o.normalizeSelf(), E.setDir(o.x, o.y), _.setDir(o.x, o.y);
      for (var x = t._fadeTime, C = !1, A = l.length - 1; A >= 0; A--) {
       var b = l[A],
        S = b.point,
        w = b.dir;
       if (b.time -= e, b.time < 0) l.splice(A, 1);
       else {
        var D = b.time / x,
         R = l[A - 1];
        if (!C) {
         if (!R) {
          l.splice(A, 1);
          continue
         }
         S.x = R.point.x - w.x * D, S.y = R.point.y - w.y * D
        }
        C = !0, c(a, w), i.dataLength += 2;
        var M = (D * T << 24 >>> 0) + (y << 16) + (g << 8) + v,
         I = p.length - 1;
        p[I].x = S.x - a.x * n, p[I].y = S.y - a.y * n, p[I].u = 0, p[I].v = D, p[I].color = M, p[--I].x = S.x + a.x * n, p[I].y = S.y + a.y * n, p[I].u = 1, p[I].v = D, p[I].color = M
       }
      }
      i.vertexCount = i.dataLength, i.indiceCount = i.vertexCount < 2 ? 0 : 3 * (i.vertexCount - 2)
     }
    },
    fillBuffers: function(t, e) {
     t.node;
     var i = t._renderData,
      n = i._data,
      s = e._meshBuffer,
      a = s.byteOffset >> 2,
      o = i.vertexCount,
      c = s.indiceOffset,
      h = s.vertexOffset;
     s.request(o, i.indiceCount);
     for (var u = s._vData, l = s._iData, _ = s._uintVData, d = void 0, f = 0, p = i.vertexCount; f < p; f++) d = n[f], u[a++] = d.x, u[a++] = d.y, u[a++] = d.u, u[a++] = d.v, _[a++] = d.color;
     for (var m = 0, v = i.vertexCount; m < v; m += 2) {
      var g = h + m;
      l[c++] = g, l[c++] = g + 2, l[c++] = g + 1, l[c++] = g + 1, l[c++] = g + 2, l[c++] = g + 3
     }
     t.node._renderFlag |= r.FLAG_UPDATE_RENDER_DATA
    }
   };
   e.exports = n._assembler = h
  }), {
   "../../../components/CCMotionStreak": 60,
   "../../render-flow": 151
  }],
  166: [(function(t, e, i) {
   var n = t("../../../../components/CCSprite").FillType;
   e.exports = {
    useModel: !1,
    updateRenderData: function(t) {
     var e = t.spriteFrame;
     t._calDynamicAtlas();
     var i = t._renderData;
     if (i && e) {
      var n = i.uvDirty,
       r = i.vertDirty;
      if (!n && !r) return t.__allocedDatas;
      var s = t._fillStart,
       a = t._fillRange;
      a < 0 && (s += a, a = -a), a = s + a, s = (s = s > 1 ? 1 : s) < 0 ? 0 : s, a = (a = a > 1 ? 1 : a) < 0 ? 0 : a;
      var o = s + (a = (a -= s) < 0 ? 0 : a);
      o = o > 1 ? 1 : o, n && this.updateUVs(t, s, o), r && (this.updateVerts(t, s, o), this.updateWorldVerts(t))
     }
    },
    updateUVs: function(t, e, i) {
     var r = t._spriteFrame,
      s = t._renderData,
      a = s._data,
      o = r._texture.width,
      c = r._texture.height,
      h = r._rect,
      u = void 0,
      l = void 0,
      _ = void 0,
      d = void 0,
      f = void 0,
      p = void 0,
      m = void 0,
      v = void 0,
      g = void 0,
      y = void 0;
     switch (r._rotated ? (u = h.x / o, l = (h.y + h.width) / c, _ = f = u, m = g = (h.x + h.height) / o, p = y = l, d = v = h.y / c) : (u = h.x / o, l = (h.y + h.height) / c, _ = m = u, f = g = (h.x + h.width) / o, d = p = l, v = y = h.y / c), t._fillType) {
      case n.HORIZONTAL:
       a[0].u = _ + (f - _) * e, a[0].v = d + (p - d) * e, a[1].u = _ + (f - _) * i, a[1].v = d + (p - d) * i, a[2].u = m + (g - m) * e, a[2].v = v + (y - v) * e, a[3].u = m + (g - m) * i, a[3].v = v + (y - v) * i;
       break;
      case n.VERTICAL:
       a[0].u = _ + (m - _) * e, a[0].v = d + (v - d) * e, a[1].u = f + (g - f) * e, a[1].v = p + (y - p) * e, a[2].u = _ + (m - _) * i, a[2].v = d + (v - d) * i, a[3].u = f + (g - f) * i, a[3].v = p + (y - p) * i;
       break;
      default:
       cc.errorID(2626)
     }
     s.uvDirty = !1
    },
    updateVerts: function(t, e, i) {
     var r = t._renderData,
      s = r._data,
      a = t.node,
      o = a.width,
      c = a.height,
      h = a.anchorX * o,
      u = a.anchorY * c,
      l = -h,
      _ = -u,
      d = o - h,
      f = c - u,
      p = void 0;
     switch (t._fillType) {
      case n.HORIZONTAL:
       p = l + (d - l) * i, l = l + (d - l) * e, d = p;
       break;
      case n.VERTICAL:
       p = _ + (f - _) * i, _ = _ + (f - _) * e, f = p;
       break;
      default:
       cc.errorID(2626)
     }
     s[4].x = l, s[4].y = _, s[5].x = d, s[5].y = _, s[6].x = l, s[6].y = f, s[7].x = d, s[7].y = f, r.vertDirty = !1
    },
    createData: function(t) {
     var e = t.requestRenderData();
     return e.dataLength = 8, e.vertexCount = 4, e.indiceCount = 6, e
    },
    updateWorldVerts: function(t) {
     for (var e = t.node, i = t._renderData._data, n = e._worldMatrix, r = n.m00, s = n.m01, a = n.m04, o = n.m05, c = n.m12, h = n.m13, u = 0; u < 4; u++) {
      var l = i[u + 4],
       _ = i[u];
      _.x = l.x * r + l.y * a + c, _.y = l.x * s + l.y * o + h
     }
    },
    fillBuffers: function(t, e) {
     e.worldMatDirty && this.updateWorldVerts(t);
     var i = t._renderData._data,
      n = t.node,
      r = n._color._val,
      s = n._worldMatrix,
      a = (s.m00, s.m01, s.m04, s.m05, s.m12, s.m13, e._meshBuffer),
      o = a.byteOffset >> 2,
      c = a.indiceOffset,
      h = a.vertexOffset;
     a.request(4, 6);
     for (var u = a._vData, l = a._uintVData, _ = 0; _ < 4; _++) {
      var d = i[_];
      u[o++] = d.x, u[o++] = d.y, u[o++] = d.u, u[o++] = d.v, l[o++] = r
     }
     var f = a._iData;
     f[c++] = h, f[c++] = h + 1, f[c++] = h + 2, f[c++] = h + 1, f[c++] = h + 3, f[c++] = h + 2
    }
   }
  }), {
   "../../../../components/CCSprite": 64
  }],
  167: [(function(t, e, i) {
   var n = t("../../../../components/CCSprite"),
    r = n.Type,
    s = n.FillType,
    a = t("./simple"),
    o = t("./sliced"),
    c = t("./tiled"),
    h = t("./radial-filled"),
    u = t("./bar-filled"),
    l = t("./mesh"),
    _ = {
     getAssembler: function(t) {
      var e = a;
      switch (t.type) {
       case r.SLICED:
        e = o;
        break;
       case r.TILED:
        e = c;
        break;
       case r.FILLED:
        e = t._fillType === s.RADIAL ? h : u;
        break;
       case r.MESH:
        e = l
      }
      return e
     },
     updateRenderData: function(t) {
      return t.__allocedDatas
     }
    };
   n._assembler = _, e.exports = _
  }), {
   "../../../../components/CCSprite": 64,
   "./bar-filled": 166,
   "./mesh": 168,
   "./radial-filled": 169,
   "./simple": 170,
   "./sliced": 171,
   "./tiled": 172
  }],
  168: [(function(t, e, i) {
   e.exports = {
    useModel: !1,
    createData: function(t) {
     return t.requestRenderData()
    },
    updateRenderData: function(t) {
     var e = t.spriteFrame;
     t._calDynamicAtlas();
     var i = t._renderData;
     if (i && e) {
      var n = e.vertices;
      if (n) i.vertexCount !== n.x.length && (i.vertexCount = n.x.length, i.indiceCount = n.triangles.length, i.dataLength = 2 * i.vertexCount, i.uvDirty = i.vertDirty = !0), i.uvDirty && this.updateUVs(t), i.vertDirty && (this.updateVerts(t), this.updateWorldVerts(t))
     }
    },
    updateUVs: function(t) {
     for (var e = t.getMaterial().effect.getProperty("texture"), i = (e._width, e._height, t.spriteFrame.vertices), n = i.nu, r = i.nv, s = t._renderData, a = s._data, o = 0, c = n.length; o < c; o++) {
      var h = a[o];
      h.u = n[o], h.v = r[o]
     }
     s.uvDirty = !1
    },
    updateVerts: function(t) {
     var e = t.node,
      i = Math.abs(e.width),
      n = Math.abs(e.height),
      r = e.anchorX * i,
      s = e.anchorY * n,
      a = t.spriteFrame,
      o = a.vertices,
      c = o.x,
      h = o.y,
      u = a._originalSize.width,
      l = a._originalSize.height,
      _ = a._rect.width,
      d = a._rect.height,
      f = a._offset.x + (u - _) / 2,
      p = a._offset.y + (l - d) / 2,
      m = i / (t.trim ? _ : u),
      v = n / (t.trim ? d : l),
      g = t._renderData,
      y = g._data;
     if (t.trim)
      for (var T = 0, E = c.length; T < E; T++) {
       var x = y[T + E];
       x.x = (c[T] - f) * m - r, x.y = (l - h[T] - p) * v - s
      } else
       for (var C = 0, A = c.length; C < A; C++) {
        var b = y[C + A];
        b.x = c[C] * m - r, b.y = (l - h[C]) * v - s
       }
     g.vertDirty = !1
    },
    updateWorldVerts: function(t) {
     for (var e = t.node, i = t._renderData, n = i._data, r = e._worldMatrix, s = r.m00, a = r.m01, o = r.m04, c = r.m05, h = r.m12, u = r.m13, l = 0, _ = i.vertexCount; l < _; l++) {
      var d = n[l + _],
       f = n[l];
      f.x = d.x * s + d.y * o + h, f.y = d.x * a + d.y * c + u
     }
    },
    fillBuffers: function(t, e) {
     var i = t.node._color._val,
      n = t._renderData,
      r = n._data,
      s = t.spriteFrame.vertices;
     if (s) {
      e.worldMatDirty && this.updateWorldVerts(t);
      var a = e._meshBuffer,
       o = a.byteOffset >> 2,
       c = a.indiceOffset,
       h = a.vertexOffset;
      a.request(n.vertexCount, n.indiceCount);
      for (var u = a._vData, l = a._uintVData, _ = a._iData, d = 0, f = n.vertexCount; d < f; d++) {
       var p = r[d];
       u[o++] = p.x, u[o++] = p.y, u[o++] = p.u, u[o++] = p.v, l[o++] = i
      }
      for (var m = s.triangles, v = 0, g = m.length; v < g; v++) _[c++] = h + m[v]
     }
    }
   }
  }), {}],
  169: [(function(t, e, i) {
   var n = 2 * Math.PI;
   e.exports = {
    useModel: !1,
    _vertPos: [cc.v2(0, 0), cc.v2(0, 0), cc.v2(0, 0), cc.v2(0, 0)],
    _vertices: [0, 0, 0, 0],
    _uvs: [0, 0, 0, 0, 0, 0, 0, 0],
    _intersectPoint_1: [cc.v2(0, 0), cc.v2(0, 0), cc.v2(0, 0), cc.v2(0, 0)],
    _intersectPoint_2: [cc.v2(0, 0), cc.v2(0, 0), cc.v2(0, 0), cc.v2(0, 0)],
    _center: cc.v2(0, 0),
    _triangles: [],
    createData: function(t) {
     return t.requestRenderData()
    },
    updateRenderData: function(t) {
     var e = t.spriteFrame;
     t._calDynamicAtlas();
     var i = t._renderData;
     if (i && e && (i.vertDirty || i.uvDirty)) {
      var r = i._data,
       s = t._fillStart,
       a = t._fillRange;
      for (a < 0 && (s += a, a = -a); s >= 1;) s -= 1;
      for (; s < 0;) s += 1;
      var o = (s *= n) + (a *= n);
      this._calculateVertices(t), this._calculateUVs(e);
      var c = this._center,
       h = this._vertPos,
       u = this._vertices,
       l = this._triangles;
      this._calcInsectedPoints(u[0], u[2], u[1], u[3], c, s, this._intersectPoint_1), this._calcInsectedPoints(u[0], u[2], u[1], u[3], c, s + a, this._intersectPoint_2);
      for (var _ = 0, d = 0; d < 4; ++d) {
       var f = l[d];
       if (f)
        if (a >= n) i.dataLength = _ + 3, this._generateTriangle(r, _, c, h[f[0]], h[f[1]]), _ += 3;
        else {
         var p = this._getVertAngle(c, h[f[0]]),
          m = this._getVertAngle(c, h[f[1]]);
         m < p && (m += n), p -= n, m -= n;
         for (var v = 0; v < 3; ++v) p >= o || (p >= s ? (i.dataLength = _ + 3, m >= o ? this._generateTriangle(r, _, c, h[f[0]], this._intersectPoint_2[d]) : this._generateTriangle(r, _, c, h[f[0]], h[f[1]]), _ += 3) : m <= s || (m <= o ? (i.dataLength = _ + 3, this._generateTriangle(r, _, c, this._intersectPoint_1[d], h[f[1]]), _ += 3) : (i.dataLength = _ + 3, this._generateTriangle(r, _, c, this._intersectPoint_1[d], this._intersectPoint_2[d]), _ += 3))), p += n, m += n
        }
      }
      i.indiceCount = i.vertexCount = _, i.vertDirty = i.uvDirty = !1
     }
    },
    _getVertAngle: function(t, e) {
     var i, n;
     if (i = e.x - t.x, n = e.y - t.y, 0 !== i || 0 !== n) {
      if (0 === i) return n > 0 ? .5 * Math.PI : 1.5 * Math.PI;
      var r = Math.atan(n / i);
      return i < 0 && (r += Math.PI), r
     }
    },
    _generateTriangle: function(t, e, i, n, r) {
     var s = this._vertices,
      a = s[0],
      o = s[1],
      c = s[2],
      h = s[3];
     t[e].x = i.x, t[e].y = i.y, t[e + 1].x = n.x, t[e + 1].y = n.y, t[e + 2].x = r.x, t[e + 2].y = r.y;
     var u = void 0,
      l = void 0;
     u = (i.x - a) / (c - a), l = (i.y - o) / (h - o), this._generateUV(u, l, t, e), u = (n.x - a) / (c - a), l = (n.y - o) / (h - o), this._generateUV(u, l, t, e + 1), u = (r.x - a) / (c - a), l = (r.y - o) / (h - o), this._generateUV(u, l, t, e + 2)
    },
    _generateUV: function(t, e, i, n) {
     var r = this._uvs,
      s = r[0] + (r[2] - r[0]) * t,
      a = r[4] + (r[6] - r[4]) * t,
      o = r[1] + (r[3] - r[1]) * t,
      c = r[5] + (r[7] - r[5]) * t,
      h = i[n];
     h.u = s + (a - s) * e, h.v = o + (c - o) * e
    },
    _calcInsectedPoints: function(t, e, i, n, r, s, a) {
     var o = Math.sin(s),
      c = Math.cos(s),
      h = void 0,
      u = void 0;
     if (0 !== Math.cos(s)) {
      if (h = o / c, (t - r.x) * c > 0) {
       var l = r.y + h * (t - r.x);
       a[0].x = t, a[0].y = l
      }
      if ((e - r.x) * c > 0) {
       var _ = r.y + h * (e - r.x);
       a[2].x = e, a[2].y = _
      }
     }
     if (0 !== Math.sin(s)) {
      if (u = c / o, (n - r.y) * o > 0) {
       var d = r.x + u * (n - r.y);
       a[3].x = d, a[3].y = n
      }
      if ((i - r.y) * o > 0) {
       var f = r.x + u * (i - r.y);
       a[1].x = f, a[1].y = i
      }
     }
    },
    _calculateVertices: function(t) {
     var e = t.node,
      i = e.width,
      n = e.height,
      r = e.anchorX * i,
      s = e.anchorY * n,
      a = -r,
      o = -s,
      c = i - r,
      h = n - s,
      u = this._vertices;
     u[0] = a, u[1] = o, u[2] = c, u[3] = h;
     var l = this._center,
      _ = t._fillCenter,
      d = l.x = Math.min(Math.max(0, _.x), 1) * (c - a) + a,
      f = l.y = Math.min(Math.max(0, _.y), 1) * (h - o) + o,
      p = this._vertPos;
     p[0].x = p[3].x = a, p[1].x = p[2].x = c, p[0].y = p[1].y = o, p[2].y = p[3].y = h;
     var m = this._triangles;
     m.length = 0, d !== u[0] && (m[0] = [3, 0]), d !== u[2] && (m[2] = [1, 2]), f !== u[1] && (m[1] = [0, 1]), f !== u[3] && (m[3] = [2, 3])
    },
    _calculateUVs: function(t) {
     var e = t._texture.width,
      i = t._texture.height,
      n = t._rect,
      r = void 0,
      s = void 0,
      a = void 0,
      o = void 0,
      c = this._uvs;
     t._rotated ? (r = n.x / e, s = (n.x + n.height) / e, a = n.y / i, o = (n.y + n.width) / i, c[0] = c[2] = r, c[4] = c[6] = s, c[3] = c[7] = o, c[1] = c[5] = a) : (r = n.x / e, s = (n.x + n.width) / e, a = n.y / i, o = (n.y + n.height) / i, c[0] = c[4] = r, c[2] = c[6] = s, c[1] = c[3] = o, c[5] = c[7] = a)
    },
    fillBuffers: function(t, e) {
     var i = t._renderData,
      n = i._data,
      r = t.node,
      s = r._color._val,
      a = r._worldMatrix,
      o = a.m00,
      c = a.m01,
      h = a.m04,
      u = a.m05,
      l = a.m12,
      _ = a.m13,
      d = e._meshBuffer,
      f = d.byteOffset >> 2,
      p = d._vData,
      m = d._uintVData,
      v = d._iData,
      g = d.indiceOffset,
      y = d.vertexOffset;
     d.request(i.vertexCount, i.indiceCount);
     for (var T = n.length, E = 0; E < T; E++) {
      var x = n[E];
      p[f++] = x.x * o + x.y * h + l, p[f++] = x.x * c + x.y * u + _, p[f++] = x.u, p[f++] = x.v, m[f++] = s
     }
     for (var C = 0; C < T; C++) v[g + C] = y + C
    }
   }
  }), {}],
  170: [(function(t, e, i) {
   e.exports = {
    useModel: !1,
    updateRenderData: function(t) {
     var e = t._spriteFrame;
     t._calDynamicAtlas();
     var i = t._renderData;
     i && e && i.vertDirty && this.updateVerts(t)
    },
    fillBuffers: function(t, e) {
     var i = t._renderData._data,
      n = t.node,
      r = n._color._val,
      s = n._worldMatrix,
      a = s.m00,
      o = s.m01,
      c = s.m04,
      h = s.m05,
      u = s.m12,
      l = s.m13,
      _ = e._meshBuffer,
      d = _.byteOffset >> 2,
      f = _.indiceOffset,
      p = _.vertexOffset;
     _.request(4, 6);
     var m = _._vData,
      v = _._uintVData,
      g = _._iData,
      y = t._spriteFrame.uv;
     m[d + 2] = y[0], m[d + 3] = y[1], m[d + 7] = y[2], m[d + 8] = y[3], m[d + 12] = y[4], m[d + 13] = y[5], m[d + 17] = y[6], m[d + 18] = y[7];
     var T = i[0],
      E = i[3],
      x = T.x,
      C = E.x,
      A = T.y,
      b = E.y,
      S = a * x,
      w = a * C,
      D = o * x,
      R = o * C,
      M = c * A,
      I = c * b,
      L = h * A,
      O = h * b;
     m[d] = S + M + u, m[d + 1] = D + L + l, m[d + 5] = w + M + u, m[d + 6] = R + L + l, m[d + 10] = S + I + u, m[d + 11] = D + O + l, m[d + 15] = w + I + u, m[d + 16] = R + O + l, v[d + 4] = r, v[d + 9] = r, v[d + 14] = r, v[d + 19] = r, g[f++] = p, g[f++] = p + 1, g[f++] = p + 2, g[f++] = p + 1, g[f++] = p + 3, g[f++] = p + 2
    },
    createData: function(t) {
     var e = t.requestRenderData();
     return e.dataLength = 4, e.vertexCount = 4, e.indiceCount = 6, e
    },
    updateVerts: function(t) {
     var e = t._renderData,
      i = t.node,
      n = e._data,
      r = i.width,
      s = i.height,
      a = i.anchorX * r,
      o = i.anchorY * s,
      c = void 0,
      h = void 0,
      u = void 0,
      l = void 0;
     if (t.trim) c = -a, h = -o, u = r - a, l = s - o;
     else {
      var _ = t.spriteFrame,
       d = _._originalSize.width,
       f = _._originalSize.height,
       p = _._rect.width,
       m = _._rect.height,
       v = _._offset,
       g = r / d,
       y = s / f,
       T = v.x + (d - p) / 2,
       E = v.x - (d - p) / 2;
      c = T * g - a, h = (v.y + (f - m) / 2) * y - o, u = r + E * g - a, l = s + (v.y - (f - m) / 2) * y - o
     }
     n[0].x = c, n[0].y = h, n[3].x = u, n[3].y = l, e.vertDirty = !1
    }
   }
  }), {}],
  171: [(function(t, e, i) {
   e.exports = {
    useModel: !1,
    createData: function(t) {
     var e = t.requestRenderData();
     return e.dataLength = 20, e.vertexCount = 16, e.indiceCount = 54, e
    },
    updateRenderData: function(t, e) {
     var i = t.spriteFrame;
     t._calDynamicAtlas();
     var n = t._renderData;
     n && i && (n.vertDirty && (this.updateVerts(t), this.updateWorldVerts(t)))
    },
    updateVerts: function(t) {
     var e = t._renderData,
      i = e._data,
      n = t.node,
      r = n.width,
      s = n.height,
      a = n.anchorX * r,
      o = n.anchorY * s,
      c = t.spriteFrame,
      h = c.insetLeft,
      u = c.insetRight,
      l = c.insetTop,
      _ = c.insetBottom,
      d = r - h - u,
      f = s - l - _,
      p = r / (h + u),
      m = s / (l + _);
     p = isNaN(p) || p > 1 ? 1 : p, m = isNaN(m) || m > 1 ? 1 : m, d = d < 0 ? 0 : d, f = f < 0 ? 0 : f, i[0].x = -a, i[0].y = -o, i[1].x = h * p - a, i[1].y = _ * m - o, i[2].x = i[1].x + d, i[2].y = i[1].y + f, i[3].x = r - a, i[3].y = s - o, e.vertDirty = !1
    },
    fillBuffers: function(t, e) {
     e.worldMatDirty && this.updateWorldVerts(t);
     var i = t._renderData,
      n = t.node._color._val,
      r = i._data,
      s = e._meshBuffer,
      a = s.byteOffset >> 2,
      o = i.vertexCount,
      c = s.indiceOffset,
      h = s.vertexOffset,
      u = t.spriteFrame.uvSliced;
     s.request(o, i.indiceCount);
     for (var l = s._vData, _ = s._uintVData, d = s._iData, f = 4; f < 20; ++f) {
      var p = r[f],
       m = u[f - 4];
      l[a++] = p.x, l[a++] = p.y, l[a++] = m.u, l[a++] = m.v, _[a++] = n
     }
     for (var v = 0; v < 3; ++v)
      for (var g = 0; g < 3; ++g) {
       var y = h + 4 * v + g;
       d[c++] = y, d[c++] = y + 1, d[c++] = y + 4, d[c++] = y + 1, d[c++] = y + 5, d[c++] = y + 4
      }
    },
    updateWorldVerts: function(t) {
     for (var e = t.node, i = t._renderData._data, n = e._worldMatrix, r = n.m00, s = n.m01, a = n.m04, o = n.m05, c = n.m12, h = n.m13, u = 0; u < 4; ++u)
      for (var l = i[u], _ = 0; _ < 4; ++_) {
       var d = i[_],
        f = i[4 + 4 * u + _];
       f.x = d.x * r + l.y * a + c, f.y = d.x * s + l.y * o + h
      }
    }
   }
  }), {}],
  172: [(function(t, e, i) {
   e.exports = {
    useModel: !1,
    createData: function(t) {
     return t.requestRenderData()
    },
    updateRenderData: function(t) {
     var e = t.spriteFrame;
     t._calDynamicAtlas();
     var i = t._renderData;
     if (e && i && (i.uvDirty || i.vertDirty)) {
      var n = e._texture,
       r = (n.width, n.height, e._rect),
       s = t.node,
       a = Math.abs(s.width),
       o = Math.abs(s.height),
       c = s.anchorX * a,
       h = s.anchorY * o,
       u = r.width,
       l = r.height,
       _ = a / u,
       d = o / l,
       f = Math.ceil(d),
       p = Math.ceil(_),
       m = i._data;
      i.dataLength = Math.max(8, f + 1, p + 1);
      for (var v = 0; v <= p; ++v) m[v].x = Math.min(u * v, a) - c;
      for (var g = 0; g <= f; ++g) m[g].y = Math.min(l * g, o) - h;
      i.vertexCount = f * p * 4, i.indiceCount = f * p * 6, i.uvDirty = !1, i.vertDirty = !1
     }
    },
    fillBuffers: function(t, e) {
     var i = t.node,
      n = i._color._val,
      r = t._renderData,
      s = r._data,
      a = e._meshBuffer,
      o = a.byteOffset >> 2,
      c = a.indiceOffset,
      h = a.vertexOffset;
     a.request(r.vertexCount, r.indiceCount);
     for (var u = a._vData, l = a._uintVData, _ = a._iData, d = t.spriteFrame._rotated, f = t.spriteFrame.uv, p = t.spriteFrame._rect, m = Math.abs(i.width), v = Math.abs(i.height), g = m / p.width, y = v / p.height, T = Math.ceil(y), E = Math.ceil(g), x = i._worldMatrix, C = x.m00, A = x.m01, b = x.m04, S = x.m05, w = x.m12, D = x.m13, R = void 0, M = void 0, I = void 0, L = void 0, O = void 0, P = void 0, F = 0, N = T; F < N; ++F) {
      I = s[F].y, L = s[F + 1].y, P = Math.min(1, y - F);
      for (var B = 0, k = E; B < k; ++B) O = Math.min(1, g - B), R = s[B].x, M = s[B + 1].x, u[o] = R * C + I * b + w, u[o + 1] = R * A + I * S + D, u[o + 5] = M * C + I * b + w, u[o + 6] = M * A + I * S + D, u[o + 10] = R * C + L * b + w, u[o + 11] = R * A + L * S + D, u[o + 15] = M * C + L * b + w, u[o + 16] = M * A + L * S + D, d ? (u[o + 2] = f[0], u[o + 3] = f[1], u[o + 7] = f[0], u[o + 8] = f[1] + (f[7] - f[1]) * O, u[o + 12] = f[0] + (f[6] - f[0]) * P, u[o + 13] = f[1], u[o + 17] = u[o + 12], u[o + 18] = u[o + 8]) : (u[o + 2] = f[0], u[o + 3] = f[1], u[o + 7] = f[0] + (f[6] - f[0]) * O, u[o + 8] = f[1], u[o + 12] = f[0], u[o + 13] = f[1] + (f[7] - f[1]) * P, u[o + 17] = u[o + 7], u[o + 18] = u[o + 13]), l[o + 4] = n, l[o + 9] = n, l[o + 14] = n, l[o + 19] = n, o += 20
     }
     for (var z = r.indiceCount, U = 0; U < z; U += 6) _[c++] = h, _[c++] = h + 1, _[c++] = h + 2, _[c++] = h + 1, _[c++] = h + 3, _[c++] = h + 2, h += 4
    }
   }
  }), {}],
  173: [(function(t, e, i) {
   var n = t("../render-engine").gfx,
    r = cc.Class({
     name: "cc.MeshBuffer",
     ctor: function(t, e) {
      this.byteStart = 0, this.byteOffset = 0, this.indiceStart = 0, this.indiceOffset = 0, this.vertexStart = 0, this.vertexOffset = 0, this._vertexFormat = e, this._vertexBytes = this._vertexFormat._bytes, this._vb = new n.VertexBuffer(t._device, e, n.USAGE_DYNAMIC, new ArrayBuffer, 0), this._ib = new n.IndexBuffer(t._device, n.INDEX_FMT_UINT16, n.USAGE_STATIC, new ArrayBuffer, 0), this._vData = null, this._iData = null, this._uintVData = null, this._renderer = t, this._initVDataCount = 256 * e._bytes, this._initIDataCount = 1536, this._reallocBuffer()
     },
     uploadData: function() {
      if (0 !== this.byteOffset && this._dirty) {
       var t = new Float32Array(this._vData.buffer, 0, this.byteOffset >> 2),
        e = new Uint16Array(this._iData.buffer, 0, this.indiceOffset);
       this._vb.update(0, t), this._ib.update(0, e), this._dirty = !1
      }
     },
     requestStatic: function(t, e) {
      var i = this.byteOffset + t * this._vertexBytes,
       n = this.indiceOffset + e,
       r = this._vData.byteLength,
       s = this._iData.length;
      if (i > r || n > s) {
       for (; r < i || s < n;) this._initVDataCount *= 2, this._initIDataCount *= 2, r = 4 * this._initVDataCount, s = this._initIDataCount;
       this._reallocBuffer()
      }
      this.vertexOffset += t, this.indiceOffset += e, this.byteOffset = i, this._dirty = !0
     },
     request: function(t, e) {
      this._renderer._buffer !== this && (this._renderer._flush(), this._renderer._buffer = this), this.requestStatic(t, e)
     },
     _reallocBuffer: function() {
      this._reallocVData(!0), this._reallocIData(!0)
     },
     _reallocVData: function(t) {
      var e = void 0;
      this._vData && (e = new Uint8Array(this._vData.buffer)), this._vData = new Float32Array(this._initVDataCount), this._uintVData = new Uint32Array(this._vData.buffer);
      var i = new Uint8Array(this._uintVData.buffer);
      if (e && t)
       for (var n = 0, r = e.length; n < r; n++) i[n] = e[n];
      this._vb._bytes = this._vData.byteLength
     },
     _reallocIData: function(t) {
      var e = this._iData;
      if (this._iData = new Uint16Array(this._initIDataCount), e && t)
       for (var i = this._iData, n = 0, r = e.length; n < r; n++) i[n] = e[n];
      this._ib._bytes = this._iData.byteLength
     },
     reset: function() {
      this.byteStart = 0, this.byteOffset = 0, this.indiceStart = 0, this.indiceOffset = 0, this.vertexStart = 0, this.vertexOffset = 0, this._dirty = !1
     },
     destroy: function() {
      this._ib.destroy(), this._vb.destroy()
     }
    });
   cc.MeshBuffer = e.exports = r
  }), {
   "../render-engine": 150
  }],
  174: [(function(t, e, i) {
   var n = t("./mesh-buffer"),
    r = cc.Class({
     name: "cc.QuadBuffer",
     extends: n,
     _fillQuadBuffer: function() {
      for (var t = this._initIDataCount / 6, e = this._iData, i = 0, n = 0; i < t; i++) {
       var r = 4 * i;
       e[n++] = r, e[n++] = r + 1, e[n++] = r + 2, e[n++] = r + 1, e[n++] = r + 3, e[n++] = r + 2
      }
      var s = new Uint16Array(this._iData.buffer, 0, 6 * t);
      this._ib.update(0, s)
     },
     uploadData: function() {
      if (0 !== this.byteOffset && this._dirty) {
       var t = new Float32Array(this._vData.buffer, 0, this.byteOffset >> 2);
       this._vb.update(0, t), this._dirty = !1
      }
     },
     _reallocBuffer: function() {
      this._reallocVData(!0), this._reallocIData(), this._fillQuadBuffer()
     }
    });
   cc.QuadBuffer = e.exports = r
  }), {
   "./mesh-buffer": 173
  }],
  175: [(function(t, e, i) {
   var n = t("../../platform/CCMacro"),
    r = t("../render-engine"),
    s = t("./vertex-format").vfmtPosUvColor,
    a = t("./stencil-manager"),
    o = t("../utils/dynamic-atlas/manager"),
    c = t("../render-flow"),
    h = t("./quad-buffer"),
    u = t("./mesh-buffer"),
    l = new(t("../../platform/id-generater"))("VertextFormat"),
    _ = (r.gfx, r.RecyclePool),
    d = r.InputAssembler,
    f = (s._bytes, n.BATCH_VERTEX_COUNT),
    p = (s._bytes, {}),
    m = new r.Material;
   m.updateHash();
   var v = function(t, e) {
    this._renderScene = e, this._device = t, this._stencilMgr = a.sharedManager, this.walking = !1, this.material = m, this.cullingMask = 1, this._iaPool = new _(function() {
     return new d
    }, 16), this._modelPool = new _(function() {
     return new r.Model
    }, 16), this._quadBuffer = this.getBuffer("quad", s), this._meshBuffer = this.getBuffer("mesh", s), this._buffer = this._quadBuffer, this._batchedModels = [], this._dummyNode = new cc.Node, this._sortKey = 0, this.node = this._dummyNode, this.parentOpacity = 1, this.parentOpacityDirty = 0, this.worldMatDirty = 0, c.init(this)
   };
   v.prototype = {
    constructor: v,
    reset: function() {
     this._iaPool.reset();
     for (var t = this._renderScene, e = this._batchedModels, i = 0; i < e.length; ++i) e[i].clearInputAssemblers(), e[i].clearEffects(), t.removeModel(e[i]);
     for (var n in this._modelPool.reset(), e.length = 0, this._sortKey = 0, p) p[n].reset();
     this._buffer = this._quadBuffer, this.node = this._dummyNode, this.material = m, this.cullingMask = 1, this.parentOpacity = 1, this.parentOpacityDirty = 0, this.worldMatDirty = 0, this._stencilMgr.reset()
    },
    _flush: function() {
     var t = this.material,
      e = this._buffer,
      i = e.indiceStart,
      n = e.indiceOffset - i;
     if (this.walking && t && !(n <= 0)) {
      var r = t.effect,
       s = this._iaPool.add();
      s._vertexBuffer = e._vb, s._indexBuffer = e._ib, s._start = i, s._count = n, this._stencilMgr.handleEffect(r);
      var a = this._modelPool.add();
      this._batchedModels.push(a), a.sortKey = this._sortKey++, a._cullingMask = this.cullingMask, a.setNode(this.node), a.addEffect(r), a.addInputAssembler(s), this._renderScene.addModel(a), e.byteStart = e.byteOffset, e.indiceStart = e.indiceOffset, e.vertexStart = e.vertexOffset
     }
    },
    _flushIA: function(t) {
     var e = t.material;
     if (t.ia && e) {
      this.material = e;
      var i = this._stencilMgr.handleEffect(e.effect),
       n = this._modelPool.add();
      this._batchedModels.push(n), n.sortKey = this._sortKey++, n._cullingMask = this.cullingMask, n.setNode(this.node), n.addEffect(i), n.addInputAssembler(t.ia), this._renderScene.addModel(n)
     }
    },
    _commitComp: function(t, e, i) {
     this.material._hash === t._material._hash && this.cullingMask === i || (this._flush(), this.node = e.useModel ? t.node : this._dummyNode, this.material = t._material, this.cullingMask = i), e.fillBuffers(t, this)
    },
    _commitIA: function(t, e, i) {
     this._flush(), this.cullingMask = i, this.material = t._material, this.node = e.useModel ? t.node : this._dummyNode, e.renderIA(t, this)
    },
    visit: function(t) {
     for (var e in this.reset(), this.walking = !0, c.render(t), o && o.update(), this._flush(), p) p[e].uploadData();
     this.walking = !1
    },
    getBuffer: function(t, e) {
     e.name || (e.name = l.getNewId());
     var i = t + e.name,
      n = p[i];
     if (!n) {
      if ("mesh" === t) n = new u(this, e);
      else {
       if ("quad" !== t) return cc.error("Not support buffer type [" + t + "]"), null;
       n = new h(this, e)
      }
      p[i] = n
     }
     return n
    }
   }, e.exports = v
  }), {
   "../../platform/CCMacro": 113,
   "../../platform/id-generater": 124,
   "../render-engine": 150,
   "../render-flow": 151,
   "../utils/dynamic-atlas/manager": 153,
   "./mesh-buffer": 173,
   "./quad-buffer": 174,
   "./stencil-manager": 176,
   "./vertex-format": 177
  }],
  176: [(function(t, e, i) {
   var n = t("../render-engine").gfx,
    r = cc.Enum({
     DISABLED: 0,
     CLEAR: 1,
     ENTER_LEVEL: 2,
     ENABLED: 3,
     EXIT_LEVEL: 4
    });

   function s() {
    this._maxLevel = 8, this._maskStack = [], this.stage = r.DISABLED
   }
   s.prototype = {
    constructor: s,
    reset: function() {
     this._maskStack.length = 0, this.stage = r.DISABLED
    },
    handleEffect: function(t) {
     var e = t.getTechnique("transparent").passes;
     if (this.stage === r.DISABLED) {
      this.stage = r.DISABLED;
      for (var i = 0; i < e.length; ++i) {
       var s = e[i];
       s._stencilTest && s.disableStencilTest()
      }
      return t
     }
     var a = void 0,
      o = void 0,
      c = void 0,
      h = void 0,
      u = void 0,
      l = void 0,
      _ = n.STENCIL_OP_KEEP,
      d = n.STENCIL_OP_KEEP;
     this.stage === r.ENABLED ? (a = this._maskStack[this._maskStack.length - 1], o = n.DS_FUNC_EQUAL, l = n.STENCIL_OP_KEEP, h = c = this.getStencilRef(), u = this.getWriteMask()) : this.stage === r.CLEAR ? (a = this._maskStack[this._maskStack.length - 1], o = n.DS_FUNC_NEVER, l = a.inverted ? n.STENCIL_OP_REPLACE : n.STENCIL_OP_ZERO, h = c = this.getWriteMask(), u = c) : this.stage === r.ENTER_LEVEL && (a = this._maskStack[this._maskStack.length - 1], o = n.DS_FUNC_NEVER, l = a.inverted ? n.STENCIL_OP_ZERO : n.STENCIL_OP_REPLACE, h = c = this.getWriteMask(), u = c);
     for (var f = 0; f < e.length; ++f) {
      var p = e[f];
      p.setStencilFront(o, c, h, l, _, d, u), p.setStencilBack(o, c, h, l, _, d, u)
     }
     return t
    },
    pushMask: function(t) {
     this._maskStack.length + 1 > this._maxLevel && cc.errorID(9e3, this._maxLevel), this._maskStack.push(t)
    },
    clear: function() {
     this.stage = r.CLEAR
    },
    enterLevel: function() {
     this.stage = r.ENTER_LEVEL
    },
    enableMask: function() {
     this.stage = r.ENABLED
    },
    exitMask: function() {
     0 === this._maskStack.length && cc.errorID(9001), this._maskStack.pop(), 0 === this._maskStack.length ? this.stage = r.DISABLED : this.stage = r.ENABLED
    },
    getWriteMask: function() {
     return 1 << this._maskStack.length - 1
    },
    getExitWriteMask: function() {
     return 1 << this._maskStack.length
    },
    getStencilRef: function() {
     for (var t = 0, e = 0; e < this._maskStack.length; ++e) t += 1 << e;
     return t
    },
    getInvertedRef: function() {
     for (var t = 0, e = 0; e < this._maskStack.length - 1; ++e) t += 1 << e;
     return t
    }
   }, s.sharedManager = new s, s.Stage = r, e.exports = cc.StencilManager = s
  }), {
   "../render-engine": 150
  }],
  177: [(function(t, e, i) {
   var n = t("../render-engine").gfx,
    r = new n.VertexFormat([{
     name: n.ATTR_POSITION,
     type: n.ATTR_TYPE_FLOAT32,
     num: 3
    }, {
     name: n.ATTR_UV0,
     type: n.ATTR_TYPE_FLOAT32,
     num: 2
    }, {
     name: n.ATTR_COLOR,
     type: n.ATTR_TYPE_UINT8,
     num: 4,
     normalize: !0
    }]);
   r.name = "vfmt3D", n.VertexFormat.XYZ_UV_Color = r;
   var s = new n.VertexFormat([{
    name: n.ATTR_POSITION,
    type: n.ATTR_TYPE_FLOAT32,
    num: 2
   }, {
    name: n.ATTR_UV0,
    type: n.ATTR_TYPE_FLOAT32,
    num: 2
   }, {
    name: n.ATTR_COLOR,
    type: n.ATTR_TYPE_UINT8,
    num: 4,
    normalize: !0
   }]);
   s.name = "vfmtPosUvColor", n.VertexFormat.XY_UV_Color = s;
   var a = new n.VertexFormat([{
    name: n.ATTR_POSITION,
    type: n.ATTR_TYPE_FLOAT32,
    num: 2
   }, {
    name: n.ATTR_UV0,
    type: n.ATTR_TYPE_FLOAT32,
    num: 2
   }]);
   a.name = "vfmtPosUv", n.VertexFormat.XY_UV = a;
   var o = new n.VertexFormat([{
    name: n.ATTR_POSITION,
    type: n.ATTR_TYPE_FLOAT32,
    num: 2
   }, {
    name: n.ATTR_COLOR,
    type: n.ATTR_TYPE_UINT8,
    num: 4,
    normalize: !0
   }]);
   o.name = "vfmtPosColor", n.VertexFormat.XY_Color = o, e.exports = {
    vfmt3D: r,
    vfmtPosUvColor: s,
    vfmtPosUv: a,
    vfmtPosColor: o
   }
  }), {
   "../render-engine": 150
  }],
  178: [(function(t, e, i) {
   t("../platform/CCSys");
   var n = /(\.[^\.\/\?\\]*)(\?.*)?$/,
    r = /((.*)(\/|\\|\\\\))?(.*?\..*$)?/,
    s = /[^\.\/]+\/\.\.\//;
   cc.path = {
    join: function() {
     for (var t = arguments.length, e = "", i = 0; i < t; i++) e = (e + ("" === e ? "" : "/") + arguments[i]).replace(/(\/|\\\\)$/, "");
     return e
    },
    extname: function(t) {
     var e = n.exec(t);
     return e ? e[1] : ""
    },
    mainFileName: function(t) {
     if (t) {
      var e = t.lastIndexOf(".");
      if (-1 !== e) return t.substring(0, e)
     }
     return t
    },
    basename: function(t, e) {
     var i = t.indexOf("?");
     i > 0 && (t = t.substring(0, i));
     var n = /(\/|\\)([^\/\\]+)$/g.exec(t.replace(/(\/|\\)$/, ""));
     if (!n) return null;
     var r = n[2];
     return e && t.substring(t.length - e.length).toLowerCase() === e.toLowerCase() ? r.substring(0, r.length - e.length) : r
    },
    dirname: function(t) {
     var e = r.exec(t);
     return e ? e[2] : ""
    },
    changeExtname: function(t, e) {
     e = e || "";
     var i = t.indexOf("?"),
      n = "";
     return i > 0 && (n = t.substring(i), t = t.substring(0, i)), (i = t.lastIndexOf(".")) < 0 ? t + e + n : t.substring(0, i) + e + n
    },
    changeBasename: function(t, e, i) {
     if (0 === e.indexOf(".")) return this.changeExtname(t, e);
     var n = t.indexOf("?"),
      r = "",
      s = i ? this.extname(t) : "";
     return n > 0 && (r = t.substring(n), t = t.substring(0, n)), n = (n = t.lastIndexOf("/")) <= 0 ? 0 : n + 1, t.substring(0, n) + e + s + r
    },
    _normalize: function(t) {
     var e = t = String(t);
     do {
      e = t, t = t.replace(s, "")
     } while (e.length !== t.length);
     return t
    },
    sep: cc.sys.os === cc.sys.OS_WINDOWS ? "\\" : "/",
    stripSep: function(t) {
     return t.replace(/[\/\\]$/, "")
    }
   }, e.exports = cc.path
  }), {
   "../platform/CCSys": 117
  }],
  179: [(function(t, e, i) {
   var n = function(t, e, i, n, r, s) {
    this.a = t, this.b = e, this.c = i, this.d = n, this.tx = r, this.ty = s
   };
   n.create = function(t, e, i, n, r, s) {
    return {
     a: t,
     b: e,
     c: i,
     d: n,
     tx: r,
     ty: s
    }
   }, n.identity = function() {
    return {
     a: 1,
     b: 0,
     c: 0,
     d: 1,
     tx: 0,
     ty: 0
    }
   }, n.clone = function(t) {
    return {
     a: t.a,
     b: t.b,
     c: t.c,
     d: t.d,
     tx: t.tx,
     ty: t.ty
    }
   }, n.concat = function(t, e, i) {
    var n = e.a,
     r = e.b,
     s = e.c,
     a = e.d,
     o = e.tx,
     c = e.ty;
    return t.a = n * i.a + r * i.c, t.b = n * i.b + r * i.d, t.c = s * i.a + a * i.c, t.d = s * i.b + a * i.d, t.tx = o * i.a + c * i.c + i.tx, t.ty = o * i.b + c * i.d + i.ty, t
   }, n.invert = function(t, e) {
    var i = e.a,
     n = e.b,
     r = e.c,
     s = e.d,
     a = 1 / (i * s - n * r),
     o = e.tx,
     c = e.ty;
    return t.a = a * s, t.b = -a * n, t.c = -a * r, t.d = a * i, t.tx = a * (r * c - s * o), t.ty = a * (n * o - i * c), t
   }, n.fromMat4 = function(t, e) {
    return t.a = e.m00, t.b = e.m01, t.c = e.m04, t.d = e.m05, t.tx = e.m12, t.ty = e.m13, t
   }, n.transformVec2 = function(t, e, i, n) {
    var r, s;
    return void 0 === n ? (n = i, r = e.x, s = e.y) : (r = e, s = i), t.x = n.a * r + n.c * s + n.tx, t.y = n.b * r + n.d * s + n.ty, t
   }, n.transformSize = function(t, e, i) {
    return t.width = i.a * e.width + i.c * e.height, t.height = i.b * e.width + i.d * e.height, t
   }, n.transformRect = function(t, e, i) {
    var n = e.x,
     r = e.y,
     s = n + e.width,
     a = r + e.height,
     o = i.a * n + i.c * r + i.tx,
     c = i.b * n + i.d * r + i.ty,
     h = i.a * s + i.c * r + i.tx,
     u = i.b * s + i.d * r + i.ty,
     l = i.a * n + i.c * a + i.tx,
     _ = i.b * n + i.d * a + i.ty,
     d = i.a * s + i.c * a + i.tx,
     f = i.b * s + i.d * a + i.ty,
     p = Math.min(o, h, l, d),
     m = Math.max(o, h, l, d),
     v = Math.min(c, u, _, f),
     g = Math.max(c, u, _, f);
    return t.x = p, t.y = v, t.width = m - p, t.height = g - v, t
   }, n.transformObb = function(t, e, i, n, r, s) {
    var a = r.x,
     o = r.y,
     c = r.width,
     h = r.height,
     u = s.a * a + s.c * o + s.tx,
     l = s.b * a + s.d * o + s.ty,
     _ = s.a * c,
     d = s.b * c,
     f = s.c * h,
     p = s.d * h;
    e.x = u, e.y = l, i.x = _ + u, i.y = d + l, t.x = f + u, t.y = p + l, n.x = _ + f + u, n.y = d + p + l
   }, cc.AffineTransform = e.exports = n
  }), {}],
  180: [(function(t, e, i) {
   var n = t("../platform/CCObject").Flags,
    r = t("./misc"),
    s = t("../platform/js"),
    a = t("../platform/id-generater"),
    o = t("../event-manager"),
    c = t("../renderer/render-flow"),
    h = n.Destroying,
    u = n.DontDestroy,
    l = n.Deactivating,
    _ = new a("Node");

   function d(t) {
    return t ? "string" == typeof t ? s.getClassByName(t) : t : (cc.errorID(3804), null)
   }

   function f(t, e) {
    if (e._sealed)
     for (var i = 0; i < t._components.length; ++i) {
      var n = t._components[i];
      if (n.constructor === e) return n
     } else
      for (var r = 0; r < t._components.length; ++r) {
       var s = t._components[r];
       if (s instanceof e) return s
      }
    return null
   }

   function p(t, e, i) {
    if (e._sealed)
     for (var n = 0; n < t._components.length; ++n) {
      var r = t._components[n];
      r.constructor === e && i.push(r)
     } else
      for (var s = 0; s < t._components.length; ++s) {
       var a = t._components[s];
       a instanceof e && i.push(a)
      }
   }
   var m = cc.Class({
    name: "cc._BaseNode",
    extends: cc.Object,
    properties: {
     _parent: null,
     _children: [],
     _active: !0,
     _level: 0,
     _components: [],
     _prefab: null,
     _persistNode: {
      get: function() {
       return (this._objFlags & u) > 0
      },
      set: function(t) {
       t ? this._objFlags |= u : this._objFlags &= ~u
      }
     },
     name: {
      get: function() {
       return this._name
      },
      set: function(t) {
       this._name = t
      }
     },
     uuid: {
      get: function() {
       return this._id
      }
     },
     children: {
      get: function() {
       return this._children
      }
     },
     childrenCount: {
      get: function() {
       return this._children.length
      }
     },
     active: {
      get: function() {
       return this._active
      },
      set: function(t) {
       if (t = !!t, this._active !== t) {
        this._active = t;
        var e = this._parent;
        if (e) e._activeInHierarchy && cc.director._nodeActivator.activateNode(this, t)
       }
      }
     },
     activeInHierarchy: {
      get: function() {
       return this._activeInHierarchy
      }
     }
    },
    ctor: function(t) {
     this._name = void 0 !== t ? t : "New Node", this._activeInHierarchy = !1, this._id = _.getNewId(), cc.director._scheduler && cc.director._scheduler.enableForTarget(this), this.__eventTargets = [], this._renderFlag = c.FLAG_TRANSFORM
    },
    getParent: function() {
     return this._parent
    },
    setParent: function(t) {
     if (this._parent !== t) {
      0;
      var e = this._parent;
      if (this._parent = t || null, this._onSetParent(t), t && (this._level = t._level + 1, o._setDirtyForNode(this), t._children.push(this), t.emit && t.emit("child-added", this), t._renderFlag |= c.FLAG_CHILDREN), e) {
       if (!(e._objFlags & h)) {
        var i = e._children.indexOf(this);
        0, e._children.splice(i, 1), e.emit && e.emit("child-removed", this), this._onHierarchyChanged(e), 0 === e._children.length && (e._renderFlag &= ~c.FLAG_CHILDREN)
       }
      } else t && this._onHierarchyChanged(null)
     }
    },
    attr: function(t) {
     s.mixin(this, t)
    },
    getChildByUuid: function(t) {
     if (!t) return cc.log("Invalid uuid"), null;
     for (var e = this._children, i = 0, n = e.length; i < n; i++)
      if (e[i]._id === t) return e[i];
     return null
    },
    getChildByName: function(t) {
     if (!t) return cc.log("Invalid name"), null;
     for (var e = this._children, i = 0, n = e.length; i < n; i++)
      if (e[i]._name === t) return e[i];
     return null
    },
    addChild: function(t) {
     cc.assertID(t, 1606), cc.assertID(null === t._parent, 1605), t.setParent(this)
    },
    insertChild: function(t, e) {
     t.parent = this, t.setSiblingIndex(e)
    },
    getSiblingIndex: function() {
     return this._parent ? this._parent._children.indexOf(this) : 0
    },
    setSiblingIndex: function(t) {
     if (this._parent)
      if (this._parent._objFlags & l) cc.errorID(3821);
      else {
       var e = this._parent._children;
       t = -1 !== t ? t : e.length - 1;
       var i = e.indexOf(this);
       t !== i && (e.splice(i, 1), t < e.length ? e.splice(t, 0, this) : e.push(this), this._onSiblingIndexChanged && this._onSiblingIndexChanged(t))
      }
    },
    walk: function(t, e) {
     var i, n, r, s, a = cc._BaseNode,
      o = 1,
      c = a._stacks[a._stackId];
     c || (c = [], a._stacks.push(c)), a._stackId++, c.length = 0, c[0] = this;
     var h = null;
     for (s = !1; o;)
      if (n = c[--o])
       if (!s && t ? t(n) : s && e && e(n), c[o] = null, s) {
        if (s = !1, i)
         if (i[++r]) c[o] = i[r], o++;
         else if (h && (c[o] = h, o++, s = !0, h._parent ? (r = (i = h._parent._children).indexOf(h), h = h._parent) : (h = null, i = null), r < 0)) break
       } else n._children.length > 0 ? (h = n, i = n._children, r = 0, c[o] = i[r], o++) : (c[o] = n, o++, s = !0);
     c.length = 0, a._stackId--
    },
    cleanup: function() {},
    removeFromParent: function(t) {
     this._parent && (void 0 === t && (t = !0), this._parent.removeChild(this, t))
    },
    removeChild: function(t, e) {
     this._children.indexOf(t) > -1 && ((e || void 0 === e) && t.cleanup(), t.parent = null)
    },
    removeAllChildren: function(t) {
     var e = this._children;
     void 0 === t && (t = !0);
     for (var i = e.length - 1; i >= 0; i--) {
      var n = e[i];
      n && (t && n.cleanup(), n.parent = null)
     }
     this._children.length = 0
    },
    isChildOf: function(t) {
     var e = this;
     do {
      if (e === t) return !0;
      e = e._parent
     } while (e);
     return !1
    },
    getComponent: function(t) {
     var e = d(t);
     return e ? f(this, e) : null
    },
    getComponents: function(t) {
     var e = d(t),
      i = [];
     return e && p(this, e, i), i
    },
    getComponentInChildren: function(t) {
     var e = d(t);
     return e ? (function t(e, i) {
      for (var n = 0; n < e.length; ++n) {
       var r = e[n],
        s = f(r, i);
       if (s) return s;
       if (r._children.length > 0 && (s = t(r._children, i))) return s
      }
      return null
     })(this._children, e) : null
    },
    getComponentsInChildren: function(t) {
     var e = d(t),
      i = [];
     return e && (p(this, e, i), (function t(e, i, n) {
      for (var r = 0; r < e.length; ++r) {
       var s = e[r];
       p(s, i, n), s._children.length > 0 && t(s._children, i, n)
      }
     })(this._children, e, i)), i
    },
    _checkMultipleComp: !1,
    addComponent: function(t) {
     var e;
     if ("string" == typeof t) {
      if (!(e = s.getClassByName(t))) return cc.errorID(3807, t), cc._RFpeek() && cc.errorID(3808, t), null
     } else {
      if (!t) return cc.errorID(3804), null;
      e = t
     }
     if ("function" != typeof e) return cc.errorID(3809), null;
     if (!s.isChildClassOf(e, cc.Component)) return cc.errorID(3810), null;
     var i = e._requireComponent;
     if (i && !this.getComponent(i) && !this.addComponent(i)) return null;
     var n = new e;
     return n.node = this, this._components.push(n), this._activeInHierarchy && cc.director._nodeActivator.activateComp(n), n
    },
    _addComponentAt: !1,
    removeComponent: function(t) {
     t ? (t instanceof cc.Component || (t = this.getComponent(t)), t && t.destroy()) : cc.errorID(3813)
    },
    _getDependComponent: !1,
    _removeComponent: function(t) {
     if (t) {
      if (!(this._objFlags & h)) {
       var e = this._components.indexOf(t); - 1 !== e ? this._components.splice(e, 1) : t.node !== this && cc.errorID(3815)
      }
     } else cc.errorID(3814)
    },
    destroy: function() {
     cc.Object.prototype.destroy.call(this) && (this.active = !1)
    },
    destroyAllChildren: function() {
     for (var t = this._children, e = 0; e < t.length; ++e) t[e].destroy()
    },
    _onSetParent: function(t) {},
    _onPostActivated: function() {},
    _onBatchRestored: function() {},
    _onBatchCreated: function() {},
    _onHierarchyChanged: function(t) {
     var e = this._parent;
     !this._persistNode || e instanceof cc.Scene || cc.game.removePersistRootNode(this);
     var i = this._active && !(!e || !e._activeInHierarchy);
     this._activeInHierarchy !== i && cc.director._nodeActivator.activateNode(this, i)
    },
    _instantiate: function(t) {
     t || (t = cc.instantiate._clone(this, this));
     var e = this._prefab;
     e && this === e.root && e.sync;
     return t._parent = null, t._onBatchRestored(), t
    },
    _registerIfAttached: !1,
    _onPreDestroy: function() {
     var t, e;
     this._objFlags |= h;
     var i = this._parent,
      n = i && i._objFlags & h;
     var r = this._children;
     for (t = 0, e = r.length; t < e; ++t) r[t]._destroyImmediate();
     for (t = 0, e = this._components.length; t < e; ++t) {
      this._components[t]._destroyImmediate()
     }
     var s = this.__eventTargets;
     for (t = 0, e = s.length; t < e; ++t) {
      var a = s[t];
      a && a.targetOff(this)
     }
     if (s.length = 0, this._persistNode && cc.game.removePersistRootNode(this), !n && i) {
      var o = i._children.indexOf(this);
      i._children.splice(o, 1), i.emit && i.emit("child-removed", this)
     }
     return n
    },
    onRestore: !1
   });
   m.idGenerater = _, m._stacks = [
    []
   ], m._stackId = 0, m.prototype._onPreDestroyBase = m.prototype._onPreDestroy, m.prototype._onHierarchyChangedBase = m.prototype._onHierarchyChanged;
   r.propertyDefine(m, ["parent", "name", "children", "childrenCount"], {}), cc._BaseNode = e.exports = m
  }), {
   "../event-manager": 75,
   "../platform/CCObject": 114,
   "../platform/id-generater": 124,
   "../platform/js": 128,
   "../renderer/render-flow": 151,
   "./misc": 186
  }],
  181: [(function(t, e, i) {
   var n = 1e-6;
   e.exports = {
    binarySearchEpsilon: function(t, e) {
     for (var i = 0, r = t.length - 1, s = r >>> 1; i <= r; s = i + r >>> 1) {
      var a = t[s];
      if (a > e + n) r = s - 1;
      else {
       if (!(a < e - n)) return s;
       i = s + 1
      }
     }
     return ~i
    }
   }
  }), {}],
  182: [(function(t, e, i) {
   var n = t("./misc").BASE64_VALUES,
    r = "0123456789abcdef".split(""),
    s = ["", "", "", ""],
    a = s.concat(s, "-", s, "-", s, "-", s, "-", s, s, s),
    o = a.map((function(t, e) {
     return "-" === t ? NaN : e
    })).filter(isFinite);
   e.exports = function(t) {
    if (22 !== t.length) return t;
    a[0] = t[0], a[1] = t[1];
    for (var e = 2, i = 2; e < 22; e += 2) {
     var s = n[t.charCodeAt(e)],
      c = n[t.charCodeAt(e + 1)];
     a[o[i++]] = r[s >> 2], a[o[i++]] = r[(3 & s) << 2 | c >> 4], a[o[i++]] = r[15 & c]
    }
    return a.join("")
   }
  }), {
   "./misc": 186
  }],
  183: [(function(t, e, i) {
   cc.find = e.exports = function(t, e) {
    if (null == t) return cc.errorID(5600), null;
    if (e) 0;
    else {
     var i = cc.director.getScene();
     if (!i) return null;
     e = i
    }
    for (var n = e, r = "/" !== t[0] ? 0 : 1, s = t.split("/"), a = r; a < s.length; a++) {
     var o = s[a],
      c = n._children;
     n = null;
     for (var h = 0, u = c.length; h < u; ++h) {
      var l = c[h];
      if (l.name === o) {
       n = l;
       break
      }
     }
     if (!n) return null
    }
    return n
   }
  }), {}],
  184: [(function(t, e, i) {
   t("./CCPath"), t("./profiler/CCProfiler"), t("./find"), t("./mutable-forward-iterator")
  }), {
   "./CCPath": 178,
   "./find": 183,
   "./mutable-forward-iterator": 187,
   "./profiler/CCProfiler": 190
  }],
  185: [(function(t, e, i) {
   var n = t("../platform/js"),
    r = t("../renderer/render-engine").math,
    s = new n.Pool(128);
   s.get = function() {
    var t = this._get();
    return t ? r.mat4.identity(t) : t = r.mat4.create(), t
   };
   var a = new n.Pool(64);
   a.get = function() {
    var t = this._get();
    return t ? (t.x = t.y = t.z = 0, t.w = 1) : t = r.quat.create(), t
   }, e.exports = {
    mat4: s,
    quat: a
   }
  }), {
   "../platform/js": 128,
   "../renderer/render-engine": 150
  }],
  186: [(function(t, e, i) {
   for (var n = t("../platform/js"), r = {
     propertyDefine: function(t, e, i) {
      function r(t, e, i, r) {
       var s = Object.getOwnPropertyDescriptor(t, e);
       if (s) s.get && (t[i] = s.get), s.set && r && (t[r] = s.set);
       else {
        var a = t[i];
        n.getset(t, e, a, t[r])
       }
      }
      for (var s, a = t.prototype, o = 0; o < e.length; o++) {
       var c = (s = e[o])[0].toUpperCase() + s.slice(1);
       r(a, s, "get" + c, "set" + c)
      }
      for (s in i) {
       var h = i[s];
       r(a, s, h[0], h[1])
      }
     },
     NextPOT: function(t) {
      return t -= 1, t |= t >> 1, t |= t >> 2, t |= t >> 4, t |= t >> 8, (t |= t >> 16) + 1
     },
     BUILTIN_CLASSID_RE: /^(?:cc|dragonBones|sp|ccsg)\..+/
    }, s = new Array(123), a = 0; a < 123; ++a) s[a] = 64;
   for (var o = 0; o < 64; ++o) s["ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".charCodeAt(o)] = o;
   r.BASE64_VALUES = s, r.pushToMap = function(t, e, i, n) {
    var r = t[e];
    r ? Array.isArray(r) ? n ? (r.push(r[0]), r[0] = i) : r.push(i) : t[e] = n ? [i, r] : [r, i] : t[e] = i
   }, r.clampf = function(t, e, i) {
    if (e > i) {
     var n = e;
     e = i, i = n
    }
    return t < e ? e : t < i ? t : i
   }, r.clamp01 = function(t) {
    return t < 0 ? 0 : t < 1 ? t : 1
   }, r.lerp = function(t, e, i) {
    return t + (e - t) * i
   }, r.degreesToRadians = function(t) {
    return t * cc.macro.RAD
   }, r.radiansToDegrees = function(t) {
    return t * cc.macro.DEG
   }, cc.misc = e.exports = r
  }), {
   "../platform/js": 128
  }],
  187: [(function(t, e, i) {
   function n(t) {
    this.i = 0, this.array = t
   }
   var r = n.prototype;
   r.remove = function(t) {
    var e = this.array.indexOf(t);
    e >= 0 && this.removeAt(e)
   }, r.removeAt = function(t) {
    this.array.splice(t, 1), t <= this.i && --this.i
   }, r.fastRemove = function(t) {
    var e = this.array.indexOf(t);
    e >= 0 && this.fastRemoveAt(e)
   }, r.fastRemoveAt = function(t) {
    var e = this.array;
    e[t] = e[e.length - 1], --e.length, t <= this.i && --this.i
   }, r.push = function(t) {
    this.array.push(t)
   }, e.exports = n
  }), {}],
  188: [(function(t, e, i) {
   var n = t("../CCNode"),
    r = n.EventType,
    s = n._LocalDirtyFlag,
    a = t("../renderer/render-engine").math,
    o = t("../renderer/render-flow"),
    c = Math.PI / 180,
    h = 1,
    u = 2,
    l = 4,
    _ = null,
    d = null;

   function f() {
    if (this._localMatDirty) {
     var t = this._matrix;
     if (a.mat4.fromRTS(t, this._quat, this._position, this._scale), this._skewX || this._skewY) {
      var e = t.m00,
       i = t.m01,
       n = t.m04,
       r = t.m05,
       s = Math.tan(this._skewX * c),
       o = Math.tan(this._skewY * c);
      s === 1 / 0 && (s = 99999999), o === 1 / 0 && (o = 99999999), t.m00 = e + n * o, t.m01 = i + r * o, t.m04 = n + e * s, t.m05 = r + i * s
     }
     this._localMatDirty = 0, this._worldMatDirty = !0
    }
   }

   function p() {
    if (this._localMatDirty && this._updateLocalMatrix(), this._parent) {
     var t = this._parent._worldMatrix;
     a.mat4.mul(this._worldMatrix, t, this._matrix)
    } else a.mat4.copy(this._worldMatrix, this._matrix);
    this._worldMatDirty = !1
   }

   function m() {
    return new cc.Vec3(this._position)
   }

   function v(t, e, i) {
    var n = void 0;
    void 0 === e ? (n = t.x, e = t.y, i = t.z || 0) : (n = t, i = i || 0);
    var a = this._position;
    a.x === n && a.y === e && a.z === i || (a.x = n, a.y = e, a.z = i, this.setLocalDirty(s.POSITION), this._renderFlag |= o.FLAG_WORLD_TRANSFORM, this._eventMask & h && this.emit(r.POSITION_CHANGED))
   }

   function g() {
    return a.quat.clone(this._quat)
   }

   function y(t, e, i, n) {
    var a = void 0;
    void 0 === e && (a = t.x, e = t.y, i = t.z, n = t.w);
    var c = this._quat;
    c.x === a && c.y === e && c.z === i && c.w === n || (c.x = a, c.y = e, c.z = i, c.w = n, this.setLocalDirty(s.ROTATION), this._renderFlag |= o.FLAG_TRANSFORM, this._eventMask & l && this.emit(r.ROTATION_CHANGED))
   }

   function T() {
    return cc.v3(this._scale)
   }

   function E(t, e, i) {
    t && "number" != typeof t ? (e = t.y, i = t.z || 1, t = t.x) : void 0 !== t && void 0 === e ? (e = t, i = t) : void 0 === i && (i = 1), this._scale.x === t && this._scale.y === e && this._scale.z === i || (this._scale.x = t, this._scale.y = e, this._scale.z = i, this.setLocalDirty(s.SCALE), this._renderFlag |= o.FLAG_TRANSFORM, this._eventMask & u && this.emit(r.SCALE_CHANGED))
   }
   cc._polyfill3D = e.exports = {
    enabled: !1,
    enable: function() {
     var t = cc.Node.prototype;
     _ || (_ = t._updateLocalMatrix, d = t._calculWorldMatrix), this.enabled || (t._updateLocalMatrix = f, t._calculWorldMatrix = p, t.getPosition = m, t.setPosition = v, t.getScale = T, t.setScale = E, t.getQuat = g, t.setQuat = y, this.enabled = !0)
    },
    disable: function() {
     this.enabled && (cc.Node.prototype._updateLocalMatrix = _, cc.Node.prototype._calculWorldMatrix = d, this.enabled = !1)
    }
   }
  }), {
   "../CCNode": 24,
   "../renderer/render-engine": 150,
   "../renderer/render-flow": 151
  }],
  189: [(function(t, e, i) {
   var n = t("../renderer").renderEngine.math;
   cc._PrefabInfo = cc.Class({
    name: "cc.PrefabInfo",
    properties: {
     root: null,
     asset: null,
     fileId: "",
     sync: !1,
     _synced: {
      default: !1,
      serializable: !1
     }
    }
   }), e.exports = {
    syncWithPrefab: function(t) {
     var e = t._prefab;
     if (e._synced = !0, !e.asset) return cc.errorID(3701, t.name), void(t._prefab = null);
     var i = t._objFlags,
      r = t._parent,
      s = t._id,
      a = t._name,
      o = t._active,
      c = t._position.x,
      h = t._position.y,
      u = t._quat,
      l = t._localZOrder,
      _ = t._globalZOrder;
     cc.game._isCloning = !0;
     var d = e.asset.data;
     d._prefab._synced = !0, d._iN$t = t, cc.instantiate._clone(d, d), cc.game._isCloning = !1, t._objFlags = i, t._parent = r, t._id = s, t._prefab = e, t._name = a, t._active = o, t._position.x = c, t._position.y = h, n.quat.copy(t._quat, u), t._localZOrder = l, t._globalZOrder = _
    }
   }
  }), {
   "../renderer": 149
  }],
  190: [(function(t, e, i) {
   var n = t("../../platform/CCMacro"),
    r = t("./perf-counter"),
    s = !1,
    a = 15,
    o = null,
    c = null,
    h = null,
    u = null;

   function l() {
    (function() {
     if (!h || !h.isValid) {
      (h = new cc.Node("PROFILER-NODE")).x = h.y = 10, h.groupIndex = cc.Node.BuiltinGroupIndex.DEBUG, cc.Camera._setupDebugCamera(), h.zIndex = n.MAX_ZINDEX, cc.game.addPersistRootNode(h);
      var t = new cc.Node("LEFT-PANEL");
      t.anchorX = t.anchorY = 0, t.parent = h;
      var e = t.addComponent(cc.Label);
      e.font = o, e.fontSize = a, e.lineHeight = a;
      var i = new cc.Node("RIGHT-PANEL");
      i.anchorX = 1, i.anchorY = 0, i.x = 200, i.parent = h;
      var r = i.addComponent(cc.Label);
      r.horizontalAlign = cc.Label.HorizontalAlign.RIGHT, r.font = o, r.fontSize = a, r.lineHeight = a, u = {
       left: e,
       right: r
      }
     }
    })();
    var t = cc.director._lastUpdate;
    c.frame._counter.start(t), c.logic._counter.start(t)
   }

   function _() {
    var t = performance.now();
    cc.director.isPaused() ? c.frame._counter.start(t) : c.logic._counter.end(t), c.render._counter.start(t)
   }

   function d() {
    var t = performance.now();
    c.render._counter.end(t), c.draws._counter.value = cc.renderer.drawCalls, c.frame._counter.end(t), c.fps._counter.frame(t);
    var e = "",
     i = "";
    for (var n in c) {
     var r = c[n];
     r._counter.sample(t), e += r.desc + "\n", i += r._counter.human() + "\n"
    }
    u.left.string = e, u.right.string = i
   }
   cc.profiler = e.exports = {
    isShowingStats: function() {
     return s
    },
    hideStats: function() {
     s && (h && (h.active = !1), cc.director.off(cc.Director.EVENT_BEFORE_UPDATE, l), cc.director.off(cc.Director.EVENT_AFTER_UPDATE, _), cc.director.off(cc.Director.EVENT_AFTER_DRAW, d), s = !1)
    },
    showStats: function() {
     s || (function() {
      if (!o) {
       var t = document.createElement("canvas");
       t.style.width = t.width = 256, t.style.height = t.height = 256;
       var e = t.getContext("2d");
       e.font = a + "px Arial", e.textBaseline = "top", e.textAlign = "left", e.fillStyle = "#fff";
       var i = 2,
        n = 2,
        r = a;
       (o = new cc.LabelAtlas)._fntConfig = {
        atlasName: "profiler-arial",
        commonHeight: r,
        fontSize: a,
        kerningDict: {},
        fontDefDictionary: {}
       }, o._name = "profiler-arial", o.fontSize = a;
       for (var s = o._fntConfig.fontDefDictionary, c = 32; c <= 126; c++) {
        var h = String.fromCharCode(c),
         u = e.measureText(h).width;
        i + u >= 256 && (i = 2, n += r + 2), e.fillText(h, i, n), s[c] = {
         xAdvance: u,
         xOffset: 0,
         yOffset: 0,
         rect: {
          x: i,
          y: n,
          width: u,
          height: r
         }
        }, i += u + 2
       }
       var l = new cc.Texture2D;
       l.initWithElement(t);
       var _ = new cc.SpriteFrame;
       _.setTexture(l), o.spriteFrame = _
      }
     }(), (function() {
      if (!c) {
       c = {
        frame: {
         desc: "Frame time (ms)",
         min: 0,
         max: 50,
         average: 500
        },
        fps: {
         desc: "Framerate (FPS)",
         below: 30,
         average: 500
        },
        draws: {
         desc: "Draw call"
        },
        logic: {
         desc: "Game Logic (ms)",
         min: 0,
         max: 50,
         average: 500,
         color: "#080"
        },
        render: {
         desc: "Renderer (ms)",
         min: 0,
         max: 50,
         average: 500,
         color: "#f90"
        },
        mode: {
         desc: cc.game.renderType === cc.game.RENDER_TYPE_WEBGL ? "WebGL" : "Canvas",
         min: 1
        }
       };
       var t = performance.now();
       for (var e in c) c[e]._counter = new r(e, c[e], t)
      }
     })(), h && (h.active = !0), cc.director.on(cc.Director.EVENT_BEFORE_UPDATE, l), cc.director.on(cc.Director.EVENT_AFTER_UPDATE, _), cc.director.on(cc.Director.EVENT_AFTER_DRAW, d), s = !0)
    }
   }
  }), {
   "../../platform/CCMacro": 113,
   "./perf-counter": 192
  }],
  191: [(function(t, e, i) {
   var n = cc.Class({
    name: "cc.Counter",
    ctor: function(t, e, i) {
     this._id = t, this._opts = e || {}, this._value = 0, this._total = 0, this._averageValue = 0, this._accumValue = 0, this._accumSamples = 0, this._accumStart = i
    },
    properties: {
     value: {
      get: function() {
       return this._value
      },
      set: function(t) {
       this._value = t
      }
     }
    },
    _average: function(t, e) {
     if (this._opts.average) {
      this._accumValue += t, ++this._accumSamples;
      var i = e;
      i - this._accumStart >= this._opts.average && (this._averageValue = this._accumValue / this._accumSamples, this._accumValue = 0, this._accumStart = i, this._accumSamples = 0)
     }
    },
    sample: function(t) {
     this._average(this._value, t)
    },
    human: function() {
     var t = this._opts.average ? this._averageValue : this._value;
     return Math.round(100 * t) / 100
    },
    alarm: function() {
     return this._opts.below && this._value < this._opts.below || this._opts.over && this._value > this._opts.over
    }
   });
   e.exports = n
  }), {}],
  192: [(function(t, e, i) {
   var n = t("./counter"),
    r = cc.Class({
     name: "cc.PerfCounter",
     extends: n,
     ctor: function(t, e, i) {
      this._time = i
     },
     start: function(t) {
      this._time = t
     },
     end: function(t) {
      this._value = t - this._time, this._average(this._value)
     },
     tick: function() {
      this.end(), this.start()
     },
     frame: function(t) {
      var e = t,
       i = e - this._time;
      this._total++, i > (this._opts.average || 1e3) && (this._value = 1e3 * this._total / i, this._total = 0, this._time = e, this._average(this._value))
     }
    });
   e.exports = r
  }), {
   "./counter": 191
  }],
  193: [(function(t, e, i) {
   e.exports = {
    label_wordRex: /([a-zA-Z0-9\xc4\xd6\xdc\xe4\xf6\xfc\xdf\xe9\xe8\xe7\xe0\xf9\xea\xe2\xee\xf4\xfb\u0430-\u044f\u0410-\u042f\u0401\u0451]+|\S)/,
    label_symbolRex: /^[!,.:;'}\]%\?>\u3001\u2018\u201c\u300b\uff1f\u3002\uff0c\uff01]/,
    label_lastWordRex: /([a-zA-Z0-9\xc4\xd6\xdc\xe4\xf6\xfc\xdf\xe9\xe8\xe7\xe0\xf9\xea\xe2\xee\xf4\xfb\u0430\xed\xec\xcd\xcc\xef\xc1\xc0\xe1\xe0\xc9\xc8\xd2\xd3\xf2\xf3\u0150\u0151\xd9\xda\u0170\xfa\u0171\xf1\xd1\xe6\xc6\u0153\u0152\xc3\xc2\xe3\xd4\xf5\u011b\u0161\u010d\u0159\u017e\xfd\xe1\xed\xe9\xf3\xfa\u016f\u0165\u010f\u0148\u011a\u0160\u010c\u0158\u017d\xc1\xcd\xc9\xd3\xda\u0164\u017c\u017a\u015b\xf3\u0144\u0142\u0119\u0107\u0105\u017b\u0179\u015a\xd3\u0143\u0141\u0118\u0106\u0104-\u044f\u0410-\u042f\u0401\u0451]+|\S)$/,
    label_lastEnglish: /[a-zA-Z0-9\xc4\xd6\xdc\xe4\xf6\xfc\xdf\xe9\xe8\xe7\xe0\xf9\xea\xe2\xee\xf4\xfb\u0430\xed\xec\xcd\xcc\xef\xc1\xc0\xe1\xe0\xc9\xc8\xd2\xd3\xf2\xf3\u0150\u0151\xd9\xda\u0170\xfa\u0171\xf1\xd1\xe6\xc6\u0153\u0152\xc3\xc2\xe3\xd4\xf5\u011b\u0161\u010d\u0159\u017e\xfd\xe1\xed\xe9\xf3\xfa\u016f\u0165\u010f\u0148\u011a\u0160\u010c\u0158\u017d\xc1\xcd\xc9\xd3\xda\u0164\u017c\u017a\u015b\xf3\u0144\u0142\u0119\u0107\u0105\u017b\u0179\u015a\xd3\u0143\u0141\u0118\u0106\u0104-\u044f\u0410-\u042f\u0401\u0451]+$/,
    label_firstEnglish: /^[a-zA-Z0-9\xc4\xd6\xdc\xe4\xf6\xfc\xdf\xe9\xe8\xe7\xe0\xf9\xea\xe2\xee\xf4\xfb\u0430\xed\xec\xcd\xcc\xef\xc1\xc0\xe1\xe0\xc9\xc8\xd2\xd3\xf2\xf3\u0150\u0151\xd9\xda\u0170\xfa\u0171\xf1\xd1\xe6\xc6\u0153\u0152\xc3\xc2\xe3\xd4\xf5\u011b\u0161\u010d\u0159\u017e\xfd\xe1\xed\xe9\xf3\xfa\u016f\u0165\u010f\u0148\u011a\u0160\u010c\u0158\u017d\xc1\xcd\xc9\xd3\xda\u0164\u017c\u017a\u015b\xf3\u0144\u0142\u0119\u0107\u0105\u017b\u0179\u015a\xd3\u0143\u0141\u0118\u0106\u0104-\u044f\u0410-\u042f\u0401\u0451]/,
    label_firstEmoji: /^[\uD83C\uDF00-\uDFFF\uDC00-\uDE4F]/,
    label_lastEmoji: /([\uDF00-\uDFFF\uDC00-\uDE4F]+|\S)$/,
    label_wrapinspection: !0,
    __CHINESE_REG: /^[\u4E00-\u9FFF\u3400-\u4DFF]+$/,
    __JAPANESE_REG: /[\u3000-\u303F]|[\u3040-\u309F]|[\u30A0-\u30FF]|[\uFF00-\uFFEF]|[\u4E00-\u9FAF]|[\u2605-\u2606]|[\u2190-\u2195]|\u203B/g,
    __KOREAN_REG: /^[\u1100-\u11FF]|[\u3130-\u318F]|[\uA960-\uA97F]|[\uAC00-\uD7AF]|[\uD7B0-\uD7FF]+$/,
    isUnicodeCJK: function(t) {
     return this.__CHINESE_REG.test(t) || this.__JAPANESE_REG.test(t) || this.__KOREAN_REG.test(t)
    },
    isUnicodeSpace: function(t) {
     return (t = t.charCodeAt(0)) >= 9 && t <= 13 || 32 === t || 133 === t || 160 === t || 5760 === t || t >= 8192 && t <= 8202 || 8232 === t || 8233 === t || 8239 === t || 8287 === t || 12288 === t
    },
    safeMeasureText: function(t, e) {
     var i = t.measureText(e);
     return i && i.width || 0
    },
    fragmentText: function(t, e, i, n) {
     var r = [];
     if (0 === t.length || i < 0) return r.push(""), r;
     for (var s = t; e > i && s.length > 1;) {
      for (var a = s.length * (i / e) | 0, o = s.substr(a), c = e - n(o), h = o, u = 0, l = 0; c > i && l++ < 10;) a *= i / c, a |= 0, c = e - n(o = s.substr(a));
      for (l = 0; c <= i && l++ < 10;) {
       if (o) {
        var _ = this.label_wordRex.exec(o);
        u = _ ? _[0].length : 1, h = o
       }
       a += u, c = e - n(o = s.substr(a))
      }
      0 == (a -= u) && (a = 1, h = h.substr(1));
      var d, f = s.substr(0, a);
      this.label_wrapinspection && this.label_symbolRex.test(h || o) && (0 == (a -= (d = this.label_lastWordRex.exec(f)) ? d[0].length : 0) && (a = 1), h = s.substr(a), f = s.substr(0, a)), this.label_firstEmoji.test(h) && (d = this.label_lastEmoji.exec(f)) && f !== d[0] && (a -= d[0].length, h = s.substr(a), f = s.substr(0, a)), this.label_firstEnglish.test(h) && (d = this.label_lastEnglish.exec(f)) && f !== d[0] && (a -= d[0].length, h = s.substr(a), f = s.substr(0, a)), 0 === r.length ? r.push(f) : (f = f.trim()).length > 0 && r.push(f), e = n(s = h || o)
     }
     return 0 === r.length ? r.push(s) : (s = s.trim()).length > 0 && r.push(s), r
    }
   }
  }), {}],
  194: [(function(t, e, i) {
   var n = t("../assets/CCTexture2D"),
    r = {
     loadImage: function(t, e, i) {
      cc.assertID(t, 3103);
      var r = cc.loader.getRes(t);
      return r ? r.loaded ? (e && e.call(i, r), r) : (r.once("load", (function() {
       e && e.call(i, r)
      }), i), r) : ((r = new n).url = t, cc.loader.load({
       url: t,
       texture: r
      }, (function(t, n) {
       if (t) return e && e.call(i, t || new Error("Unknown error"));
       n.handleLoadedTexture(), e && e.call(i, null, n)
      })), r)
     },
     cacheImage: function(t, e) {
      if (t && e) {
       var i = new n;
       i.initWithElement(e);
       var r = {
        id: t,
        url: t,
        error: null,
        content: i,
        complete: !1
       };
       return cc.loader.flowOut(r), i
      }
     },
     postLoadTexture: function(t, e) {
      t.loaded ? e && e() : t.url ? cc.loader.load({
       url: t.url,
       skips: ["Loader"]
      }, (function(i, n) {
       n && (t.loaded || (t._nativeAsset = n)), e && e(i)
      })) : e && e()
     }
    };
   cc.textureUtil = e.exports = r
  }), {
   "../assets/CCTexture2D": 44
  }],
  195: [(function(t, e, i) {
   var n = t("./value-type"),
    r = t("../platform/js"),
    s = (function() {
     function e(t, e, i, n) {
      "object" == typeof t && (e = t.g, i = t.b, n = t.a, t = t.r), t = t || 0, e = e || 0, i = i || 0, n = "number" == typeof n ? n : 255, this._val = (n << 24 >>> 0) + (i << 16) + (e << 8) + t
     }
     r.extend(e, n), t("../platform/CCClass").fastDefine("cc.Color", e, {
      r: 0,
      g: 0,
      b: 0,
      a: 255
     });
     var i = {
      WHITE: [255, 255, 255, 255],
      BLACK: [0, 0, 0, 255],
      TRANSPARENT: [0, 0, 0, 0],
      GRAY: [127.5, 127.5, 127.5],
      RED: [255, 0, 0],
      GREEN: [0, 255, 0],
      BLUE: [0, 0, 255],
      YELLOW: [255, 235, 4],
      ORANGE: [255, 127, 0],
      CYAN: [0, 255, 255],
      MAGENTA: [255, 0, 255]
     };
     for (var s in i) r.get(e, s, (function(t) {
      return function() {
       return new e(t[0], t[1], t[2], t[3])
      }
     })(i[s]));
     var a = e.prototype;
     return a.clone = function() {
      var t = new e;
      return t._val = this._val, t
     }, a.equals = function(t) {
      return t && this._val === t._val
     }, a.lerp = function(t, i, n) {
      n = n || new e;
      var r = this.r,
       s = this.g,
       a = this.b,
       o = this.a;
      return n.r = r + (t.r - r) * i, n.g = s + (t.g - s) * i, n.b = a + (t.b - a) * i, n.a = o + (t.a - o) * i, n
     }, a.toString = function() {
      return "rgba(" + this.r.toFixed() + ", " + this.g.toFixed() + ", " + this.b.toFixed() + ", " + this.a.toFixed() + ")"
     }, a.getR = function() {
      return 255 & this._val
     }, a.setR = function(t) {
      return t = ~~cc.misc.clampf(t, 0, 255), this._val = (4294967040 & this._val | t) >>> 0, this
     }, a.getG = function() {
      return (65280 & this._val) >> 8
     }, a.setG = function(t) {
      return t = ~~cc.misc.clampf(t, 0, 255), this._val = (4294902015 & this._val | t << 8) >>> 0, this
     }, a.getB = function() {
      return (16711680 & this._val) >> 16
     }, a.setB = function(t) {
      return t = ~~cc.misc.clampf(t, 0, 255), this._val = (4278255615 & this._val | t << 16) >>> 0, this
     }, a.getA = function() {
      return (4278190080 & this._val) >>> 24
     }, a.setA = function(t) {
      return t = ~~cc.misc.clampf(t, 0, 255), this._val = (16777215 & this._val | t << 24 >>> 0) >>> 0, this
     }, a._fastSetA = function(t) {
      this._val = (16777215 & this._val | t << 24 >>> 0) >>> 0
     }, r.getset(a, "r", a.getR, a.setR, !0), r.getset(a, "g", a.getG, a.setG, !0), r.getset(a, "b", a.getB, a.setB, !0), r.getset(a, "a", a.getA, a.setA, !0), a.toCSS = function(t) {
      return "rgba" === t ? "rgba(" + (0 | this.r) + "," + (0 | this.g) + "," + (0 | this.b) + "," + (this.a / 255).toFixed(2) + ")" : "rgb" === t ? "rgb(" + (0 | this.r) + "," + (0 | this.g) + "," + (0 | this.b) + ")" : "#" + this.toHEX(t)
     }, a.fromHEX = function(t) {
      t = 0 === t.indexOf("#") ? t.substring(1) : t;
      var e = parseInt(t.substr(0, 2), 16) || 0,
       i = parseInt(t.substr(2, 2), 16) || 0,
       n = parseInt(t.substr(4, 2), 16) || 0,
       r = parseInt(t.substr(6, 2), 16) || 255;
      return this._val = (r << 24 >>> 0) + (n << 16) + (i << 8) + e, this
     }, a.toHEX = function(t) {
      var e = [(0 | this.r).toString(16), (0 | this.g).toString(16), (0 | this.b).toString(16)],
       i = -1;
      if ("#rgb" === t)
       for (i = 0; i < e.length; ++i) e[i].length > 1 && (e[i] = e[i][0]);
      else if ("#rrggbb" === t)
       for (i = 0; i < e.length; ++i) 1 === e[i].length && (e[i] = "0" + e[i]);
      else if ("#rrggbbaa" === t)
       for (e.push((0 | this.a).toString(16)), i = 0; i < e.length; ++i) 1 === e[i].length && (e[i] = "0" + e[i]);
      return e.join("")
     }, a.toRGBValue = function() {
      return 16777215 & this._val
     }, a.fromHSV = function(t, e, i) {
      var n, r, s;
      if (0 === e) n = r = s = i;
      else if (0 === i) n = r = s = 0;
      else {
       1 === t && (t = 0), t *= 6, e = e, i = i;
       var a = Math.floor(t),
        o = t - a,
        c = i * (1 - e),
        h = i * (1 - e * o),
        u = i * (1 - e * (1 - o));
       switch (a) {
        case 0:
         n = i, r = u, s = c;
         break;
        case 1:
         n = h, r = i, s = c;
         break;
        case 2:
         n = c, r = i, s = u;
         break;
        case 3:
         n = c, r = h, s = i;
         break;
        case 4:
         n = u, r = c, s = i;
         break;
        case 5:
         n = i, r = c, s = h
       }
      }
      return n *= 255, r *= 255, s *= 255, this._val = (this.a << 24 >>> 0) + (s << 16) + (r << 8) + n, this
     }, a.toHSV = function() {
      var t = this.r / 255,
       e = this.g / 255,
       i = this.b / 255,
       n = {
        h: 0,
        s: 0,
        v: 0
       },
       r = Math.max(t, e, i),
       s = Math.min(t, e, i),
       a = 0;
      return n.v = r, n.s = r ? (r - s) / r : 0, n.s ? (a = r - s, n.h = t === r ? (e - i) / a : e === r ? 2 + (i - t) / a : 4 + (t - e) / a, n.h /= 6, n.h < 0 && (n.h += 1)) : n.h = 0, n
     }, a.set = function(t) {
      t._val ? this._val = t._val : (this.r = t.r, this.g = t.g, this.b = t.b, this.a = t.a)
     }, e
    })();
   cc.Color = s, cc.color = function(t, e, i, n) {
    return "string" == typeof t ? (new cc.Color).fromHEX(t) : "object" == typeof t ? new cc.Color(t.r, t.g, t.b, t.a) : new cc.Color(t, e, i, n)
   }, e.exports = cc.Color
  }), {
   "../platform/CCClass": 108,
   "../platform/js": 128,
   "./value-type": 201
  }],
  196: [(function(t, e, i) {
   t("./value-type"), t("./vec2"), t("./vec3"), t("./quat"), t("./mat4"), t("./size"), t("./rect"), t("./color"), cc.vmath = t("../renderer/render-engine").math
  }), {
   "../renderer/render-engine": 150,
   "./color": 195,
   "./mat4": 197,
   "./quat": 198,
   "./rect": 199,
   "./size": 200,
   "./value-type": 201,
   "./vec2": 202,
   "./vec3": 203
  }],
  197: [(function(t, e, i) {
   var n = t("./value-type"),
    r = t("../platform/js"),
    s = t("../platform/CCClass"),
    a = t("../renderer/render-engine").math.mat4;

   function o(t, e, i, n, r, s, a, o, c, h, u, l, _, d, f, p) {
    var m = this;
    m.m00 = t, m.m01 = e, m.m02 = i, m.m03 = n, m.m04 = r, m.m05 = s, m.m06 = a, m.m07 = o, m.m08 = c, m.m09 = h, m.m10 = u, m.m11 = l, m.m12 = _, m.m13 = d, m.m14 = f, m.m15 = p
   }
   r.extend(o, n), s.fastDefine("cc.Mat4", o, {
    m00: 1,
    m01: 0,
    m02: 0,
    m03: 0,
    m04: 0,
    m05: 1,
    m06: 0,
    m07: 0,
    m08: 0,
    m09: 0,
    m10: 1,
    m11: 0,
    m12: 0,
    m13: 0,
    m14: 0,
    m15: 1
   }), r.mixin(o.prototype, {
    clone: function() {
     var t = this;
     return new o(t.m00, t.m01, t.m02, t.m03, t.m04, t.m05, t.m06, t.m07, t.m08, t.m09, t.m10, t.m11, t.m12, t.m13, t.m14, t.m15)
    },
    set: function(t) {
     var e = this;
     return e.m00 = t.m00, e.m01 = t.m01, e.m02 = t.m02, e.m03 = t.m03, e.m04 = t.m04, e.m05 = t.m05, e.m06 = t.m06, e.m07 = t.m07, e.m08 = t.m08, e.m09 = t.m09, e.m10 = t.m10, e.m11 = t.m11, e.m12 = t.m12, e.m13 = t.m13, e.m14 = t.m14, e.m15 = t.m15, this
    },
    equals: function(t) {
     return a.exactEquals(this, t)
    },
    fuzzyEquals: function(t) {
     return a.equals(this, t)
    },
    toString: function() {
     var t = this;
     return "[\n" + t.m00 + ", " + t.m01 + ", " + t.m02 + ", " + t.m03 + ",\n" + t.m04 + ", " + t.m05 + ", " + t.m06 + ", " + t.m07 + ",\n" + t.m08 + ", " + t.m09 + ", " + t.m10 + ", " + t.m11 + ",\n" + t.m12 + ", " + t.m13 + ", " + t.m14 + ", " + t.m15 + "\n]"
    },
    identity: function() {
     return a.identity(this)
    },
    transpose: function(t) {
     return t = t || new cc.Mat4, a.transpose(t, this)
    },
    invert: function(t) {
     return t = t || new cc.Mat4, a.invert(t, this)
    },
    adjoint: function(t) {
     return t = t || new cc.Mat4, a.adjoint(t, this)
    },
    determinant: function() {
     return a.determinant(this)
    },
    add: function(t, e) {
     return e = e || new cc.Mat4, a.add(e, this, t)
    },
    sub: function(t, e) {
     return e = e || new cc.Mat4, a.subtract(e, this, t)
    },
    mul: function(t, e) {
     return e = e || new cc.Mat4, a.multiply(e, this, t)
    },
    mulScalar: function(t, e) {
     return e = e || new cc.Mat4, a.mulScalar(e, this, t)
    },
    translate: function(t, e) {
     return e = e || new cc.Mat4, a.translate(e, this, t)
    },
    scale: function(t, e) {
     return e = e || new cc.Mat4, a.scale(e, this, t)
    },
    rotate: function(t, e, i) {
     return i = i || new cc.Mat4, a.rotate(i, this, t, e)
    },
    getTranslation: function(t) {
     return t = t || new cc.Vec3, a.getTranslation(t, this)
    },
    getScale: function(t) {
     return t = t || new cc.Vec3, a.getScaling(t, this)
    },
    getRotation: function(t) {
     return t = t || new cc.Quat, a.getRotation(t, this)
    },
    fromRTS: function(t, e, i) {
     return a.fromRTS(this, t, e, i)
    },
    fromQuat: function(t) {
     return a.fromQuat(this, t)
    }
   }), cc.mat4 = function(t, e, i, n, r, s, a, c, h, u, l, _, d, f, p, m) {
    return new o(t, e, i, n, r, s, a, c, h, u, l, _, d, f, p, m)
   }
  }), {
   "../platform/CCClass": 108,
   "../platform/js": 128,
   "../renderer/render-engine": 150,
   "./value-type": 201
  }],
  198: [(function(t, e, i) {
   var n = t("./value-type"),
    r = t("../platform/js"),
    s = t("../platform/CCClass");

   function a(t, e, i, n) {
    t && "object" == typeof t && (i = t.z, e = t.y, n = (t = t.x).w), this.x = t || 0, this.y = e || 0, this.z = i || 0, this.w = n || 1
   }
   r.extend(a, n), s.fastDefine("cc.Quat", a, {
    x: 0,
    y: 0,
    z: 0,
    w: 1
   });
   var o = a.prototype;
   o.clone = function() {
    return new a(this.x, this.y, this.z, this.w)
   }, o.set = function(t) {
    return this.x = t.x, this.y = t.y, this.z = t.z, this.w = t.w, this
   }, o.equals = function(t) {
    return t && this.x === t.x && this.y === t.y && this.z === t.z && this.w === t.w
   }, o.getRoll = function() {
    var t = 2 * (this.w * this.x + this.y * this.z),
     e = 1 - 2 * (this.x * this.x + this.y * this.y);
    return 180 * Math.atan2(t, e) / Math.PI
   }, o.getPitch = function() {
    var t = 2 * (this.w * this.y - this.z * this.x),
     e = t > 1 ? 1 : t;
    return e = t < -1 ? -1 : t, e = 180 * Math.asin(e) / Math.PI
   }, o.getYaw = function() {
    var t = 2 * (this.w * this.z + this.x * this.y),
     e = 1 - 2 * (this.y * this.y + this.z * this.z);
    return 180 * Math.atan2(t, e) / Math.PI
   }, cc.quat = function(t, e, i, n) {
    return new a(t, e, i, n)
   }, e.exports = cc.Quat = a
  }), {
   "../platform/CCClass": 108,
   "../platform/js": 128,
   "./value-type": 201
  }],
  199: [(function(t, e, i) {
   var n = t("./value-type"),
    r = t("../platform/js");

   function s(t, e, i, n) {
    t && "object" == typeof t && (e = t.y, i = t.width, n = t.height, t = t.x), this.x = t || 0, this.y = e || 0, this.width = i || 0, this.height = n || 0
   }
   r.extend(s, n), t("../platform/CCClass").fastDefine("cc.Rect", s, {
    x: 0,
    y: 0,
    width: 0,
    height: 0
   }), s.fromMinMax = function(t, e) {
    var i = Math.min(t.x, e.x),
     n = Math.min(t.y, e.y);
    return new s(i, n, Math.max(t.x, e.x) - i, Math.max(t.y, e.y) - n)
   };
   var a = s.prototype;
   a.clone = function() {
    return new s(this.x, this.y, this.width, this.height)
   }, a.equals = function(t) {
    return t && this.x === t.x && this.y === t.y && this.width === t.width && this.height === t.height
   }, a.lerp = function(t, e, i) {
    i = i || new s;
    var n = this.x,
     r = this.y,
     a = this.width,
     o = this.height;
    return i.x = n + (t.x - n) * e, i.y = r + (t.y - r) * e, i.width = a + (t.width - a) * e, i.height = o + (t.height - o) * e, i
   }, a.set = function(t) {
    this.x = t.x, this.y = t.y, this.width = t.width, this.height = t.height
   }, a.intersects = function(t) {
    var e = this.x + this.width,
     i = this.y + this.height,
     n = t.x + t.width,
     r = t.y + t.height;
    return !(e < t.x || n < this.x || i < t.y || r < this.y)
   }, a.intersection = function(t, e) {
    var i = this.x,
     n = this.y,
     r = this.x + this.width,
     s = this.y + this.height,
     a = e.x,
     o = e.y,
     c = e.x + e.width,
     h = e.y + e.height;
    return t.x = Math.max(i, a), t.y = Math.max(n, o), t.width = Math.min(r, c) - t.x, t.height = Math.min(s, h) - t.y, t
   }, a.contains = function(t) {
    return this.x <= t.x && this.x + this.width >= t.x && this.y <= t.y && this.y + this.height >= t.y
   }, a.containsRect = function(t) {
    return this.x <= t.x && this.x + this.width >= t.x + t.width && this.y <= t.y && this.y + this.height >= t.y + t.height
   }, a.union = function(t, e) {
    var i = this.x,
     n = this.y,
     r = this.width,
     s = this.height,
     a = e.x,
     o = e.y,
     c = e.width,
     h = e.height;
    return t.x = Math.min(i, a), t.y = Math.min(n, o), t.width = Math.max(i + r, a + c) - t.x, t.height = Math.max(n + s, o + h) - t.y, t
   }, a.transformMat4 = function(t, e) {
    var i = this.x,
     n = this.y,
     r = i + this.width,
     s = n + this.height,
     a = e.m00 * i + e.m04 * n + e.m12,
     o = e.m01 * i + e.m05 * n + e.m13,
     c = e.m00 * r + e.m04 * n + e.m12,
     h = e.m01 * r + e.m05 * n + e.m13,
     u = e.m00 * i + e.m04 * s + e.m12,
     l = e.m01 * i + e.m05 * s + e.m13,
     _ = e.m00 * r + e.m04 * s + e.m12,
     d = e.m01 * r + e.m05 * s + e.m13,
     f = Math.min(a, c, u, _),
     p = Math.max(a, c, u, _),
     m = Math.min(o, h, l, d),
     v = Math.max(o, h, l, d);
    return t.x = f, t.y = m, t.width = p - f, t.height = v - m, t
   }, a.toString = function() {
    return "(" + this.x.toFixed(2) + ", " + this.y.toFixed(2) + ", " + this.width.toFixed(2) + ", " + this.height.toFixed(2) + ")"
   }, r.getset(a, "xMin", (function() {
    return this.x
   }), (function(t) {
    this.width += this.x - t, this.x = t
   })), r.getset(a, "yMin", (function() {
    return this.y
   }), (function(t) {
    this.height += this.y - t, this.y = t
   })), r.getset(a, "xMax", (function() {
    return this.x + this.width
   }), (function(t) {
    this.width = t - this.x
   })), r.getset(a, "yMax", (function() {
    return this.y + this.height
   }), (function(t) {
    this.height = t - this.y
   })), r.getset(a, "center", (function() {
    return new cc.Vec2(this.x + .5 * this.width, this.y + .5 * this.height)
   }), (function(t) {
    this.x = t.x - .5 * this.width, this.y = t.y - .5 * this.height
   })), r.getset(a, "origin", (function() {
    return new cc.Vec2(this.x, this.y)
   }), (function(t) {
    this.x = t.x, this.y = t.y
   })), r.getset(a, "size", (function() {
    return new cc.Size(this.width, this.height)
   }), (function(t) {
    this.width = t.width, this.height = t.height
   })), cc.Rect = s, cc.rect = function(t, e, i, n) {
    return new s(t, e, i, n)
   }, e.exports = cc.Rect
  }), {
   "../platform/CCClass": 108,
   "../platform/js": 128,
   "./value-type": 201
  }],
  200: [(function(t, e, i) {
   var n = t("./value-type"),
    r = t("../platform/js");

   function s(t, e) {
    t && "object" == typeof t && (e = t.height, t = t.width), this.width = t || 0, this.height = e || 0
   }
   r.extend(s, n), t("../platform/CCClass").fastDefine("cc.Size", s, {
    width: 0,
    height: 0
   }), r.get(s, "ZERO", (function() {
    return new s(0, 0)
   }));
   var a = s.prototype;
   a.clone = function() {
    return new s(this.width, this.height)
   }, a.equals = function(t) {
    return t && this.width === t.width && this.height === t.height
   }, a.lerp = function(t, e, i) {
    i = i || new s;
    var n = this.width,
     r = this.height;
    return i.width = n + (t.width - n) * e, i.height = r + (t.height - r) * e, i
   }, a.set = function(t) {
    this.width = t.width, this.height = t.height
   }, a.toString = function() {
    return "(" + this.width.toFixed(2) + ", " + this.height.toFixed(2) + ")"
   }, cc.size = function(t, e) {
    return new s(t, e)
   }, cc.Size = e.exports = s
  }), {
   "../platform/CCClass": 108,
   "../platform/js": 128,
   "./value-type": 201
  }],
  201: [(function(t, e, i) {
   var n = t("../platform/js");

   function r() {}
   n.setClassName("cc.ValueType", r);
   var s = r.prototype;
   s.toString = function() {
    return "" + {}
   }, cc.ValueType = e.exports = r
  }), {
   "../platform/js": 128
  }],
  202: [(function(t, e, i) {
   var n = t("./value-type"),
    r = t("../platform/js"),
    s = t("../platform/CCClass"),
    a = t("../renderer/render-engine").math,
    o = t("../utils/misc");

   function c(t, e) {
    t && "object" == typeof t && (e = t.y, t = t.x), this.x = t || 0, this.y = e || 0
   }
   r.extend(c, n), s.fastDefine("cc.Vec2", c, {
    x: 0,
    y: 0
   });
   var h = c.prototype;
   h.clone = function() {
    return new c(this.x, this.y)
   }, h.set = function(t) {
    return this.x = t.x, this.y = t.y, this
   }, h.equals = function(t) {
    return t && this.x === t.x && this.y === t.y
   }, h.fuzzyEquals = function(t, e) {
    return this.x - e <= t.x && t.x <= this.x + e && this.y - e <= t.y && t.y <= this.y + e
   }, h.toString = function() {
    return "(" + this.x.toFixed(2) + ", " + this.y.toFixed(2) + ")"
   }, h.lerp = function(t, e, i) {
    i = i || new c;
    var n = this.x,
     r = this.y;
    return i.x = n + (t.x - n) * e, i.y = r + (t.y - r) * e, i
   }, h.clampf = function(t, e) {
    return this.x = o.clampf(this.x, t.x, e.x), this.y = o.clampf(this.y, t.y, e.y), this
   }, h.addSelf = function(t) {
    return this.x += t.x, this.y += t.y, this
   }, h.add = function(t, e) {
    return (e = e || new c).x = this.x + t.x, e.y = this.y + t.y, e
   }, h.subSelf = function(t) {
    return this.x -= t.x, this.y -= t.y, this
   }, h.sub = function(t, e) {
    return (e = e || new c).x = this.x - t.x, e.y = this.y - t.y, e
   }, h.mulSelf = function(t) {
    return this.x *= t, this.y *= t, this
   }, h.mul = function(t, e) {
    return (e = e || new c).x = this.x * t, e.y = this.y * t, e
   }, h.scaleSelf = function(t) {
    return this.x *= t.x, this.y *= t.y, this
   }, h.scale = function(t, e) {
    return (e = e || new c).x = this.x * t.x, e.y = this.y * t.y, e
   }, h.divSelf = function(t) {
    return this.x /= t, this.y /= t, this
   }, h.div = function(t, e) {
    return (e = e || new c).x = this.x / t, e.y = this.y / t, e
   }, h.negSelf = function() {
    return this.x = -this.x, this.y = -this.y, this
   }, h.neg = function(t) {
    return (t = t || new c).x = -this.x, t.y = -this.y, t
   }, h.dot = function(t) {
    return this.x * t.x + this.y * t.y
   }, h.cross = function(t) {
    return this.x * t.y - this.y * t.x
   }, h.mag = function() {
    return Math.sqrt(this.x * this.x + this.y * this.y)
   }, h.magSqr = function() {
    return this.x * this.x + this.y * this.y
   }, h.normalizeSelf = function() {
    var t = this.x * this.x + this.y * this.y;
    if (1 === t) return this;
    if (0 === t) return this;
    var e = 1 / Math.sqrt(t);
    return this.x *= e, this.y *= e, this
   }, h.normalize = function(t) {
    return (t = t || new c).x = this.x, t.y = this.y, t.normalizeSelf(), t
   }, h.angle = function(t) {
    var e = this.magSqr(),
     i = t.magSqr();
    if (0 === e || 0 === i) return console.warn("Can't get angle between zero vector"), 0;
    var n = this.dot(t) / Math.sqrt(e * i);
    return n = o.clampf(n, -1, 1), Math.acos(n)
   }, h.signAngle = function(t) {
    var e = this.angle(t);
    return this.cross(t) < 0 ? -e : e
   }, h.rotate = function(t, e) {
    return (e = e || new c).x = this.x, e.y = this.y, e.rotateSelf(t)
   }, h.rotateSelf = function(t) {
    var e = Math.sin(t),
     i = Math.cos(t),
     n = this.x;
    return this.x = i * n - e * this.y, this.y = e * n + i * this.y, this
   }, h.project = function(t) {
    return t.mul(this.dot(t) / t.dot(t))
   }, h.transformMat4 = function(t, e) {
    e = e || new c, a.vec2.transformMat4(e, this, t)
   }, r.get(c, "ONE", (function() {
    return new c(1, 1)
   })), r.get(c, "ZERO", (function() {
    return new c(0, 0)
   })), r.get(c, "UP", (function() {
    return new c(0, 1)
   })), r.get(c, "RIGHT", (function() {
    return new c(1, 0)
   })), cc.Vec2 = c, cc.v2 = function(t, e) {
    return new c(t, e)
   }, cc.p = cc.v2, e.exports = cc.Vec2
  }), {
   "../platform/CCClass": 108,
   "../platform/js": 128,
   "../renderer/render-engine": 150,
   "../utils/misc": 186,
   "./value-type": 201
  }],
  203: [(function(t, e, i) {
   var n = t("./value-type"),
    r = t("../platform/js"),
    s = t("../platform/CCClass"),
    a = t("../renderer/render-engine").math,
    o = t("../utils/misc");

   function c(t, e, i) {
    t && "object" == typeof t && (i = t.z, e = t.y, t = t.x), this.x = t || 0, this.y = e || 0, this.z = i || 0
   }
   r.extend(c, n), s.fastDefine("cc.Vec3", c, {
    x: 0,
    y: 0,
    z: 0
   });
   var h = c.prototype;
   h.clone = function() {
    return new c(this.x, this.y, this.z)
   }, h.set = function(t) {
    return this.x = t.x, this.y = t.y, this.z = t.z, this
   }, h.equals = function(t) {
    return t && this.x === t.x && this.y === t.y && this.z === t.z
   }, h.fuzzyEquals = function(t, e) {
    return this.x - e <= t.x && t.x <= this.x + e && this.y - e <= t.y && t.y <= this.y + e && this.z - e <= t.z && t.z <= this.z + e
   }, h.toString = function() {
    return "(" + this.x.toFixed(2) + ", " + this.y.toFixed(2) + ", " + this.z.toFixed(2) + ")"
   }, h.lerp = function(t, e, i) {
    return i = i || new c, a.vec3.lerp(i, this, t, e), i
   }, h.clampf = function(t, e) {
    return this.x = o.clampf(this.x, t.x, e.x), this.y = o.clampf(this.y, t.y, e.y), this.z = o.clampf(this.z, t.z, e.z), this
   }, h.addSelf = function(t) {
    return this.x += t.x, this.y += t.y, this.z += t.z, this
   }, h.add = function(t, e) {
    return (e = e || new c).x = this.x + t.x, e.y = this.y + t.y, e.z = this.z + t.z, e
   }, h.subSelf = function(t) {
    return this.x -= t.x, this.y -= t.y, this.z -= t.z, this
   }, h.sub = function(t, e) {
    return (e = e || new c).x = this.x - t.x, e.y = this.y - t.y, e.z = this.z - t.z, e
   }, h.mulSelf = function(t) {
    return this.x *= t, this.y *= t, this.z *= t, this
   }, h.mul = function(t, e) {
    return (e = e || new c).x = this.x * t, e.y = this.y * t, e.z = this.z * t, e
   }, h.scaleSelf = function(t) {
    return this.x *= t.x, this.y *= t.y, this.z *= t.z, this
   }, h.scale = function(t, e) {
    return (e = e || new c).x = this.x * t.x, e.y = this.y * t.y, e.z = this.z * t.z, e
   }, h.divSelf = function(t) {
    return this.x /= t, this.y /= t, this.z /= t, this
   }, h.div = function(t, e) {
    return (e = e || new c).x = this.x / t, e.y = this.y / t, e.z = this.z / t, e
   }, h.negSelf = function() {
    return this.x = -this.x, this.y = -this.y, this.z = -this.z, this
   }, h.neg = function(t) {
    return (t = t || new c).x = -this.x, t.y = -this.y, t.z = -this.z, t
   }, h.dot = function(t) {
    return this.x * t.x + this.y * t.y + this.z * t.z
   }, h.cross = function(t, e) {
    return e = e || new c, a.vec3.cross(e, this, t), e
   }, h.mag = function() {
    return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z)
   }, h.magSqr = function() {
    return this.x * this.x + this.y * this.y + this.z * this.z
   }, h.normalizeSelf = function() {
    return a.vec3.normalize(this, this), this
   }, h.normalize = function(t) {
    return t = t || new c, a.vec3.normalize(t, this), t
   }, h.transformMat4 = function(t, e) {
    e = e || new c, a.vec3.transformMat4(e, this, t)
   }, cc.v3 = function(t, e, i) {
    return new c(t, e, i)
   }, e.exports = cc.Vec3 = c
  }), {
   "../platform/CCClass": 108,
   "../platform/js": 128,
   "../renderer/render-engine": 150,
   "../utils/misc": 186,
   "./value-type": 201
  }],
  204: [(function(t, e, i) {
   cc.js
  }), {}],
  205: [(function(t, e, i) {
   t("./core/CCGame"), t("./actions")
  }), {
   "./actions": 7,
   "./core/CCGame": 23
  }],
  206: [(function(t, e, i) {
   var n = t("../core/assets/CCAsset"),
    r = t("../core/assets/CCSpriteFrame"),
    s = cc.Class({
     name: "cc.ParticleAsset",
     extends: n,
     properties: {
      spriteFrame: {
       default: null,
       type: r
      }
     }
    });
   cc.ParticleAsset = e.exports = s
  }), {
   "../core/assets/CCAsset": 28,
   "../core/assets/CCSpriteFrame": 41
  }],
  207: [(function(t, e, i) {
   var n = cc.Class({
    name: "cc.TiledMapAsset",
    extends: cc.Asset,
    properties: {
     tmxXmlStr: "",
     textures: {
      default: [],
      type: [cc.Texture2D]
     },
     textureNames: [cc.String],
     tsxFiles: [cc.TextAsset],
     tsxFileNames: [cc.String]
    },
    statics: {
     preventDeferredLoadDependents: !0
    },
    createNode: !1
   });
   cc.TiledMapAsset = n, e.exports = n
  }), {}],
  208: [(function(t, e, i) {
   t("./cocos2d/core"), t("./cocos2d/animation"), t("./cocos2d/particle"), t("./cocos2d/tilemap"), t("./cocos2d/videoplayer/CCVideoPlayer"), t("./cocos2d/webview/CCWebView"), t("./cocos2d/core/components/CCStudioComponent"), t("./extensions/ccpool/CCNodePool"), t("./cocos2d/actions"), t("./extensions/spine"), t("./extensions/dragonbones"), t("./cocos2d/deprecated")
  }), {
   "./cocos2d/actions": 7,
   "./cocos2d/animation": 15,
   "./cocos2d/core": 85,
   "./cocos2d/core/components/CCStudioComponent": 65,
   "./cocos2d/deprecated": 204,
   "./cocos2d/particle": void 0,
   "./cocos2d/particle/CCParticleAsset": 206,
   "./cocos2d/tilemap": void 0,
   "./cocos2d/tilemap/CCTiledMapAsset": 207,
   "./cocos2d/videoplayer/CCVideoPlayer": void 0,
   "./cocos2d/webview/CCWebView": void 0,
   "./extensions/ccpool/CCNodePool": void 0,
   "./extensions/dragonbones": void 0,
   "./extensions/spine": void 0
  }],
  209: [(function(t, e, i) {
   var n = "undefined" == typeof window ? global : window;
   n.cc = n.cc || {}, n._cc = n._cc || {}, t("./predefine"), t("./polyfill/string"), t("./polyfill/misc"), t("./polyfill/array"), t("./polyfill/object"), t("./polyfill/array-buffer"), t("./polyfill/typescript"), t("./cocos2d/core/predefine"), t("./cocos2d"), t("./extends"), e.exports = n.cc
  }), {
   "./cocos2d": 205,
   "./cocos2d/core/predefine": 133,
   "./extends": 208,
   "./package": void 0,
   "./polyfill/array": 211,
   "./polyfill/array-buffer": 210,
   "./polyfill/misc": 212,
   "./polyfill/object": 213,
   "./polyfill/string": 214,
   "./polyfill/typescript": 215,
   "./predefine": 216
  }],
  210: [(function(t, e, i) {
   ArrayBuffer.isView || (function() {
    var t = Object.getPrototypeOf(Object.getPrototypeOf(new Uint8Array)).constructor;
    ArrayBuffer.isView = function(e) {
     return e instanceof t
    }
   })()
  }), {}],
  211: [(function(t, e, i) {
   Array.isArray || (Array.isArray = function(t) {
    return "[object Array]" === Object.prototype.toString.call(t)
   })
  }), {}],
  212: [(function(t, e, i) {
   if (Math.sign || (Math.sign = function(t) {
     return 0 === (t = +t) || isNaN(t) ? t : t > 0 ? 1 : -1
    }), Number.isInteger || (Number.isInteger = function(t) {
     return "number" == typeof t && isFinite(t) && Math.floor(t) === t
    }), !console.time) {
    var n = window.performance || Date,
     r = Object.create(null);
    console.time = function(t) {
     r[t] = n.now()
    }, console.timeEnd = function(t) {
     var e = r[t],
      i = n.now() - e;
     console.log(t + ": " + i + "ms")
    }
   }
  }), {}],
  213: [(function(t, e, i) {
   Object.assign || (Object.assign = function(t, e) {
    cc.js.mixin(t, e)
   }), Object.getOwnPropertyDescriptors || (Object.getOwnPropertyDescriptors = function(t) {
    for (var e = {}, i = Object.getOwnPropertyNames(t).concat(Object.getOwnPropertySymbols(t)), n = 0; n < i.length; ++n) {
     var r = i[n];
     e[r] = Object.getOwnPropertyDescriptor(t, r)
    }
    return e
   })
  }), {}],
  214: [(function(t, e, i) {
   String.prototype.startsWith || (String.prototype.startsWith = function(t, e) {
    return e = e || 0, this.lastIndexOf(t, e) === e
   }), String.prototype.endsWith || (String.prototype.endsWith = function(t, e) {
    (void 0 === e || e > this.length) && (e = this.length), e -= t.length;
    var i = this.indexOf(t, e);
    return -1 !== i && i === e
   })
  }), {}],
  215: [(function(t, e, i) {
   var n = Object.setPrototypeOf || {
    __proto__: []
   }
   instanceof Array && function(t, e) {
    t.__proto__ = e
   } || function(t, e) {
    for (var i in e) e.hasOwnProperty(i) && (t[i] = e[i])
   };
   window.__extends = function(t, e) {
    function i() {
     this.constructor = t
    }
    n(t, e), t.prototype = null === e ? Object.create(e) : (i.prototype = e.prototype, new i)
   }, window.__assign = Object.assign || function(t) {
    for (var e, i = 1, n = arguments.length; i < n; i++)
     for (var r in e = arguments[i]) Object.prototype.hasOwnProperty.call(e, r) && (t[r] = e[r]);
    return t
   }, window.__rest = function(t, e) {
    var i = {};
    for (var n in t) Object.prototype.hasOwnProperty.call(t, n) && e.indexOf(n) < 0 && (i[n] = t[n]);
    if (null != t && "function" == typeof Object.getOwnPropertySymbols) {
     var r = 0;
     for (n = Object.getOwnPropertySymbols(t); r < n.length; r++) e.indexOf(n[r]) < 0 && (i[n[r]] = t[n[r]])
    }
    return i
   }, window.__decorate = function(t, e, i, n) {
    var r, s = arguments.length,
     a = s < 3 ? e : null === n ? n = Object.getOwnPropertyDescriptor(e, i) : n;
    if ("object" == typeof Reflect && "function" == typeof Reflect.decorate) a = Reflect.decorate(t, e, i, n);
    else
     for (var o = t.length - 1; o >= 0; o--)(r = t[o]) && (a = (s < 3 ? r(a) : s > 3 ? r(e, i, a) : r(e, i)) || a);
    return s > 3 && a && Object.defineProperty(e, i, a), a
   }, window.__param = function(t, e) {
    return function(i, n) {
     e(i, n, t)
    }
   }, window.__metadata = function(t, e) {
    if ("object" == typeof Reflect && "function" == typeof Reflect.metadata) return Reflect.metadata(t, e)
   }, window.__awaiter = function(t, e, i, n) {
    return new(i || (i = Promise))(function(r, s) {
     function a(t) {
      try {
       c(n.next(t))
      } catch (t) {
       s(t)
      }
     }

     function o(t) {
      try {
       c(n.throw(t))
      } catch (t) {
       s(t)
      }
     }

     function c(t) {
      t.done ? r(t.value) : new i(function(e) {
       e(t.value)
      }).then(a, o)
     }
     c((n = n.apply(t, e || [])).next())
    })
   }, window.__generator = function(t, e) {
    var i, n, r, s, a = {
     label: 0,
     sent: function() {
      if (1 & r[0]) throw r[1];
      return r[1]
     },
     trys: [],
     ops: []
    };
    return s = {
     next: o(0),
     throw: o(1),
     return: o(2)
    }, "function" == typeof Symbol && (s[Symbol.iterator] = function() {
     return this
    }), s;

    function o(s) {
     return function(o) {
      return (function(s) {
       if (i) throw new TypeError("Generator is already executing.");
       for (; a;) try {
        if (i = 1, n && (r = n[2 & s[0] ? "return" : s[0] ? "throw" : "next"]) && !(r = r.call(n, s[1])).done) return r;
        switch (n = 0, r && (s = [0, r.value]), s[0]) {
         case 0:
         case 1:
          r = s;
          break;
         case 4:
          return a.label++, {
           value: s[1],
           done: !1
          };
         case 5:
          a.label++, n = s[1], s = [0];
          continue;
         case 7:
          s = a.ops.pop(), a.trys.pop();
          continue;
         default:
          if (!(r = (r = a.trys).length > 0 && r[r.length - 1]) && (6 === s[0] || 2 === s[0])) {
           a = 0;
           continue
          }
          if (3 === s[0] && (!r || s[1] > r[0] && s[1] < r[3])) {
           a.label = s[1];
           break
          }
          if (6 === s[0] && a.label < r[1]) {
           a.label = r[1], r = s;
           break
          }
          if (r && a.label < r[2]) {
           a.label = r[2], a.ops.push(s);
           break
          }
          r[2] && a.ops.pop(), a.trys.pop();
          continue
        }
        s = e.call(t, a)
       } catch (t) {
        s = [6, t], n = 0
       } finally {
        i = r = 0
       }
       if (5 & s[0]) throw s[1];
       return {
        value: s[0] ? s[1] : void 0,
        done: !0
       }
      })([s, o])
     }
    }
   }, window.__exportStar = function(t, e) {
    for (var i in t) e.hasOwnProperty(i) || (e[i] = t[i])
   }, window.__values = function(t) {
    var e = "function" == typeof Symbol && t[Symbol.iterator],
     i = 0;
    return e ? e.call(t) : {
     next: function() {
      return t && i >= t.length && (t = void 0), {
       value: t && t[i++],
       done: !t
      }
     }
    }
   }, window.__read = function(t, e) {
    var i = "function" == typeof Symbol && t[Symbol.iterator];
    if (!i) return t;
    var n, r, s = i.call(t),
     a = [];
    try {
     for (;
      (void 0 === e || e-- > 0) && !(n = s.next()).done;) a.push(n.value)
    } catch (t) {
     r = {
      error: t
     }
    } finally {
     try {
      n && !n.done && (i = s.return) && i.call(s)
     } finally {
      if (r) throw r.error
     }
    }
    return a
   }, window.__spread = function() {
    for (var t = [], e = 0; e < arguments.length; e++) t = t.concat(__read(arguments[e]));
    return t
   }, window.__await = function(t) {
    return this instanceof __await ? (this.v = t, this) : new __await(t)
   }, window.__asyncGenerator = function(t, e, i) {
    if (!Symbol.asyncIterator) throw new TypeError("Symbol.asyncIterator is not defined.");
    var n, r = i.apply(t, e || []),
     s = [];
    return n = {}, a("next"), a("throw"), a("return"), n[Symbol.asyncIterator] = function() {
     return this
    }, n;

    function a(t) {
     r[t] && (n[t] = function(e) {
      return new Promise(function(i, n) {
       s.push([t, e, i, n]) > 1 || o(t, e)
      })
     })
    }

    function o(t, e) {
     try {
      (function(t) {
       t.value instanceof __await ? Promise.resolve(t.value.v).then(c, h) : u(s[0][2], t)
      })(r[t](e))
     } catch (t) {
      u(s[0][3], t)
     }
    }

    function c(t) {
     o("next", t)
    }

    function h(t) {
     o("throw", t)
    }

    function u(t, e) {
     t(e), s.shift(), s.length && o(s[0][0], s[0][1])
    }
   }, window.__asyncDelegator = function(t) {
    var e, i;
    return e = {}, n("next"), n("throw", (function(t) {
     throw t
    })), n("return"), e[Symbol.iterator] = function() {
     return this
    }, e;

    function n(n, r) {
     t[n] && (e[n] = function(e) {
      return (i = !i) ? {
       value: __await(t[n](e)),
       done: "return" === n
      } : r ? r(e) : e
     })
    }
   }, window.__asyncValues = function(t) {
    if (!Symbol.asyncIterator) throw new TypeError("Symbol.asyncIterator is not defined.");
    var e = t[Symbol.asyncIterator];
    return e ? e.call(t) : "function" == typeof __values ? __values(t) : t[Symbol.iterator]()
   }
  }), {}],
  216: [(function(t, e, i) {
   var n = "undefined" == typeof window ? global : window;

   function r(t, e) {
    void 0 === n[t] && (n[t] = e)
   }
   r("CC_BUILD", !1), n.CC_BUILD = !0, n.CC_TEST = !1, n.CC_EDITOR = !1, n.CC_PREVIEW = !1, n.CC_DEV = !1, n.CC_DEBUG = !1, n.CC_JSB = !1, n.CC_WECHATGAMESUB = !1, n.CC_WECHATGAME = !0, n.CC_QQPLAY = !1, n.CC_RUNTIME = !1, n.CC_SUPPORT_JIT = !1;
   n.CocosEngine = cc.ENGINE_VERSION = "2.0.7"
  }), {}]
 }, {}, [209]);
})