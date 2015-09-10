package br.scmba.sisgep.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import br.scmba.arquitetura.service.AbstractService;
import br.scmba.exception.AppException;
import br.scmba.sisgep.vo.Menu;
import br.scmba.sisgep.vo.Papel;
import br.scmba.util.LogUtil;
import br.scmba.util.MensagemUtil;

@Stateless
public class MenuService extends AbstractService<Menu>  {

	private static final long serialVersionUID = 3016183975736128968L;
	
	@Inject
	EntityManager entityManagerSistema;
	
	@Inject
	private transient Logger log = Logger.getLogger(MenuService.class);

	
	@Override
	protected void validaCampos(Menu entity) {	}

	@Override
	protected void validaRegras(Menu entity) {}

	@Override
	protected void validaRegrasExcluir(Menu entity) throws AppException { }

	@SuppressWarnings("unchecked")
	public List<Menu> findMenuByTipoAndPapelAnTipoMenu(Boolean menuPai, Papel papel, String tipoMenu) {
		StringBuilder hql = new StringBuilder();
		try {
			hql.append(" SELECT menu FROM Menu menu left join fetch  menu.listaPapelMenu  papelMenu  left join fetch  papelMenu.papel  papel        ");

			if (menuPai)
				hql.append(" where    menu.menuPai  is null ");
			else
				hql.append(" where    menu.menuPai  is not null ");

			if (tipoMenu != null && !tipoMenu.equalsIgnoreCase(""))
				hql.append("  and    menu.tipo  = '" + tipoMenu + "'");

			hql.append(" and     papel.id  = " + papel.getId());

			hql.append(" ORDER BY menu.ordem ");

			Query query = entityManagerSistema.createQuery(hql.toString(), Menu.class);

			return mount(query.getResultList());

		} catch (Exception ej) {
			mensagens.add(MensagemUtil.obterMensagem("geral.message.erro.desconhecido"));
			logger.error(LogUtil.getMensagemPadraoLog("", "findAll", "Unidade"));
		}

		return null;
	}
	
	
	
	private List<Menu> mount(List<Menu> list) {
		Map<Long, Menu> map = new HashMap<Long, Menu>();
		for (Menu obj : list) {
			if (!map.containsKey(obj.getId())) {
				map.put((Long) obj.getId(), obj);
			}
		}

		List<Menu> menus = new ArrayList<Menu>(map.values());
		Collections.sort(menus);
		return ( menus );

	}


}
