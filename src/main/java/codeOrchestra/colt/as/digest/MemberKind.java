package codeOrchestra.colt.as.digest;

import apparat.abc.AbcTraitKind;

public enum MemberKind {

  METHOD,
  GETTER,
  SETTER,
  FIELD;
  
  public static MemberKind byTraitKind(int traitKind) {
    if (traitKind == AbcTraitKind.Method()) {
      return METHOD;
    }
    if (traitKind == AbcTraitKind.Getter()) {
      return GETTER;
    }
    if (traitKind == AbcTraitKind.Setter()) {
      return SETTER;
    }
    if (traitKind == AbcTraitKind.Const()) {
      return FIELD;
    }
    if (traitKind == AbcTraitKind.Slot()) {
      return FIELD;
    }
    throw new IllegalArgumentException("Unknown trait kind: " + traitKind);
  }
}