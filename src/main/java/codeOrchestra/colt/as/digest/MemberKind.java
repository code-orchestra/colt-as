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
    } else if (traitKind == AbcTraitKind.Getter()) {
      return GETTER;
    } else if (traitKind == AbcTraitKind.Setter()) {
      return SETTER;
    } else if (traitKind == AbcTraitKind.Const()) {
      return FIELD;
    } else if (traitKind == AbcTraitKind.Slot()) {
      return FIELD;
    } else {
      throw new IllegalArgumentException("Unknown trait kind: " + traitKind);
    }
  }
  
}
