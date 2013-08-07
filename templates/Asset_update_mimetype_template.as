package codeOrchestra.liveCoding.load {
  
  import codeOrchestra.actionScript.liveCoding.util.IAssetUpdate;
  import flash.system.Security;
  
  public class Asset_{CLASS_POSTFIX} implements IAssetUpdate {
    {
      try {
        Security.allowDomain("*");
      } catch (e : *)  {
      }
    }

    [Embed(source="{RELATIVE_PATH}", mimeType="{MIME_TYPE}")]
    private var asset : Class ;

    public function Asset_{CLASS_POSTFIX}(  ){
      super();
    }

    public function getAsset (  ) : Class {
      return asset;
    }
  }
}




