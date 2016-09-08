package init;

import java.util.List;

import dao.ConexaoPool;
import model.BancoDadosVO;

public abstract class InitConexaoPool {

	public static void init() throws Exception {
		List<BancoDadosVO> listaBancos = ImportarConexoes.importarBancos();
		criarPools(listaBancos);
	}

	private static void criarPools(List<BancoDadosVO> listaBancos) {

		for (BancoDadosVO banco : listaBancos) {
			ConexaoPool.criarPool(banco);
		}
	}

}
